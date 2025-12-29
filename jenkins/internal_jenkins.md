# Nexus Issue

He mentions a problem with Nexus, described as an artifactory system. Nexus problems may affect builds

**IT term: Artifactory (Nexus)** = a repository where files like libraries or packages are stored for developers.

**Example**:

If a project needs a specific library version, Nexus stores it so builds can access it automatically.

# Resources

- plugins site (he updated to last version): https://ci.svc.ifortuna.cz/manage/pluginManager/

* good youtube source: https://www.youtube.com/@CloudBeesTV

* jenkins related playlist: https://www.youtube.com/watch?v=A2dEsu9dRUc&list=PLvBBnHmZuNQJeznYL2F-MpZYBUeLIXYEe

* official documentation at FEG: https://cnfl.myfortuna.eu/spaces/DEV/pages/203717678/Jenkins+with+OCP4+backend

* tips for agents _access is denied_: https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/OS/repos/ocp4_jenkins_docker_slaves/README.md

# Jenkins Configuration as Code (JCasC)

**IT term: YAML** = a human-readable file used to store settings.

**IT term: JCasC** = plugin allowing Jenkins configuration to be stored as text files.

Jenkins.yaml contains:

- secrets (example: JIRA connection)

- permissions

- roles

- agent configuration

Example:
If you add a new user to a group in Jenkins GUI, you should also edit the YAML file so the setting is not lost when Jenkins restarts.

## Yaml formatting -> whitespaces matter (indentation)

Example:
One extra space can break a configuration:

```yaml
agent:
  name: windows
```

# GUI vs Persistence

- GUI changes apply immediately

- but will NOT survive a restart unless written into YAML

  **Example**:

  You add a “Developers” group in GUI. After restart — it disappears. Unless it’s also added into jenkins.yaml.

# Agents and Pods

**IT term: agent** = a machine that executes Jenkins jobs (builds, tests).

**IT term: OpenShift** = a platform to run containers, often Kubernetes-based.

Example:
When Jenkins runs a build, the actual work might be done by an agent in OpenShift cluster.

**Pod**

- What it is: A Kubernetes/OpenShift concept - the smallest deployable unit in Kubernetes
- Technical definition: A group of one or more containers that share storage and network resources
- Where it runs: In the OpenShift cluster (in your case, the `jenkins-shared` namespace)
- Lifecycle: Created dynamically, runs for the duration of the build, then gets destroyed
- Example: When you request a `maven-java17` agent, OpenShift creates a pod containing containers with Maven and Java 17

**Agent**

- **What it is:** A Jenkins concept - a worker that executes build jobs
- **Where it runs:** Can run anywhere - physical machines, VMs, Docker containers, or Kubernetes pods
- **Lifecycle:** Jenkins perspective - an agent is "available" when it connects and "busy" when running a job
- **Example:** In your Jenkinsfile, `agent { label 'maven-java17' }` requests a Jenkins agent

**Relationship**
In your Jenkins + OpenShift setup:

- **Agent = Pod:** Each Jenkins agent IS a Kubernetes pod
- **Pod contains the agent:** The pod wraps the Jenkins agent container(s)
- **Same thing, different perspectives:** "Agent" is Jenkins terminology, "Pod" is Kubernetes terminology

### Visual Example

When a build runs:

1. **Jenkins perspective:** "I need a `maven-java17` agent to run this build"
2. **Kubernetes perspective:** "Creating a pod named `maven-java17-xyz123` with Maven and Java 17 containers"
3. **Reality:** They're the same thing - the pod IS the agent

### Pod Template in Configuration

In your Jenkins configuration file, each pod template defines both:

```yaml
- name: "maven-java17" # Jenkins agent name
  namespace: "jenkins-shared" # Kubernetes pod location
  label: "maven-java17" # Label used in Jenkinsfile
  containers: # Containers inside the pod
    - name: "jnlp" # Jenkins agent container
    - name: "maven" # Maven tool container
```

### What is JNLP?

**JNLP** stands for **Java Network Launch Protocol** (previously known as **Java Web Start**). In the context of Jenkins, it refers to the agent connection protocol used for communication between the Jenkins master and agent nodes.

### JNLP in Jenkins

JNLP agents connect to the Jenkins master using a Java-based protocol. Key characteristics:

- **Inbound connection:** The agent initiates the connection to Jenkins master (not the other way around)
- **Firewall-friendly:** Works well in environments where the Jenkins master cannot directly reach agent machines
- **Persistent connection:** Maintains a TCP connection for receiving build tasks

### Why Every Pod Has a JNLP Container

Notice that all specialized agents inherit from the base `jnlp` template:

```yaml
- name: "maven-java17"
  inheritFrom: "jnlp"
  containers:
    - name: "maven"
      image: "maven:3.8.6-openjdk-17"
```

This means every agent pod includes:

1. **JNLP container:** Handles Jenkins communication
2. **Tool container(s):** Provides build tools (Maven, Node.js, etc.)

The JNLP container is the "bridge" that makes the pod work as a Jenkins agent, while additional containers provide the actual build environment.

# Build servers & SSH Connection

We use **Windows Build Server** (automates compiling code into applications)

Windows Build Server is connected to Jenkins over SSH

**IT term: Build server** = a machine that compiles code, runs tests, or produces application artifacts.
**IT term: SSH = secure shell** — a protocol for remotely accessing another machine safely.

Example:
Jenkins triggers a job → it connects to the Windows server via SSH → code is built there.

# Creating New Teams/Spaces in Jenkins

When a new team wants Jenkins space:

Steps are:

1. Create folder / space in Jenkins.

2. Update configuration-as-code files.

3. IT term: Configuration as Code = storing Jenkins settings in text files rather than manual GUI edits.

Example:
Adding a new group to the YAML ensures the configuration survives Jenkins restarts.

# Restart Behavior

The YAML config loads only when Jenkins restarts, while GUI changes apply immediately.

Example:
Updating a plugin → restart needed.
Adding a folder via GUI → instant.

# Migration: Docker → OpenShift

Originally Jenkins agents were using Docker directly, but they moved workloads to OpenShift.

- Docker = container platform

- OpenShift = enterprise Kubernetes-based platform

Reason:
Some tests used Docker API directly, which didn’t work well inside OpenShift.

So they created a workaround: special agent in OpenShift that runs a container with Docker inside it. This works — but is considered a bad practice.

Example:
Test containers that require Docker API → must run on special agent.

# Master vs Agents

- Master (controller) = GUI & coordination

- Agents (nodes) = run builds

Example:
Pipeline starts → master instructs agent → agent builds app.

Most workloads run in OpenShift agents.
Exceptions:

- iOS builds (Mac)

- Windows builds

- legacy Linux

# Agent Counts & Automation

There are ~35–36 agents. Agent creation is automated:

- If a Dockerfile or agent config changes → only affected agents rebuild.

Example:
Updating certificate in agents → Jenkins rebuilds only those agents, not everything.

# Registry & Artifacts

Built apps are:

1. built via pipeline

2. pushed into Quay registry

- Registry = a storage system for built images

- Quay = a container registry similar to Docker Hub

Example:
Android app → built → pushed to Quay → deployed.

# Continuous Testing & Deployment

Some teams use Jenkins not just to build, but also to:

- deploy to test environment

- trigger automated tests

- track versions

Example:
Test job runs every hour automatically.

# Jira Integration

- pipelines send status to Jira.

This is likely via internal scripts or plugins.

Example:
After pipeline succeeds → update Jira ticket with version number.

# Multiple URLs (8080 vs HTTPS)

Jenkins originally used:

- http:...:8080 (non-secure)
  Later migrated to:

- secure HTTPS URL with certificates

- Port = communication endpoint on a server

- SSL/Certificate = encrypts connection for security

Old URL still works and is bookmarked but not secure.

# Folders & Access Control

Folders represent teams or shared projects.

- access is controlled via Bitbucket groups

- admins can see everything

- regular teams see only assigned folders

Example:

- Team A sees Folder A

- Team B sees Folder B

Admins: full access.

# Credentials & Vault

Credentials are stored:

- in Jenkins credentials store for technical accounts

- in Vault for other secrets

- Vault = secure storage for passwords, tokens, keys.

# Queue, Running Jobs & Troubleshooting

- Jobs wait in queue until an agent is free.

- OpenShift migration → fewer stuck jobs.

- In case of old systems → jobs could hang → required manual container kill.

# Console Output & Logs

Jenkins console logs help troubleshoot:

- agent assignment

- commands

- errors

Example:
Gradle build logs → visible in console output.

# Multi-Branch Pipelines

Most developers use multi-branch pipelines.

- automatically detects branches in Bitbucket

- builds for each branch

Example:
Branch feature/login appears → Jenkins scans and builds it automatically.

- Multi-branch = common

- Default version = rarely used

# Organizationally

- Admins create folders & set access.

- Teams configure their own pipelines.

- Some unclear folder naming conventions.

- Jira integration exists but not centrally managed.

# OpenShift

**OpenShift** - a container orchestration platform built on Kubernetes

Since migration jobs rarely fail. Isses appear during updates only

# Manage Jenkins — The Admin Console

**Manage Jenkins section.**

This includes:

- plugin warnings

- security status

- system configuration

**Important point:** Plugins are the weakest link.

If a plugin becomes outdated or unsupported, it may break dependencies.

Example:
Plugin A relies on Plugin B.
Plugin B is abandoned → Plugin A might stop working → pipeline fails.

## Best Practices for Plugins

Recommendations:

- Avoid installing unnecessary plugins.

- Use as few as possible.

- Prefer Long-Term Support (LTS) versions of Jenkins.

**IT term: LTS** = stable version updated less frequently with long support.

Reason:
Many plugins depend on others. Updates can create dependency hell.

# Jenkins Home & Logs

Jenkins home directory stores:

- job definitions

- plugin files

- logs

- configuration

**IT term: log** = a text output showing system events, errors, and activity.

Admins rarely need to check logs except for:

- startup failures

- update failures

Example:
If Jenkins fails to start → check service logs on server.

# Logging Tools

There is internal logging in Jenkins, but:

- often not user-friendly

- rarely used

- sometimes insufficient

If needed, admins can increase **verbosity**.

**IT term: verbosity** = how detailed logging output is

# Email / Notifications / Webhooks

Jenkins historically supported:

- email notifications

- Teams integrations (webhooks)

**IT term: webhook** = a real-time message from an app to another app, e.g., pipeline → Teams channel.

But Teams integration was complicated and changed over time.
Some teams replaced it with scripts.

# Credential Management & Vault

Credentials are stored via:

- Jenkins credential store

- Vault for secure secrets

**IT term: Vault** = secure password/token manager.

Example:
Pipeline uses Bitbucket token
→ instead of writing password in script, it references variable injected at runtime.

# Pipeline Shared Libraries

Jenkins has limitations:

- very large Jenkinsfiles cause issues (Java memory limit)

Workaround:

- Shared Libraries stored in Bitbucket

- They are loaded automatically when pipeline runs

**IT term: Shared Library** = reusable pipeline code.

Example:
Instead of repeating deployment steps, teams put them in shared library → pipelines reuse them.

## Shared Libraries & Best Practice

A Jenkins **shared library** is a Git repository that contains reusable pipeline code written in **Groovy**.
Pipelines can import this code instead of copying the same logic again and again.

_Without shared libraries:_

```groovy
// Jenkinsfile
stage('Build') { ... }
stage('Test')  { ... }
stage('Deploy'){ ... }
```

Copied into dozens of repos -> painful to maintain

_With shared libraries_

```groovy
buildApp()
runTests()
deployToEnv('prod')
```

**Modular shared libraries** (in Jenkins) mean **breaking your shared pipeline code into small, reusable, focused pieces** instead of one huge, monolithic script.

Think of it as LEGO blocks for CI/CD pipelines.

**Modular** = split the library into small, single-purpose modules

Instead of:

```vbnet
shared-lib/
└── vars/
    └── pipeline.groovy   ❌ 1000 lines, does everything
```

You create:

```vbnet
shared-lib/
├── vars/
│   ├── buildApp.groovy
│   ├── runTests.groovy
│   ├── deployApp.groovy
│   ├── notifySlack.groovy
│   └── jiraCheck.groovy
└── src/
    └── utils/
        ├── DockerUtils.groovy
        ├── GitUtils.groovy
        └── HelmUtils.groovy
```

Each file = one responsibility

### Why modular shared libraries exist (real Jenkins reasons)

1. Jenkinsfile size limits

Jenkins (Java) has a **method bytecode size limit**.
Huge Jenkinsfiles -> errors like:

_Method code is too large_

**Modular libraries avoid this** by moving logic out of Jenkinsfile

2. Reuse across teams

One team fixes deployment logic -> all pipelines benefit instantly.

3. Easier maintenance

a) Bug in deploy logic? → fix one function

b) Update credentials handling? → fix one module

c) Change tool version? → fix one place

4. Clear ownership

a) buildApp() → platform team

b) deployK8s() → DevOps team

c) runUITests() → QA team

**Modular vs Monolithic**
| Monolithic shared lib | Modular shared lib |
| --------------------------- | ------------------ |
| One huge file | Many small files |
| Hard to read | Easy to understand |
| One bug breaks everything | Bugs are isolated |
| Hard to test | Easy to test |
| Everyone afraid to touch it | Safe to modify |

# Update Procedures

Admins describe:

- using test Jenkins instance first

- updating plugins gradually

- applying updates during low-traffic times (Fridays/Monday early)

Procedure:

1. update Jenkins core

2. restart

3. update plugins

4. restart again

**Important:**
Never update during major release periods.

# Containers & Docker — Fundamental Concepts

Docker and container basics:

- Image = template

- Container = running instance of image

- Containers share host OS kernel

- Containers are much faster than virtual machines

IT terms simplified:

- **Image** = recipe (ingredients + instructions)

- **Container** = cake made from recipe

- **VM** = separate kitchen

- **Container** = a dish served in same kitchen but isolated

Example:
Android build agent runs inside container → isolated but lightweight.

Image:

- static

- stored in registry (Quay, Docker Hub, Nexus)

- versioned (app:1.2.3)

- shared across systems

- no CPU, no memory

* doesnt run itself. its used to create containers. Thats why its called template/recipe

Container:

- runtime instance of an image

- has CPU, memory, PID

- can crash, restart

- has writable layer

**Why DevOps people prefer the word “template”**

Because in CI/CD and OpenShift:

- Jenkins creates containers on demand

- OpenShift schedules containers from images

- multiple pipelines use the same image

- containers come and go, images stay

So we say:

“Spin up an agent from an image”

**A container image** is a packaged application environment that acts as a template for creating running containers.

# Integration with OpenShift

OpenShift manages:

- scheduling containers

- scaling them:

  Scaling = changing the number of running containers. From one image you can run as many containers as you want

- isolating processes

Limitations:

- cannot "live migrate" a running container to another host

This affects design of apps and agents.

# Dockerfiles & Agent Build

- Dockerfile defines agent image

- base images reused

- cascade rebuild structure

For example:

Slave Base → Java Base → Maven Agent → Android Agent

1️⃣ Slave Base

Lowest level, shared by everything

Contains:

- OS base (Linux)

- Jenkins user

- certificates

- proxy configuration

- basic tools (curl, bash, ca-certificates)

  **curl** = command-line tool used to send requests to URLs and receive responses. Curl lets you talk to servers from the terminal. you can

  - fetch a webpage

    - **fetch a webpage** meams **download the content of a webpage from a server**

          When you run:

          curl https://example.com

          curl:
          1️⃣ sends a request to the web server
          2️⃣ the server responds with data
          3️⃣ curl prints that data to your terminal

          That data is usually HTML.

          What you see vs what a browser shows

          Browser
          * downloads HTML
          * downloads CSS, images, JS
          * executes JavaScript
          * renders a nice page

          curl
          * downloads only the raw response
          * no rendering
          * no JavaScript execution

          So “fetch a webpage” with curl means:

          “Give me the raw HTML the server sends.”

          Concrete example
          curl https://example.com


          Output will look like:

          <!doctype html>
          <html>
          <head>
            <title>Example Domain</title>
          </head>
          <body>
            <h1>Example Domain</h1>
          </body>
          </html>


          That is the webpage, just not visually rendered.

          Why this matters in DevOps

          You often only care:

          Did the server respond?

          Was it status 200 or 500?

          What data did it return?

  - call an API
  - send data (JSON, forms)
  - test if a service is alive
  - debug network issues

👉 Almost every agent depends on this

2️⃣ Java Base

Built **FROM Slave Base**

Adds:

- Java runtime (e.g. Java 17)

- JAVA_HOME

- Java certificates

👉 All Java-based builds depend on this

3️⃣ Maven Agent

    -> **MAVEN**
    Maven is a build and dependency management tool for Java projects.

      In plain language:

      Maven downloads libraries, builds your app, and packages it in a repeatable way.

      The problem Maven solves (why it exists)

      Without Maven, you would have to:

      manually download .jar files

      keep track of versions

      configure classpaths

      explain to teammates how to build the app

      👉 This is messy and error-prone.

      Maven automates all of this.

      What Maven actually does (3 core jobs)
      1️⃣ Dependency management

      You declare what libraries you need:

      <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <version>6.1.2</version>
      </dependency>


      Maven:

      downloads it automatically

      downloads its dependencies

      keeps versions consistent

      2️⃣ Build automation

      You run:

      mvn package


      Maven:

      compiles Java code

      runs tests

      packages the app (usually into a .jar or .war)

      Same command works:

      on your laptop

      on CI server

      inside Docker

      3️⃣ Standard project structure

      Maven enforces a convention:

      src/main/java
      src/main/resources
      src/test/java
      target/


      Everyone knows where things are → less chaos.

      Very common Maven commands
      mvn clean       # delete old build files
      mvn compile    # compile code
      mvn test       # run tests
      mvn package    # build .jar/.war
      mvn install    # put artifact into local repo

      Where Maven fits in DevOps

      Typical CI/CD flow:

      Git commit
      → CI runs mvn test
      → CI runs mvn package
      → Docker image built
      → App deployed


      Maven is often used:

      in Jenkins pipelines

      inside Dockerfiles

      with artifact repositories (Nexus, Artifactory)

      Mental model (important)

      Think of Maven as:

      npm for Java
      or
      a recipe that tells machines how to build your Java app

      One-sentence takeaway

      Maven makes Java projects buildable, repeatable, and dependency-safe — locally and in CI/CD.

      If you want next, I can:

      explain pom.xml line by line

      compare Maven vs Gradle

      show how Maven is used inside a Dockerfile

Built **FROM Java Base**

Adds:

- Maven

- Maven settings.xml

- Maven cache volume

- company-specific repo config

👉 Used by Java backend projects

4️⃣ Android Agent

Built **FROM Maven Agent**

Adds:

- Android SDK

- Gradle

- Android platform tools

- emulator tooling (if needed)

👉 Used only by Android pipelines

## What “cascade rebuild” means in practice

**Scenario A — change in Slave Base**

Example change:

- new company certificate

- proxy change

- OS security patch

What happens:

```nginx
Slave Base rebuilt
↓
Java Base rebuilt
↓
Maven Agent rebuilt
↓
Android Agent rebuilt
```

✅ Everything downstream must rebuild
❌ You cannot safely rebuild only one

This is cascade rebuild.

**Scenario B — change in Android Agent only**

Example change:

- new Android SDK version

- new Gradle version

What happens:

```nginx
Android Agent rebuilt
```

Nothing else.

# Registries & Artifacts

Images are stored in:

- Quay registry (external)

- OpenShift internal registry

IT term: registry = storage for container images.

Because large teams deploy simultaneously, internal caching in OpenShift is safer and faster.

# Tools used

Tools mentioned:

- Vault (secrets)

- Helm (deployment charts)

- OC command (OpenShift CLI)

- Kubernetes plugin (to connect Jenkins to OpenShift)

Example:
Helm manages versioned deployment of app into OpenShift cluster.

Continue from chat Recap with IT terms 5) Build Tools Agent (buildup)
