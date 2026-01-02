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

# Jenkins <-> Openshift connection

- Jenkins runs the controller (master)
- OpenShift runs build agents as containers
- Pipelines spin up contaners dynamically to run jobs

**Example:** Developer triggers a pipeline → Jenkins assigns agent → OpenShift launches container → job runs inside that container.

    👨‍💻 Developer clicks “run pipeline”

    “Hey Jenkins, please build my app.”

    🧑‍💼 Jenkins (the boss) looks at the job

    “I need a worker to do this.”

    🏭 Jenkins asks OpenShift

    “Create me a worker with these tools.”

    📦 OpenShift creates a container

    This container is the agent.

    It has Java / Maven / Node / whatever is needed.

    ⚙️ Job runs inside that container

    Build

    Tests

    Packaging

    🗑️ Job finishes → container disappears

    No wasted resources.

    Clean every time.

# Special "Priveleged" Agent Image

**Privileged** means the container runs with elevated system permissions, allowing it to do powerful system-level tasks that normal containers are not allowed to do.

**The special agent** is a small Java 17–based Jenkins agent image that runs in privileged mode, not because of Java itself, but because the jobs it runs need extra system permissions.

Purpose: reduce size & network transfer. This image is downloaded for every pipeline run

**The normal rule (what usually happens)**: teams create one or a few big “standard” agent images, those images contain:

- Java

- Maven

- Git

- curl

- debugging tools

- sometimes Docker, Node, etc.

Why?

- convenience

- less maintenance

- fewer images to manage

👉 These images are large, but flexible.

**The exception for this special "priveleged" Agent image**: tjos special agent:

- does not reuse the big standard agent
- does not include many tools
- is intentially tiny
- contains only Java 17 + bare minimum

# Why to break this rule of big images and have tiny special image?

- this agent is downloaded every pipeline run
- startup speed matters more than convenience
- network traffic becomes a bottleneck at scale

Smaller images = faster pipeline start & reduced load.

Continue from: 3) ImageRegistry, Quay (aka “Cay”), and ImageStreams

# ImageRegistry, Quay (aka “Cay”), and ImageStreams

Image flow works like this:

1. Images are stored in Quay (external registry).

2. OpenShift creates ImageStreams, which act like links+cache.

3. Jenkins agents pull images from OpenShift registry, not directly from Quay.

**IT term: ImageStream** = OpenShift abstraction pointing to container images, enabling caching and versioning.

Example:
Instead of 30 pipelines downloading from Quay simultaneously → they download from OpenShift’s cached copy. This prevents overload on Quay during peak deployments.

# Environment Variables & Host Resolution

Agents inherit (when it starts it doesnt start empty, but gets prefilled with settings)

- Variables for tools like Sonar, Java
- Host mapping in /etc/hosts for internal IP resolution

  **/etc/hosts** is a tiny text file inside Linux containers that can say: "When someone tries to reach jira.company.com, actually go to this IP address.”
  So it’s like a mini phonebook inside the container.

      What “host mapping” means

      They add lines into /etc/hosts like:

      10.x.x.x jira.company.com

      Now, inside the container:

      if a tool tries to open jira.company.com

      it will be sent to 10.x.x.x

_Why this is needed in OpenShift_:

- Inside an OpenShift/Kubernetes cluster, networking often works differently: The public DNS might resolve jira.company.com to a public IP

- But from inside the cluster, they want traffic to go to an internal IP (private network), like 10.x.x.x

_Reasons this happens_:

- internal routing is faster / cheaper

- public route might be blocked from inside

- security rules: internal systems only accept traffic from inside networks

So they “override” the hostname → IP mapping inside the agent.
**Example**:

- Publicly, your laptop sees:
  jira.company.com → public IP (internet-facing)

- Inside OpenShift, they force:
  jira.company.com → 10.x.x.x (private/internal)

Result: the Jenkins agent can always reach Jira _reliably_ from inside the company network.

# Build tools Agent

- This agent is a special Jenkins worker container whose job is **deployment**.
- Not building code.
- Not running tests.
- 👉 Pushing the app into OpenShift.

So it contains deployment tools instead of programming tools.

**What tools does it contain?**

1. 1️⃣ OC (OpenShift CLI)

   - OC is a command-line remote control for OpenShift

   - It lets you say things like:

   - “Deploy this app”

   - “Restart these pods”

   - “Check deployment status”

   - Think:

2. 2️⃣ Helm

   - Helm is a packaging and templating tool
   - It bundles:
     - Kubernetes/OpenShift YAML files
     - configuration values
     - versions

   **Helm = installer package for applications**

   Instead of copying many files manually, Helm installs everything in one go.

## Why put these tools into a special agent?

Because:

- not every pipeline step needs them
- they are only used during deployment
- keeping them separate keeps other agents smaller and safer

So this agent exists only for: “I am about to deploy something.”

The _Build Tools Agent_ is a Jenkins worker that contains deployment tools (Helm + OC) so pipelines can install, upgrade, and restart applications in OpenShift automatically.

# Legacy: Docker Swarm & Testcontainers Problem

Historical context:

- Jenkins once used Docker Swarm to run agents.
- Developers used Testcontainers, which relied on native Docker API.
- OpenShift uses CRI-O instead of Docker — APIs differ.

_IT term: Testcontainers_ = library to run lightweight containers during tests.
_IT term: CRI-O_ = container runtime used by OpenShift.

Workaround:
Create an agent that contains Docker inside a container.

Inception-like outcome:
container → running Docker → running test containers.

It’s a hack, but needed to support legacy tools.

# Additional Agent Types Outside OpenShift:

Even if:

- most builds run in OpenShift containers

👉 Jenkins is flexible and can use other machines too.

## Those machines can be:

- physical servers
- virtual machines
- laptops (in theory)

These are real machines, not containers:

- 🪟 Windows machines
- 🐧 Linux machines

They already exist and keep running all the time.

## How does Jenkins talk to them?

Using SSH.

**SSH = secure remote control**

It lets Jenkins:

- log in to another machine
- run commands there
- copy files
- get results back

All securely.

SSH = Jenkins typing commands on another computer from far away.

## Why would you need machines outside OpenShift?

Because some things cannot run in containers easily.

**Example: Windows builds**

- .NET apps

  - **.NET apps** are applications built with Microsoft’s .NET platform, and many of them require Windows, which is why Jenkins sometimes uses Windows machines instead of OpenShift containers to build them.

- Windows-only tools
- Software that needs Windows APIs

You cannot run these in Linux containers.

So instead:

- Jenkins connects to a Windows server
- runs the build there
- gets the results

**Concrete example**

Jenkins pipeline starts

- Jenkins says: “This job needs Windows”
- Jenkins connects via SSH to a Windows server
- The build runs on that Windows machine
- Jenkins collects the output\*

# Jenkins Configuration as Code (JCasC)

Configuration is stored in jenkins.yaml.

Key points:

- GUI changes must be replicated in YAML
- YAML ensures persistence across restarts
- Export tool does NOT generate perfect config — manual adjustments needed

  - The export tool:
    - looks at your already-configured Jenkins
    - tries to convert the current UI configuration into YAML
    * the exported YAML is: a starting point, not production-ready

**IT term: JCasC** = plugin enabling YAML-based Jenkins configuration.

Example:
Add new AD group in GUI → also update YAML → apply configuration.

# Update & Error Handling

Process:

- Update plugins
- Update Jenkins core
- Update plugins again
- Restart

If config is invalid:

- Jenkins boot halts
- Error appears in logs

There is an option to continue boot even if config fails — but admin avoids it to catch errors.

# Credentials Management

Stored credentials include:

- Bitbucket tokens
- Jira credentials
- SSH keys

Developers reference credentials using variables.

_IT term: base64 encoding_ = representation of binary data in ASCII text.

Example workflow:

- Developer references $BITBUCKET_TOKEN
- Jenkins injects the actual token from credential store

Important detail:

- Shared pipelines sometimes hardcode passwords → bad practice → painful during password changes.

# Active Directory Authentication

Jenkins uses AD for login:

- AD groups govern roles
- Some login slowness exists
- Still unresolved, but optimized where possible

_IT term: AD = centralized user management system._

Example:
Member of AD_DEV_TEAM → gets access to folder Dev.

# Roles & Permissions

Two building blocks:

- Roles = WHAT you can do (read/configure/build)
- Folder assignment = WHERE you can do it

**Example:**
Role _mobile_admin_ applied to folder _/mobile-app_ enables:

- read
- configure
- build

Roles map to AD groups.

# Script Approval

Jenkins sometimes blocks scripts for security reasons.

      Jenkins can run scripts (small programs) inside pipelines.

      Some scripts are powerful.
      Too powerful.

      So Jenkins thinks:

      “I don’t fully trust this script yet.”

      And it blocks it.

Admin can:

- approve script
- or reject it

This happens rarely, but requires trust — because approval could open vulnerabilities.

## What happens when a script is blocked?

- Pipeline starts
- Jenkins sees an unsafe operation
- Jenkins stops the pipeline
- Jenkins asks an admin for a decision

This is called Script Approval.

# Node =

A node is one individual point in a system that is made of multiple connected parts.

- A map of cities
  → each city = a node
  → roads = connections
- A family tree
  → each person = a node
- A metro map
  → each station = a node

# Real Task: updating and building a new Jenkins agent

Agent uses:

- Gradle 8 instead of Gradle 7
- Java 21 instead of Java 17

      Mobile developers want Gradle 8 support → admins build agent with Java 21 & Gradle 8 → pipelines can then compile newer Android projects.

**Gradle** is a build tool that automatically compiles, tests, and packages applications so builds are fast, repeatable, and reliable.

Building usually means:

- compiling source code
- downloading required libraries
- running tests
- packaging everything into a final file (JAR, WAR, APK, etc.)

**What Gradle does for you (step by step)**

When you run Gradle, it:

- 📥 Downloads dependencies
-     libraries your app needs
- 🧱 Compiles the code
-     turns source code into runnable code
- 🧪 Runs tests
-     checks that code works
- 📦 Packages the app
-     creates the final output file

Other Build Tool is **MAVEN**

| Tool   | Style                          |
| ------ | ------------------------------ |
| Maven  | very strict, XML               |
| Gradle | flexible, faster, script-based |

_A project uses one build tool: Maven or Gradle_

## How to tell which one your project uses

Maven project → you will see:

```pqsql
pom.xml
```

Gradle project -> you will see:

```pqsql
build.gradle
```

or

```pqsql
build.gradle.kts
```

**In Jenkins pipelines you’ll see either:**:

```bash
mvn clean install
```

or

```bash
gradle build
```

## Repository and Access Setup

The agent source code lives in Bitbucket.

Some participants don’t have access → new permissions must be set.

**IT term: Bitbucket** = Git repository hosting platform.

Example:
Agent Dockerfiles and Jenkinsfiles are stored and versioned there.

## Base Image Selection

The admin already prepared a base Docker image with Java 21.

- They duplicate / rename existing agent image
- Then update Gradle version variable

_IT term: Docker image_ = template used to run containers.

**Reasoning:**
Reusing a base image avoids installing Java manually each time.

## Downloading Gradle & Uploading to Nexus

Gradle binaries are uploaded into a repository.

- Nexus (or another artifact store) holds the Gradle binary for builds.

**IT term: Nexus** = artifact repository for storing binaries.

**Example:**
Instead of downloading from the internet during build → agent fetches Gradle locally → faster & reliable.

## Changing the Dockerfile

Changes include:

- updating Gradle version number
- adjusting environment variables

The Dockerfile defines:

- base image
- installed tools
- dependencies

## Updating Pipeline Logic (Jenkinsfile)

The Jenkinsfile defines:

- build steps
- dependencies
- triggers

The admin updates the Jenkinsfile to:

- add the new agent definition
- define dependencies (base image → agent image)

This ensures:

- when base image changes → dependent agents rebuild automatically.

This is called **cascade build dependency**.

## Repository Creation in Quay

Before building, an empty repository must exist in **Quay**.

_IT term: Quay_ = container registry similar to Docker Hub.

**Example:**
gr-8-java-21 repo is created to host the finished image.

They make it **public** to avoid permission issues.

## ImageStreams & OpenShift Link

After pushing to Quay:

- OpenShift must import the image
- via oc import-image

_IT term: ImageStream_ = OpenShift mechanism linking container images.

This lets Jenkins agents pull from OpenShift cache instead of external registry.

## Building the Agent

Pipeline builds the agent:

- downloads Gradle
- packages it
- pushes image to Quay
- OpenShift imports it automatically

Some agents take:

- 2–3 minutes
- heavy Android ones up to ~20 minutes (many dependencies)

## Making It Available to Jenkins

Final step:

- modify Jenkins YAML to define new agent template
- remove ID field to avoid conflicts
- assign proper name and resources

**Example:**
`gradle8-java21` added under Kubernetes pod templates.

This ensures developers can select the new agent in their pipelines.

## Testing the New Agent

They run a trial pipeline:

- select new agent
- verify Gradle version
- verify Java version
- check console logs

First run is slower (ImageStream cache warm-up).
Subsequent runs are fast.

## Trick: Decoding Passwords in Script Console

The presenter shows a sensitive trick:

- Jenkins has a Script Console
- It can decrypt stored credentials
- using internal master key

_IT term: Script Console = tool to run Groovy code directly on Jenkins controller._

**Important concept:**

- This bypasses masked passwords from UI
- It is powerful and risky
- Only admins should use it

Example use case:

- debugging webhook access issues

## CSP (Content Security Policy) Troubleshooting

Issue: HTML report plugin blocked due to CSP.

_IT term: CSP = browser security mechanism controlling what content can load._

Workaround:

- run Java argument to relax policy
- add command to Jenkins startup options for persistence

This is an advanced admin topic.

## Jenkins Hardware Overview

Jenkins master (main Jenkins server) runs on a VM:

- behind HTTPS proxy/terminator

  - HTTPS = encrypted web traffic
  - **proxy** sits in front of Jenkins
    _ accepts HTTPS connections from users
    _ handles encryption
    - forwards requests to Jenkins

  * **terminator** = HTTPS encryption ends there, not inside Jenkins itself

          **So Jenkins:**
          * talks plain HTTP internally
          * the proxy handles HTTPS for the outside world

- certificate stored separately

  - HTTPS needs a certificate (security file)

    - That certificate:

      - is NOT stored inside Jenkins
      - is stored on the proxy server

    - Why?

      - easier renewal
      - better security
      - Jenkins stays simpler

      **Think: Jenkins doesn’t deal with locks and keys — the proxy does.**

- service handled via systemctl (system control)

  - **systemctl** is a Linux tool to manage services
    - **This means Jenkins: runs as a system service** and can be:
      - started
      - stopped
      - restarted
      - enabled at boot

**🧠 Put together in plain English**
Jenkins runs as a normal Linux service on a virtual machine.
Users access it through a secure HTTPS proxy that handles certificates, while Jenkins itself runs behind it and is managed by systemctl.

- **certificate = encryption credential**

termination = handling SSL before forwarding traffic

**File system** (how files stored on disk)

Jenkins needs a lot of disk space because it stores:

- job configs
- build history
- logs
- artifacts

**Jenkins home = main folder** (where everything important lives)

**LVM** = Logical Volume Management: it lets Linux treat storage like _flevible blocks_ instead of fixed disks.

With LVM, you can:

- increase disk size later
- add more space without reinstalling
- avoid running out of disk space

So this means: **Jenkins home sits on a disk that can grow when needed**

### Why Jenkins needs LVM

Because Jenkins data:

- grows over time
- is hard to predict
- can suddenly explode (logs, artifacts)

Using LVM prevents:

- “disk full” disasters
- painful migrations later

### What a symlink is

A symlink **(symbolic link)** is:

- a shortcut
- a pointer from one folder to another

Example idea:

- Jenkins thinks home is in /var/lib/jenkins
- but the real data lives in /data/jenkins

A symlink connects them.

**Why use a symlink here**

Symlinks allow:

- moving Jenkins data without changing configs
- switching storage locations easily
- future migrations with minimal changes

Think:

Jenkins doesn’t care where the data is — the symlink handles it.

**🧠 Put together in plain English**:

_Jenkins stores its important data on a flexible, resizable disk (LVM), and uses a symlink so the data can be moved or resized later without breaking Jenkins._

\*\*🧩 One-line takeaway

Jenkins home is placed on a resizable LVM disk and linked with a symlink so storage can grow and move without pain.\*\*

# Certificate Validity

Certificates expire in 2026 — noted for future renewal.

# Recap: To upgrade to gradle 8 and java 21:

- Build new agent:

  - Dockerfile edits
  - Jenkinsfile edits
  - registry setup
  - OpenShift import
  - YAML definition

- Cascade dependencies simplify rebuilds.
- Script Console provides admin power but risk.
- CSP issues require Java-level fixes.
- VM, certificates, storage → important infra pieces.

# OpenShift as the Runtime for Agents

- Jenkins needs agents (workers) to do jobs

- “Runtime” means where those workers actually run

**OpenShift is the place where Jenkins agents live and run.**

**OpenShift:**

- creates containers
- runs them
- deletes them after the job is done

So when Jenkins needs a worker:

- Jenkins asks OpenShift
- OpenShift starts a container
- That container becomes a Jenkins agent

**A cluster is:**

- a group of machines
- working together
- acting like one big computer

**“Management cluster”** means: this cluster is used for internal company services, not customer apps. Jenkins is an internal tool → it belongs there.

**A namespace is:**

- a folder / box inside a cluster
- used to organize and isolate things

Think:

- Cluster = building
- Namespace = one room in that building

**Why jenkins-shared?**

The name tells you:

- Jenkins resources are grouped together
- shared by teams
- isolated from other systems

So they live in one dedicated space:

- Jenkins agents
- Jenkins-related pods

### Put it all together

_Jenkins creates its worker containers in OpenShift.
They run in the company’s management cluster, inside a dedicated namespace called `jenkins-shared`._

**PVC = Persistent Volume Claim**
“I need a piece of disk storage that does NOT disappear when my container stops.”

      Example:
      All Jenkins agents run in one namespace so logs, pods, and PVCs are centralized.

      They observe failed pods and delete them to clean the environment.
      Failed pods = crashed or terminated agent containers.

# Two Ways to Interact with OpenShift

You can manage OpenShift via:

- web GUI
- CLI using oc

_IT term: oc = command-line tool for OpenShift_ , like kubectl but Red Hat specific.

Downloading the CLI:

- from web console
- supports Linux/Mac/Windows

Example usage:

```pgsql
oc get pods
oc delete pod <name>
```

# OpenShift Repository Structure

Inside the Bitbucket repo, a folder contains:

```
OCP/
   roles
   bindings
   credentials
   PVC templates
```

These define:

- roles for OpenShift
- persistent storage
- secrets needed by agents

_IT term: PVC (Persistent Volume Claim) = reserved disk storage inside OpenShift._

Example:
Maven cache can be 300GB, so it is stored in a PVC.

# Caching & Storage Strategies

Caches reduce network traffic during pipeline runs:

- Maven cache
- Chrome binaries
- NPM cache
- Rush cache

Different tool versions sometimes need:

- different cache directories
- different PVCs

Reason:
Caches are not compatible across versions.

Example:

```pgsql
node18-cache
node20-cache
node22-cache
```

# Rebuilding Agents & Cascading Builds

When base images are updated:

- dependent agents rebuild automatically
- but errors may interrupt cascading builds

To trigger rebuild:

- add a comment line to Dockerfile (# trigger build)

This forces Jenkins to treat it as a change.

**Example:**
slave-base-java17 → rebuild triggers all agents based on Java 17.

# Docker-in-Docker (DinD) & Testcontainers

Some developers use **Testcontainers**, which requires Docker API.

OpenShift uses CRI-O, not Docker → APIs differ.

Solution:

- run a “Docker-in-Docker” container as a sidecar
- expose Docker API over port 2375

**IT term: sidecar = secondary container in same pod providing service to main container.**

**Problem:**
Startup timing issues → must loop/wait until Docker API is ready.

Example pseudo-code:

```bash
while !docker info; do sleep 1; done
```

# Resource Tuning for Agents

## What “resource tuning” means

Every Jenkins agent (container) needs:

- **CPU →** how much computing power
- **Memory (RAM)** → how much it can keep in its head

OpenShift must know **how much to give it.**

That’s where requests and limits come in.

## CPU / Memory request (minimum)

**Request** means:

_“I need at least this much to start and work.”_

- OpenShift reserves this amount
- If the cluster can’t provide it → the agent won’t start

Think:

_Request = guaranteed minimum_

## CPU / Memory limit (maximum)

**Limit** means:

_“I am not allowed to use more than this.”_

If the agent tries to go beyond:

- CPU → OpenShift slows it down
- Memory → OpenShift kills it

Think:

_Limit = hard ceiling_

## Why some pipelines need high requests

Some jobs are **heavy**:

🐳 Docker-in-Docker builds

- building container images
- running Docker inside Docker
- very memory-hungry

🧠 Complex pipelines

- large builds
- many tests
- big dependency trees

If request is too low:

- agent struggles
- build becomes slow or unstable

## Example explained (very plainly)

```makefile
request: 1GB
limit: 16GB
```

Means:

- OpenShift guarantees 1 GB RAM
- Agent can grow up to 16 GB RAM
- But never more than 16 GB

This gives:

- flexibility when needed
- protection for the cluster

## What is OOM and why it matters

**OOM = Out Of Memory**

It happens when:

- the agent uses more memory than its limit
- OpenShift immediately kills the container

Result:

- build suddenly stops
- logs show “OOMKilled”

So tuning limits properly helps: avoid random pipeline crashes

### Real-world analogy

- Request = minimum salary you need to survive
- Limit = credit card limit

If you hit the limit → card blocked

If you never get the minimum → you can’t start

# Monitoring with Grafana

Grafana dashboards help track:

- CPU spikes
- memory usage
- agent failures

_IT term: Grafana = tool for visualization of metrics._

Helpful for:

- diagnosing memory problems
- deciding resource increases

Example:

- find pod name in Jenkins log
- search it in Grafana
- view historical graph

# Persistent Volume Claims (PVC)

PVCs store long-term data:

- caches
- configuration files
- shared resources

Each agent mounts PVCs differently.

Example in YAML:

```yaml
persistentVolumeClaim:
  claimName: maven-cache
mountPath: /home/jenkins/.m2
```

# Nodes Outside OpenShift

Not all agents run in containers.

There are:

- Mac nodes
- Windows nodes
- Linux nodes

Used via SSH when:

- platform-specific builds are required

Example:

- iOS builds → Mac
- .NET builds → Windows

# Testing & Upgrade Environment

There is a dedicated test Jenkins instance:

- used for testing upgrades
- replicates production setup
- used before upgrading main Jenkins

Pipeline tests still often run on production because they are low-risk.

# Certificates

Certificates are added to agents to allow:

- HTTPS communication
- internal company systems access

Cert expiring dates are tracked.
