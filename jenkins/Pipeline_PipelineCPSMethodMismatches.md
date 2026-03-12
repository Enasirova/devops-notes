# Jenkins Pipeline — CPS Method Mismatches (Recap)

## Main Idea

Jenkins Pipeline runs Groovy code using a special execution model called **CPS (Continuation-Passing Style)**.

This allows Jenkins to:

- pause pipelines
- persist pipeline state to disk
- resume pipelines after Jenkins restarts

Pipeline state is stored in:

```
program.dat
```

inside the build directory.

Because of this CPS transformation, **not all Groovy constructs behave exactly like normal Groovy**.

Some combinations of CPS and non-CPS code can cause **unexpected behavior or errors**, called **CPS method mismatches**.

---

# What Code Is CPS-Transformed

The following parts of pipeline code are **CPS-transformed**:

- most Pipeline script code
- shared library code
- Pipeline steps
- steps that accept blocks (`node`, `stage`, etc.)

Example:

```groovy
node {
    sh 'build'
}
```

---

# What Code Is NOT CPS-Transformed

Some code runs **outside CPS**.

Examples include:

- compiled Java bytecode
- Jenkins core
- Jenkins plugins
- Groovy runtime
- constructors in pipeline scripts
- methods marked with `@NonCPS`
- simple steps like `echo`

Example:

```groovy
@NonCPS
def myMethod() {
    ...
}
```

---

# Core Rule

Allowed calls:

```
CPS → CPS
CPS → non-CPS
non-CPS → non-CPS
```

Not allowed:

```
non-CPS → CPS
```

When non-CPS code calls CPS code, Jenkins cannot correctly manage execution state.

This leads to confusing runtime warnings or incorrect results.

---

# Typical Error Message

When mismatches occur, Jenkins logs warnings like:

```
expected to call X but wound up catching Y
```

Example:

```
expected to call WorkflowScript.compileOnPlatforms but wound up catching node
```

---

# Common Problems and Fixes

## 1. Using Pipeline Steps Inside `@NonCPS`

`@NonCPS` disables CPS transformation for a method.

But Pipeline steps require CPS.

Example problem:

```groovy
@NonCPS
def compileOnPlatforms() {
  ['linux', 'windows'].each { arch ->
    node(arch) {
      sh 'make'
    }
  }
}
```

Problem:

```
node
sh
```

are Pipeline steps (CPS).

Calling them from `@NonCPS` is illegal.

### Fix

Remove `@NonCPS`.

Correct version:

```groovy
def compileOnPlatforms() {
  ['linux', 'windows'].each { arch ->
    node(arch) {
      sh 'make'
    }
  }
}
```

---

# 2. Passing CPS Closures to Non-CPS Methods

Some Java/Groovy methods accept **closures** as arguments.

Example:

```groovy
list.toSorted { a, b -> a.length() <=> b.length() }
```

Problem:

- closure is CPS-transformed
- `toSorted()` is not CPS-aware

This leads to incorrect behavior.

Example result:

```
sorted = -1
```

instead of the sorted list.

### Fix

Wrap the method in a `@NonCPS` method.

Example:

```groovy
@NonCPS
def sortByLength(list) {
    list.toSorted { a, b -> a.length() <=> b.length() }
}
```

---

# 3. Constructors Cannot Run CPS Code

Groovy constructors cannot be CPS-transformed.

Example problem:

```groovy
class Test {
  def x
  public Test() {
    setX()
  }
  private void setX() {
    this.x = 1
  }
}
```

Calling CPS-transformed methods inside constructors causes errors.

Typical warning:

```
expected to call Test.<init> but wound up catching Test.setX
```

### Fix

Avoid calling CPS methods inside constructors.

Better approach:

- move logic outside constructor
- use a factory method

Example:

```groovy
def createTest() {
    return new Test(1)
}
```

---

# 4. Overriding Non-CPS Methods

When extending Java/Groovy classes, overridden methods must be compatible.

Example problem:

```groovy
class Test {
  @Override
  public String toString() {
    return "Test"
  }
}
```

Calling this from non-CPS code (e.g., `StringBuilder`) causes errors.

Example warning:

```
expected to call java.lang.StringBuilder.append but wound up catching Test.toString
```

### Fix

Mark overridden methods with:

```groovy
@NonCPS
```

Example:

```groovy
@NonCPS
public String toString() {
  return "Test"
}
```

Also ensure no Pipeline steps are used inside.

---

# 5. Closures Inside GString

Groovy allows closures inside strings.

Example:

```groovy
def x = 1
def s = "x = ${-> x}"
```

But closures inside GStrings become CPS-transformed.

This causes runtime issues.

Example warning:

```
expected to call WorkflowScript.echo but wound up catching CpsClosure2.call
```

### Fix

Replace closure with a function.

Example:

```groovy
def x = 1
def s = { -> "x = ${x}" }
echo(s())
```

---

# False Positives

Sometimes Jenkins reports a **CPS mismatch warning even when code works correctly**.

If that happens:

- check Jenkins issues
- file a bug report for the **workflow-cps plugin**

---

# Practical Rules to Avoid CPS Issues

## Avoid

- calling Pipeline steps inside `@NonCPS`
- CPS closures passed to non-CPS Java methods
- CPS logic inside constructors
- overriding Java methods without `@NonCPS`
- closures inside GStrings

---

## Prefer

- simple Pipeline logic
- using `@NonCPS` only for pure Groovy logic
- keeping Pipeline steps outside `@NonCPS`
- separating CPS and non-CPS logic clearly

---

# Mental Model

Think of Pipeline as running in two worlds:

```
CPS world
    Pipeline steps
    pipeline script

Non-CPS world
    Java code
    Jenkins internals
    @NonCPS methods
```

Rule:

```
CPS → non-CPS = OK
non-CPS → CPS = NOT OK
```

---

# One-Sentence Summary

**CPS method mismatches occur when non-CPS code (like `@NonCPS` methods or Java methods) tries to call CPS-transformed Pipeline code, which Jenkins cannot safely resume after restart, leading to errors or unpredictable behavior.**