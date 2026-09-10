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
  The `gradle-build-*` cache contract (tag `gradle-build-<sha12>`, asset
  `gradle-<sha12>-bin.zip`) is shared through `tools/gradle_dist.sh`
  (`resolve` / `coordinates` / `exists`) — used by both measure-commits.yml and
  profile-sync.yml, never hand-compose those URLs.
  `profile-sync.yml` profiles a healthy Studio sync of a chosen repo ref (`ref` input,
  releases `run-profile-N-*`): async-profiler (cpu+alloc+wall into one JFR) and JDK JFR
  (`settings=profile`) both start with the daemon via `org.gradle.jvmargs`; the watchdog
  flushes both periodically (non-stopping `asprof dump` + `jcmd JFR.dump`) and once more
  before teardown — SIGKILLing the daemon would destroy both recordings. A
  `kill_after_hours` cutoff (default 5h) keeps the run inside the 6h runner cap.
  The Gradle under test is either `gradle_repo`@`gradle_ref` (resolved to the
  `gradle-build-<sha12>` cache, built on demand if missing) or, when `gradle_ref`
  is empty, a direct `gradle_distribution_url`.
  `measure-jfr.yml` is the memory-first successor to OOM-watching (releases
  `run-jfr-N`): daemon JFR with Gradle's build-operation events
  (`org.gradle.internal.operations.jfr=true`, Gradle >= 9.7.0) as markers plus a
  custom periodic `org.gradle.measure.HeapUsage` event from an in-daemon sampler
  — the dense memory graph. Setting the `dump_marker` regex input turns the run
  into a deterministic dump-at-marker: an in-daemon `EventStream` consumer calls
  `HotSpotDiagnosticMXBean.dumpHeap` right after the Nth matching operation.
  All orchestration lives in `tools/jfr/` (`measure.sh` setup/run,
  `jfr-measure.init.gradle`, `render_memory_graph.py`, `publish.sh`); the
  workflow is thin glue. Flow: measurement run → pick a marker from
  `markers.csv` in the release → re-dispatch with `dump_marker`. Real runs
  always use the 9g default — the point is avoiding OOM, and the Verdict only
  accepts the JFR snapshot / memory graph / marker dump (validated end-to-end
  by smoke run 34520571109).

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
- OOM is non-deterministic at a fixed heap: under high concurrency,
  heavily-allocating threads may or may not pile up at the same moment — the
  same SHA can fit or OOM at the same `-Xmx`. As of Sep 2026: **candidate fits
  at 9g, base (upstream) grinds forever at 9g and OOMs at <=8g** — 9g is the
  A/B operating point.
- Runner is 16 GB: daemon `-Xmx` must be low enough that the *JVM* throws OOM before
  the kernel OOM-killer (kernel kills write no dump; signature is exit 143 with all
  `if: always()` steps skipped). For 9.8.0-era Gradle with 4g IDE heap: **9g works,
  10g loses the race**.
- **gradle-profiler ignores `GRADLE_USER_HOME` env for studio-sync** — it defaults to
  `<project>/gradle-user-home` (run 34518694398). Pass `--gradle-user-home` explicitly;
  anything GUH-dependent (init.d scripts, GUH gradle.properties) must also be mirrored
  into the project-dir fallback.
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
