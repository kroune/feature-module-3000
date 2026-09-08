# AGENTS.md

## What this repo is

Synthetic 3000-module Android project that reproduces the Gradle daemon OOM during
Android Studio sync on a 16 GB machine, plus GitHub Actions machinery that reproduces
it on demand and publishes heap dumps. Benchmark harness — nothing to "fix" in app
modules.

## Layout

- `api/f<N>-api`, `impl/f<N>-impl`, `ui/f<N>-ui` — 3000 feature triplets (N = 0..999
  per tier), plus `foundation/`, `core/`, `common/`, one `app/app` module. All
  generated: change them with generator-style edits across the board, never by hand.
- `build-logic/` — included build with convention plugins (`compileSdk = 37`).
- `gradle.properties` — the repro: `-Xmx10g`, heap-dump-on-OOM for Gradle and Kotlin
  daemons, config cache + Isolated Projects + tooling parallel.
- Workflows in `.github/workflows/`: `sync-benchmark.yml` (the base benchmark),
  `build-profiler.yml` (patched gradle-profiler release), and three A/B comparison
  twins differing in what varies between legs: `measure-commits.yml` (Gradle refs,
  releases `run-N-{base,candidate}`), `measure-idea-commits.yml` (IntelliJ refs,
  `run-idea-N-*`), `measure-agp-commits.yml` (AGP refs, `run-agp-N-*`). Built dists are
  cached as release assets keyed by SHA (`gradle-build-*`, `idea-build-*`, `agp-build-*`).

## Commands

- Smoke test (~12 min): `gh workflow run sync-benchmark.yml -f daemon_xmx=2g`
- Results: `gh release list` — a run is successful when its `Verdict` step is green.
- `/measure-oom` and `/measure-idea-oom` skills are manual-only — never dispatch a
  benchmark on your own; the user triggers every run.
- Local repro: see README.

## Gotchas

- **build-logic does not see the root gradle.properties.** Any property (e.g.
  `agpOverrideRepoUrl`) must be appended to both `gradle.properties` and
  `build-logic/gradle.properties` or build-logic silently compiles against the catalog.
- **Daemon OOM hangs gradle-profiler's TAPI call forever.** The workflow watchdog
  kills the profiler tree on a size-stable daemon dump or on GC thrash detected in
  `gc-logs/daemon-gc-%p.log` — keep it intact. **JDK 25+ writes dumps in segments**:
  wait while any `*.p[0-9]` file exists, or you kill the daemon mid-dump and publish
  a header-only file.
- Thrash detector (measure-commits.yml, inputs `thrash_*`): two limbs over consecutive
  non-System full GCs — hard (>=97% live OR <2% reclaimed → fire) and slow (>=90% live
  for 10 continuous minutes → fire). Calibrated on captured logs with known outcomes:
  a healthy 9g sync sits at 89-95% live with 5-11% reclaims for ~4 min at peak model
  fetch (a bare ">=90% x5" rule false-kills it — run 34180871620); true spirals
  reclaim ~0.1%. `thrash_watchdog=false` + `publish_dumps=false` = cheap ground-truth
  runs. Never parse `8379M(9216M` with `awk -F'[M(]'` — consecutive separators leave
  `$2` empty and any occupancy test becomes always-true.
- Runner-image drift moves the OOM threshold between identical SHAs: candidate synced
  at 8g on image 20260816.277, OOMs at 8g on 20260831.293 (6/6 runs, real dumps;
  JDK build exonerated via `jdk_version` pin). As of Sep 2026: **candidate fits at 9g,
  base (upstream) grinds forever at 9g and OOMs at <=8g** — 9g is the A/B operating
  point. Re-baseline after image bumps.
- Runner is 16 GB: daemon `-Xmx` must be low enough that the *JVM* throws OOM before
  the kernel OOM-killer (kernel kills write no dump; signature is exit 143 with all
  `if: always()` steps skipped). For 9.8.0-era Gradle with 4g IDE heap: **9g works,
  10g loses the race**.
- Upstream gradle-profiler waits only 60 s for the IDE plugin — use the patched build
  from the `patched-profiler` release. On CI the IDE must run headless:
  `GRADLE_PROFILER_OPTS=-Dide.tests.headless=true`.
- `warm-ups = 0` requires `--single-shot`.
- GitHub release assets are capped at 2 GiB → larger dumps are split into
  `*.part-*` (reassemble with `cat`). Actions artifact downloads are ~0.2 MB/s;
  releases are the distribution channel.
- `HeapDumpPath` must be a directory; the JVM uses `O_EXCL`, no overwrite/FIFO tricks.
- Android SDK: API 37 is `platforms;android-37.0`. Studio "Quail" downloads are named
  by codename, not version.
- S3 uploads go through `tools/s3_upload.sh` (CRC64 checksums disabled for SeaweedFS,
  HEAD-verified, retried — aws-cli exit status alone lies). Multi-GB files upload one
  at a time from a dedicated `upload-s3` job; concurrent uploads starve the lane.
  S3 steps are `continue-on-error`; the GitHub release is the source of truth.
- `HEAP_REPORT_PAT` secret gates the cross-repo MAT-index dispatch to kroune/heap-report
  (silent no-op without it).
- Repro facts: configuration fits in 10g; OOM is in the IDE model-fetch phase; the 10g
  dump decompresses to ~18 GB.

## Conventions

- Never commit analysis artifacts (dumps, indexes, profiler outputs, `gc-logs/`,
  `heap-report/`, `workspace/` etc. — all gitignored). `org.gradle.java.home` lives in
  `~/.gradle/gradle.properties`, not here. `local.properties` is local-only.
- Workflow changes: validate YAML, then smoke-test with the `daemon_xmx=2g` run above
  before a full run.
