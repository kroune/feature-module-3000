#!/usr/bin/env bash
# Resolve a Gradle source ref (branch / tag / short or full SHA in any GitHub repo)
# to the cached bin-distribution release in the results repo, so workflows never
# hand-compose gradle-build-<sha12> URLs.
#
# Release contract (produced by the build jobs in measure-commits.yml and
# profile-sync.yml):
#   tag    gradle-build-<sha12>
#   asset  gradle-<sha12>-bin.zip    (sha12 = first 12 chars of the full commit SHA)
#
# Commands:
#   resolve <src_repo> <ref>                stdout: full commit SHA
#   coordinates <results_repo> <sha>        stdout: KEY=VALUE lines (sha12, tag, asset, url)
#   exists <results_repo> <tag>             exit 0 when the release already exists
#
# Requires: gh (authenticated), coreutils.
set -euo pipefail

cmd="${1:?usage: gradle_dist.sh resolve <src_repo> <ref> | coordinates <results_repo> <sha> | exists <results_repo> <tag>}"

case "$cmd" in
  resolve)
    src_repo="${2:?missing src_repo}"; ref="${3:?missing ref}"
    gh api "repos/$src_repo/commits/$ref" -q .sha
    ;;
  coordinates)
    results_repo="${2:?missing results_repo}"; sha="${3:?missing sha}"
    sha12=$(echo "$sha" | cut -c1-12)
    tag="gradle-build-$sha12"
    asset="gradle-$sha12-bin.zip"
    url="https://github.com/$results_repo/releases/download/$tag/$asset"
    printf 'sha12=%s\ntag=%s\nasset=%s\nurl=%s\n' "$sha12" "$tag" "$asset" "$url"
    ;;
  exists)
    results_repo="${2:?missing results_repo}"; tag="${3:?missing tag}"
    gh release view "$tag" -R "$results_repo" >/dev/null 2>&1
    ;;
  *)
    echo "unknown command: $cmd" >&2
    exit 2
    ;;
esac
