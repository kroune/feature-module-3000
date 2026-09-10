#!/usr/bin/env bash
# publish.sh — assemble measure-jfr results and publish them as a GitHub release.
#
# Compresses recordings/dumps, copies the effective configs as evidence, splits
# any asset past the 2 GiB release cap, then (re)creates the release.
#
# Env:  RELEASE_TAG, RELEASE_TITLE, REPO (owner/name), SUMMARY_MD,
#       GRADLE_USER_HOME (for the GUH gradle.properties evidence copy)
# Cwd:  project root (GITHUB_WORKSPACE).
set -euo pipefail

WS="${GITHUB_WORKSPACE:-$PWD}"
cd "$WS"
GUH="${GRADLE_USER_HOME:-/nonexistent}"
SUMMARY_MD="${SUMMARY_MD:-summary.md}"

# --- compress + evidence -----------------------------------------------------
for f in profile/*.jfr; do [ -e "$f" ] && gzip -1 "$f" || true; done
for f in graph/*.jfr; do [ -e "$f" ] && gzip -1 "$f" || true; done
find heap-dumps -name '*.hprof' -exec gzip -1 {} \; 2>/dev/null || true
if [ -f "$GUH/gradle.properties" ]; then cp "$GUH/gradle.properties" guh-gradle.properties; fi

# --- asset list --------------------------------------------------------------
assets=()
add() { if [ -f "$1" ]; then assets+=("$1"); fi; }
for f in profile/*.jfr.gz graph/memory.html graph/memory.csv graph/gc.csv graph/markers.csv \
         profiler.log "$SUMMARY_MD" sync.scenarios.ci gradle.properties guh-gradle.properties \
         gradle/wrapper/gradle-wrapper.properties \
         results/benchmark.csv results/benchmark.html results/studio-sandbox/logs/idea.log; do
  add "$f"
done

# Largest heap dump, if any (marker-triggered or OOM insurance).
dump=$(ls -S heap-dumps/daemon/*.hprof.gz 2>/dev/null | head -1) || true
if [ -n "$dump" ]; then
  cp "$dump" daemon.hprof.gz
  size=$(stat -c%s daemon.hprof.gz)
  if [ "$size" -gt 2000000000 ]; then
    split -b 1900m daemon.hprof.gz daemon.hprof.gz.part-
    rm daemon.hprof.gz
    assets+=(daemon.hprof.gz.part-*)
    {
      echo
      echo "**Note:** \`daemon.hprof.gz\` exceeds the 2 GiB release-asset limit and is split into parts. Reassemble with:"
      echo '```'
      echo "cat daemon.hprof.gz.part-* > daemon.hprof.gz"
      echo '```'
    } >> "$SUMMARY_MD"
  else
    assets+=(daemon.hprof.gz)
  fi
fi

# Last-resort split for anything else past the cap.
for f in "${assets[@]}"; do
  if [ -f "$f" ] && [ "$(stat -c%s "$f")" -gt 2000000000 ]; then
    split -b 1900m "$f" "$f.part-" && rm "$f"
    assets+=("$f".part-*)
  fi
done

echo "Publishing ${#assets[@]} assets to $REPO@$RELEASE_TAG"
gh release delete "$RELEASE_TAG" --yes -R "$REPO" 2>/dev/null || true
gh release create "$RELEASE_TAG" -R "$REPO" \
  --title "$RELEASE_TITLE" \
  --notes-file "$SUMMARY_MD" \
  "${assets[@]}"
