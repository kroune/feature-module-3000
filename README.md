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
