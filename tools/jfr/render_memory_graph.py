#!/usr/bin/env python3
"""render_memory_graph.py — turn the measure-jfr JFR snapshot into the v1 deliverables.

Usage: render_memory_graph.py <jdk-measure.jfr> <outdir>

Outputs in <outdir>:
  memory.csv   dense heap time series (org.gradle.measure.HeapUsage events)
  gc.csv       jdk.GCHeapSummary points (exact post/pre-GC occupancy)
  markers.csv  every org.gradle.internal.operations.BuildOperation event
               (operationId, parentId, displayName, start, duration, failure)
               — pick the dump_marker regex for the follow-up run from this
  memory.html  standalone report: SVG memory graph + top-50 operation table

Uses the `jfr` CLI (JDK bin) for parsing; stdlib only otherwise.
"""
import csv
import json
import re
import subprocess
import sys
from datetime import datetime, timezone
from html import escape

MEM_EVENTS = "org.gradle.measure.HeapUsage,jdk.GCHeapSummary,jdk.PhysicalMemory"
MARKER_EVENTS = "org.gradle.internal.operations.BuildOperation,org.gradle.measure.MarkerDump"


def jfr_print(jfr_file, events):
    out = subprocess.run(
        ["jfr", "print", "--json", "--events", events, jfr_file],
        capture_output=True, text=True)
    if out.returncode != 0:
        print(f"jfr print --events {events} failed: {out.stderr[:2000]}", file=sys.stderr)
        return []
    try:
        return json.loads(out.stdout)["recording"]["events"]
    except (json.JSONDecodeError, KeyError) as e:
        print(f"cannot parse jfr JSON for {events}: {e}", file=sys.stderr)
        return []


def parse_time(s):
    return datetime.fromisoformat(str(s).replace("Z", "+00:00"))


_SIZE = {"B": 1, "KB": 1e3, "MB": 1e6, "GB": 1e9,
         "KIB": 1024, "MIB": 1024**2, "GIB": 1024**3}


def to_bytes(v):
    """jfr --json prints raw numbers, but be tolerant of humanized '1.5 GB'."""
    if isinstance(v, (int, float)):
        return int(v)
    m = re.match(r"^\s*([\d.,]+)\s*([A-Za-z]*)\s*$", str(v))
    if not m:
        return 0
    return int(float(m.group(1).replace(",", "")) * _SIZE.get(m.group(2).upper(), 1))


def to_ms(v):
    """Duration: number (assume ms) or ISO-8601 'PT1.234S'."""
    if isinstance(v, (int, float)):
        return float(v)
    m = re.match(r"^PT(?:(\d+)M)?([\d.]+)S$", str(v))
    if not m:
        return 0.0
    return (float(m.group(1) or 0) * 60 + float(m.group(2))) * 1000


def main():
    jfr_file, outdir = sys.argv[1], sys.argv[2]

    mem_events = jfr_print(jfr_file, MEM_EVENTS)
    marker_events = jfr_print(jfr_file, MARKER_EVENTS)

    t0 = None
    for ev in mem_events + marker_events:
        try:
            t = parse_time(ev["values"]["startTime"])
            t0 = t if t0 is None or t < t0 else t0
        except (KeyError, ValueError):
            pass
    if t0 is None:
        t0 = datetime.now(timezone.utc)

    # ---- memory.csv (dense sampler series) ----
    heap_rows = []
    gc_rows = []
    for ev in mem_events:
        v = ev["values"]
        t = (parse_time(v["startTime"]) - t0).total_seconds()
        if ev["type"] == "org.gradle.measure.HeapUsage":
            heap_rows.append((t, to_bytes(v.get("heapUsed")), to_bytes(v.get("heapCommitted")),
                              to_bytes(v.get("heapMax")), to_bytes(v.get("metaspaceUsed")),
                              v.get("loadedClasses", 0), v.get("threadCount", 0)))
        elif ev["type"] == "jdk.GCHeapSummary":
            gc_rows.append((t, str(v.get("when", "")), to_bytes(v.get("heapUsed")),
                            to_bytes((v.get("heapSpace") or {}).get("committedSize", 0))
                            if isinstance(v.get("heapSpace"), dict) else to_bytes(v.get("heapSpaceCommitted", 0)),
                            v.get("gcId", "")))
    heap_rows.sort()
    gc_rows.sort()

    with open(f"{outdir}/memory.csv", "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["time_s", "heap_used", "heap_committed", "heap_max",
                    "metaspace_used", "loaded_classes", "thread_count"])
        w.writerows(heap_rows)
    with open(f"{outdir}/gc.csv", "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["time_s", "when", "heap_used", "heap_committed", "gc_id"])
        w.writerows(gc_rows)

    # ---- markers.csv ----
    ops = []
    marker_dumps = []
    for ev in marker_events:
        v = ev["values"]
        t = (parse_time(v["startTime"]) - t0).total_seconds()
        if ev["type"] == "org.gradle.internal.operations.BuildOperation":
            dur_ms = to_ms(v.get("duration", 0))
            if not dur_ms and v.get("gradleEndTime") and v.get("gradleStartTime"):
                dur_ms = float(v["gradleEndTime"]) - float(v["gradleStartTime"])
            ops.append({
                "operationId": v.get("operationId", ""),
                "parentId": v.get("parentId", ""),
                "displayName": v.get("displayName", ""),
                "start_s": round(t, 3),
                "duration_ms": round(dur_ms, 1),
                "failureType": v.get("failureType", "") or "",
            })
        elif ev["type"] == "org.gradle.measure.MarkerDump":
            marker_dumps.append((t, v.get("matchedOperation", ""), v.get("dumpFile", "")))
    ops.sort(key=lambda o: o["start_s"])

    with open(f"{outdir}/markers.csv", "w", newline="") as f:
        w = csv.DictWriter(f, fieldnames=["operationId", "parentId", "displayName",
                                          "start_s", "duration_ms", "failureType"])
        w.writeheader()
        w.writerows(ops)

    # ---- memory.html ----
    w_px, h_px, pad_l, pad_b, pad_t = 1200, 420, 90, 40, 30
    plot_w, plot_h = w_px - pad_l - 20, h_px - pad_t - pad_b
    t_max = max([r[0] for r in heap_rows] + [r[0] for r in gc_rows] + [1.0])
    y_max = max([r[2] for r in heap_rows] + [r[3] for r in heap_rows] + [1]) * 1.05

    def xy(t, val):
        return (pad_l + t / t_max * plot_w, pad_t + plot_h - val / y_max * plot_h)

    def polyline(rows, col, color, width=1.5):
        pts = " ".join(f"{x:.1f},{y:.1f}" for x, y in (xy(r[0], r[col]) for r in rows))
        return f'<polyline points="{pts}" fill="none" stroke="{color}" stroke-width="{width}"/>'

    svg = [f'<svg viewBox="0 0 {w_px} {h_px}" xmlns="http://www.w3.org/2000/svg" '
           f'style="background:#111;color:#ddd;font:12px sans-serif">']
    for i in range(6):  # y grid + labels
        val = y_max * i / 5
        y = xy(0, val)[1]
        svg.append(f'<line x1="{pad_l}" y1="{y:.1f}" x2="{w_px-20}" y2="{y:.1f}" stroke="#333"/>')
        svg.append(f'<text x="{pad_l-6}" y="{y+4:.1f}" text-anchor="end" fill="#999">{val/1e9:.1f} GB</text>')
    for i in range(11):  # x labels
        t = t_max * i / 10
        x = xy(t, 0)[0]
        svg.append(f'<text x="{x:.1f}" y="{h_px-12}" text-anchor="middle" fill="#999">{t/60:.0f} min</text>')
    if heap_rows:
        svg.append(polyline(heap_rows, 2, "#555"))          # committed
        svg.append(polyline(heap_rows, 1, "#4da3ff", 2))    # used
    gc_after = [r for r in gc_rows if r[1].lower().startswith("after")]
    for r in gc_after:
        x, y = xy(r[0], r[2])
        svg.append(f'<circle cx="{x:.1f}" cy="{y:.1f}" r="2.5" fill="#ffb84d"/>')
    for t, name, _ in marker_dumps:  # marker dump trigger point
        x = xy(t, 0)[0]
        svg.append(f'<line x1="{x:.1f}" y1="{pad_t}" x2="{x:.1f}" y2="{pad_t+plot_h}" stroke="#ff4d4d" stroke-width="2"/>')
    svg.append(f'<text x="{pad_l+8}" y="{pad_t+14}" fill="#4da3ff">heap used</text>'
               f'<text x="{pad_l+90}" y="{pad_t+14}" fill="#888">heap committed</text>'
               f'<text x="{pad_l+210}" y="{pad_t+14}" fill="#ffb84d">&#9679; after-GC live set</text>')
    svg.append('</svg>')

    top = sorted(ops, key=lambda o: -o["duration_ms"])[:50]
    rows_html = "".join(
        f"<tr><td>{escape(str(o['displayName']))}</td><td>{o['start_s']:.1f}</td>"
        f"<td>{o['duration_ms']:,.0f}</td><td>{o['operationId']}</td><td>{o['parentId']}</td>"
        f"<td>{escape(str(o['failureType']))}</td></tr>" for o in top)

    html = f"""<!doctype html><meta charset="utf-8"><title>measure-jfr memory graph</title>
<style>body{{background:#1a1a1a;color:#ddd;font:14px sans-serif;margin:24px}}
table{{border-collapse:collapse;margin-top:16px}}td,th{{border:1px solid #444;padding:3px 8px}}
th{{background:#333}}tr:nth-child(even){{background:#222}}</style>
<h1>Gradle daemon memory — {escape(jfr_file)}</h1>
<p>{len(heap_rows)} heap samples, {len(gc_rows)} GC summaries, {len(ops)} build operations,
{len(marker_dumps)} marker dumps. Full data: memory.csv, gc.csv, markers.csv.</p>
{''.join(svg)}
<h2>Top 50 build operations by duration</h2>
<table><tr><th>displayName</th><th>start (s)</th><th>duration (ms)</th><th>opId</th><th>parentId</th><th>failure</th></tr>
{rows_html}</table>"""
    with open(f"{outdir}/memory.html", "w") as f:
        f.write(html)

    print(f"memory.csv: {len(heap_rows)} samples; gc.csv: {len(gc_rows)}; "
          f"markers.csv: {len(ops)} operations; memory.html written")


if __name__ == "__main__":
    main()
