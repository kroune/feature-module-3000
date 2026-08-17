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
- `.github/workflows/measure-commits.yml` — end-to-end comparison of two Gradle refs
  (e.g. an upstream commit vs a fork branch with an optimization) in a single run:
  resolves the refs to SHAs, builds both bin distributions from source
  (`:distributions-full:binDistributionZip`, JDK 25) and runs the sync benchmark
  against both in parallel. Built dists are cached as `gradle-build-<sha12>` release
  assets, so re-measuring a commit skips its ~35 min build. Default daemon heap here
  is `9g` (not 10g) — see the kernel OOM-killer gotcha below for why.
- `kroune/heap-report` (separate repo) — the dump viewer + MAT index builder. Both
  benchmark workflows end with a `Trigger MAT index build` step that fires a
  `repository_dispatch` there (`build-indexes` workflow → `idx-<tag>` release with
  zstd-compressed MAT indexes). The step is a silent no-op unless the
  **`HEAP_REPORT_PAT`** secret is set on this repo (PAT with Contents-RW on
  heap-report — GITHUB_TOKEN can't dispatch cross-repo) and a daemon dump exists.

## Commands

- Compare two Gradle refs: `gh workflow run measure-commits.yml`
  `-f base_repo=gradle/gradle -f base_ref=<sha>`
  `-f candidate_repo=kroune/gradle-fork -f candidate_ref=<branch>`
  (result releases: `run-<N>-base` / `run-<N>-candidate`).
- AI-driven branch benchmark: the `/measure-oom` project skill
  (`.kimi-code/skills/measure-oom/SKILL.md`) takes a fork-branch link, dispatches
  measure-commits with base = merge-base against `gradle/gradle` master, watches the run
  (plus a recurring 30-min stuck-check cron), retries infra failures (max 2), and walks
  `daemon_xmx` down 1g at a time (floor 3g) until the candidate OOMs. Explicit user
  invocation only — the model must never dispatch a benchmark on its own.
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
  **JDK 25+ writes gzipped heap dumps in parallel segments**: the main `.hprof.gz`
  holds the header (small and size-stable) while workers write `*.p0`/`*.p1` segments
  that are merged at the end. A size-stable main file is NOT enough — the watchdog must
  keep waiting while any `*.p[0-9]` segment exists, or it kills the daemon mid-dump and
  publishes a header-only file (observed: 14.7 MB "dump" + a 1 GB orphan `.p0`).
- `warm-ups = 0` requires `--single-shot` (scenario validation fails otherwise).
- Upstream gradle-profiler waits only 60 s for the IDE plugin to connect; opening 9000
  modules takes longer. Use the patched build from the `patched-profiler` release
  (default input); rebuild via `build-profiler.yml` when bumping the profiler version.
- On CI the IDE must run headless: `GRADLE_PROFILER_OPTS=-Dide.tests.headless=true`.
  Full-GUI Studio under xvfb hangs in project frame creation and the sync never starts.
- Heap dumps: `-XX:HeapDumpGzipLevel=1` makes the JVM write gzipped hprofs directly
  (no raw dump on disk). Runners have ~100 GB of SSD (the older "14 GB" note here was
  wrong), so disk pressure is not a concern for builds; keep the gzip setting anyway —
  it keeps release uploads small (assets are capped at 2 GiB, see below).
  `HeapDumpPath` must point to a **directory** (per-PID filenames); the JVM refuses to
  overwrite existing files (`O_EXCL`), so FIFO tricks do not work.
- GitHub release assets are capped at 2 GiB — the release step splits larger dumps into
  `daemon.hprof.gz.part-*` (reassemble with `cat`). Actions artifact downloads are very
  slow (~0.2 MB/s); releases are the primary distribution channel.
- The runner is 16 GB: keep daemon `-Xmx` + IDE `-Xmx` low enough that the *JVM* throws
  OOM before the kernel OOM-killer fires (kernel kills write no dump). If `-Xmx` is too
  high the daemon sits at max RSS while the IDE model-fetch squeezes the box, and the
  kernel kills the daemon (it carries `oom_score_adj=500` — the first target). The
  signature: the sync step dies ~35-45 min in with exit 143 / "The operation was
  canceled" / "hosted runner lost communication", all `if: always()` steps get skipped,
  and nothing is published. Confirm via `dmesg`: `Out of memory: Killed process ...
  (java)`. For 9.8.0-era Gradle with the default 4g IDE heap, **9g works, 10g loses the
  race** (10g was fine for 9.7.0-rc-1, which JVM-OOM'd earlier).
- The zram step currently fails open: `modprobe zram` → "Module zram not found" on the
  6.17-azure kernel of current ubuntu-latest images, so runs fall back to the runner's
  default 3 GB `/swapfile`. Keep the step (it may come back), but don't count on the 8G.
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
