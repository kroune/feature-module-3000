#!/usr/bin/env bash
# measure.sh — runner-side orchestration for measure-jfr.yml.
#
# Subcommands:
#   setup   Patch gradle.properties (heap cap, JFR recording, dump paths),
#           write $GRADLE_USER_HOME/gradle.properties (JFR emitter flag +
#           jfr.measure.* system properties) and install the init script.
#   run     Launch the Studio sync via gradle-profiler in the background and
#           watch it: periodic non-stopping jcmd JFR.dump flushes, early
#           teardown when the marker heap dump (or an OOM dump) is complete,
#           wall-clock cutoff so the run always ends inside the runner cap.
#
# Expected env (workflow passes inputs; defaults are local-friendly):
#   GITHUB_WORKSPACE     project root (default: $PWD)
#   RUNNER_TEMP          scratch dir      (default: /tmp/jfr-measure)
#   GRADLE_USER_HOME     (default: $RUNNER_TEMP/guh)
#   DAEMON_XMX           daemon -Xmx=-Xms (default: 9g)
#   DUMP_MARKER          regex on BuildOperation displayName; empty = measure only
#   DUMP_MARKER_OCCURRENCE  Nth match triggers the dump (default: 1)
#   DUMP_LIVE            true = live-objects-only dump (full GC first) (default: false)
#   HEAP_SAMPLE_SECONDS  HeapUsage event period (default: 2)
#   KILL_AFTER_HOURS     watchdog cutoff (default: 5)
#   STUDIO_DIR           Android Studio install dir (default: $RUNNER_TEMP/studio)
set -euo pipefail

WS="${GITHUB_WORKSPACE:-$PWD}"
RT="${RUNNER_TEMP:-/tmp/jfr-measure}"
GUH="${GRADLE_USER_HOME:-$RT/guh}"
XMX="${DAEMON_XMX:-9g}"
MARKER="${DUMP_MARKER:-}"
OCCURRENCE="${DUMP_MARKER_OCCURRENCE:-1}"
LIVE="${DUMP_LIVE:-false}"
PERIOD="${HEAP_SAMPLE_SECONDS:-2}"
KILL_AFTER_HOURS="${KILL_AFTER_HOURS:-5}"
STUDIO_DIR="${STUDIO_DIR:-$RT/studio}"

INIT_SCRIPT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/jfr-measure.init.gradle"

cmd_setup() {
  cd "$WS"
  mkdir -p heap-dumps/daemon heap-dumps/kotlin heap-dumps/ide profile

  # Heap cap + OOM-dump insurance (kept from the OOM-era workflows: a too-low
  # heap or runner-image drift can still OOM the daemon, and then the JVM dump
  # is the fallback artifact). JFR recording streams to disk from JVM start so
  # jcmd JFR.dump snapshots work throughout the sync.
  sed -i -E "/^org.gradle.jvmargs=/ s|-Xmx[0-9]+[gGmM]|-Xmx$XMX|" gradle.properties
  sed -i -E "/^org.gradle.jvmargs=/ s|-Xms[0-9]+[gGmM]|-Xms$XMX|" gradle.properties
  sed -i "/^org.gradle.jvmargs=/ s|\$| -XX:HeapDumpPath=$WS/heap-dumps/daemon -XX:HeapDumpGzipLevel=1 -XX:+UseCompactObjectHeaders|" gradle.properties
  sed -i "/^kotlin.daemon.jvmargs=/ s|\$| -XX:HeapDumpPath=$WS/heap-dumps/kotlin -XX:HeapDumpGzipLevel=1|" gradle.properties
  sed -i "/^org.gradle.jvmargs=/ s|\$| -XX:StartFlightRecording=name=measure,filename=$WS/profile/jdk-measure.jfr,settings=profile,maxsize=4g,dumponexit=true|" gradle.properties

  # Gradle's build-operation JFR emitter (Gradle >= 9.7.0) is an internal option
  # resolved from gradle.properties; the marker/dump knobs reach the daemon as
  # system properties via systemProp.*. Both live in GRADLE_USER_HOME so the
  # repo gradle.properties stays the shared repro baseline.
  mkdir -p "$GUH/init.d"
  {
    echo "org.gradle.internal.operations.jfr=true"
    echo "systemProp.jfr.measure.marker=$MARKER"
    echo "systemProp.jfr.measure.marker.occurrence=$OCCURRENCE"
    echo "systemProp.jfr.measure.dump.live=$LIVE"
    echo "systemProp.jfr.measure.dump.dir=$WS/heap-dumps/daemon"
    echo "systemProp.jfr.measure.heap.period.seconds=$PERIOD"
  } > "$GUH/gradle.properties"
  cp "$INIT_SCRIPT" "$GUH/init.d/jfr-measure.init.gradle"

  sed "s|@HEAP_DUMP_DIR@|$WS/heap-dumps|" sync.scenarios > sync.scenarios.ci

  echo "== gradle.properties =="; cat gradle.properties
  echo "== GUH gradle.properties =="; cat "$GUH/gradle.properties"
  echo "== sync.scenarios.ci =="; cat sync.scenarios.ci
}

cmd_run() {
  cd "$WS"
  local_kill_after=$(( KILL_AFTER_HOURS * 3600 ))
  flush_every=600

  daemon_pid() { pgrep -f 'GradleDaemon' | head -1; }

  # Non-stopping snapshot: the recording continues afterwards. timeout-guarded
  # because the attach can block while the JVM is stopped writing a heap dump.
  flush_jfr() {
    local pid; pid=$(daemon_pid)
    if [ -z "$pid" ]; then echo "watchdog: no daemon to flush"; return 0; fi
    echo "watchdog: flushing JFR (pid $pid, ${SECONDS}s elapsed)"
    timeout 120 jcmd "$pid" JFR.dump name=measure filename="$WS/profile/jdk-measure.jfr" 2>&1 | tail -2 || true
  }

  # A dump file is complete when its size is stable across two polls and no
  # JDK-25+ segmented OOM dump (*.p0/*.p1/...) is still being merged.
  dump_complete() {
    local f="$1"
    if ls heap-dumps/daemon/*.p[0-9] >/dev/null 2>&1; then return 1; fi
    local s1 s2
    s1=$(stat -c%s "$f"); sleep 30; s2=$(stat -c%s "$f" 2>/dev/null || echo 0)
    [ "$s1" -gt 0 ] && [ "$s1" = "$s2" ]
  }

  set +e
  xvfb-run -a gradle-profiler --benchmark \
    --single-shot \
    --project-dir . \
    --scenario-file sync.scenarios.ci \
    --studio-install-dir "$STUDIO_DIR" \
    --output-dir results \
    > profiler.log 2>&1 &
  PROF=$!
  SECONDS=0
  LAST_FLUSH=0
  DUMP_FOUND=""
  TIMED_OUT=""
  while kill -0 $PROF 2>/dev/null; do
    # Marker dump (HotSpotDiagnosticMXBean, uncompressed) or OOM insurance dump.
    f=$(ls -S heap-dumps/daemon/*.hprof heap-dumps/daemon/*.hprof.gz 2>/dev/null | head -1)
    if [ -n "$f" ]; then
      if [ -z "$DUMP_FOUND" ]; then
        DUMP_FOUND="$f"
        flush_jfr
      fi
      if dump_complete "$f"; then
        echo "watchdog: complete dump detected ($f, $(stat -c%s "$f") bytes) — terminating the run"
        break
      fi
    elif [ $SECONDS -ge $local_kill_after ]; then
      echo "watchdog: ${KILL_AFTER_HOURS}h cutoff reached — flushing and terminating"
      TIMED_OUT=1
      flush_jfr
      break
    else
      if [ $((SECONDS - LAST_FLUSH)) -ge $flush_every ]; then
        flush_jfr
        LAST_FLUSH=$SECONDS
      fi
      sleep 30
    fi
  done

  # Final graceful flush while the daemon is still alive (healthy sync or
  # cutoff): JFR.dump snapshots the buffered recording. If the daemon already
  # exited cleanly, dumponexit=true wrote the file itself.
  if [ -z "$DUMP_FOUND" ]; then
    pid=$(daemon_pid)
    if [ -n "$pid" ]; then
      echo "watchdog: final flush (pid $pid)"
      timeout 120 jcmd "$pid" JFR.dump name=measure filename="$WS/profile/jdk-measure.jfr" 2>&1 | tail -2 || true
    fi
  fi

  kill -KILL $PROF 2>/dev/null || true
  pkill -KILL -f gradle-profiler 2>/dev/null || true
  pkill -KILL -f GradleDaemon 2>/dev/null || true
  pkill -KILL -f studio 2>/dev/null || true
  wait $PROF 2>/dev/null; rc=$?
  echo "gradle-profiler exit code: $rc (dump=$DUMP_FOUND timed_out=$TIMED_OUT)" | tee -a profiler.log
  echo "rc=$rc" >> "${GITHUB_OUTPUT:-/dev/null}"
  echo "timed_out=$TIMED_OUT" >> "${GITHUB_OUTPUT:-/dev/null}"
  echo "dump=$DUMP_FOUND" >> "${GITHUB_OUTPUT:-/dev/null}"
  echo "== profiler.log tail =="
  tail -100 profiler.log || true
  exit 0
}

case "${1:-}" in
  setup) cmd_setup ;;
  run)   cmd_run ;;
  *) echo "usage: $0 {setup|run}" >&2; exit 2 ;;
esac
