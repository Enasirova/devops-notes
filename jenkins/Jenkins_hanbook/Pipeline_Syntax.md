# Jenkins Pipeline Syntax — Main Recap

## What this page is

This page is a **reference overview** of Jenkins Pipeline syntax.

Jenkins supports **two pipeline syntaxes**:

- **Declarative Pipeline**
- **Scripted Pipeline**

Both run on the same Jenkins Pipeline engine underneath, both support **Pipeline steps** and **Shared Libraries**, but they differ in **structure, readability, and flexibility**.

---

# 1. Core Idea: Step = Basic Building Block

The most fundamental part of every pipeline is a **step**.

A step tells Jenkins what to do.

Examples:

```groovy
echo 'Hello'
sh 'mvn test'
checkout scm
```

Both Declarative and Scripted Pipelines are built from steps.

---

# 2. Declarative vs Scripted

## Declarative Pipeline

Declarative is:

- simpler
- more structured
- opinionated
- easier to read for teams

It must always be inside:

```groovy
pipeline {
    ...
}
```

Example:

```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Hello'
            }
        }
    }
}
```

### Main rules of Declarative

- top-level must be `pipeline { }`
- no semicolons between statements
- each statement on its own line
- blocks may contain only allowed pipeline elements
- property references like `input` are treated like method calls

### Limitation

There is a known limit on the **maximum size of code inside the `pipeline {}` block**.

This limitation does **not** apply to Scripted Pipeline.

---

## Scripted Pipeline

Scripted is:

- more flexible
- more like normal Groovy scripting
- better for complex logic
- harder for beginners

Example:

```groovy
node {
    stage('Build') {
        echo 'Hello'
    }
}
```

Scripted gives much more freedom, but you must manage logic yourself using:

- `if / else`
- `try / catch`
- loops
- variables
- Groovy expressions

Example:

```groovy
node {
    stage('Example') {
        if (env.BRANCH_NAME == 'master') {
            echo 'On master'
        } else {
            echo 'Not on master'
        }
    }
}
```

---

# 3. Declarative Pipeline — Main Structure

A typical Declarative Pipeline looks like this:

```groovy
pipeline {
    agent any

    environment {
        APP_ENV = 'test'
    }

    options {
        timeout(time: 1, unit: 'HOURS')
    }

    parameters {
        string(name: 'VERSION', defaultValue: '1.0')
    }

    triggers {
        cron('H 2 * * 1-5')
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building'
            }
        }
    }

    post {
        always {
            echo 'Done'
        }
    }
}
```

---

# 4. Main Declarative Sections

## `agent`

Defines **where the pipeline or stage runs**.

Examples:

```groovy
agent any
```

```groovy
agent none
```

```groovy
agent { label 'linux' }
```

```groovy
agent {
    docker {
        image 'maven:3.9.3-eclipse-temurin-17'
    }
}
```

```groovy
agent {
    kubernetes {
        yaml '...'
    }
}
```

### Common agent types

- `any` → any available agent
- `none` → no global agent, each stage must define its own
- `label` → run on a node with a given label
- `node` → like label, but supports more options
- `docker` → run in a Docker container
- `dockerfile` → build container from Dockerfile in repo
- `kubernetes` → run in a Kubernetes pod

### Important top-level vs stage-level difference

With **top-level agent**:
- agent is allocated first
- then `timeout` starts

With **stage-level agent**:
- stage options run first
- then agent allocation happens
- so timeout includes waiting/provisioning time

This matters because slow agent provisioning can consume stage timeout.

---

## `stages`

Contains the main work of the pipeline.

Example:

```groovy
stages {
    stage('Build') {
        steps {
            echo 'Building'
        }
    }
}
```

---

## `stage`

A named phase of the pipeline.

Example:

```groovy
stage('Test') {
    steps {
        sh 'mvn test'
    }
}
```

A stage can contain only one of these main execution forms:

- `steps`
- `stages`
- `parallel`
- `matrix`

---

## `steps`

Contains actual Jenkins steps to run inside a stage.

Example:

```groovy
steps {
    echo 'Hello'
    sh 'mvn test'
}
```

---

## `post`

Defines actions to run **after pipeline or stage completion**.

Common conditions:

- `always`
- `changed`
- `fixed`
- `regression`
- `aborted`
- `failure`
- `success`
- `unstable`
- `unsuccessful`
- `cleanup`

Example:

```groovy
post {
    always {
        echo 'Always run this'
    }
    failure {
        echo 'Only on failure'
    }
}
```

---

# 5. Common Declarative Directives

## `environment`

Defines environment variables.

Example:

```groovy
environment {
    APP_ENV = 'prod'
}
```

Can be used globally or inside a stage.

### Credentials helper

You can use:

```groovy
credentials('credential-id')
```

Supported examples include:

- Secret Text
- Secret File
- Username/Password
- SSH Private Key

Example:

```groovy
environment {
    SERVICE_CREDS = credentials('my-creds')
}
```

---

## `options`

Configures pipeline behavior.

Common options:

- `buildDiscarder(...)`
- `disableConcurrentBuilds()`
- `disableResume()`
- `preserveStashes()`
- `quietPeriod(30)`
- `retry(3)`
- `skipDefaultCheckout()`
- `skipStagesAfterUnstable()`
- `timeout(...)`
- `timestamps()`
- `parallelsAlwaysFailFast()`
- `disableRestartFromStage()`

Example:

```groovy
options {
    timeout(time: 1, unit: 'HOURS')
    timestamps()
}
```

### Stage-level options

Inside a stage, only some options are allowed, mainly:

- `timeout`
- `retry`
- `timestamps`
- `skipDefaultCheckout`

---

## `parameters`

Defines build parameters users can fill in when running the pipeline.

Supported examples:

- `string`
- `text`
- `booleanParam`
- `choice`
- `password`

Example:

```groovy
parameters {
    string(name: 'DEPLOY_ENV', defaultValue: 'staging')
    booleanParam(name: 'DEBUG', defaultValue: false)
}
```

Access them with:

```groovy
params.DEPLOY_ENV
params.DEBUG
```

---

## `triggers`

Defines automatic ways to start a pipeline.

Main trigger types:

- `cron(...)`
- `pollSCM(...)`
- `upstream(...)`

Example:

```groovy
triggers {
    cron('H */4 * * 1-5')
}
```

### Note

If webhook integration already exists for GitHub/Bitbucket, extra triggers may not be necessary.

---

## Jenkins cron syntax

Jenkins cron uses 5 fields:

```text
MINUTE HOUR DOM MONTH DOW
```

Example:

```groovy
triggers {
    cron('H/15 * * * *')
}
```

This means: run roughly every 15 minutes, using Jenkins hash balancing.

### Important Jenkins-specific point: `H`

`H` is not random each time.  
It is a **stable hash based on the job name**, used to spread load across jobs.

Example:

```text
H H * * *
```

means run once a day, but not all jobs at the same time.

---

## `tools`

Auto-installs tools and adds them to `PATH`.

Supported examples:

- `maven`
- `jdk`
- `gradle`

Example:

```groovy
tools {
    maven 'apache-maven-3.0.1'
}
```

The tool name must already exist in:

```text
Manage Jenkins → Tools
```

---

## `input`

Pauses a stage and asks for manual approval.

Example:

```groovy
input {
    message "Should we continue?"
    ok "Yes"
}
```

Useful for:

- production deploy approvals
- manual checkpoints
- selecting values before continuing

Can also limit who may approve via `submitter`.

---

## `when`

Controls whether a stage should run.

Examples of conditions:

- `branch`
- `buildingTag`
- `changelog`
- `changeset`
- `changeRequest`
- `environment`
- `equals`
- `expression`
- `tag`
- `triggeredBy`

And nesting helpers:

- `not`
- `allOf`
- `anyOf`

Example:

```groovy
when {
    branch 'production'
}
```

Example:

```groovy
when {
    allOf {
        branch 'production'
        environment name: 'DEPLOY_TO', value: 'production'
    }
}
```

### Important advanced switches

Inside `when`, you can control when evaluation happens:

- `beforeAgent true`
- `beforeInput true`
- `beforeOptions true`

This matters when you want Jenkins to decide early and avoid unnecessary agent allocation, input prompt, or options execution.

---

# 6. Advanced Stage Structures

## Sequential stages

A stage may contain nested `stages {}` to run child stages one after another.

Example:

```groovy
stage('Sequential') {
    stages {
        stage('Build') {
            steps {
                echo 'Build'
            }
        }
        stage('Test') {
            steps {
                echo 'Test'
            }
        }
    }
}
```

---

## Parallel

A stage may contain `parallel {}` to run branches at the same time.

Example:

```groovy
stage('Parallel Tests') {
    parallel {
        stage('Linux') {
            steps {
                echo 'Linux test'
            }
        }
        stage('Windows') {
            steps {
                echo 'Windows test'
            }
        }
    }
}
```

### Fail-fast

You can stop all parallel branches when one fails:

```groovy
failFast true
```

or globally:

```groovy
options {
    parallelsAlwaysFailFast()
}
```

---

## Matrix

A stage may contain `matrix {}` for multi-dimensional combinations.

Useful for things like:

- OS × browser
- OS × Java version
- platform × architecture

### Matrix parts

#### `axes`

Defines dimensions and values.

Example:

```groovy
axes {
    axis {
        name 'PLATFORM'
        values 'linux', 'windows', 'mac'
    }
    axis {
        name 'BROWSER'
        values 'chrome', 'firefox'
    }
}
```

#### `stages`

Defines stages each matrix cell runs.

Example:

```groovy
stages {
    stage('Build') {
        steps {
            echo 'Build'
        }
    }
    stage('Test') {
        steps {
            echo 'Test'
        }
    }
}
```

#### `excludes`

Removes invalid combinations.

Example:

```groovy
excludes {
    exclude {
        axis {
            name 'PLATFORM'
            values 'linux'
        }
        axis {
            name 'BROWSER'
            values 'safari'
        }
    }
}
```

#### Matrix cell-level directives

Inside `matrix`, you can also use stage-like directives for each cell:

- `agent`
- `environment`
- `input`
- `options`
- `post`
- `tools`
- `when`

---

# 7. Declarative `script` Step

Declarative allows an escape hatch:

```groovy
script {
    ...
}
```

This lets you execute **Scripted Pipeline / Groovy logic inside Declarative**.

Example:

```groovy
steps {
    script {
        def browsers = ['chrome', 'firefox']
        for (int i = 0; i < browsers.size(); i++) {
            echo "Testing ${browsers[i]}"
        }
    }
}
```

### Best practice

Use `script {}` only for small logic.  
If it becomes large, move it into a **Shared Library**.

---

# 8. Scripted Pipeline Recap

Scripted Pipeline is more like programming in Groovy.

Example:

```groovy
node {
    stage('Build') {
        sh 'mvn clean verify'
    }
}
```

### Flow control in Scripted

Use Groovy directly:

- `if / else`
- loops
- variables
- `try / catch / finally`

Example:

```groovy
node {
    stage('Example') {
        try {
            sh 'exit 1'
        } catch (exc) {
            echo 'Something failed'
            throw
        }
    }
}
```

---

# 9. Differences from Plain Groovy

Scripted Pipeline is based on Groovy, but it is **not the same as normal Groovy script execution**.

Because Jenkins pipelines must be **durable** and survive controller restart, Jenkins must serialize pipeline state.

That is why some Groovy patterns do not work well or are limited.

Example: some collection/closure idioms may behave unexpectedly.

---

# 10. Practical Comparison: Declarative vs Scripted

## Declarative

Best when you want:

- standard structure
- readability
- easier maintenance
- simpler CI/CD pipelines
- clearer stage visualization

Good for most team pipelines.

---

## Scripted

Best when you want:

- advanced logic
- custom control flow
- high flexibility
- complex dynamic behavior

Good for power users and tricky workflows.

---

# 11. Main Mental Model

## Declarative = describe pipeline structure

You say:

- where it runs
- what stages exist
- under what conditions a stage runs
- what should happen after completion

It is more **structured and opinionated**.

---

## Scripted = program pipeline behavior

You say:

- exactly what to do
- exactly when to do it
- exactly how to control the flow

It is more **flexible and imperative**.

---

# 12. Very Short Summary

## Declarative Pipeline

- wrapped in `pipeline { }`
- strict structure
- easier to read
- supports sections like:
  - `agent`
  - `stages`
  - `stage`
  - `steps`
  - `post`
  - `environment`
  - `options`
  - `parameters`
  - `triggers`
  - `tools`
  - `input`
  - `when`
  - `parallel`
  - `matrix`

## Scripted Pipeline

- based on Groovy DSL
- wrapped commonly in `node { }`
- very flexible
- flow control written manually
- more power, more complexity

## Both

- use Pipeline steps
- support plugins
- support Shared Libraries
- run on the same Jenkins Pipeline engine

---

# 13. Best Beginner Takeaway

When reading a Jenkinsfile, first ask:

## Is it Declarative?

Look for:

```groovy
pipeline {
```

If yes, then think in terms of:

- sections
- directives
- stages
- steps
- rules

## Is it Scripted?

Look for things like:

```groovy
node {
```

or heavy Groovy logic.

If yes, then think in terms of:

- Groovy control flow
- custom logic
- full scripting flexibility

---

# 14. One-Line Summary

**Declarative Pipeline is a structured, easier-to-read way to define CI/CD flow, while Scripted Pipeline is a more flexible Groovy-based way to program that flow.**