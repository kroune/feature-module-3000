---
name: measure-oom
description: Sync OOM benchmark for a Gradle fork branch (manual-only)
whenToUse: Never — manual invocation only
disableModelInvocation: true
---

# measure-oom

Drive the `measure-commits.yml` workflow in `kroune/feature-module-3000` end-to-end for
one candidate ref: dispatch, watch, retry infra failures, detect OOM vs no-OOM, and walk
the daemon heap down until OOM is reproduced. Everything runs through `gh`; never modify
or commit anything in this repo.

**Explicit invocation only.** Never dispatch `measure-commits` on your own initiative —
the user triggers every benchmark themselves. Only the heap-loop re-dispatches in §7 are
part of an invoked run and need no separate approval.

Input: `$ARGUMENTS` — one GitHub link or ref (see parsing below).

All commands target `-R kroune/feature-module-3000` (the repo this skill lives in).

## 1. Parse the input into `CAND_REPO` + `CAND_REF`

Accepted forms:

- `https://github.com/<owner>/<repo>/tree/<ref>` — everything after `/tree/` is the ref
  (refs may contain slashes; strip any trailing `/`).
- `https://github.com/<owner>/<repo>/commit/<sha>`
- `<owner>/<repo>#<ref>`, `<owner>/<repo>@<ref>`, or `<owner>/<repo> <ref>`
- bare `<ref>` → repo defaults to `kroune/gradle-fork`.

If the input is ambiguous and none of these match, ask the user.

## 2. Pre-flight: resolve refs

```bash
# Candidate ref -> full SHA (also validates the repo/branch exists). Use this SHA as
# candidate_ref for every dispatch so all heap iterations measure the same commit.
CAND_SHA=$(gh api "repos/$CAND_REPO/commits/$CAND_REF" -q .sha)

# Base = merge-base of the candidate with upstream gradle/gradle master.
if [ "$CAND_REPO" = "gradle/gradle" ]; then
  BASE_SHA=$(gh api "repos/gradle/gradle/compare/master...$CAND_REF" -q .merge_base_commit.sha)
else
  # cross-fork compare: head is <owner>:<repo>:<ref>
  BASE_SHA=$(gh api "repos/gradle/gradle/compare/master...${CAND_REPO/\//:}:$CAND_REF" -q .merge_base_commit.sha)
fi
```

(For a fork compare, build the head parameter as `owner:repo:ref`, e.g.
`repos/gradle/gradle/compare/master...kroune:gradle-fork:lazy-outgoing-publications`.)

If the ref doesn't resolve → tell the user the link/ref looks wrong, stop.
If the compare fails (repo not in the gradle/gradle fork network) → ask the user which
base ref to use instead.

## 3. Determine the starting heap

Do NOT hardcode the heap. Read the current workflow default (it changes over time):

```bash
awk '/^      daemon_xmx:/{f=1} f && /default:/{print; exit}' .github/workflows/measure-commits.yml
# -> default: "9g"   (parse the value, keep the unit)
```

Start at that value. Step size is 1g, floor is 3g (see §7).

## 4. Dispatch

```bash
DISPATCH_TS=$(date -u +%Y-%m-%dT%H:%M:%SZ)
gh workflow run measure-commits.yml -R kroune/feature-module-3000 \
  -f base_repo=gradle/gradle -f base_ref="$BASE_SHA" \
  -f candidate_repo="$CAND_REPO" -f candidate_ref="$CAND_SHA" \
  -f daemon_xmx="$HEAP"
sleep 20
RUN_ID=$(gh run list -R kroune/feature-module-3000 --workflow=measure-commits.yml \
  --json databaseId,createdAt --limit 10 \
  -q "[.[] | select(.createdAt > \"$DISPATCH_TS\")][0].databaseId")
```

Record RUN_ID and the run URL (`gh run view $RUN_ID -R ... --json url -q .url`) and tell
the user the run started, with the URL, the heap, base SHA and candidate SHA.

## 5. Watch (runs are long)

A run takes ~45-60 min with cached builds, up to ~3 h when a Gradle distribution must be
built from source. Set up BOTH of the following — never block the turn in a foreground
sleep loop:

**a) Background watcher task, no timeout:**

```bash
gh run watch "$RUN_ID" -R kroune/feature-module-3000 --interval 300 --exit-status
```

Non-zero exit just means a non-success conclusion — an expected path, classify it per §6.

**b) Recurring health check, every 30 min.** Pipelines break silently — a runner hangs, a
step gets stuck, the run sits queued — and the watcher alone won't tell you. Right after
dispatching, create a recurring cron (`9,39 * * * *`, off the herd marks) with a
self-contained prompt (fill in the real run id):

> Health check for measure-oom run <RUN_ID> in kroune/feature-module-3000. The full
> procedure is in `.kimi-code/skills/measure-oom/SKILL.md` (§6-§7) — read it if you need
> it.
> 1. `gh run view <RUN_ID> -R kroune/feature-module-3000 --json status,conclusion,number,jobs`
> 2. Run terminal (completed/cancelled) → delete this recurring check (CronList →
>    CronDelete the job whose prompt mentions run <RUN_ID>), then handle the outcome per
>    skill §6. If the run was already handled via the watcher task, just delete the cron.
> 3. Run looks stuck: still queued after 30+ min, or a step running past its sanity bound
>    ("Resolve refs" >10 min, "Build bin distribution" >2.5 h, "Run Android Studio sync"
>    >2 h — note the sync step legitimately takes 30-60 min). Confirm with the live log
>    tail: `gh api repos/kroune/feature-module-3000/actions/jobs/<jobDatabaseId>/logs | tail -50`
>    for the running job. No progress / repeating errors → `gh run cancel <RUN_ID> -R kroune/feature-module-3000`
>    and treat it as an infra failure per skill §6 (rerun --failed, consumes that run's
>    retry budget). Log shows healthy progress → do nothing.
> 4. Everything progressing normally → one short status line (which job/step, elapsed),
>    end the turn.

When the watcher (a) completes, go to §6.

## 6. Classify the completed run

On entry: delete the run's health-check cron if it still exists (CronList → the job whose
prompt mentions this run id → CronDelete), so terminal runs stop the checks. Then fetch
the state:

```bash
gh run view "$RUN_ID" -R kroune/feature-module-3000 \
  --json conclusion,number,url,jobs \
  -q '{conclusion, number, url, jobs: [.jobs[] | {name, conclusion, failedSteps: [.steps[] | select(.conclusion=="failure") | .name]}]}'
```

Retry budget: **2 automatic retries per run**, via
`gh run rerun "$RUN_ID" -R kroune/feature-module-3000 --failed`, then watch again (§5).
Each retry consumes budget; when the budget is exhausted, stop and report with logs.

**Success** (`conclusion: success`, both sync legs green): go to §7.

**Infra failures — retry automatically:**

- Run `cancelled`/`failure` where a sync leg died with no release published for it (the
  kernel OOM-killer signature: exit 143, "The operation was canceled", "hosted runner lost
  communication", all `if: always()` steps skipped — see AGENTS.md). This is the heap
  losing the race to the kernel, not a result — rerun.
- Failed steps in setup/plumbing: checkout, setup-java, Free disk space, Android SDK,
  Install gradle-profiler/Android Studio, downloads, "Publish … release", artifact upload.
- The zram step logging "Module zram not found" is **not** a failure (fails open) — ignore.

**Candidate/user-code failures — do NOT retry, report to the user:**

- Failed step "Resolve refs to SHAs" → bad repo/ref (shouldn't happen after §2; report).
- Failed step "Build bin distribution" → the code at that ref does not compile. Fetch the
  log tail (`gh run view "$RUN_ID" -R kroune/feature-module-3000 --log-failed | tail -200`),
  quote the actual compiler errors, and stop. If it was the **base** leg that failed,
  note that upstream's own build can be flaky and suggest a retry as an option.
- Sync leg failed AND its release exists with "failed, no dump" in the title → download
  `profiler.log` from that release
  (`gh release download <tag> -R kroune/feature-module-3000 -p profiler.log --clobber -D /tmp/measure-oom-$RUN_ID`)
  and inspect the tail: daemon failing to start or candidate-specific Gradle exceptions →
  report to the user with the excerpt. Obvious profiler/TAPI hang or watchdog kill with no
  daemon errors → treat as infra (retry, consumes budget). When genuinely unsure, prefer
  retrying once over bothering the user; if the same signature repeats, report.

## 7. Read the outcome; heap loop

Release tags are `run-<run_number>-base` and `run-<run_number>-candidate` (run **number**,
not run id). The release title carries the outcome:

```bash
gh release view "run-$RUN_NUMBER-candidate" -R kroune/feature-module-3000 --json name -q .name
# "... — OOM reproduced (N bytes gzipped)" | "... — sync completed, no OOM" | "... — failed, no dump"
```

Match "OOM reproduced" first, then "no OOM", then "failed".

- Candidate **OOM reproduced** → done. Report per §8.
- Candidate **no OOM** and heap > 3g → lower by 1g (e.g. 9g → 8g) and dispatch again (§4,
  same SHAs — and set up the new watcher + health-check cron per §5 for the new run id).
  Builds are cached as `gradle-build-<sha12>` releases, so iterations go
  straight to the sync phase (~45-60 min). Record base and candidate outcomes per heap —
  base data comes free and belongs in the final report.
- Candidate **no OOM at 3g** → stop, report "no OOM down to the 3g floor".

## 8. Final report

Report to the user, one line per heap level tried, e.g.:

```
| heap | base      | candidate |
| 9g   | OOM       | no OOM    |
| 8g   | OOM       | OOM       |  <- candidate OOM threshold
```

Plus: run URLs, release links (`run-<N>-candidate` carries `daemon.hprof.gz`, possibly
split into `daemon.hprof.gz.part-*` — mention the `cat` reassembly), the resolved
base/candidate SHAs, and the candidate's OOM threshold (or "no OOM down to 3g").

## Hard-won quirks (do not rediscover)

- gradle-profiler hangs forever if the daemon OOMs mid-call; the workflow's watchdog kills
  it once a size-stable dump exists. A hung-looking sync leg is normal — trust the watcher.
- Release assets are capped at 2 GiB; big dumps are split into parts.
- Never raise the heap above the workflow default to "make it pass" — on the 16 GB runner
  that flips the OOM from the JVM (dump written) to the kernel (run canceled, nothing
  published).
- `gh run rerun --failed` only reruns failed jobs — a green leg is not re-run.
