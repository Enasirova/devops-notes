# Jenkins Pipeline — Scaling Pipelines (Recap)

## Main Idea

Jenkins Pipelines store **transient execution data on disk frequently** so that they can:

- survive Jenkins restarts
- resume execution
- keep stage visualization

This durability has a **performance cost** because of frequent disk writes.

Jenkins provides **Speed/Durability settings** that allow users to trade:

```
more performance
vs
more durability (restart safety)
```

By default, Jenkins uses **maximum durability**.

---

# Why Scaling Pipelines Matters

Frequent disk writes can become a **bottleneck** when:

- many pipelines run simultaneously
- storage is slow
- pipelines are very large
- pipelines run many steps

Scaling features allow Jenkins to:

- reduce disk I/O
- improve performance
- run more pipelines concurrently

But at the cost of **less reliable recovery after crashes**.

---

# Speed vs Durability Trade-Off

Pipeline durability controls **how often Jenkins saves execution state to disk**.

Trade-off:

| Mode | Performance | Durability |
|-----|-----|-----|
| Maximum durability | slowest | safest |
| Less durable | faster | small risk |
| Performance optimized | fastest | lowest durability |

If Jenkins crashes unexpectedly, pipelines with lower durability may **not resume correctly**.

---

# How To Configure Durability Settings

Durability can be configured in **three places**.

## 1. Global setting

Location:

```
Manage Jenkins → System → Pipeline Speed/Durability Settings
```

This defines the **default for all pipelines**.

---

## 2. Per Pipeline Job

Each job can override the global setting.

Location:

```
Job configuration → Custom Pipeline Speed/Durability Level
```

Or via pipeline code:

```groovy
properties(...)
```

Note:

The change applies **from the next run**, not immediately.

---

## 3. Per Branch (Multibranch Pipelines)

For multibranch projects, durability can be set per branch using:

```
Branch Property Strategy
```

This allows different durability settings for:

- `main`
- `release`
- `feature` branches

---

# When Higher Performance Settings Help

Performance modes are helpful when Jenkins has:

- slow storage (NFS, magnetic disks)
- many pipelines running concurrently
- pipelines with **hundreds of steps**
- pipelines storing large variables
- large parsed files (JSON/XML)
- large arrays/maps in Groovy variables

Example cases:

- reading large configuration files
- collecting results from many parallel branches
- storing large summary objects

---

# When Performance Settings Will NOT Help

Performance mode will **not improve speed** when:

- pipelines mostly run long shell scripts
- pipelines wait on external tools
- pipelines generate huge logs
- Jenkins bottleneck is elsewhere
- pipelines are not used

Durability settings only affect **pipeline state persistence**, not external workloads.

---

# What You Lose With Lower Durability

Reducing durability **does not affect Jenkins stability**.

It only affects **pipeline recovery after crashes**.

Worst case:

```
running pipelines cannot resume
pipeline UI visualization may break
logs still exist
```

Pipelines may behave like **Freestyle builds** after failure.

---

# Graceful vs Dirty Shutdown

Durability risk matters only when Jenkins shuts down **unexpectedly**.

## Graceful shutdown

Examples:

- Jenkins `/exit`
- service stop
- SIGTERM
- system restart

Pipelines save state correctly.

No durability problem.

---

## Dirty shutdown

Examples:

- SIGKILL
- container killed
- VM crash
- OOM killer
- OS crash

In this case pipelines with lower durability **may lose state**.

---

# Atomic Writes

Maximum durability uses **atomic disk writes**.

Other modes skip atomic writes to improve speed.

Risk:

- buffered data may not be written
- pipeline state may be lost

This situation is rare but possible.

---

# Requirements for Durability Settings

Minimum requirements:

- Jenkins LTS **2.73+**

Required plugins:

- workflow-api ≥ 2.25
- workflow-cps ≥ 2.43
- workflow-job ≥ 2.17
- workflow-support ≥ 2.17
- workflow-multibranch ≥ 2.17 (optional)

After updating plugins, **restart Jenkins**.

---

# Pipeline Durability Modes

## 1. Performance Optimized (`PERFORMANCE_OPTIMIZED`)

Characteristics:

- minimal disk I/O
- fastest pipeline execution
- lowest durability

Risk:

- pipelines may not resume after crash

Recommended for:

```
build/test pipelines
non-critical pipelines
pipelines that can be rerun easily
```

---

## 2. Maximum Durability (`MAX_SURVIVABILITY`)

Characteristics:

- safest mode
- writes data frequently
- highest disk I/O
- slowest performance

Recommended for:

```
critical pipelines
production deployments
audited pipelines
```

---

## 3. Survivable Nonatomic (`SURVIVABLE_NONATOMIC`)

Characteristics:

- faster than maximum durability
- still writes data every step
- avoids atomic writes
- small risk of data loss

Recommended for:

```
moderately critical pipelines
network storage setups
```

---

# Recommended Best Practices

Typical strategy:

```
Global default → PERFORMANCE_OPTIMIZED
Critical pipelines → MAX_SURVIVABILITY
```

Example:

| Pipeline Type | Recommended Mode |
|-----|-----|
| Build/Test | PERFORMANCE_OPTIMIZED |
| CI pipelines | PERFORMANCE_OPTIMIZED |
| Production deploy | MAX_SURVIVABILITY |
| Infrastructure changes | MAX_SURVIVABILITY |

---

# Forcing Pipeline Persistence

If needed, pipeline state can be forced to save by:

```
pausing the pipeline
```

Example:

```
input step
```

---

# Other Pipeline Scaling Tips

## Use `@NonCPS` for heavy logic

`@NonCPS` methods:

- run faster
- skip CPS transformation
- support more Groovy features

Example usage:

```
complex data processing
loops
transformations
```

Limitations:

- cannot call Pipeline steps (`sh`, `sleep`, etc.)
- should return serializable values

---

## Use fast storage

Jenkins controller should ideally run on:

```
SSD storage
```

Slow disks significantly affect pipeline performance.

---

## Offload heavy work to agents

Pipeline DSL should not perform heavy computation.

Instead run:

- shell scripts
- Python scripts
- Groovy scripts
- REST API tools

Example:

```
sh './process-data.sh'
```

---

## Simplify pipeline code

Try to:

- reduce number of steps
- combine similar steps
- keep Groovy simple

Example:

Instead of many steps:

```
sh 'cmd1'
sh 'cmd2'
sh 'cmd3'
```

Prefer:

```
sh '''
cmd1
cmd2
cmd3
'''
```

---

## Reduce log size

Huge logs slow Jenkins.

If tools produce large output:

- write logs to file
- compress them
- archive as artifact

Example:

```
archiveArtifacts 'build.log.gz'
```

---

## Keep plugins updated

New versions of pipeline plugins include:

- performance improvements
- bug fixes

Regular updates help scaling.

---

## Tune JVM when Jenkins is large

If Jenkins uses **more than 6GB heap**, configure JVM garbage collection tuning to reduce GC pauses.

---

# Key Scaling Principles

To scale Jenkins Pipelines effectively:

- reduce disk writes when possible
- offload work to agents
- simplify pipelines
- minimize pipeline steps
- keep controller lightweight

---

# One-Sentence Summary

**Scaling Jenkins Pipelines mainly involves balancing performance and durability by adjusting how frequently pipeline state is written to disk while keeping pipelines simple, agent-driven, and optimized for minimal controller workload.**