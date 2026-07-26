# AGENTS.md

## What this repo is

A **synthetic 3000-module Android project** used to reproduce the Gradle daemon
`OutOfMemoryError` during **Android Studio Gradle sync** on a 16 GB machine, plus the
GitHub Actions machinery that reproduces it on demand and publishes heap dumps.
It is a benchmark/repro harness — there is no production code and nothing to "fix" in
the app modules themselves.

## Layout

- `api/f<N>-api`, `impl/f<N>-impl`, `ui/f<N>-ui` — the 3000 feature triplets (N = 0..999
  per tier), plus `foundation/`, `core/`, `common/` libraries and one `app/app` module.
  All generated, all follow the same pattern; changes should be made by generator-style
  edits across the board, not by hand-editing individual modules.
- `build-logic/` — included build with the convention plugins
  (`CompositeBuildPluginAndroid{App,Lib,KmpLib}.kt`, `compileSdk = 37`).
- `settings.gradle.kts` — ~3000 `include` lines; `build.gradle.kts` is near-empty.
- `gradle.properties` — the heart of the repro: `-Xmx10g -Xms7g`,
  `-XX:+HeapDumpOnOutOfMemoryError` for both the Gradle and Kotlin daemons,
  configuration cache + Isolated Projects + `org.gradle.tooling.parallel=true`.
- `sync.scenarios` — gradle-profiler scenario (`studio-sync`, zero warm-ups);
  `@HEAP_DUMP_DIR@` is substituted with an absolute path before use.
- `.github/workflows/sync-benchmark.yml` — the benchmark (see README for the full
  run-down). `.github/workflows/build-profiler.yml` — builds the patched gradle-profiler
  and publishes it under the `patched-profiler` release tag.

## Commands

- Trigger a benchmark run: `gh workflow run sync-benchmark.yml`
  (inputs: `gradle_distribution_url`, `daemon_xmx`, `studio_url`, `gradle_profiler_url`;
  use `-f daemon_xmx=2g` for a ~12 min smoke run).
- Rebuild the patched profiler: `gh workflow run build-profiler.yml`.
- Check results: `gh release list` — each run publishes a `run-N` release with the dump
  and logs. A run is successful when its `Verdict` step is green (daemon dump captured,
  or sync completed without OOM).
- Local repro: see README ("Running locally").

## Hard-won gotchas (don't rediscover these)

- gradle-profiler's build inspection runs `:help` (full configuration), and its TAPI
  call **hangs forever when the daemon OOMs mid-call**. The workflow's watchdog polls
  for a size-stable `heap-dumps/daemon/*.hprof.gz` and kills the profiler tree — keep
  that mechanism intact; without it every run burns the full job timeout.
- `warm-ups = 0` requires `--single-shot` (scenario validation fails otherwise).
- Upstream gradle-profiler waits only 60 s for the IDE plugin to connect; opening 9000
  modules takes longer. Use the patched build from the `patched-profiler` release
  (default input); rebuild via `build-profiler.yml` when bumping the profiler version.
- On CI the IDE must run headless: `GRADLE_PROFILER_OPTS=-Dide.tests.headless=true`.
  Full-GUI Studio under xvfb hangs in project frame creation and the sync never starts.
- Heap dumps: `-XX:HeapDumpGzipLevel=1` makes the JVM write gzipped hprofs directly
  (no raw dump on disk — essential on the 14 GB runner SSD). `HeapDumpPath` must point
  to a **directory** (per-PID filenames); the JVM refuses to overwrite existing files
  (`O_EXCL`), so FIFO tricks do not work.
- GitHub release assets are capped at 2 GiB — the release step splits larger dumps into
  `daemon.hprof.gz.part-*` (reassemble with `cat`). Actions artifact downloads are very
  slow (~0.2 MB/s); releases are the primary distribution channel.
- The runner is 16 GB: keep daemon `-Xmx` + IDE `-Xmx` low enough that the *JVM* throws
  OOM before the kernel OOM-killer fires (kernel kills write no dump). zram swap is the
  insurance — a swap *file* would eat the disk.
- Android SDK: API 37 is published as `platforms;android-37.0` (not `android-37`).
- Android Studio "Quail" downloads are named by codename
  (`android-studio-quail2-linux.tar.gz`), not by version.
- Facts about the repro itself: configuration fits in 10 g, the OOM is in the IDE
  model-fetch phase; the 10g dump decompresses to ~18 GB (~1.8× hprof inflation from
  8-byte reference IDs).

## Conventions

- **Never commit** machine-specific paths or analysis artifacts. `org.gradle.java.home`
  lives in `~/.gradle/gradle.properties`, not here. Heap dumps (`*.hprof*`), MAT index
  files (`*.index`, `*.threads`), `gc-logs/`, `heap-report/`, `leak_report/`,
  `workspace/`, `graph.dot`, profiler outputs (`results/`, `heap-dumps/`,
  `gradle-user-home/`, `profile-out*/`, `studio-sandbox/`, `sync.scenarios.ci`) are all
  gitignored on purpose — they can be huge (tens of GB).
- `local.properties` (SDK path) is also local-only; CI writes it from `$ANDROID_HOME`.
- Workflow changes: validate YAML before pushing, then smoke-test with
  `gh workflow run sync-benchmark.yml -f daemon_xmx=2g` (~12 min) before a full run.
- The git history is self-contained — this directory used to sit untracked inside a
  larger experiments repo; do not re-embed it.
