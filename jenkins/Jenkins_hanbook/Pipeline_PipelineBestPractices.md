# Jenkins Pipeline Best Practices — Main Recap

## Purpose of this page

This page explains **practical best practices** for writing Jenkins Pipelines that are:

- easier to maintain
- safer
- faster
- less demanding on the Jenkins controller

Main idea:

> Use Pipeline as **orchestration glue**, not as the place where heavy logic happens.

---

# 1. Use Groovy as Glue, Not as the Main Work

Pipeline Groovy should mainly **connect actions together**.

Good pattern:

```text
Pipeline decides WHAT happens and WHEN
shell/scripts/tools do the heavy work
```

Bad pattern:

```text
Pipeline Groovy itself does complex processing, parsing, loops, transformations, and business logic
```

Why:

- Groovy code in Pipelines runs on the **Jenkins controller**
- more Groovy = more controller CPU / memory usage
- complex Groovy makes pipelines slower and harder to debug

### Preferred approach

Instead of many tiny Groovy-driven actions, use one build tool or script to do the work.

Example:

- let `mvn` handle build/test/package/deploy flow
- let a shell script handle several commands
- let external tools parse files instead of Groovy

---

# 2. Use Shell Scripts to Simplify Pipelines

A shell script can combine multiple commands into one place.

Benefits:

- fewer Jenkins steps
- simpler pipeline file
- easier updates
- less Pipeline engine overhead

Instead of:

```groovy
sh 'echo step 1'
sh 'echo step 2'
sh 'echo step 3'
```

Prefer:

```groovy
sh '''
  echo step 1
  echo step 2
  echo step 3
'''
```

Or call a script from the repo:

```groovy
sh './ci/build.sh'
```

This keeps the Jenkinsfile smaller and easier to maintain.

---

# 3. Avoid Complex Groovy in Pipelines

Because Groovy runs on the controller, avoid expensive Groovy operations.

## Common examples to avoid

### `JsonSlurper`, `XmlSlurper`, `readFile` for large files

Problem:

- file is loaded into controller memory
- parsed on the controller
- can use a lot of RAM

Bad example idea:

```groovy
def obj = new JsonSlurper().parseText(readFile('big.json'))
```

### Better approach

Use a shell command on the agent and return only the needed result.

Example:

```groovy
def result = sh(
    returnStdout: true,
    script: "jq '.field' big.json"
).trim()
```

This uses **agent resources**, not controller-heavy Groovy parsing.

---

### `httpRequest` for large or frequent calls

Problem:

- request comes from controller
- response may be stored in memory on controller
- controller certificates/network path may differ from agent

### Better approach

Use shell commands like:

- `curl`
- `wget`

and filter results as much as possible on the agent side.

Example:

```groovy
def version = sh(
    returnStdout: true,
    script: "curl -s https://example/api | jq -r '.version'"
).trim()
```

---

# 4. Reduce Repetition of Similar Pipeline Steps

Each Pipeline step has overhead.

If you run many separate steps, Jenkins must repeatedly:

- start the step
- manage execution state
- collect output
- clean up

So avoid unnecessary repetition.

Instead of:

```groovy
sh 'command1'
sh 'command2'
sh 'command3'
```

Prefer:

```groovy
sh '''
  command1
  command2
  command3
'''
```

Main idea:

> Fewer, larger steps are often better than many tiny steps.

---

# 5. Avoid `Jenkins.instance` / `Jenkins.getInstance()`

Using Jenkins internal APIs directly in Pipelines or Shared Libraries is a bad practice.

Why it is risky:

- security risk
- performance risk
- unstable across Jenkins updates
- can effectively turn your shared library into a pseudo-plugin
- may require sandbox whitelisting that gives too much power

If sandboxed code gets access to unsafe Jenkins APIs, users modifying Pipelines may gain more permissions than intended.

### Recommended approach

If access to Jenkins internals is really necessary:

- implement a **small Jenkins plugin**
- expose a safe wrapper through a proper Step API

Best option:

- avoid direct Jenkins API usage if possible

---

# 6. Clean Up Old Jenkins Builds

Old builds consume:

- disk space
- metadata storage
- controller resources

Keeping too many old builds can make Jenkins slower and more cluttered.

Use:

```groovy
options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
}
```

This keeps the latest builds and removes older ones.

Benefit:

- more efficient Jenkins controller
- less wasted disk usage
- easier navigation

---

# 7. Use Shared Libraries Carefully

Shared Libraries are good for:

- reusing pipeline logic
- reducing duplication
- standardizing CI/CD behavior

But they should be designed carefully.

---

## 7a. Do Not Override Built-in Pipeline Steps

Do **not** redefine built-in steps like:

- `sh`
- `timeout`
- other core Pipeline steps

Why this is dangerous:

- Jenkins Pipeline APIs can change
- your custom override may silently break
- troubleshooting becomes very hard
- impact can be huge because these steps are used everywhere

Rule:

> Never replace standard Pipeline behavior unless absolutely unavoidable.

---

## 7b. Avoid Large Global Variable Declaration Files

Large files in `vars/` are loaded for every Pipeline using the library.

Problem:

- extra memory usage
- slower startup
- little benefit if most variables are not used

Prefer:

- smaller, focused `vars/*.groovy` files
- only load what is relevant

---

## 7c. Avoid Very Large Shared Libraries

Very large libraries cause:

- bigger checkouts
- more startup time
- more memory overhead
- slower pipeline loading

Prefer:

- smaller, focused libraries
- split unrelated functionality
- avoid turning one shared library into a giant monolith

---

# 8. Concurrency Best Practices

## Avoid sharing workspaces

Do not let multiple builds write to the same workspace at the same time.

Risks:

- file corruption
- unpredictable build results
- workspace renaming
- race conditions

### Better approaches

#### Best option: unique build environments

Use:

- separate workspaces
- separate containers
- cloud/ephemeral agents

This makes builds cleaner and repeatable.

---

#### Shared data should live outside the workspace

If you must share files:

- mount shared volume elsewhere
- copy files into the current workspace
- copy results back after build if needed

---

#### If unique environments are not possible

Use one of these:

- disable concurrency
- use Lockable Resources plugin

But be aware:

- builds may wait longer
- pipelines can become blocked on locked resources

So unique resources are still the preferred solution.

---

# 9. Avoid `NotSerializableException`

Jenkins Pipelines use **CPS transformation** so they can survive:

- Jenkins restarts
- node disconnects
- long-running execution

To do that, Jenkins must **serialize pipeline state**.

If your pipeline stores non-serializable objects in variables, Jenkins may fail with:

```text
NotSerializableException
```

---

## Why this happens

Pipeline state includes local variables.

If a variable contains an object Jenkins cannot serialize, the pipeline fails when trying to persist state.

---

## Best practices to avoid it

### Do not store non-serializable objects in variables

Bad pattern:

```groovy
def obj = someNonSerializableThing()
```

Prefer:

- calculate values only when needed
- avoid persisting complex objects
- store simple strings, booleans, maps, lists when possible

---

### Infer values “just in time”

Instead of computing and storing a complex object early, get the needed value at the moment you need it.

This reduces serialization problems.

---

## `@NonCPS`

If necessary, a method can be marked:

```groovy
@NonCPS
```

This disables CPS transformation for that method.

Use this only when needed.

### Important warning

Inside `@NonCPS` methods, avoid Pipeline steps like:

- `sh`
- `sleep`
- `input`

Reason:

- Pipeline steps are CPS-aware
- `@NonCPS` code is not

So mixing them can cause problems.

Rule:

> `@NonCPS` is for pure Groovy logic, not for Jenkins steps.

---

# 10. Durability and Serialization

Lowering pipeline durability (for example with performance-optimized settings) may reduce how often Jenkins serializes state.

That can make `NotSerializableException` appear less often.

But this is **not a real fix**.

Reason:

- the problem still exists
- Jenkins is just persisting less often

So:

> Do not change durability just to hide serialization issues.

---

# 11. Main Practical Rules

## Prefer this style

- simple Jenkinsfile
- shell scripts for heavy work
- build tools do build logic
- small shared libraries
- few Pipeline steps
- unique workspaces
- serializable variables only

## Avoid this style

- heavy Groovy logic in pipeline
- parsing large files in Groovy
- many tiny repeated steps
- direct Jenkins API access
- overriding built-in steps
- giant shared libraries
- shared mutable workspaces
- storing complex non-serializable objects

---

# 12. Best Mental Model

Think of Jenkins Pipeline as:

```text
an orchestrator
```

not as:

```text
the place where all logic and processing should live
```

Jenkins should mostly coordinate:

- checkout
- invoke scripts/tools
- call build/test/deploy actions
- collect and display results

The heavy lifting should happen in:

- shell scripts
- Maven/Gradle/NPM
- external tools
- agent-side commands
- focused shared library helpers

---

# One-Sentence Summary

**The main Jenkins Pipeline best practice is to keep Pipelines small, simple, and orchestration-focused, while moving heavy logic, parsing, and repeated work into scripts, tools, agents, and carefully designed shared libraries.**