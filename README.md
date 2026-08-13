# feature-module-3000 — Gradle sync OOM repro

Synthetic Android project with **3000 feature modules** (`api/` + `impl/` + `ui/` per
feature, plus `foundation/`, `core/`, `common/`, `app/`) used to reproduce the Gradle
daemon `OutOfMemoryError` during **Android Studio Gradle sync** on a 16 GB machine.

- AGP 9.2.1, Kotlin 2.4.0, Gradle 9.7.0-rc-1 by default (wrapper), configuration cache +
  Isolated Projects + parallel tooling model fetch enabled (see `gradle.properties`).
- The daemon is capped at `-Xmx10g`; the sync needs more, so it always OOMs and writes
  a gzipped heap dump (`-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpGzipLevel=1`).
  On CI the daemon runs on **JDK 25** with `-XX:+UseCompactObjectHeaders`.

## Running the benchmark on GitHub Actions

Actions → **sync-benchmark** → *Run workflow*. Inputs:

| Input | Default | Meaning |
|---|---|---|
| `gradle_distribution_url` | `gradle-9.7.0-rc-1-bin.zip` | Any public Gradle zip URL — stable, RC, nightly, or a self-hosted custom build. Rewrites the wrapper for that run; the resolved version is printed in the log and job summary. |
| `daemon_xmx` | `10g` | Gradle daemon heap (`-Xmx` = `-Xms`). Lower it for a quick smoke run (e.g. `2g` OOMs early, ~12 min end to end). |
| `studio_url` | Studio 2026.1.2 (Quail 2) | Android Studio `linux.tar.gz` URL — pick any build from [the releases list](https://jb.gg/android-studio-releases-list.json). |
| `gradle_profiler_url` | patched 0.25.2 (this repo's releases) | gradle-profiler dist zip. The default build ships longer IDE-connect timeouts (upstream hardcodes 60s, too short for 9000 modules — see the `build-profiler` workflow). |

What a run does, end to end (~45 min at defaults):

1. Frees runner disk, adds zram swap (insurance against the kernel OOM-killer so the
   *JVM* throws — kernel kills write no dump), sets up JDK 25 + Android SDK
   (`platforms;android-37.0`), pins the Gradle wrapper from `gradle_distribution_url`,
   and patches `gradle.properties` with the heap cap, per-PID `HeapDumpPath`
   (`heap-dumps/daemon/`, `heap-dumps/kotlin/`) and compact object headers.
2. Runs one cold, headless Android Studio sync via gradle-profiler (`--single-shot`,
   `GRADLE_PROFILER_OPTS=-Dide.tests.headless=true`).
3. When the daemon OOMs, the JVM writes `heap-dumps/daemon/java_pid*.hprof.gz` directly
   (gzip level 1 — a ~18 GB raw dump lands as ~4.9 GB; no raw dump ever touches disk).
   gradle-profiler hangs forever once the daemon dies mid-call, so a **watchdog** in the
   workflow kills it ~1 min after the dump finishes instead of waiting for the timeout.
4. Publishes results two ways:
   - **A `run-N` release per run** (permanent, fast public download URLs — the primary
     channel): `daemon.hprof.gz` (split into `daemon.hprof.gz.part-*` when over the
     2 GiB asset limit; `cat daemon.hprof.gz.part-* > daemon.hprof.gz` to reassemble),
     `profiler.log`, benchmark CSV/HTML, the Studio `idea.log`, and the exact
     `gradle.properties` / wrapper properties / scenario file used. Release title and
     notes carry the Gradle version, heap settings and outcome.
   - **A `sync-benchmark-dumps` Actions artifact** (kept 7 days): everything above plus
     the full sandbox logs. Handy for debugging; artifact downloads are slow (~0.2 MB/s
     vs ~5 MB/s from releases).

The job is green when the daemon dump exists (OOM reproduced) or the sync completed
without OOM; red otherwise.

## Heap-dump analysis: `heap-report`

Daemon dumps are analyzed with the viewer at
**[kroune/heap-report](https://github.com/kroune/heap-report)**: after each run
release is published, the workflows here fire a `repository_dispatch` (guarded by
the `HEAP_REPORT_PAT` secret; skipped silently when unset or when no dump exists)
that triggers its `build-indexes` workflow — an Eclipse MAT parse on a CI runner,
published as an `idx-<tag>` release (zstd-compressed indexes). The viewer's local
UI (`serve.py` → Remote tab) autodiscovers `run-*`/`idx-*` releases, downloads
dump + indexes on demand, and runs only the cheap analysis locally — no local
40 min MAT parse. See that repo's README for setup.

**Choosing `daemon_xmx`:** the daemon heap must be small enough that the *JVM* throws
`OutOfMemoryError` before the 16 GB runner's kernel OOM-killer picks the daemon
(kernel kills write no dump). With Gradle 9.7.0-rc-1, 10g works; with newer Gradle
(9.8.0-era) 10g loses that race — use **9g**. The failure signature is a step dying
~35–45 min into the sync with exit 143 / "The operation was canceled" / "hosted runner
lost communication", all `if: always()` steps skipped and no artifacts published.
Also note the zram step currently fails open (`modprobe zram` finds no module on the
6.17-azure kernel), so runs fall back to the runner's default 3 GB swapfile.

## The `measure-commits` workflow

Actions → **measure-commits** → *Run workflow* — end-to-end memory comparison of two
Gradle refs (typically an upstream commit vs a fork branch with an optimization) in a
single run: `resolve → build (both in parallel) → sync (both in parallel)`.

```bash
gh workflow run measure-commits.yml \
  -f base_repo=gradle/gradle -f base_ref=<sha> \
  -f candidate_repo=kroune/gradle-fork -f candidate_ref=<branch>
```

| Input | Default | Meaning |
|---|---|---|
| `base_repo` / `base_ref` | `gradle/gradle` / (required) | Baseline Gradle checkout — branch, tag, or SHA. |
| `candidate_repo` / `candidate_ref` | `kroune/gradle-fork` / (required) | Candidate Gradle checkout — branch, tag, or SHA. |
| `daemon_xmx` | `9g` | Daemon heap for both syncs. Lower than sync-benchmark's 10g on purpose — see the warning above. |
| `studio_url`, `gradle_profiler_url` | same as sync-benchmark | Same meaning as in sync-benchmark. |

What a run does:

1. `resolve` turns both refs into full SHAs (via the GitHub API).
2. `build` (one job per ref, parallel) compiles the bin distribution from source
   (`:distributions-full:binDistributionZip`, JDK 25 + 21/17 toolchains) and publishes
   it as `gradle-<sha12>-bin.zip` under the `gradle-build-<sha12>` release tag in this
   repo. The build is **skipped entirely when that release already exists**, so
   re-measuring a commit costs no rebuild. Build time ranges from minutes (remote
   build-cache hits) to ~1–2 h cold.
3. `sync` (one job per ref, parallel) runs the exact same cold headless Studio sync as
   sync-benchmark against the freshly built distribution and publishes
   **`run-<N>-base` / `run-<N>-candidate`** releases with the daemon heap dump, logs,
   and configs. Both legs use identical settings, so the two dumps are directly
   comparable.

Green means the same as in sync-benchmark (per leg): dump captured, or sync completed
without OOM.

### Observed behavior (baseline, Gradle 9.7.0-rc-1, JDK 25)

- Full configuration (`:help`, run by gradle-profiler's inspection) fits in 10 g — the
  OOM happens later, during the IDE model-fetch phase of the sync.
- The daemon dump at `-Xmx10g` decompresses to **~18 GB** (~1.8× the live heap — hprof
  stores references as 8-byte IDs even with compressed oops) and matches local dumps
  from the same repro in size and shape.

## The `build-profiler` workflow

Actions → **build-profiler** → *Run workflow* clones gradle-profiler (default `v0.25.2`),
patches the IDE-connect timeouts in
`src/main/java/org/gradle/profiler/ide/process/IdeProcess.java`
(`IDE_START`/`PLUGIN_CONNECT`/`AGENT_CONNECT` → 10/30/30 min), builds `distZip`, and
publishes it as a release asset under the `patched-profiler` tag. This is the default
`gradle_profiler_url`. Why it's needed: upstream waits only 60 s for the IDE plugin to
connect, but opening 9000 modules takes longer, so the sync never starts.

## Running locally

Requirements: JDK 21+, Android Studio 2026.1+, gradle-profiler 0.25.2+ (prefer the
patched build from this repo's releases), Android SDK with platform android-37
(`platforms;android-37.0`).

```bash
echo "sdk.dir=$ANDROID_HOME" > local.properties   # required by gradle-profiler
mkdir -p heap-dumps
sed "s|@HEAP_DUMP_DIR@|$PWD/heap-dumps|" sync.scenarios > sync.scenarios.ci

gradle-profiler --benchmark \
  --single-shot \
  --project-dir . \
  --scenario-file sync.scenarios.ci \
  --studio-install-dir /path/to/android-studio \
  --output-dir results
```

Dumps land in `heap-dumps/`, the benchmark report in `results/`. On a headless machine
add `GRADLE_PROFILER_OPTS=-Dide.tests.headless=true` (full-GUI Studio hangs on project
frame creation without a display).

Note: `org.gradle.java.home` is deliberately not committed — set it in
`~/.gradle/gradle.properties` if your machine needs a pinned JDK.
