# feature-module-3000 — Gradle sync OOM repro

Synthetic Android project with **3000 feature modules** (`api/` + `impl/` + `ui/` per
feature, plus `foundation/`, `core/`, `common/`, `app/`) used to reproduce the Gradle
daemon `OutOfMemoryError` during **Android Studio Gradle sync** on a 16 GB machine.

- AGP 9.2.1, Kotlin 2.4.0, Gradle 9.7.0-rc-1 (wrapper), configuration cache +
  Isolated Projects + parallel tooling model fetch enabled (see `gradle.properties`).
- The daemon is capped at `-Xmx10g`; the sync needs more, so it always OOMs and writes
  a gzipped heap dump (`-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpGzipLevel=1`).

## Running the benchmark on GitHub Actions

Actions → **sync-benchmark** → *Run workflow*. Inputs:

| Input | Default | Meaning |
|---|---|---|
| `gradle_distribution_url` | `gradle-9.7.0-rc-1-bin.zip` | Any public Gradle zip URL — stable, RC, nightly, or a self-hosted custom build. Rewrites the wrapper for that run; the resolved version is printed in the log and job summary. |
| `daemon_xmx` | `10g` | Gradle daemon heap (`-Xmx` = `-Xms`). Lower it for a quick smoke run (e.g. `2g` OOMs early). |
| `studio_url` | Studio 2026.1.2 (Quail 2) | Android Studio `linux.tar.gz` URL — pick any build from [the releases list](https://jb.gg/android-studio-releases-list.json). |

The job runs one cold Android Studio sync via gradle-profiler (`--single-shot`) on a 16 GB `ubuntu-latest` runner and uploads the artifact
**sync-benchmark-dumps** (retained 7 days):

- `heap-dumps/daemon/java_pid*.hprof.gz` — the Gradle daemon heap dump (the point of the repro)
- `heap-dumps/ide/java_pid*.hprof.gz` — IDE dump, if the IDE died instead
- `results/` — gradle-profiler benchmark report, `profiler.log`, Studio sandbox logs
- the exact `gradle.properties`, wrapper properties and scenario file used

CI runs the Gradle daemon on **JDK 25** with `-XX:+UseCompactObjectHeaders` (compressed
object headers); the workflow patches that in, along with the absolute `HeapDumpPath`.

## Running locally

Requirements: JDK 21+, Android Studio 2026.1+, gradle-profiler 0.25.2+, Android SDK
with platform android-37.

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

Dumps land in `heap-dumps/`, the benchmark report in `results/`.

Note: `org.gradle.java.home` is deliberately not committed — set it in
`~/.gradle/gradle.properties` if your machine needs a pinned JDK.
