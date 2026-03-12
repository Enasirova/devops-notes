# Jenkins — Pipeline as Code (Recap)

## What Pipeline as Code Means

**Pipeline as Code** means defining Jenkins build and deployment pipelines **in code stored in a repository** instead of configuring jobs manually in Jenkins UI.

The pipeline definition is stored in the repository as a file named:

```
Jenkinsfile
```

Benefits:

- pipelines are **version controlled**
- changes are **reviewed like normal code**
- Jenkins can **automatically discover and run pipelines**
- no manual job configuration required

---

# The Jenkinsfile

A **Jenkinsfile** contains the pipeline script that defines:

- how the project builds
- how it is tested
- how it is deployed

Example:

```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn package'
            }
        }
    }
}
```

Requirements:

- file name must be `Jenkinsfile`
- placed in **repository root**

Once present, Jenkins can automatically detect it.

---

# How Jenkins Discovers Pipelines

Pipeline-as-Code relies on special Jenkins job types:

1. **Multibranch Pipeline**
2. **Organization Folder**
3. **Regular Pipeline with "Use SCM"**

These features allow Jenkins to automatically detect:

- repositories
- branches
- pull requests

and create jobs automatically.

---

# Multibranch Pipeline

A **Multibranch Pipeline** automatically creates a pipeline job **for every branch** in a repository.

Example repository structure:

```
Repository
├─ master
├─ feature-a
├─ feature-b
└─ pull-request-1
```

Jenkins creates jobs like:

```
repo
├─ master
├─ feature-a
├─ feature-b
└─ PR-1
```

Each branch runs the **Jenkinsfile from that branch**.

Benefits:

- different pipelines per branch
- automatic job creation
- automatic detection of new branches

---

# Important Environment Variable

Multibranch pipelines expose:

```
BRANCH_NAME
```

Example usage:

```groovy
when {
    branch 'master'
}
```

---

# Important Checkout Command

In multibranch pipelines, use:

```groovy
checkout scm
```

Why?

Because it ensures Jenkins checks out the **exact commit associated with the Jenkinsfile**.

This is important for pull requests and fork builds.

---

# Organization Folder

An **Organization Folder** automatically discovers repositories inside:

- GitHub Organization
- Bitbucket Team

Example:

```
GitHub Organization
├─ project1
├─ project2
└─ project3
```

Jenkins will automatically create:

```
Organization Folder
├─ project1 (Multibranch Pipeline)
├─ project2 (Multibranch Pipeline)
└─ project3 (Multibranch Pipeline)
```

Each repository then manages its branches automatically.

Benefits:

- automatic repository discovery
- automatic pipeline creation
- minimal Jenkins administration

---

# Repository Hierarchy Example

```
GitHub Organization
├─ Project1
│  ├─ master
│  ├─ feature-1
│  └─ feature-2
│
└─ Project2
   ├─ master
   └─ pull-request-1
```

Jenkins mirrors this structure automatically.

---

# Configuration Concepts

Two types of credentials are used:

### Scan credentials

Used to access the GitHub/Bitbucket **API** during repository scans.

Example usage:

- discover repositories
- detect branches
- detect pull requests

---

### Checkout credentials

Used when Jenkins **clones the repository**.

Example:

- SSH key
- anonymous checkout
- Git credentials

---

# Branch and Repository Scanning

Jenkins periodically scans repositories to detect:

- new branches
- new pull requests
- deleted branches

This can happen via:

- **webhooks** (recommended)
- **scheduled scans**

Example default scan interval:

```
once per day
```

---

# Orphaned Item Strategy

When branches are deleted, Jenkins must decide what to do with the old jobs.

Options include:

- delete immediately
- keep for some days
- keep limited number

Keeping them can help with:

- debugging old builds
- reviewing historical results

---

# Icon and View Strategy

Organization folders can have custom icons and views.

For example:

- show repository health
- display organization branding
- group pipelines visually

This can be configured using plugins like:

```
Custom Folder Icon plugin
```

---

# Continuous Delivery with Pipeline

Pipeline helps organizations implement **continuous delivery**.

Continuous delivery means:

- software is always **ready to release**
- deployments are **automated**
- feedback comes **early and quickly**

Typical pipeline stages:

```
Build
Test
Deploy
```

---

# Why Jenkins Pipelines Exist

As organizations mature in automation, pipelines become:

- more complex
- longer running
- more integrated with infrastructure

Pipeline plugin allows:

- loops
- retries
- parallel stages
- manual approvals
- advanced orchestration

---

# Important Requirements for Enterprise Pipelines

## Reliability

Pipelines must survive failures such as:

- Jenkins restart
- node disconnect
- long running tasks

Jenkins pipelines are **durable** and can resume execution.

---

## Debuggability

Teams must be able to see:

- stage progress
- where failures occurred
- build history

---

# Pipeline Job Type

Pipeline jobs define the **entire software delivery flow**.

Example Scripted Pipeline:

```groovy
node('linux'){
    git url: 'https://github.com/example/project.git'
    sh 'mvn clean verify'
}
```

This script controls the whole build process.

---

# Stages

Stages represent **logical phases** of a pipeline.

Example:

```
Build
Test
Deploy
```

Each stage builds on the previous one.

Purpose:

- structure pipelines
- visualize progress
- control concurrency

---

# Stage Concurrency Example

A deployment stage might allow only **one execution at a time**.

Example:

```groovy
stage name: 'Production', concurrency: 1
```

This prevents multiple deployments running simultaneously.

---

# Gates and Approvals

Pipelines often require **manual approvals**.

Example:

```groovy
input message: "Does staging look good?"
```

Pipeline pauses until a user approves.

This allows:

- manual validation
- human oversight
- controlled production releases

---

# Deployment of Artifacts

Deployment steps vary depending on infrastructure.

Possible deployment methods:

- third-party deployment tools
- custom scripts
- custom pipeline functions

Example:

```groovy
def deploy(war, id) {
    sh "cp ${war} /tmp/webapps/${id}.war"
}
```

Teams can write custom Groovy functions for deployments.

---

# Restartable Pipelines

Pipelines are **resumable**.

If Jenkins restarts during a build:

- pipeline resumes at the same step
- progress is not lost

Example scenario:

```
build finished
tests finished
deployment failed due to network
```

Instead of rebuilding everything, the pipeline can resume from deployment.

---

# Pipeline Stage View

Stage View visualizes pipeline execution.

It shows:

- stages as columns
- builds as rows
- execution time
- failure location

Benefits:

- easier debugging
- better pipeline visibility
- user-friendly interface

---

# Artifact Traceability (Fingerprinting)

Jenkins can track artifacts across builds.

This is done using **fingerprints**.

Example:

```groovy
archiveArtifacts artifacts: '**/*.war', fingerprint: true
```

This allows Jenkins to track:

- where an artifact was created
- which builds used it
- where it was deployed

Benefits:

- traceability
- impact analysis
- deployment tracking

---

# Key Takeaways

Pipeline as Code enables:

- pipelines defined in **version-controlled code**
- automatic discovery of **repositories and branches**
- automated **CI/CD pipelines**
- advanced build orchestration

Core components:

```
Jenkinsfile
Multibranch Pipeline
Organization Folder
Stages
Steps
```

Together they allow Jenkins to manage large-scale CI/CD pipelines automatically.

---

# One-Sentence Summary

**Pipeline as Code means defining the entire CI/CD pipeline in a Jenkinsfile stored in the repository, allowing Jenkins to automatically discover, run, and manage pipelines for branches and repositories.**