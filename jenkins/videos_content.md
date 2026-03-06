# Jenkins 1 - 20250602_130343 - Introduction & Access Basics

https://efortuna-my.sharepoint.com/:v:/r/personal/ekartlimbergova_zdenka_feg_eu/Documents/Nahr%C3%A1vky/Jenkins%20I.-20250602_130343-Meeting%20Recording.mp4?csf=1&web=1&e=HDqhCj&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D


## 1. Introduction

This lecture is an introductory session about **Jenkins** – what it is, how it is used in the company, and how its configuration works.

The session also covers:
- Useful learning resources (YouTube playlists, official docs)
- Access and permissions in Jenkins
- Configuration persistence using *Configuration as Code*
- Responsibility and handover scope (GUI vs backend)

---

## 2. Learning Resources

- Official Jenkins documentation (mentioned but not deeply explored)
- Google search (practical shortcut)
- YouTube tutorials (some older but still relevant)
- VS Code plugin for:
  - Writing Markdown
  - Previewing slides rendered from Markdown

---

## 3. Environment Update

Before the lecture:
- Jenkins was updated
- All plugins were updated
- Operating system was updated

This ensures:
- Stable environment
- Latest compatible versions
- Clean baseline for training

---

## 4. Nexus / Artifact Repository

Brief mention of:
- **Nexus (Artifactory-like repository)**
- Used to upload artifacts/files
- Developers can later reuse stored files

There were minor issues reported with Nexus during the session.

---

## 5. Jenkins Access & Permissions

A participant reported:

> “Missing the Overall Read permission”

This highlights:
- Jenkins uses role-based access control
- Users need at least:
  - `Overall Read` permission to see the UI properly
- Permissions are managed centrally

After adjustment, the user was able to log in correctly.

---

## 6. Jenkins UI vs Persistent Configuration

### Important Concept: GUI changes are NOT automatically persistent

When you:
- Create groups
- Assign roles
- Modify permissions
- Configure settings

They apply immediately in the GUI.

BUT...

If Jenkins:
- Restarts
- Is rebuilt
- Is redeployed

These changes may be lost unless stored properly.

---

## 7. Jenkins Configuration as Code (JCasC)

Jenkins uses:

> **Configuration as Code (YAML file)**

Key points:

- Configuration is stored in a YAML file on the server
- Plugin: *Configuration as Code*
- GUI does NOT automatically generate full working YAML
- You must manually update the YAML file when:
  - Adding users
  - Adding groups
  - Changing roles
  - Modifying permissions
  - Updating backend settings

After updating the YAML:
- Jenkins can rebuild its configuration automatically
- Configuration becomes persistent and reproducible

---

## 8. What Is Stored in Configuration

Examples mentioned:

- User roles and assigned permissions
- Permission matrix
- Secrets (e.g., Jira credentials)
- Integration settings
- Build backend configuration (e.g., OpenShift agents)
- Bucket/group definitions

This YAML file represents the full Jenkins configuration.

---

## 9. Backend vs GUI Responsibility Discussion

A discussion emerged about:

### What is part of Jenkins handover?

Two perspectives:

1. **GUI-only responsibility**
   - Managing jobs
   - Managing users
   - Running builds
   - UI-based administration

2. **Backend responsibility**
   - Server access
   - OpenShift agents
   - Configuration YAML
   - Updates
   - Infrastructure

Key takeaway:
- Backend configuration (OpenShift, server access) may belong to a different role.
- Application-level Jenkins administration may belong to the team.
- Clear ownership must be defined.

---

## 10. Build History & Basic Navigation

The presenter suggested:
- Start from the first page of Jenkins UI
- Understand:
  - What a build is
  - What build history means
  - Basic navigation

This part seems to transition into practical demonstration.

---

## 11. Logging Settings

Logging settings were mentioned but not prepared at the start.
Indicates future topics:
- Log configuration
- Debugging
- Troubleshooting

---

## 12. Key Concepts Introduced in This Lecture

- Jenkins basics
- Role-based permissions
- GUI vs persistent configuration
- Configuration as Code (YAML)
- Backend vs application ownership
- Artifact repository (Nexus)
- Importance of environment updates

---

## Overall Summary

This lecture serves as:

> **Foundational introduction to Jenkins administration**

Main focus areas:
- How Jenkins is configured
- Why GUI changes are not enough
- Importance of Configuration as Code
- Understanding permissions
- Clarifying responsibility boundaries

It prepares the ground for:
- Deeper Jenkins configuration
- Agent/backend understanding
- Infrastructure-level management



# Jenkins 1 - 20250602_132759 - UI Orientation, Folders/Roles, Queue, and Multibranch Pipelines

https://efortuna-my.sharepoint.com/:v:/r/personal/ekartlimbergova_zdenka_feg_eu/Documents/Nahr%C3%A1vky/Jenkins%20I.-20250602_132759-Meeting%20Recording.mp4?csf=1&web=1&e=KKj8Ep&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D


## 🎯 Overview

This session segment is a practical orientation to **how Jenkins is organized and used day-to-day**:

* Jenkins UI: queue vs running builds, console output
* Folder structure + role-based access (who can see/configure what)
* What Jenkins admins do vs what teams/developers do
* How teams create jobs (especially **Multibranch Pipelines**) linked to Bitbucket
* A quick reminder of agent execution model (mostly OpenShift; some SSH nodes)

---

## 1️⃣ Agents & Execution Model (quick reminder)

Roman mentions:

* There are also **classic SSH agents**, e.g. a **Windows build server over SSH** connected to Jenkins.
* Most execution is in **OpenShift**, but SSH-connected machines exist as exceptions.

---

## 2️⃣ Folder Structure & Access Model (Roles)

The discussion around ~26–32 minutes focuses on **who has access** and how folders are structured.

### What admins do

* Jenkins admins create/prepare:

  * **Folders** (projects/team spaces)
  * **Roles / access assignments** (permissions)

### What teams do

* Teams/developers create and manage:

  * **Pipelines / jobs inside their folder**
  * Job configuration, Jenkinsfiles, triggers, etc.

Key takeaway from Michaela:

> Admins prepare the folder and access; pipeline configuration is on the team.

### Naming conventions

* Folder naming follows the **team structure** from “Fortuna teams” (middleware, QA, native, etc.)
* Some areas contain **multiple teams in one folder**, depending on how their Bitbucket groups and pipelines are organized.

### “What if a team splits / wants a different folder but same pipelines?”

Model scenario discussed:

* A team splits and requests:

  * new folder (and possibly new Bitbucket space)
  * but wants to keep using the same pipeline logic as before

Answer pattern:

* Creating folders/projects is an **admin responsibility**
* Pipeline job configuration is possible for **anyone in the Bitbucket group assigned to that folder**

So, access is effectively:

* Folder visibility + configuration rights come from the **assigned group(s)**

---

## 3️⃣ JCasC vs GUI Configuration (very important mental model)

There is a recurring theme:

* GUI changes apply immediately
* Long-term stability comes from saving configuration in **Configuration as Code**

Roman mentions that you can:

* use “Configuration as Code” output (generated text)
* copy relevant parts into the Jenkins config files to make changes persistent

(They later show “Apply new configuration” in other sessions; here the important point is understanding **GUI vs persisted config**.)

---

## 4️⃣ Jenkins Queue, Running Builds, and Troubleshooting Basics

Roman points out UI areas:

### Queue

* Shows jobs **waiting for an available agent** / executor
* In OpenShift-based setup, “stuck forever” is much less common than in older Docker-era setups

Historical note:

* Before OpenShift migration, some builds could get stuck and required manual killing of containers on the server.

### Running pipelines

* The UI shows currently running pipelines
* You can click into:

  * build history
  * Bitbucket change overview
  * SCM checkout info
  * **Console Output**

**Console output patterns**

* Often starts with SCM checkout steps (Bitbucket)
* Later shows agent/container details
* In long-running jobs you might see:

  * one step taking hours
  * repeated logs for a single command

Roman notes the UI now includes a helpful overview of agents/containers used for the build (useful for troubleshooting).

---

## 5️⃣ How Teams Create Jobs: “New Item” and Multibranch Pipelines

Roman explains the most common workflow:

1. Team has repository in Bitbucket
2. They want Jenkins to build/deploy it
3. They go to Jenkins → **New Item**

### Why Multibranch Pipeline is the default choice

* The most used job type is **Multibranch Pipeline**
* Jenkins links the job to a Bitbucket repo URL
* Jenkins automatically:

  * scans branches
  * detects new branches
  * runs builds when changes are pushed (via Bitbucket triggers/webhooks)

Key point:

> With Multibranch Pipeline you don’t need to manually create a job per branch.

### Question: “Multibranch Pipeline” vs “Multibranch Pipeline default”

* Vaclav asks about the difference
* Roman says he hasn’t used “default” and recommends using plain **Multibranch Pipeline**

Practical guidance:

* For real projects: use Multibranch Pipeline
* For very quick experiments: Jenkins has “simple/testing” pipeline options, but they lack versioning benefits

---

## 6️⃣ Trade-offs: Simple pipelines vs versioned pipelines

Roman mentions that simple pipelines defined directly in Jenkins UI are:

* good for quick first tests
* **not used in practice** because:

  * no code versioning
  * harder to maintain/change collaboratively

So the recommended approach is:

* Jenkinsfile in repo
* Multibranch pipeline in Jenkins

---

## 🔎 Key Concepts Reinforced

* Jenkins org model: folders + roles
* Admin responsibilities vs team responsibilities
* Queue vs running pipelines, and where to look first
* Console output as primary troubleshooting source
* Multibranch Pipelines as the standard job type
* Prefer versioned Jenkinsfile workflows over UI-only pipelines

---

## 🧠 One-Sentence Summary

This Jenkins I session segment explains how Jenkins is organized (folders/roles), how to read queue and build output for troubleshooting, and why teams mostly use Multibranch Pipelines connected to Bitbucket instead of UI-only pipelines.






# Jenkins 1 - 20250602_142320 - Containers, agent images, and how agent builds cascade

https://efortuna-my.sharepoint.com/:v:/r/personal/ekartlimbergova_zdenka_feg_eu/Documents/Nahr%C3%A1vky/Jenkins%20I.-20250602_142320-Meeting%20Recording.mp4?csf=1&web=1&e=ryj9IJ&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D


 This section starts with a quick ops/support context and then goes deep into **containers**, **Dockerfiles**, and **how Jenkins agents are built & distributed in OpenShift**.

## What this lecture is mainly about

This part of **Jenkins I** is a practical “how our Jenkins actually works” overview:

* **Basic ops reality:** sometimes people call you to quickly check if Jenkins/server is OK; Jenkins itself rarely fails, but external dependencies can.
* **Plugins are the weak spot:** most risk appears during **updates**, because plugins/config can break boot or integrations.
* **Core architecture:** Jenkins controller runs the UI and orchestration, but most workload is executed in **OpenShift pods (agents)**.
* **Agent build system:** agents are built as **container images** from Dockerfiles in the “jenkins docker slaves / agents” repo.
* **Layered images:** “base images” (certs + Java) are reused by many agent images.
* **Cascade rebuilds:** if a base image changes, dependent images rebuild automatically (chain reaction).
* **Distribution:** images are versioned in **Quay**, then **imported to OpenShift ImageStreams**, and Jenkins runs them from the internal OpenShift registry to avoid overload.

---

## Key concepts explained in the lecture

### 1) Containers vs Virtual Machines (high-level but practical)

* **Image** = a packaged filesystem + metadata (think: “bundle of files”).
* **Container** = a running instance of an image.
* Containers are **Linux-native** (they run using the host Linux kernel), but you can build/run them from macOS/Windows via a Linux VM/runtime.

**Main difference vs VM:**

* VM emulates “a whole computer” (virtual CPU/RAM, full OS).
* Container is just **a process on the host** with isolation:

  * CPU limits
  * memory limits
  * filesystem isolation
  * networking isolation
  * namespaces/cgroups

This is why containers are usually **faster/lighter** than VMs.

### 2) Why OpenShift exists (in this Jenkins context)

OpenShift (Kubernetes) manages containers across many hosts:

* schedules pods onto nodes
* restarts/reschedules when needed
* scales replicas
* routes traffic / handles service discovery

Important point from the talk:

* A container is “just a process”, so it can’t be “live-migrated” like some VM scenarios.
* Apps should be designed to tolerate being killed and restarted on another node.

---

## Repo layout and how agents are built

The transcript references a repo like **“Jenkins Docker slaves” / agent repo**:

* **Each agent has its own folder**.
* There are **base images** (e.g., `slave-base-java-21`) and “tooling” images built on top.

### Dockerfile

A **Dockerfile** is described as the “recipe” to build an image.

Typical patterns mentioned:

* Variables


# Jenkins 2 – 20250616_100803

https://efortuna-my.sharepoint.com/:v:/r/personal/ekartlimbergova_zdenka_feg_eu/Documents/Nahr%C3%A1vky/Jenkins%20II.-20250616_100803-Meeting%20Recording.mp4?csf=1&web=1&e=bjXXNV&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D

**Date:** June 16, 2025  
**Presenter:** Roman Proschek  

---

## 1. Purpose of this lecture

This session continues Jenkins I and focuses on:
- **How Jenkins connects to OpenShift**
- **How Jenkins agents/pods look in OpenShift while pipelines run**
- **How configuration is managed and made persistent** (JCasC / `jenkins.yaml`)
- **How to safely change Jenkins settings** (GUI → YAML → apply)
- Credentials management and common pitfalls (hardcoding)
- Roles / permissions via AD groups

---

## 2. Recap: Jenkins ↔ OpenShift connection (runtime view)

What Roman summarizes from Jenkins I:
- Pipelines run in **OpenShift pods/containers** when using OpenShift/Kubernetes agents.
- In OpenShift you can find the **namespace/project where pipelines run** (mentioned as something like “U21”).
- Each running pipeline corresponds to a pod/container.
- There is a component/container that enables **communication between Jenkins controller (master) and the agent containers**.

Agent image details mentioned:
- The agent image is **not a minimal base like UBI8/alpine**, but a **privileged image** with **Java 17**.
- The agent is built “a bit differently” to support what Jenkins needs (connectivity + tooling).

---

## 3. Configuration export vs real configuration (important warning)

Roman points out a key Jenkins reality:
- Jenkins UI can show / export “generated configuration as code”
- BUT **the export is not meant to be directly usable as a full working configuration**
- It’s a helper: you **copy pieces**, compare, and write your own final YAML.

Quote idea (paraphrased):
- Export helps you *write* your own configuration, not replace it.

---

## 4. How the team manages Jenkins YAML changes (workflow)

### Why versioning / backups matter
- They keep **auto backup files** and timestamps.
- When something breaks, you can:
  - compare changes
  - see exactly what was modified
  - roll back quickly

### Typical change flow (the “safe way”)
1. Make a change in **GUI** (quick feedback)
2. Verify it behaves correctly
3. Copy relevant snippet from “generated config”
4. Add/merge it into **`jenkins.yaml`**
5. Click **“Apply new configuration”**
6. Confirm everything persists and is consistent after reload

Key reminder:
- If it’s not in `jenkins.yaml`, it can disappear after restart/rebuild.

---

## 5. Jenkins upgrade process (plugins + core)

Roman describes the usual upgrade order:
1. **Update plugins** as much as possible
2. **Update Jenkins core**
3. **Update plugins again** (because plugins may require a newer core)

Risk:
- Plugin changes or configuration changes can cause **Jenkins boot to fail** when Configuration as Code loads.
- If JCasC encounters an error, it can **stop the boot process** and you must inspect logs.

There is apparently a setting to allow Jenkins to boot even with config errors, but:
- they don’t use it (harder to notice errors / less safe in practice).

---

## 6. “Apply new configuration” button (what it does)

- Reloads JCasC configuration
- Can be done “whenever you want”
- Should not affect running pipelines (only configuration reload)

Used for:
- testing that `jenkins.yaml` is correct
- making sure GUI changes are now persistent

---

## 7. Example: making an AD group / auth entry persistent

Roman demonstrates:
- Find the relevant part in “generated config”
- Locate it under **Authorization Strategy** section
- Copy-paste into `jenkins.yaml`
- Apply configuration so the group / rights stay across reboots

Note:
- He couldn’t fully finish one step due to missing/incorrect credentials available at that moment, but the method is the key takeaway.

---

## 8. Registry / Quay configuration cleanup example

They noticed:
- Some entries are old/outdated (example: old Quay / default registry entry)
- Likely unused but should be corrected
- They modify the YAML and apply config to see the new entry reflected in UI

Takeaway:
- Jenkins config tends to accumulate legacy values → periodic cleanup is needed.

---

## 9. Security & login settings (Active Directory)

- Jenkins login is integrated with **Active Directory**
- There were historical attempts to optimize **slow logins**
- Still at least one unresolved login-performance issue
- Some older security settings may no longer be used, but they remain visible.

---

## 10. Credentials management (how it should work)

Roman explains the point of Jenkins credentials:
- Central place for usernames/passwords/tokens used by pipelines
- Developers reference credentials in pipelines
- If a password/token changes, you update it **once** in Jenkins and pipelines keep working

UI note:
- Adding credentials can be confusing because you must select a **domain** first
- They mostly use **Global** domain
- Common types:
  - Username/password
  - Personal access token (e.g., Bitbucket)
  - SSH username + private key

Critical rule (again):
- New credentials must be added to **`jenkins.yaml`** or they won’t persist.

---

## 11. Incident example: hardcoded credentials in shared pipelines (anti-pattern)

A real problem discussed:
- Someone generated “fake logins” / abusive logins against a Jenkins user in Jira
- They created a **new user + new password** to mitigate
- But they discovered:
  - Some **shared pipelines** had **hardcoded credentials**
  - Example: function like `jiraCheckDeployTicket`
  - Credentials were embedded (even base64) → still essentially hardcoded

Impact:
- Changing credentials centrally doesn’t help if pipelines hardcode them.
- Developers must refactor to pull credentials from Jenkins:
  - load from Jenkins credentials store
  - pass via environment variables / bindings
  - use token/basic auth properly (their choice)

Ownership:
- Shared pipelines are in developers’ hands; Jenkins admins can point out the issue, but devs must fix the code.

---

## 12. Master key & credential decoding (important concept)

Roman mentions:
- Credential encryption/decryption is tied to the Jenkins installation via a **master key**.
- Example implication:
  - You can’t decode production credentials on test if test has a different master key.
  - Only the instance with the correct master key can decode what it encrypted.

---

## 13. Roles, folders, and AD groups (RBAC model)

They review the security model:
- There are **global roles** (e.g., Admin)
- Access is granted via **AD groups**
- Roles define:
  - **where** (which folder)
  - **what** actions (view, build, configure, delete, credentials, etc.)

How it works:
- Define roles
- Define which folder scope they apply to
- Assign those roles to AD groups in “Assign roles”

Warning:
- It’s easy to click changes in UI,
- but writing correct YAML entries is harder → compare side-by-side and be careful.

---

## 14. Other admin screens briefly mentioned

- A screen where admins sometimes must **approve actions** requested by pipelines/scripts
  - Rarely used (a few times in years)
  - Slight discomfort: admin often approves based on dev explanation

- A newer troubleshooting topic touched briefly:
  - permissions for resources for HTML pages (not fully covered here)

---

## Overall Summary

**Jenkins II is mostly about “how Jenkins is actually operated safely”**:
- Jenkins runs builds in **OpenShift agent pods**
- Jenkins configuration must be **persisted via `jenkins.yaml` (JCasC)**
- UI is for quick testing, but YAML is the source of truth
- Upgrades can fail due to config/plugin mismatches → keep backups and validate with “Apply new configuration”
- Credentials should be centralized in Jenkins, not hardcoded in shared pipeline code
- RBAC is built around roles, folders, and AD groups


# Jenkins 3 - 20250616_140626

https://efortuna-my.sharepoint.com/:v:/r/personal/ekartlimbergova_zdenka_feg_eu/Documents/Nahr%C3%A1vky/Jenkins%20III.-20250616_140626-Meeting%20Recording.mp4?csf=1&web=1&e=TyYPA0&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D

* its a live demo of creating and depliying a new Jenkins build agent (upgrading to Java 21 and Gradle 8). in my notes: [3. agent_building](3.%20agent_building.md)


## 1. Purpose of this lecture

This session is practical/ops-focused. Main topics:
- **Build and publish an updated Jenkins agent image** (Gradle + Java upgrade)
- **Register the new agent in Jenkins (OpenShift pod templates)** via `jenkins.yaml`
- **Validate the agent by running a test pipeline**
- “Admin tricks”:
  - Jenkins **Script Console** (decrypting credentials, changing runtime params)
  - Fixing **HTML Report / CSP** issues
- Quick tour of **Jenkins infrastructure** (server, certificates, storage/LVM, logs)

---

## 2. Request: build a new Jenkins agent (Gradle 8 + Java 21)

Problem statement:
- Teams requested an updated agent with:
  - **Gradle 8**
  - **Java 21**
- Current agent had:
  - **Gradle 7**
  - **Java 17**

Roman explains:
- Agent build process and details are documented (Bitbucket repo + presentation notes).

---

## 3. How the agent build is triggered (Bitbucket → webhook → Jenkins job)

Workflow described:
1. Update repo files (example: **Jenkinsfile / README**)
2. Use a dedicated file/marker (mentioned as something like `.new-agent` or similar)
3. Push to Bitbucket
4. A **webhook** triggers a Jenkins build (example build number mentioned: ~480)
5. In Jenkins:
   - Open job
   - Click build number
   - Check **Console Output** for logs/errors

Build time notes:
- Some agents take ~2 minutes
- Large agents (e.g., Android) can take **15–20 min** because they include many GB of dependencies/tools

Result:
- Pipeline builds agent image and **pushes it to Quay** (image registry)
- After push, the new image/tag is visible in Quay

---

## 4. OpenShift: ImageStream / making the image available

After push to registry:
- They check OpenShift ImageStream and notice it wasn’t created/linked initially.
- Fix is done from terminal / OpenShift side (manual step).
- After creation, `oc describe imagestream ...` shows:
  - the new Gradle/Java 21 image
  - link to internal registry (`registry svc ...`)

Takeaway:
- Agent image exists in registry, but OpenShift **ImageStream** must exist/import correctly so Jenkins can use it.

---

## 5. Jenkins: register new agent in pod templates (via `jenkins.yaml`)

Goal:
- Make the new agent selectable in Jenkins pipelines (pod templates)

Roman’s preferred method:
- Edit directly in **`jenkins.yaml` on the server**
  - Faster than copying between UI windows
  - Avoids mistakes
  - Fits the “configuration is code” approach

### How they add the new template
- Start from an existing similar template (Gradle 7 / Java 17)
- Copy it as a base (same CPU/memory/resources/env)
- Rename appropriately to something like:
  - “gradle8-java21” (exact name not important; the idea is consistent naming)
- Update image reference/tag to the new one
- Remove/avoid conflicting `id` fields (he mentions it can behave like a different agent if left)
- There’s also mention of a “cache” concept:
  - cache mounted for Gradle (to speed builds)

Then:
- Save changes
- Go to Configuration as Code
- **Reload/apply configuration**
- Refresh pod templates page → new template appears

---

## 6. Validate: run a pipeline with the new agent

Roman uses a test repo/pipeline (his own example) to validate:
- Change pipeline to use the new agent (Gradle 8 + Java 21)
- Run build

Observations:
- First run can be slower because ImageStream needs to pull image locally
- Later runs are faster (image cached locally)

In pipeline console:
- At start, Jenkins prints which pods/containers were used:
  - `jnlp` container always present
  - plus the selected build container (Gradle 8 / Java 21)
- Verification:
  - Gradle version is correct

The demo run failed due to a Gradle-related error (“gradle failed”), but:
- The goal (agent creation + availability + selection) was demonstrated successfully.

---

## 7. Summary: steps to create a new agent (as presented)

Roman frames it as:
- “This is how to create a new agent in ~40 minutes” (rough idea)

Rough breakdown:
- Small Dockerfile edits (about 1–2)
- Jenkins pipeline: mostly copy/paste + minor changes
- One manual step in OpenShift (ImageStream/availability)
- Add/adjust pod template in `jenkins.yaml`
- Reload configuration
- Validate via test pipeline

---

## 8. Jenkins Script Console: decrypt credentials + quick troubleshooting

Roman shows an admin trick:
- Jenkins Script Console URL:
  - `JENKINS_URL/script`

What it’s used for:
- Run Groovy commands on the Jenkins server
- Can **decrypt hashed/encrypted credential values** (from configuration export)
- Useful when you want to manually test access (log in somewhere, debug by hand)

Important notes:
- In Jenkins UI you usually **can’t view a stored password**, only change it.
- You can find the hashed/encoded value in configuration export / JCasC snippet, then decrypt using script console function.

He also notes:
- He sometimes used Jenkins credentials to debug things in other teams’ Bitbucket orgs (e.g., checking webhooks), when cooperation is slow.
- Demo credential entry was **not persisted** (not added to `jenkins.yaml`) and then removed to keep Jenkins clean.

---

## 9. Fixing HTML Report plugin issue (CSP / Content Security Policy)

Problem:
- HTML Report plugin page looked broken / different
- Browser dev tools showed CSP errors (Content Security Policy)

Context:
- Jenkins controller shows the HTML report page, but the actual content might be served/stored on another machine (mentioned Mac mini / Mac computer).

Solution:
- Run a prepared **one-liner** in Script Console to change a default setting related to CSP so the report loads.
- For production:
  - this must be made **persistent via Jenkins/Java arguments**
  - not via `jenkins.yaml`
  - because it’s a **Java-level option**

Key idea:
- Some settings are **outside JCasC** and must be applied at JVM/service startup level.

---

## 10. Jenkins infrastructure overview (server, certs, storage, logs)

### Jenkins URL / port history
- Some developers use an old URL/hostname (e.g., “jenkins01”)
- Port 80 history is mentioned:
  - during migration (old Jenkins / docker swarm → OpenShift backend)
  - there was a period without certificate
- Now certificate handling is part of the setup (termination and cert/key storage)

### Where the server is
- Jenkins runs on a VM (VMware)
- Base OS mentioned: **RHEL 8**
- Service management:
  - `systemctl status jenkins` (status check)
  - and a “good command” to view service logs (likely `journalctl -u jenkins`)

### Logs in UI
- Many logs are also accessible in GUI:
  - Manage Jenkins → System Log
  - (with color differentiation, but no auto refresh)

### Certificates validity
- Certificates were mentioned as valid until **5 March 2026**
- Roman wasn’t sure if still valid at the time of the lecture (but the date is explicitly mentioned)

### Jenkins home / filesystem layout
- Jenkins home typically:
  - `/var/lib/jenkins` (symlinked to another location)
- Reason:
  - keep large data/logs on a bigger storage mount

### Storage / LVM
- Jenkins data is stored on a larger disk (roughly ~1 TB)
- It’s on **LVM** so storage can be enlarged if needed
- He mentions there is documentation/commands for:
  - rescanning disk size
  - enlarging physical volume / logical volume
  - resizing filesystem
  - then verifying the final size

---

## 11. What’s next (mentioned)

Roman mentions upcoming focus:
- Look at OpenShift “tomorrow” (agents, volumes, etc.)
- Possibly upgrade test Jenkins soon
- Some steps will be “first time” / unusual items

---

## Overall Summary

**Jenkins III is a hands-on operations session**:
- Building a new Jenkins OpenShift agent image (**Gradle 8 + Java 21**)
- Publishing to registry and ensuring OpenShift ImageStream points to it
- Registering the new pod template in Jenkins via **`jenkins.yaml`**
- Validating via a test pipeline (containers shown: `jnlp` + build container)
- Using **Script Console** for:
  - decrypting credentials (for troubleshooting)
  - applying quick runtime changes
- Fixing an **HTML Report plugin** issue caused by **CSP**
- Understanding Jenkins server basics:
  - certificates, OS, service status/logs
  - Jenkins home and storage via LVM


# Jenkins 4 - 20250617_110433

https://efortuna-my.sharepoint.com/:v:/r/personal/ekartlimbergova_zdenka_feg_eu/Documents/Nahr%C3%A1vky/Jenkins%20IV.-20250617_110433-Meeting%20Recording.mp4?csf=1&web=1&e=GJl5iL&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D

---

## 1. Purpose of this lecture

This session continues the Jenkins administration series and shifts focus to:
- **OpenShift side of Jenkins** (agents, namespaces, pods, cleanup)
- **Agent rebuild strategies** (cascade rebuilds when base images change)
- **Shared Libraries / Shared Pipelines** (why they exist, how they avoid Jenkinsfile limits)
- **Docker-in-Docker (DinD) pattern** in OpenShift (sidecar, port, readiness loop)
- **Resource sizing (requests vs limits)** and troubleshooting using **Grafana**
- **Persistent caches** via **PVCs** (Maven/NPM/Rush caches)
- **Non-OpenShift nodes** (Mac/Windows/Linux via SSH) and basic prerequisites
- Overview of **test vs prod Jenkins** usage and what’s next (upgrade + Bitbucket/Nexus + backups)

---

## 2. Quick recap of previous topics (from presentation)

Roman lists items already covered earlier:
- Managing + assigning roles
- Script approval
- Retrieving hashed password (and decoding)
- Jenkins hardware / master server
- Resize filesystem (doc exists; he wants to refine it later)

---

## 3. Jenkins ↔ OpenShift overview (where things run)

Key architecture points:
- Jenkins primarily uses **OpenShift to run agents** (pods)
- It runs in the **management cluster**
- Everything runs in namespace/project like:
  - **`jenkins-shared`** (mentioned explicitly)
- You can list and inspect **running pods** for current pipelines.

### Cleanup of failed pods
- Roman notices there are **many failed pods** (unusual)
- He demonstrates that they can be cleaned up (select pods with error and delete)
- Point: failed pods normally shouldn’t accumulate, but sometimes they do and need cleanup.

---

## 4. Agent build details & Chrome versioning approach

Roman mentions historical packaging challenges:
- Sometimes easiest way to install unusual packages is using repos from other RHEL-based distros.
- **Chrome in agents** is now installed differently:
  - they download Chrome manually, place it into correct folders, adjust paths
  - reason: **better control over Chrome versions**
  - repo-based install tends to give only “latest”, making older versions hard.

Some parts are described as old troubleshooting steps and may not be used anymore.

---

## 5. Rebuilding many agents (cascade rebuild concept)

Important operational tip:
- If you rebuild / change a **base image** (example: “Java 17 base”), it can trigger rebuilding **many dependent agents**.

What can go wrong:
- Package changes may cause installation failures in some downstream images.
- If an agent build fails, pipeline stops → you must identify which images were not rebuilt.

Ways to recover:
- Fix the root issue, then trigger rebuild again.
- One “easy trigger” method:
  - make a harmless change in Dockerfile (e.g., add a comment line)
  - this forces pipeline to rebuild.

How to check what still needs rebuild:
- Look in **Quay registry**
- Sort by **last modification date**
- Example mentioned: Docker-in-Docker agent was last rebuilt “last year” → needs rebuild.

---

## 6. Jenkinsfile size limit and “Method code too large”

Roman warns about an error you may hit as pipelines grow:
- **`Method code too large`**
- Root idea: **Java bytecode size limits** can be hit when Jenkinsfile / compiled script becomes too big.

Mitigation approach:
- Move logic into **Shared Library / Shared Pipelines**:
  - stored as Groovy scripts
  - callable from Jenkinsfile as simple functions

Example referenced:
- `jiraCheckDeployTicket`
  - moved into shared library
  - previously had hardcoded credentials issues
  - fixed later (Andro Bobica found hardcoded creds in multiple places)

Why Shared Libraries help:
- Jenkinsfile stays small (one-liners calling functions)
- Shared scripts avoid the same Jenkinsfile size/structure limitations
- Reuse across teams: write once, use everywhere

---

## 7. Alternative mitigation: use the whole repo as a pipeline library

Roman describes an additional “future solution”:
- Load the **whole repository as a shared library**
- Create a `vars/` directory in that repo (same structure as shared libraries)
- Put a large pipeline Groovy script there (e.g., `salaPipeline.groovy`)
- Jenkinsfile becomes basically:
  - “call `salaPipeline`” (file name without extension)
- Jenkins automatically loads from `vars/` and executes it.

He calls it “Ouroboros-like” because Jenkins downloads the repo and then uses that same repo as a library.

Trade-off notes:
- Global trusted libraries exist (shared across the company)
- Teams can also configure their own private libraries
- Security trade-off:
  - shared library in OpenShift can be changed more broadly (“not super safe”)
  - private team libraries restrict access but reduce sharing.

---

## 8. Docker-in-Docker (DinD) in OpenShift agents

Roman references internal documentation and explains the pattern:

### Sidecar model
- Pipeline runs in a main container (agent)
- **Docker-in-Docker runs as a sidecar container** in the same pod
- Docker API is exposed to the main container via environment variable:
  - points to `localhost:<port>`

### Port correction
- There’s a documentation mismatch:
  - one place said `2475`
  - Roman notes correct port is **`2375`** and says he will fix docs.

### Timing issue & readiness loop
Historical issue:
- DinD can take **20–30 seconds** to start
- Some pipelines start Testcontainers immediately → Docker isn’t ready yet
- Solution: a loop that checks Docker API availability on `localhost:2375`
  - retry every second until ready
  - then continue pipeline

Symptom/error:
- “No Docker environment ready” (Docker API not available yet)

---

## 9. Best-practice discussion: smaller, purpose-built agents

Roman notes an “ideal world” approach:
- Prefer smaller agents (single-purpose):
  - node-only image
  - java-only images (17/21)
- Run each step in the minimal container required

Reality:
- Would require reworking many existing pipelines → “more of a dream” for now.

---

## 10. OpenShift resources: CPU/memory requests vs limits

Roman explains practical resource strategy:
- Typically set:
  - **lower requests** (what it needs to start/run normally)
  - **higher limits** (max allowed burst)

Why request=limit isn’t great (in general):
- Less flexible scheduling / inefficient resource handling

But special case:
- Some pods start small (e.g., 1 GB RAM), then later spawn multiple containers and jump to huge memory usage (example: ~12 GB).
- If requests are too small, OpenShift schedules the pod onto a node that doesn’t have enough headroom → later OOM / failures.
- Fix: reserve “insane” memory in requests (e.g., **16 GB**) so the pod lands on a node with enough memory.

Example values mentioned (illustrative):
- Request: 0.5 CPU, 1 GB RAM
- Limit: 5 CPU, 8 GB RAM
(And in some special cases, requests must be much higher.)

---

## 11. Troubleshooting resource issues with Grafana

They use Grafana dashboards for Jenkins agents:
- If a build fails due to memory/CPU, you can look up pod metrics.

Workflow described:
1. Get pod name:
   - from OpenShift
   - or from pipeline logs (it’s printed)
2. Open Grafana (OpenShift → Jenkins agent dashboards)
3. Search by pod/container name
4. Inspect CPU and memory usage over time
5. Adjust pod template requests/limits if needed

Note:
- Grafana time-range UI can be tricky; sometimes you need to refresh when changing the time window.

---

## 12. ImageStreams and PVCs (caches & storage)

### ImageStream
- Works like a cache for images pulled from Quay.

### PVCs (PersistentVolumeClaims)
- Used as persistent caches (disk space) for agents.
- Examples mentioned:
  - **Maven cache** (old was ~39 GB; later enlarged to ~300 GB → docs should reflect this)
  - NPM caches **per Node version** (cannot share across versions):
    - separate cache for Node 18, Node 20, Node 22
  - “Rush tool” also needs cache (large data, avoid downloading every build)

How it’s wired into agents:
- In pod templates you define PVC mounts:
  - PVC name in OpenShift
  - mount path(s) inside container
- Maven cache is mounted to two places (likely due to Maven setup specifics).

If you introduce new tool/version combination:
- you may need a **new PVC** with a new name and then reference it in templates.

---

## 13. Non-OpenShift agents (Mac/Windows/Linux) via SSH

Roman mentions nodes outside OpenShift:
- Mac / Windows / Linux nodes exist
- Jenkins connects via **SSH**
- These machines are **not managed by Jenkins/OpenShift team**
  - devs/admins own them
  - Jenkins side ensures connectivity only

Prerequisites:
- **Java installed** (Java 17+ recommended; LTS preferred)

He mentions there’s a way to run limited commands on such nodes from Jenkins, but:
- it’s rarely used
- developers often have full admin access via remote desktop, so they do most operations themselves.

---

## 14. Jenkins Pipeline agent configuration tip (common mistake)

For OpenShift/Kubernetes agents:
- You typically set:
  - “inheritFrom” (template)
  - “defaultContainer”
so pipeline steps run inside the intended container automatically.

If you don’t set it:
- you must wrap each step in an explicit `container('name') { ... }`
- that’s useful for multi-container workflows, but most pipelines prefer a single default container.

He demonstrates that you can use **Replay** to view the full pipeline and see:
- agent selection via label/template
- where commands are executed.

---

## 15. Test vs Production Jenkins usage

They have a test Jenkins server (and may have two soon):
- Same Jenkins version as production
- Used mainly for:
  - testing **upgrades**
  - testing Jenkins-level changes

Roman notes:
- He often tests pipelines on production because pipelines “can’t destroy anything” in many cases
- Sometimes it’s easier to test on prod because you can quickly revert configuration:
  - edit `jenkins.yaml`
  - apply configuration
- On test, you might need a pipeline that reproduces the exact case and has all dependencies.

---

## 16. Certificates

Roman mentions:
- Certificates are mainly added to agents because they need to access internal websites.
- He plans to re-check certificate status/validity.

---

## 17. What’s next

Planned upcoming sessions:
- **Jenkins upgrade** (needs ~1 hour, many steps)
- **Bitbucket + Nexus** (another hour)
- Backup topics
- “DR solution” is still work in progress
- Roman will ask Zdenka for **two more hours** total to finish remaining topics.

---

## Overall Summary

**Jenkins IV is a deep-dive into the operational reality of Jenkins on OpenShift**:
- How agents run in `jenkins-shared`, how to clean failed pods
- How large-scale agent rebuilds work and how to trigger/verify rebuilds via Quay timestamps
- Why Shared Libraries exist (size limits, reuse, removing hardcoded secrets)
- How Docker-in-Docker is implemented (sidecar + readiness loop on `localhost:2375`)
- How to set resources safely (requests vs limits) and validate issues via Grafana
- How caches work (PVCs for Maven/NPM/Rush) and why some caches must be per-version
- Reminder of non-OpenShift nodes via SSH and best practices for choosing default container/template
- Next steps are Jenkins upgrade + Bitbucket/Nexus + backup/DR topics



# Jenkins 4 - 20250619_100216

https://efortuna-my.sharepoint.com/:v:/r/personal/ekartlimbergova_zdenka_feg_eu/Documents/Nahr%C3%A1vky/Jenkins%20IV.-20250619_100216-Meeting%20Recording.mp4?csf=1&web=1&e=i9K73a&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D


---

## 1. Purpose of this lecture

This session is a **hands-on upgrade of TEST Jenkins** and a deep dive into:
- Safe upgrade workflow (snapshot + backups + staged updates)
- Why this was a “bad time” to upgrade (Bitbucket plugin refactoring / fast changes)
- Downgrading specific plugins to keep Bitbucket ↔ Jenkins integration working
- Practical OS/Jenkins/plugin update steps on the server
- Snapshot-based DR approach (LVM snapshots + copy to another server)

---

## 2. Why the upgrade was unusually painful (Bitbucket plugin changes)

Roman says this is basically the worst time to upgrade because:
- Bitbucket plugin changes are actively being developed/refactored
- Some plugin updates this week broke compatibility / dependencies
- To keep existing Bitbucket ↔ Jenkins configuration working, he needed to:
  - **downgrade ~3 plugins** (at least)
  - because newer versions removed/changed webhook behavior or dependency chain

Key lesson:
- Jenkins upgrades are not only “Jenkins core” → plugin ecosystem can force you into **pinning** or **downgrading**.

---

## 3. Safety first: snapshots + external backup

### Snapshot strategy (VM snapshot)
- Before risky work, take a **snapshot** (VMware snapshot)
- Requirements:
  - Stop/prepare Jenkins properly before snapshot
  - Ensure snapshot creation won’t power off unexpectedly (he removes a setting so snapshot creation doesn’t auto power off)
- Rationale:
  - You can always revert the whole server state (especially valuable for prod too)
- Caveat shown in session:
  - “Revert to snapshot” failed because the **snapshot was deleted** → reminder to verify snapshot exists before starting.

### Secondary safety net (backup on another server)
- Roman mentions he tested a DR/backup approach:
  - even if snapshot is missing, there is a **backup on another server**
- DR solution discussion is planned for next week (still WIP)

---

## 4. Test vs prod restart behavior

In Jenkins plugin manager there is an option:
- Restart Jenkins automatically “when no jobs are running”

Roman’s practice:
- **OK for test** (few jobs)
- **Not for production**
  - In prod he restarts manually on the server
  - Reason: waiting for “all jobs finished” can take *hours* (he cites a case where someone waited ~2 hours)

---

## 5. Roman’s operational habit: prepare copy/paste command list

He mentions good practice:
- Before doing upgrades (often late afternoon/evening), prepare:
  - the exact commands
  - in the right order
- So you can copy/paste without thinking under time pressure.

---

## 6. Upgrade workflow used in this session (high-level)

The pattern described:

1. **Update plugins** in Jenkins UI (select all → update)
2. **Stop Jenkins**
3. Handle a special plugin that must be upgraded alongside Jenkins (he follows documentation):
   - Comment out related Configuration-as-Code entries temporarily (so JCasC doesn’t block startup)
   - Remove old plugin folder/files
   - Download correct plugin version (via proxy)
   - Fix filesystem permissions (because actions done as root)
4. **Update OS + Jenkins** (system packages)
5. Start Jenkins
6. Repeat: update plugins → restart → update plugins until nothing left

Why multiple rounds:
- Plugin dependencies change after Jenkins core update
- After restart, new plugin updates appear (in the session it jumps to a lot of updates, e.g., ~172)

---

## 7. Disk space and “why snapshot timing matters”

Roman describes a previous issue:
- Upgrades can fail if there isn’t enough space
- Temporary plugin/cache files can reappear after revert
- He ended up deleting some files multiple times, then learned:
  - do cleanup **before** snapshot → much easier

He also mentions removing unused “engine …” components:
- They have something like “engine spools” on prod/test but don’t use capabilities
- Paid/plus version is unnecessary
- Removing it is “easy” and separate from Jenkins upgrade itself

---

## 8. The core problem: Bitbucket webhooks stopped working after updates

Observed symptom:
- He made a change in a repo (Jenkinsfile) but **build was not triggered**
- This indicated Bitbucket webhook integration was broken.

Roman’s diagnosis and fix path:
- The warning/error appears depending on plugin dependency satisfaction.
- The fix required downgrading:
  - **Bitbucket Branch Source plugin** (first)
  - then additional dependent plugins (because once Branch Source is older, newer dependencies break)

He mentions that:
- After downgrading Branch Source, Jenkins may show warnings about unsatisfied dependencies,
  - but webhook behavior can start working again (at least partially).

---

## 9. The “chain downgrade”: Branch Source → Bitbucket Pipeline → Blue Ocean

Roman explains the dependency chain (as described in the transcript):
- Newer implementations removed/changed the webhook type/behavior
- Therefore he needed **older versions** of:
  - Bitbucket Branch Source (explicit)
  - Bitbucket Pipeline (explicit)
  - Blue Ocean (explicit)

Downgrade mechanics:
- Stop Jenkins
- Remove the plugin files/folders
- Download older plugin versions manually
- Note about file types:
  - Jenkins normally installs `.jpi`
  - manual downgrade uses `.hpi`
  - Roman says he doesn’t know why both exist, but both appear in practice

Outcome check:
- He tests again by committing a change
- Build triggers successfully (example: Build #17 triggered)
- Then he reboots server to load new kernel (OS upgrade finalization)

Roman’s comment:
- “Worst upgrade I’ve ever done” because of rapid plugin churn + forced downgrades.

---

## 10. How he plans to do Production upgrade

He says production upgrade will follow the same structure:
- Stop Jenkins
- Perform all update/downgrade steps while stopped
- Start Jenkins and verify Bitbucket triggers
- Snapshot is “best thing” before doing it
- He also wants better documentation/screenshots next time

---

## 11. Disaster Recovery plan (snapshot + copy Jenkins home)

Key concept:
- “Good thing about Jenkins: everything is stored in one place”
- DR idea:
  1. Create an **LVM snapshot** of Jenkins filesystem inside the VM (not VMware snapshot)
  2. Copy snapshot data to another server/location
  3. Delete snapshot (self-cleaning, avoid space usage)
  4. In disaster, start Jenkins on another server with that copied folder
     - possibly run a script to adjust a few values
     - Jenkins should come up with jobs + config + logs as of backup time

Backup frequency:
- Plan is **daily** (mentioned explicitly)

He shows:
- A DR test server running “the same Jenkins” but with different credentials / limitations
- Some access issues existed due to firewall rules, but Jenkins itself runs.

---

## 12. Closing notes (Bitbucket future-proofing)

Roman notes:
- Current situation is unstable because everyone relies on webhook integration and it’s changing rapidly
- They need to find out what developers are actually using
- Likely they will switch to **another webhook** option that is:
  - free from Bitbucket
  - more future-proof (in theory)
- The breaking change they saw happened “2 days ago” (relative to the lecture).

---

## Overall Summary

This lecture is a real-world example of Jenkins ops reality:
- Upgrading Jenkins is mostly **plugin dependency management**
- Always protect yourself with **snapshots + backup**
- Do test + prod upgrades close together (plugin ecosystem changes fast)
- Be ready to **downgrade** Bitbucket-related plugins to keep webhooks working
- Validate by pushing a commit and confirming build triggers
- Long-term: implement DR using **LVM snapshots + daily copy** of Jenkins home


# Jenkins 4 - 20250619_144218

https://efortuna-my.sharepoint.com/:v:/r/personal/ekartlimbergova_zdenka_feg_eu/Documents/Nahr%C3%A1vky/Jenkins%20IV.-20250619_144218-Meeting%20Recording.mp4?csf=1&web=1&e=M5bSpQ&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D

* [11. adding_new_mac.md](11.%20adding_new_mac.md)


---

## 1. Purpose of this lecture

This session is a practical walkthrough of adding a **new Mac machine as a Jenkins permanent agent**.

Main topics:
- Create a new **Jenkins node** called **`macos-qa2`** (second QA Mac; one already exists)
- Configure it as a **Permanent Agent** (static machine, not OpenShift/Kubernetes)
- Add **SSH credentials** (client cert / private key)
- Diagnose connection failure (network/firewall rules missing)
- Persist configuration via **JCasC / `jenkins.yaml`**
- Create an infra firewall ticket for network admins

---

## 2. Context: request + access details

- Request came to add a **new Mac** Jenkins agent.
- IP address and SSH key material were provided (generated by someone from infra; name referenced in transcript).
- Planned node name: **Mac OS QA 2 / `macos-qa2`**.

---

## 3. Jenkins configuration: creating the node

Path (conceptually):
- **Manage Jenkins → Nodes (Agents) → New Node**

Configuration choices shown:
- **Name:** `macos-qa2`
- **Type:** **Permanent Agent**
  - Static machine always available
  - Jenkins connects and keeps it registered
  - Not created dynamically like OpenShift agents

### Executors (parallelism)
- “Number of executors” determines how many pipelines can run simultaneously on that Mac.
- Can be adjusted later if needed.
- In the session, **20 executors** is mentioned and confirmed as correct (even though it sounded high at first).

### Usage restriction (important)
- Set usage to **“Only build jobs with label expressions”** (or equivalent)
- Add a label (QA/mac label) so:
  - Jobs explicitly targeting this Mac can run there
  - Jenkins won’t try to run OpenShift/Kubernetes agents on the Mac

### Launch method
- Launch via **SSH**:
  - Provide host/IP address
  - Use SSH credentials (username + private key)

---

## 4. Credentials: SSH username + private key

- Roman creates a new credential entry:
  - Type: **SSH Username with private key**
- Mentions downloading/using a **client certificate** (or key material) as part of setup.
- Goal: Jenkins can authenticate to the Mac reliably.

---

## 5. First connection attempt: failure diagnosis

After saving node config:
- Jenkins tries to connect → check node log:
  - “Trying to connect…”
  - Connection times out

Roman diagnoses:
- **Firewall / network rules are missing**
- Jenkins controller cannot reach the Mac on the required ports (SSH)

Key point:
- This is not a Jenkins config issue; it’s **network connectivity**.

---

## 6. Infra action: create firewall ticket (Jira)

Roman decides it’s best to create an infrastructure firewall request ticket.

Ticket details (as described):
- **Source host:** Jenkins server (controller)
- **Destination host:** the Mac machine (IP)
- Wants both protocols mentioned:
  - He says “TCP and UDP” (but for SSH specifically, TCP/22 is the key requirement)
- Application/catalog context: “Jenkins”
- Then they must wait for infra/network admins to implement the rule.

---

## 7. Persist changes: Configuration as Code (JCasC)

Even though the node and credentials were added in GUI, Roman stresses persistence:
- Go to:
  - **Manage Jenkins → Configuration as Code**
- Generate/export configuration snippet
- Search for the new node / new credentials entry
- Add it into **`jenkins.yaml`**
- Apply/reload configuration so it survives restart/rebuild

He confirms they are “prepared” from Jenkins side once this is saved.

---

## 8. Timeline & communication about infra response

A participant asks how fast infra responds.

Roman’s answer (paraphrased):
- Used to be faster; he has multiple requests pending.
- Ticket was created recently (yesterday / 1–2 days ago).
- Approval step depends on a person who was on vacation that day.
- Expectation:
  - approval tomorrow
  - access likely by Monday/Tuesday

---

## 9. Closing comparison: why OpenShift agents are easier

Roman contrasts:
- **OpenShift-based agents**:
  - just update Dockerfile + Jenkinsfile
  - rebuild image
  - no extra firewall rules typically needed (because everything runs inside cluster)

- **External nodes (Mac/Windows/Linux via SSH)**:
  - require network connectivity from Jenkins controller
  - often require **firewall rule changes**
  - more coordination with infra

Recording ends after this.

---

## Overall Summary

This lecture shows the full process of adding a **Mac QA machine as a Jenkins permanent SSH agent**:
- Create `macos-qa2` as a permanent agent, set executors, labels, and SSH launch method
- Add SSH credentials (username + private key)
- Troubleshoot timeout → root cause is missing firewall/network rules from Jenkins to the Mac
- Create an infra ticket to open connectivity
- Persist node + credentials using **JCasC (`jenkins.yaml`)**
- Wait for infra approval/implementation, then re-test and continue troubleshooting if needed


# Jenkins 4 - 20250625_100529

https://efortuna-my.sharepoint.com/:v:/r/personal/ekartlimbergova_zdenka_feg_eu/Documents/Nahr%C3%A1vky/Jenkins%20IV.-20250625_100529-Meeting%20Recording.mp4?csf=1&web=1&e=rLnDVf&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D

[my detailed notes to the video:](32.external_aps.md)

## 1. Purpose of this lecture

This session connects Jenkins to the “ecosystem around it” and focuses on practical troubleshooting.

Main themes:
- Overview of apps used together with Jenkins:
  - **Bitbucket**, **Vault**, **Nexus**, **Quay**, **SonarQube**
  - plus a mention of a simple “text-file list” tool (unclear in transcript)
- How to troubleshoot pipeline failures using:
  - Jenkins **Replay**
  - OpenShift (`oc`) + remote shell into running pods
- Common pitfalls:
  - ending up in the wrong container (`jnlp`)
  - missing **proxy** configuration / using the wrong proxy for the environment
  - quoting issues in shell commands
- Ops hygiene:
  - cleaning Jenkins workspace folders periodically
- Re-cap / deeper context on:
  - HTML Publisher plugin + **Content Security Policy (CSP)** fix and persistence
  - Bitbucket webhooks: deprecation of plugin approach vs moving to native Bitbucket webhooks
- Extra real-life troubleshooting:
  - adding a Mac node: SSH keys / `authorized_keys`
  - Mac toolchain issues (Ruby versions, Homebrew, Bundler)

---

## 2. “Other apps used with Jenkins” (integration map)

Roman lists key systems and what they’re for:

- **Bitbucket**
  - Source control (repos, PRs, webhooks triggering builds)

- **Vault**
  - Secret storage (credentials, tokens) used by pipelines

- **Nexus**
  - “Artifactory-like” storage:
    - binary/file repository
    - proxy to the internet for package repositories

- **Quay**
  - Container image registry (store/pull/push build images)

- **SonarQube**
  - Code quality/testing metrics (mostly developer-facing)

- “WASP” (or similar; unclear)
  - described as a text file containing a list of something  
  - transcript is incomplete here, so exact meaning isn’t reliable

---

## 3. Troubleshooting approach: stop the pipeline and reproduce with Replay

Roman’s “fastest way” when something fails:
1. Open the failing pipeline build
2. Use **Replay**
3. Insert a command / temporary step so the pipeline:
   - stops at the stage you want
   - or “sleeps” so you can inspect the environment while it’s running

This is helpful because:
- you can debug the exact state of the build environment
- you can reproduce without rewriting the pipeline permanently

---

## 4. OpenShift troubleshooting: inspect running agent pods

Roman demonstrates typical steps in the management cluster (namespace/project like `jenkins-shared`):

1. `oc get pod`  
   - find the currently running pipeline pod

2. `oc rsh <pod>` (or equivalent remote shell)  
   - get into the running container

### Common mistake: landing in `jnlp`
- OpenShift/Jenkins often drops you into **`jnlp`** by default
- `jnlp` is mostly the **communication container**
- It doesn’t contain build tools or workspace files you expect

Fix:
- specify the correct container name (example mentioned: **node 8** container)
- then you can see:
  - Jenkins workspace directory
  - checked-out source code
  - tools needed to run build commands

Workspace detail:
- Jenkins always works in a **workspace folder** named after what it builds.

---

## 5. Proxy issues (very common root cause)

Roman emphasizes:
- Developers often run a command that needs internet access
- It fails because they **forgot proxy** settings

He shows:
- With proper proxy env vars, the same command works

Important nuance:
- Proxies differ by environment:
  - management cluster (where agents run) uses one proxy
  - dev/test/stage/prod can have different proxy endpoints

He gives a pattern of proxy names (example):
- “...66” for testing
- “...82” for production
- mismatching proxies happens when someone copies a pipeline from another environment (e.g., deployment pipeline) without adjusting proxy.

If proxy is correct but target URL is still unreachable:
- The URL may need to be **allowed on the proxy**
- That requires an internal ticket to whoever manages proxy allowlists (he mentions a colleague but not clearly by name).

Real example mentioned:
- On test, a repo tried to reach a target route (he mentions “Austria” as an intermediate hop) and it was blocked at proxy level → needed allowlisting.

---

## 6. Shell quoting pitfalls

Roman notes:
- Single quotes vs double quotes behave differently
- Sometimes commands fail due to quoting differences (especially in shell)
- His habit: test and adjust (and Google exact quoting rules when needed)

---

## 7. Workspace cleanup on Jenkins server

He mentions operational hygiene:
- Jenkins workspace can grow over time
- Good to clean occasionally

Example approach:
- delete workspace/build artifacts older than a threshold (he mentions a tested command like “delete older than 90 days”)

Options:
- run manually from time to time
- or schedule it as a Jenkins job / cron-like cleanup

---

## 8. HTML Publisher plugin + CSP (Content Security Policy) recap

Roman revisits a previous troubleshooting story:

Problem:
- HTML Publisher reports page looked “ugly” / missing videos/images (empty content)

Cause:
- Browser errors showed **CSP (Content Security Policy)** blocking resources

Context nuance:
- Jenkins displays the HTML page, but the media files (images/videos) are on a **Mac machine**
- So CSP must allow loading resources from that source

Fix:
- He found a working CSP tweak (doesn’t fully understand CSP yet, but it works)

Persistence issue:
- Currently applied as a **runtime** setting via Jenkins **Script Console**
- After Jenkins restart you must re-apply the script:
  - Manage Jenkins → Script Console → run one-liner

To make it permanent:
- Should be added as a **Jenkins Java argument** / service configuration (`jenkins` service file / startup args)
- Not in `jenkins.yaml`
- He suggests this is a good opportunity to test it properly.

Governance question raised:
- Should CSP rules be standardized / discussed with security?
- He notes Jenkins is not public, so risk is lower, but standardization might still be better.

---

## 9. Bitbucket webhooks: plugin approach is being deprecated

Roman summarizes the situation:

History:
- Bitbucket originally lacked built-in webhook capabilities needed → community/plugins filled the gap:
  - Bitbucket-side plugin
  - Jenkins-side plugin that depended on it

Now:
- That plugin-based approach is being **deprecated/stopped**
- Vendor pushes people toward **native Bitbucket webhooks**
  - but native has limited capabilities

Current state:
- Jenkins was updated, but they are keeping older plugin versions (downgraded) so the old plugin-based webhook continues to work

Decision options:
1. **Rework to native Bitbucket webhooks** (ideally supported long-term)
2. **Wait** for developers to resolve / new solution to appear

Roman’s concern:
- Waiting has no guarantee someone will do the required work.
- Hard to find clear information/roadmap on how quickly things will change.

Test environment advice:
- Use snapshots:
  - snapshot → test changes all day → revert snapshot → clean state

They also now have:
- another copy of test Jenkins (as part of DR approach) on a newer server (RHEL9 mentioned earlier in other lecture context).

---

## 10. Mac node troubleshooting (SSH + toolchain issues)

### SSH access basics (for Mac nodes outside OpenShift)
- Jenkins uses:
  - private key on Jenkins side
  - public key must be added to the Mac user’s:
    - `~/.ssh/authorized_keys`

### Typical Mac build tool issues
Roman describes real issues when adding another MacBook:
- Ruby version mismatch (Mac served an older Ruby than needed)
- Needed to adjust PATH / binary locations so Jenkins finds correct tools
- Homebrew overview:
  - macOS default packages are often obsolete
  - Homebrew installs newer versions of CLI tools/languages

Other issues mentioned:
- Bundler error (likely a Ruby dependency issue) — dev needed to resolve
- Some documentation updates needed (also mentions Windows doc changes)

Key point:
- These are not “Jenkins problems” strictly — more *Unix/toolchain* problems — but they surface as Jenkins build failures.

---

## 11. Process improvement: centralize troubleshooting in a DevOps channel

Roman mentions an agreement:
- Developers should post pipeline problems into a central channel (e.g., **“SB DevOps”**)
- Then others can review and try to solve collaboratively.
- He also mentions they need to know/confirm who manages that channel (he will ask Misha).

---

## Overall Summary

**Jenkins IV (June 25) is a practical integration + troubleshooting session:**
- Quick map of Jenkins dependencies (Bitbucket/Vault/Nexus/Quay/SonarQube)
- How to debug effectively: Replay + OpenShift shell, but avoid the `jnlp` trap
- Proxy problems are a top root cause (and proxies vary by environment)
- Keep Jenkins clean (workspace cleanup)
- CSP fix for HTML Publisher is working but needs to be made persistent via service/JVM args
- Bitbucket webhook ecosystem is in flux (plugin deprecation → likely move to native)
- Mac nodes require SSH key hygiene + managing developer toolchains (Ruby/Homebrew/etc.)
- Push troubleshooting into a shared DevOps channel to reduce repeated investigations


# Yazdan 1 - Part 1 - 13 October - Jenkins / Buildah / Registry Troubleshooting Lecture Recap

https://teams.microsoft.com/l/meetingrecap?driveId=b%21QAnRGb8Jl0eu3NiS-k9viCoNGt4Kod5IiNnbjtEHyRN8Ny48g_jDTL8PU4gpX_cK&driveItemId=01DRD7PCJBI5Y3EFATKNBZ22GDMJV5ABZR&sitePath=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2Fjenkins-20251013_143309-Meeting+Recording.mp4&fileUrl=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2Fjenkins-20251013_143309-Meeting+Recording.mp4%3Fweb%3D1&iCalUid=040000008200E00074C5B7101A82E008000000000BD75F0F173CDC01000000000000000010000000E7881A343E23FB45B53A6A34BB2F821E&threadId=19%3Ameeting_NjNkMDgxYTctNTk4Ny00OGFhLThjMDctMDNjODUyZmExYmU3%40thread.v2&organizerId=1d309b0d-e5cf-4c53-8298-7f4b3a3019ab&tenantId=2acba9fe-1f29-49de-a1ee-45b3b7aff8f5&callId=c5d6f731-3f0b-4fda-80cb-96e2c224086e&threadType=Meeting&meetingType=Scheduled&subType=RecapSharingLink_RecapCore

## 1. Context of the lecture

The transcript is messy because the meeting mostly turned into **live troubleshooting**.

The team is experimenting with a **new Bitbucket test environment**.

Current situation:

- There are **two Bitbucket instances**
  - **Bitbucket Test**
  - **Bitbucket Production**
- The **test instance currently has a bug**, so Roman allowed the team to **work temporarily in production**.
- The work is related to **FEG tools / OpenShift team repositories**.

Goal of the session:

> Try creating a repository and test building/pushing images through Jenkins.

---

## 2. What they were trying to achieve

The workflow they were testing:

```
Git repository (Bitbucket)
        ↓
Jenkins pipeline
        ↓
Build container image (Buildah)
        ↓
Login to container registry
        ↓
Push image to registry
```

This is a **standard CI/CD container workflow**.

---

## 3. Where the problem appeared

During pipeline execution Jenkins failed with an error:

```
could not find credential entry with ID registry
```

This was the **core issue of the whole session**.

---

## 4. Why this error happened

The pipeline expected **credentials stored inside Jenkins**.

Example:

```
Credentials ID: registry
```

Inside:

```
Jenkins → Manage Jenkins → Credentials
```

Example pipeline code:

```groovy
withCredentials([usernamePassword(
  credentialsId: 'registry',
  usernameVariable: 'REG_USER',
  passwordVariable: 'REG_PASS'
)])
```

But:

**That credential does not exist in Jenkins.**

So the pipeline fails.

---

## 5. Why Roman said they usually do it via a file

Roman mentioned:

> "We usually do it via file because there are multiple tokens."

This means using a **credential file instead of username/password variables**.

Example files:

```
~/.docker/config.json
```

or

```
auth.json
```

Typical Buildah / Podman authentication file:

```json
{
 "auths": {
   "registry.company.com": {
     "auth": "BASE64TOKEN"
   }
 }
}
```

Instead of storing credentials in pipeline variables, Jenkins simply **uses the authentication file**.

---

## 6. Why login worked in terminal but not in Jenkins

Roman said:

> it always works in terminal

That is common because your machine already has login stored.

Example:

```
buildah login registry
```

This creates:

```
~/.config/containers/auth.json
```

But Jenkins runs in a **clean environment**.

So:

```
Jenkins agent
   ↓
No auth.json
   ↓
Registry login fails
```

---

## 7. Roman's proposed solution

Roman suggested:

1. Download the authentication file
2. Put it into the repository
3. Reference the file in the Jenkins pipeline

Example structure:

```
repo/
  registry-auth.json
  Jenkinsfile
```

Pipeline usage:

```
buildah --authfile registry-auth.json push image
```

---

## 8. Why he ended the meeting

Roman realized fixing it live would require:

- downloading credentials
- creating authentication file
- updating pipeline
- testing Buildah
- pushing image

That could take **30–60 minutes**, so he said:

> I'll create a proof of concept and show it tomorrow.

This is a **normal engineering workflow**.

---

## 9. Important DevOps concepts from the lecture

### 1️⃣ Jenkins credentials system

Sensitive data should be stored in:

```
Jenkins
 → Manage Jenkins
 → Credentials
```

Not inside code.

---

### 2️⃣ Credential types used in Jenkins

Common credential types:

```
Username + password
Secret text
Secret file
SSH key
```

Roman suggested using **Secret File**.

---

### 3️⃣ Buildah authentication

Buildah reads credentials from:

```
auth.json
```

or

```
docker config.json
```

---

### 4️⃣ Pipeline dependency on credentials

If the pipeline contains:

```
credentialsId: 'registry'
```

Then Jenkins **must have a credential with exactly that ID**.

Otherwise the pipeline fails.

---

## 10. Example final working solution

Example pipeline:

```groovy
withCredentials([file(credentialsId: 'registry-auth', variable: 'AUTH_FILE')]) {

    sh '''
    buildah bud -t myimage .
    buildah push --authfile $AUTH_FILE myimage registry.company.com/myimage
    '''
}
```

Where Jenkins stores:

```
Credentials ID: registry-auth
Type: Secret File
File: auth.json
```

---

## 11. The real lesson from this session

The lecture was not really about Bitbucket.

It was about **authentication inside CI/CD pipelines**.

Key idea:

> Pipelines must authenticate to external systems (registry, git, cloud).

Authentication can be done using:

```
Jenkins credentials
Secret files
Tokens
Vault
```

---

## 12. The bigger CI/CD authentication chain

Understanding this flow helps debug most pipeline issues:

```
Jenkins
   ↓
Bitbucket
   ↓
Container Registry
   ↓
OpenShift
```

Once you understand this chain, **most Jenkins troubleshooting becomes much easier**.

# Yazdan 1 - Part 2 - 14 October - Git Repositories, Permissions, and Pushing Code

https://teams.microsoft.com/l/meetingrecap?driveId=b%21QAnRGb8Jl0eu3NiS-k9viCoNGt4Kod5IiNnbjtEHyRN8Ny48g_jDTL8PU4gpX_cK&driveItemId=01DRD7PCLUYS22XS7AR5DJTWDJBZHGNK2V&sitePath=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2Fjenkins-20251014_142959-Meeting+Recording.mp4&fileUrl=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2Fjenkins-20251014_142959-Meeting+Recording.mp4%3Fweb%3D1&iCalUid=040000008200E00074C5B7101A82E008000000000BD75F0F173CDC01000000000000000010000000E7881A343E23FB45B53A6A34BB2F821E&threadId=19%3Ameeting_NjNkMDgxYTctNTk4Ny00OGFhLThjMDctMDNjODUyZmExYmU3%40thread.v2&organizerId=1d309b0d-e5cf-4c53-8298-7f4b3a3019ab&tenantId=2acba9fe-1f29-49de-a1ee-45b3b7aff8f5&callId=7ede5ddf-eeb3-4930-b259-e9c244ef2052&threadType=Meeting&meetingType=Scheduled&subType=RecapSharingLink_RecapCore

## 1. Context of the session

During the meeting the participants discussed:

- repository access permissions
- how to create and push code to a Git repository
- the difference between **creating a repository first vs pushing an existing project**
- how **Git tracks files**
- typical workflow when starting a new project

The discussion was practical and focused on **common Git workflows used in CI/CD environments**.

---

## 2. Repository permissions

At the beginning of the meeting, the user reported that:

> the link to the repository was not working due to missing permissions.

Roman explained that access must be granted manually.

Typical permission workflow:

```
Repository
   → Settings
   → Permissions
   → Add user or group
```

Users must have permission such as:

```
Read
Write
Admin
```

Without proper permissions:

- the repository cannot be opened
- cloning the repository fails
- pushing code is impossible

---

## 3. Two ways to start working with a repository

Roman explained that there are **two different workflows when working with Git repositories**.

Understanding this is important.

---

## 4. Workflow 1 — Repository already exists (most common)

If the repository already exists on the server (Bitbucket / GitHub):

### Step 1 — Clone repository

```
git clone <repository-url>
```

Example:

```
git clone git@bitbucket.company.com:team/project.git
```

This command:

- downloads the repository
- creates a local working copy

---

### Step 2 — Create or modify code

You work inside the downloaded folder:

```
project/
   file1
   file2
   Jenkinsfile
```

---

### Step 3 — Track files

Git only tracks files that are added.

In VS Code, untracked files are shown as:

```
U = untracked
```

To track them:

```
git add .
```

---

### Step 4 — Commit changes

```
git commit -m "initial commit"
```

---

### Step 5 — Push changes to the repository

```
git push origin main
```

---

## 5. Workflow 2 — Code already exists locally

Sometimes you already have a project folder and want to push it to a new repository.

Example:

```
my-project/
   code files
```

In this case you must **initialize Git manually**.

---

### Step 1 — Initialize Git repository

```
git init
```

This creates:

```
.git/
```

which stores Git metadata.

---

### Step 2 — Add remote repository

```
git remote add origin <repository-url>
```

Example:

```
git remote add origin git@bitbucket.company.com:team/project.git
```

---

### Step 3 — Add files

```
git add .
```

---

### Step 4 — Commit files

```
git commit -m "initial commit"
```

---

### Step 5 — Push to remote repository

```
git push -u origin main
```

Now the existing project becomes a Git repository connected to the remote server.

---

## 6. Understanding "Untracked files"

In VS Code you often see:

```
U  filename
```

This means:

```
Untracked file
```

The file exists but Git is **not tracking it yet**.

To start tracking it:

```
git add filename
```

or

```
git add .
```

After that the file becomes part of the next commit.

---

## 7. Why this matters for pipelines

In CI/CD environments (like Jenkins):

- pipelines are stored in Git repositories
- Jenkins clones the repository automatically

Typical pipeline flow:

```
Developer pushes code
        ↓
Git repository (Bitbucket)
        ↓
Jenkins detects change
        ↓
Pipeline starts
        ↓
Build / Test / Deploy
```

If the repository is not correctly initialized or pushed:

- Jenkins cannot run the pipeline
- repository appears empty
- builds fail

---

## 8. Common beginner confusion explained

The conversation highlighted a common misunderstanding.

People often mix these two workflows:

```
Create repository first
        vs
Create code first
```

Correct approach depends on which already exists.

---

### Case 1 — Repository exists

Use:

```
git clone
```

---

### Case 2 — Code exists locally

Use:

```
git init
git remote add
git push
```

---

## 9. Key takeaway from the lecture

Understanding **the difference between these two workflows** is essential for working with Git in DevOps environments.

Key idea:

```
Repository exists → clone it

Code exists locally → initialize Git and push it
```

---

## 10. Typical DevOps repository lifecycle

```
Developer creates repository
        ↓
Developers clone repository
        ↓
Code changes committed
        ↓
Changes pushed
        ↓
CI/CD pipeline triggered
```

This is the foundation of modern DevOps workflows.

---

## Summary

The session explained:

- how repository permissions work
- how to clone repositories
- how to push existing code to a repository
- what "untracked files" mean
- how Git tracking works
- why proper repository setup is necessary for CI/CD pipelines

# Yazdan 3 - OpenShift Access, Cluster Management, Certificates (on mac - to access company sites), and Role Context

https://teams.microsoft.com/l/meetingrecap?driveId=b%21QAnRGb8Jl0eu3NiS-k9viCoNGt4Kod5IiNnbjtEHyRN8Ny48g_jDTL8PU4gpX_cK&driveItemId=01DRD7PCLW7FIUEW7RDVEJVUTYXPZ44XGG&sitePath=https%3A%2F%2Fefortuna-my.sharepoint.com%2F%3Av%3A%2Fg%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FEXb5UUJb8R1ImtJ4u_POXMYBTpIchopIQ3yImyTOso_JeQ&fileUrl=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2FJenkins-20251020_153352-Meeting+Recording.mp4%3Fweb%3D1&iCalUid=040000008200E00074C5B7101A82E00800000000F10C30ADAF41DC010000000000000000100000006675B2AF77F564438C13543F785EE052&threadId=19%3Ameeting_NjM2ZjdmMmEtNTIwNC00MTJkLThlYzItYzA2MjdiNzIxYTcy%40thread.v2&organizerId=1d309b0d-e5cf-4c53-8298-7f4b3a3019ab&tenantId=2acba9fe-1f29-49de-a1ee-45b3b7aff8f5&callId=407238ed-f3cc-4354-a484-96bc4134c85f&threadType=Meeting&meetingType=Scheduled&subType=RecapSharingLink_RecapCore


## 1. Context of the session

In this meeting the discussion focused on:

- access to OpenShift clusters
- authentication issues
- cluster management architecture
- internal certificates used inside the company infrastructure
- understanding what the DevOps role will involve
- why learning Java may be useful in this environment

The conversation also clarified **how the infrastructure is organized internally**.

---

## 2. Access to OpenShift clusters

During the training the participant tried to access a **Red Hat OpenShift cluster** but could not log in.

Key points discussed:

- The provided admin password did not work.
- The credentials used for another system were different.
- Access to clusters is controlled centrally.

This is normal in enterprise environments.

Users typically authenticate using:

```
Corporate Active Directory (AD)
```

or company identity providers.

---

## 3. Multiple cluster management

Roman explained that the company does not manage clusters individually.

Instead they use a centralized management system:

```
Advanced Cluster Management (ACM)
```

Architecture example:

```
Advanced Cluster Management
        │
        ├── Cluster A
        ├── Cluster B
        ├── Cluster C
```

This allows administrators to:

- manage multiple Kubernetes/OpenShift clusters
- apply policies
- monitor cluster health
- manage deployments

from **a single interface**.

Because of this:

> Users may not have access to all clusters.

Permissions are usually **restricted to specific environments**.

---

## 4. Understanding the role responsibilities

During the meeting, Roman explained what tasks the participant will mostly work on.

The work includes:

- managing automation
- maintaining CI/CD pipelines
- working with Jenkins
- supporting automation logic written in Java

Important clarification:

> Jenkins itself does not contain the business logic.

Instead:

```
Java application
        ↓
Automation logic
        ↓
Jenkins triggers jobs
```

Meaning Jenkins is mainly used as an **automation orchestrator**.

---

## 5. Why Java knowledge is useful

Roman explained that learning Java is helpful because:

- Jenkins itself is written in Java
- many internal systems are written in Java
- automation scripts and tools may be Java-based

Example architecture:

```
Java application
       ↓
Automation logic
       ↓
Jenkins job
       ↓
Infrastructure tasks
```

So Java is not required for Jenkins administration, but it helps understand **automation logic behind the pipelines**.

---

## 6. Internal certificate issues

During the session the participant saw a browser warning:

```
Not Secure
```

This happens because internal services use **company certificates**.

To fix this, Roman explained that the user must install the **company CA certificate bundle**.

Example internal location:

```
lonador.svc.fortuna.cz
```

From there users can download:

```
fortuna CA bundle
```

---

## 7. Why certificates must be installed

Without installing the company certificate authority:

- browsers cannot trust internal HTTPS services
- security warnings appear
- authentication may fail

Typical fix:

```
Download CA bundle
Import into system certificate store
Import into browser (if needed)
```

---

## 8. Certificate bundle problem

Roman mentioned that the certificate bundle contains **multiple certificates in one file**.

Example:

```
CA bundle
   ├── certificate 1
   ├── certificate 2
   ├── certificate 3
   └── ...
```

Some browsers cannot correctly load all certificates from a single bundle.

To solve this, the bundle can be **split into individual certificates**.

Example command used on Linux:

```
awk command to split certificate bundle
```

Result:

```
cert1.pem
cert2.pem
cert3.pem
...
```

Each certificate becomes a separate file.

---

## 9. Differences between systems

Roman mentioned that behavior may differ depending on the system.

### Linux

Often requires splitting certificates.

### Mac

Sometimes the whole bundle can be imported directly.

### Browsers

Different browsers handle certificate bundles differently:

```
Chrome
Firefox
Safari
```

Safari sometimes handles bundles better on macOS.

---

## 10. Browser authentication confusion

Another issue occurred because the link opened in the **personal Chrome profile** instead of the corporate one.

This caused:

```
authentication prompts
```

Enterprise environments often rely on:

```
Single Sign-On (SSO)
```

If the wrong browser profile is used, authentication may fail.

---

## 11. Typical enterprise infrastructure workflow

The conversation revealed the following infrastructure structure:

```
Developers
      ↓
Git repositories
      ↓
Jenkins pipelines
      ↓
Automation logic (Java)
      ↓
Infrastructure tasks
      ↓
OpenShift clusters
```

In this architecture:

- Jenkins orchestrates automation
- Java code contains business logic
- OpenShift runs the workloads

---

## 12. Next steps mentioned in the session

The participant planned to:

- finish the remaining Jenkins training modules
- continue learning automation infrastructure
- schedule another meeting later to discuss questions

---

## Summary

This session clarified several important concepts:

- how OpenShift clusters are centrally managed
- why access permissions differ between clusters
- how internal company certificates must be installed
- why certificate bundles sometimes need splitting
- how Jenkins fits into the automation architecture
- why Java knowledge is useful in this environment

The main takeaway is that Jenkins is only one component of a larger automation system involving:

```
Git
Jenkins
Java automation logic
OpenShift clusters
Enterprise authentication systems
```

# Yazdan 4 - Part 1 - ripgrep, Searching Code, and Handling Large Jenkins Pipelines

https://teams.microsoft.com/l/meetingrecap?driveId=b%21QAnRGb8Jl0eu3NiS-k9viCoNGt4Kod5IiNnbjtEHyRN8Ny48g_jDTL8PU4gpX_cK&driveItemId=01DRD7PCPHKIWVQ6UPNZCJDZZGS5UTCRNN&sitePath=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2FJenkins+4-20251024_150118-Meeting+Recording.mp4&fileUrl=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2FJenkins+4-20251024_150118-Meeting+Recording.mp4%3Fweb%3D1&iCalUid=040000008200E00074C5B7101A82E0080000000084D759D3BC44DC010000000000000000100000004CEE12967787CB4987378066C7C6E2EB&threadId=19%3Ameeting_NzlkZjEwZmMtNTNhMS00YzU4LTlkZDktZjI2MTgwMTRlOWY3%40thread.v2&organizerId=1d309b0d-e5cf-4c53-8298-7f4b3a3019ab&tenantId=2acba9fe-1f29-49de-a1ee-45b3b7aff8f5&callId=b0952702-bc89-4f36-a9e4-8077e3402de9&threadType=Meeting&meetingType=Scheduled&subType=RecapSharingLink_RecapCore


## 1. Context of the session

During this meeting the discussion focused on:

- searching code efficiently from the command line
- the tool **ripgrep**
- differences between `grep` and `ripgrep`
- issues with **large Jenkins pipelines**
- how to restructure pipelines using **Groovy scripts and folders**

The conversation mainly addressed **developer productivity and Jenkins pipeline structure**.

---

## 2. Searching code efficiently

Roman introduced a command-line tool called:

```
ripgrep
```

Command name:

```
rg
```

ripgrep is a modern alternative to the classic Linux command:

```
grep
```

It is commonly used by developers to **search through large codebases quickly**.

---

## 3. Why ripgrep is preferred over grep

ripgrep provides better default behavior compared to grep.

### grep example

To search recursively with grep you must specify flags:

```
grep -r "text" .
```

To ignore case:

```
grep -ri "text" .
```

Searching hidden files often requires additional configuration.

---

### ripgrep default behavior

ripgrep already includes many useful defaults:

```
rg "text"
```

By default it:

- searches **recursively through directories**
- is **very fast**
- respects `.gitignore`
- provides better output formatting

---

## 4. Useful ripgrep options

Roman mentioned using ripgrep with options such as:

### ignore letter case

```
rg -i "text"
```

### search hidden files

```
rg --hidden "text"
```

### search everything

```
rg --hidden -i "text"
```

Many developers create an alias:

```
alias rg="rg --hidden -i"
```

This makes searching large projects easier.

---

## 5. Why recursive search matters

When working in large projects (for example Jenkins infrastructure repositories), code is spread across many folders.

Example:

```
repo/
   Jenkinsfile
   pipeline/
       build.groovy
       deploy.groovy
   shared-library/
       utils.groovy
```

Recursive search allows you to quickly find:

- pipeline steps
- configuration values
- function usage
- variables
- credentials references

Example search:

```
rg "credentialsId"
```

This helps identify where credentials are used in pipelines.

---

## 6. Discussion about large Jenkins pipelines

The conversation also touched on issues with **large pipeline definitions**.

In Jenkins, pipelines can become very large and complex.

Example problem:

```
Method code too large
```

This happens when:

- a Groovy method becomes too large
- too much code is inside one pipeline script

This limitation comes from **JVM bytecode size limits**.

---

## 7. Typical cause of this Jenkins error

Example problematic pipeline:

```
Jenkinsfile
   huge script
   many stages
   many functions
```

If the pipeline becomes too large, Jenkins may fail during compilation.

Common error:

```
Method code too large
```

---

## 8. Solution: split pipelines into smaller components

Roman explained a common solution.

Instead of one huge Jenkinsfile:

```
Jenkinsfile
```

Split the logic into separate Groovy scripts.

Example structure:

```
repo/
   Jenkinsfile
   vars/
       build.groovy
       deploy.groovy
   pipeline/
       helpers.groovy
```

This reduces the size of the main pipeline.

---

## 9. Using Groovy scripts in pipelines

Groovy scripts allow pipelines to reuse logic.

Example Jenkinsfile:

```groovy
pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                buildProject()
            }
        }
    }
}
```

Where `buildProject()` is implemented in:

```
vars/buildProject.groovy
```

This approach:

- improves readability
- avoids large pipeline methods
- enables code reuse

---

## 10. Why shared libraries are important

Large organizations often use **Jenkins Shared Libraries**.

Structure example:

```
shared-library/
   vars/
       build.groovy
       deploy.groovy
   src/
       com/company/utils.groovy
```

Benefits:

- pipelines become shorter
- common logic is centralized
- easier maintenance

---

## 11. Why this matters in real DevOps environments

In enterprise CI/CD environments:

- pipelines control complex infrastructure
- pipelines can become thousands of lines long

Without modular design:

```
pipeline complexity ↑
maintenance difficulty ↑
error risk ↑
```

Splitting logic into Groovy scripts improves:

- maintainability
- readability
- scalability

---

## 12. Typical DevOps workflow when debugging pipelines

Tools like `ripgrep` help developers navigate large pipeline repositories.

Example workflow:

```
Developer investigates pipeline issue
        ↓
Search repository using ripgrep
        ↓
Locate relevant Groovy script
        ↓
Fix pipeline logic
```

---

## Summary

This session explained:

- how to search code efficiently using **ripgrep**
- why ripgrep is better than traditional grep
- how recursive search helps navigate large repositories
- why Jenkins pipelines may fail when methods become too large
- how to fix this by splitting pipeline logic into Groovy scripts
- why shared libraries are commonly used in enterprise CI/CD systems

# Yazdan 4 - Part 2 - Jenkins Shared Libraries and the `vars` Folder

https://teams.microsoft.com/l/meetingrecap?driveId=b%21QAnRGb8Jl0eu3NiS-k9viCoNGt4Kod5IiNnbjtEHyRN8Ny48g_jDTL8PU4gpX_cK&driveItemId=01DRD7PCM4FKON7LO3KRGYRIPRDPZKEHYK&sitePath=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2FJenkins+4-20251024_151855-Meeting+Recording.mp4&fileUrl=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2FJenkins+4-20251024_151855-Meeting+Recording.mp4%3Fweb%3D1&iCalUid=040000008200E00074C5B7101A82E0080000000084D759D3BC44DC010000000000000000100000004CEE12967787CB4987378066C7C6E2EB&threadId=19%3Ameeting_NzlkZjEwZmMtNTNhMS00YzU4LTlkZDktZjI2MTgwMTRlOWY3%40thread.v2&organizerId=1d309b0d-e5cf-4c53-8298-7f4b3a3019ab&tenantId=2acba9fe-1f29-49de-a1ee-45b3b7aff8f5&callId=917573ef-cb65-4c0d-8309-8073a797b563&threadType=Meeting&meetingType=Scheduled&subType=RecapSharingLink_RecapCore


## 1. Context of the session

In this session the discussion focused on:

- Jenkins **Shared Libraries**
- how pipelines can be wrapped inside functions
- how Jenkins loads **Groovy scripts from the `vars` folder**
- how shared libraries allow pipelines to be reused across projects

The goal was to explain **how to structure Jenkins pipelines using shared libraries instead of large Jenkinsfiles**.

---

## 2. What Jenkins Shared Libraries are

A **Jenkins Shared Library** is a repository that contains reusable pipeline code.

Instead of repeating the same pipeline logic in multiple repositories, the logic can be centralized.

Example problem:

```
repo1/Jenkinsfile
repo2/Jenkinsfile
repo3/Jenkinsfile
```

All pipelines contain similar code.

Solution:

```
shared-library/
```

The shared library contains reusable pipeline logic.

---

## 3. Typical Shared Library structure

A Jenkins Shared Library usually follows this structure:

```
shared-library/
│
├── vars/
│     build.groovy
│     deploy.groovy
│
├── src/
│     com/company/utils.groovy
│
└── resources/
```

Important folder:

```
vars/
```

This folder contains **pipeline functions that Jenkins loads automatically**.

---

## 4. How Jenkins loads shared libraries

When a repository is configured as a shared library in Jenkins:

```
Manage Jenkins
    → Configure System
    → Global Pipeline Libraries
```

Jenkins loads all scripts inside the:

```
vars/
```

folder.

Each script becomes available as a **pipeline step**.

Example:

```
vars/build.groovy
```

becomes usable in pipelines as:

```
build()
```

---

## 5. Wrapping pipelines inside functions

Roman explained that a pipeline can be wrapped inside a function.

Example structure:

```
vars/devcall.groovy
```

Example content:

```groovy
def call() {

    pipeline {
        agent any

        stages {
            stage('Build') {
                steps {
                    echo "Building application"
                }
            }
        }
    }

}
```

The important part:

```
def call()
```

This allows the script to be executed like a **pipeline step**.

---

## 6. Calling the shared pipeline

Once the shared library is configured in Jenkins, the pipeline can be called simply.

Example Jenkinsfile:

```groovy
@Library('shared-library') _

devcall()
```

Here:

```
devcall()
```

calls the pipeline defined in:

```
vars/devcall.groovy
```

This means the Jenkinsfile itself stays **very small**.

---

## 7. Why this approach is useful

Without shared libraries:

```
Jenkinsfile
  500+ lines
```

Problems:

- difficult to maintain
- repeated logic
- hard to reuse

With shared libraries:

```
Jenkinsfile
   5–10 lines
```

Most logic lives inside the shared library.

Benefits:

- reuse across projects
- centralized updates
- cleaner pipeline files
- easier maintenance

---

## 8. Example architecture using shared libraries

Typical CI/CD architecture:

```
Application repository
      │
      └── Jenkinsfile (small)

            ↓

Jenkins Shared Library
      │
      ├── vars/build.groovy
      ├── vars/deploy.groovy
      └── src/utils.groovy
```

Pipeline flow:

```
Developer pushes code
        ↓
Jenkins detects change
        ↓
Jenkinsfile loads shared library
        ↓
Shared pipeline logic runs
```

---

## 9. What Jenkins automatically exposes

Everything inside the `vars` folder becomes **a pipeline function**.

Example:

```
vars/build.groovy
vars/deploy.groovy
vars/test.groovy
```

Available commands:

```
build()
deploy()
test()
```

This is why the `vars` folder is special.

---

## 10. Difference between `vars` and `src`

Two main folders in shared libraries:

### vars

Contains **pipeline steps**.

Example:

```
vars/build.groovy
```

Used as:

```
build()
```

---

### src

Contains **regular Groovy classes**.

Example:

```
src/com/company/utils.groovy
```

Used as:

```
import com.company.Utils
```

---

## 11. Typical enterprise Jenkins setup

In large companies:

```
Many repositories
        ↓
Single shared Jenkins library
```

Example:

```
app1
app2
app3
app4
```

All use:

```
shared-ci-library
```

This keeps CI/CD logic consistent.

---

## 12. Problem encountered during the session

During the explanation, screen sharing stopped working.

Possible causes mentioned:

- computer performance issues
- high screen resolution (4K)
- Teams screen sharing limitations

The discussion stopped before further demonstration.

---

## Summary

This session explained:

- what Jenkins Shared Libraries are
- how pipelines can be wrapped inside functions
- how Jenkins loads scripts from the `vars` folder
- how shared libraries allow pipelines to be reused
- why large Jenkinsfiles should be avoided
- how enterprise CI/CD systems centralize pipeline logic

The key concept is that **Jenkins pipelines should be modular and reusable**, using shared libraries instead of large standalone pipeline files.

# Yazdan 4 - Part 3 - Testing Jenkins Shared Pipeline, Linux Navigation, and File Permissions

https://teams.microsoft.com/l/meetingrecap?driveId=b%21QAnRGb8Jl0eu3NiS-k9viCoNGt4Kod5IiNnbjtEHyRN8Ny48g_jDTL8PU4gpX_cK&driveItemId=01DRD7PCJIUR6MXOZELVEIFSE27VD3LQHN&sitePath=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2FJenkins+4-20251024_152641-Meeting+Recording.mp4&fileUrl=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2FJenkins+4-20251024_152641-Meeting+Recording.mp4%3Fweb%3D1&iCalUid=040000008200E00074C5B7101A82E0080000000084D759D3BC44DC010000000000000000100000004CEE12967787CB4987378066C7C6E2EB&threadId=19%3Ameeting_NzlkZjEwZmMtNTNhMS00YzU4LTlkZDktZjI2MTgwMTRlOWY3%40thread.v2&organizerId=1d309b0d-e5cf-4c53-8298-7f4b3a3019ab&tenantId=2acba9fe-1f29-49de-a1ee-45b3b7aff8f5&callId=fccafe4f-a57b-4b02-971b-f1c5fc477c74&threadType=Meeting&meetingType=Scheduled&subType=RecapSharingLink_RecapCore


## 1. Context of the session

During this session the discussion focused on:

- testing a Jenkins shared pipeline
- how Jenkins calls Groovy scripts from the `vars` folder
- pushing pipeline changes to a repository
- basic Linux navigation in the terminal
- fixing file permission issues on macOS

The goal was to **verify that a shared pipeline stored in a repository can be executed by Jenkins**.

---

## 2. Testing a pipeline stored in a shared library

Roman explained that the pipeline being tested should:

```
call a Groovy script stored in the vars folder
```

Example shared library structure:

```
shared-library/
│
├── vars/
│      pipeline.groovy
│
└── src/
```

The Jenkins job then calls the pipeline from the shared library.

Example Jenkinsfile:

```groovy
@Library('shared-library') _
pipeline()
```

This runs the pipeline defined inside:

```
vars/pipeline.groovy
```

---

## 3. Creating a simple test pipeline

Roman mentioned that the test pipeline **just sleeps**.

This is useful for debugging.

Example pipeline:

```groovy
def call() {
    pipeline {
        agent any

        stages {
            stage('Test') {
                steps {
                    sleep 30
                }
            }
        }
    }
}
```

Purpose:

- verify Jenkins loads the shared library
- verify the agent runs correctly
- allow time to troubleshoot

---

## 4. Testing with a specific Jenkins agent

Roman mentioned an **Appium agent** used for testing.

Example agent usage:

```
agent { label 'appium' }
```

Agents represent machines where Jenkins executes jobs.

Example Jenkins infrastructure:

```
Jenkins Controller
        │
        ├── Linux agent
        ├── Mac agent
        └── Appium agent
```

Each agent can run specific types of builds.

---

## 5. Updating the repository

After modifying the pipeline, the changes must be pushed to the repository.

Typical workflow:

```
Edit pipeline
       ↓
Commit changes
       ↓
Push to Git repository
       ↓
Jenkins loads new pipeline version
```

This allows Jenkins to execute the updated pipeline.

---

## 6. Copying files using Linux commands

During the session the user copied a file using the `cp` command.

Example command:

```
cp source destination
```

Example:

```
cp oc /target/directory/
```

Important detail:

```
destination must end with /
```

Otherwise the command may fail.

---

## 7. Navigating directories in the terminal

Roman noticed the user was located at the **filesystem root directory**.

Example:

```
/
```

This is the top-level directory in Unix systems.

Typical directory structure:

```
/
├── bin
├── etc
├── usr
├── home
└── var
```

The user's files normally live in:

```
/home/username
```

or on macOS:

```
/Users/username
```

---

## 8. Using the `cd` command

To move between directories:

```
cd directory
```

Example:

```
cd Downloads
```

To go to the home directory:

```
cd ~
```

Roman noticed the user was still at:

```
/
```

instead of the expected home directory.

---

## 9. Permission issues on macOS

When copying the file, the user received:

```
Permission denied
```

This means the current user **does not have permission to modify the target directory**.

Common solution:

```
sudo command
```

Example:

```
sudo cp oc /target/directory/
```

`sudo` temporarily runs the command with **administrator privileges**.

---

## 10. Differences between Linux and macOS permissions

Roman mentioned he was unsure about the exact macOS behavior.

However the underlying model is similar.

Unix permission system:

```
Owner
Group
Others
```

Each permission can include:

```
read
write
execute
```

Example:

```
rwxr-xr-x
```

---

## 11. Typical troubleshooting workflow

During the session the debugging process looked like this:

```
Pipeline created
       ↓
Push pipeline to repository
       ↓
Run Jenkins job
       ↓
Observe behavior
       ↓
Fix environment or permissions
```

Using simple pipelines (like a sleep stage) makes troubleshooting easier.

---

## 12. Skills mentioned as important for the role

At the end of the meeting the participant summarized the key learning areas:

```
Java
Linux commands
Practice with infrastructure
```

These are essential for DevOps work.

Roman confirmed that continuing to practice these areas is the right direction.

---

## Summary

This session covered:

- testing Jenkins pipelines stored in shared libraries
- calling Groovy scripts from the `vars` folder
- verifying pipelines using simple sleep stages
- basic Linux terminal navigation
- file copying with `cp`
- resolving permission issues with `sudo`
- understanding filesystem locations on Unix systems

The main takeaway is that **successful CI/CD work requires both pipeline knowledge and strong command-line skills**.

# Yazdan 5 - Android Builds, Jenkins Agent Resources, and Enterprise Access Accounts

https://teams.microsoft.com/l/meetingrecap?driveId=b%21QAnRGb8Jl0eu3NiS-k9viCoNGt4Kod5IiNnbjtEHyRN8Ny48g_jDTL8PU4gpX_cK&driveItemId=01DRD7PCNK4HOY4ME5CVAKUMMOAUPLJBLH&sitePath=https%3A%2F%2Fefortuna-my.sharepoint.com%2F%3Av%3A%2Fg%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FEarh3Y4wnRVAqjGOBR60hWcBM4gaFNLF51YO6h-fQlhmNw&fileUrl=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2FJenkins+5-20251103_153054-Meeting+Recording.mp4%3Fweb%3D1&iCalUid=040000008200E00074C5B7101A82E00800000000A845AF67954CDC01000000000000000010000000C584C88FC7C39B47A0321A0B0E06EEAF&threadId=19%3Ameeting_MjJiNmIwMjgtNDY1YS00MDljLThlYTQtM2YzYTNhYTU4MmYw%40thread.v2&organizerId=1d309b0d-e5cf-4c53-8298-7f4b3a3019ab&tenantId=2acba9fe-1f29-49de-a1ee-45b3b7aff8f5&callId=66343af0-f8c6-4300-8484-f6d2522c06d9&threadType=Meeting&meetingType=Scheduled&subType=RecapSharingLink_RecapCore


## 1. Context of the session

In this session the discussion focused on:

- Android builds using Java 21
- Jenkins agent resource allocation
- memory issues during builds
- enterprise identity management (IDM)
- how to obtain system access accounts
- contacting internal user support

The conversation mainly addressed **build failures caused by insufficient system resources and required access to internal systems**.

---

## 2. Android builds failing due to memory limits

Roman mentioned that **Android builds using Java 21 were failing**.

The cause was:

```
Insufficient RAM on the build agent
```

Originally the build environment had:

```
16 GB RAM
```

This was not enough for the build process.

---

## 3. Increasing build agent resources

To resolve the problem, the memory allocation was increased.

Old configuration:

```
16 GB RAM
```

New configuration:

```
20 GB RAM
```

Increasing available RAM allowed the Android builds to run successfully.

---

## 4. Why Android builds require large memory

Android builds are resource intensive because they involve:

```
Java compilation
Gradle builds
Dependency resolution
Code generation
Packaging APK / AAB files
```

Typical Android build process:

```
Source code
      ↓
Gradle build system
      ↓
Java/Kotlin compilation
      ↓
Resource processing
      ↓
APK packaging
```

Large projects can easily consume **multiple gigabytes of memory**.

---

## 5. Jenkins agent resource configuration

Build resources are usually configured on the **Jenkins agent machine**.

Example Jenkins architecture:

```
Jenkins Controller
        │
        ├── Android build agent
        ├── Linux build agent
        └── Mac build agent
```

The Android agent runs Android builds and requires:

```
High CPU
High RAM
Large disk space
```

If resources are insufficient, builds may fail.

---

## 6. Infrastructure environment (VMware)

Roman mentioned that the build environment is hosted on:

```
VMware
```

This means Jenkins agents are likely running on **virtual machines**.

Typical virtualization setup:

```
Physical server
      ↓
VMware hypervisor
      ↓
Virtual machines
      ↓
Jenkins agents
```

Resource changes such as RAM increases are performed at the **virtual machine level**.

---

## 7. Enterprise identity management (IDM)

The conversation also discussed **IDM (Identity Management)**.

IDM systems manage:

```
User accounts
Roles
Permissions
Access to systems
```

In enterprise environments, users often have multiple roles assigned.

Example:

```
User account
     ↓
Roles
     ├── Developer
     ├── DevOps
     └── Infrastructure access
```

These roles determine which systems the user can access.

---

## 8. Special system accounts

Roman mentioned that a **special account** must be created.

Example naming format:

```
_a_chaos
```

These accounts are typically:

```
Service accounts
Infrastructure accounts
System login accounts
```

They are separate from normal user accounts.

---

## 9. Accessing internal systems

To access some internal infrastructure, users must obtain these special accounts.

Example systems requiring such accounts:

```
Infrastructure servers
Build environments
Internal tools
Automation systems
```

Without this account, login attempts will fail.

---

## 10. Contacting user support

Roman recommended contacting **user support** to obtain the required account.

User support responsibilities typically include:

```
Creating accounts
Assigning roles
Resetting passwords
Granting system access
```

The process usually involves submitting a request.

---

## 11. Password management for service accounts

Roman mentioned that password changes previously required assistance from user support.

Later he discovered that password changes could be done through:

```
VPN access
```

This indicates the company likely uses an internal password management system accessible only from inside the corporate network.

---

## 12. Typical enterprise access flow

Enterprise system access usually follows this process:

```
User requests access
        ↓
User support creates account
        ↓
Roles assigned in IDM
        ↓
User receives credentials
        ↓
Access to infrastructure systems
```

This ensures proper security and auditing.

---

## 13. DevOps perspective

For DevOps engineers, access to infrastructure systems is essential.

Typical systems include:

```
Jenkins
Git repositories
OpenShift clusters
Virtual machines
Monitoring systems
```

Each system may require separate credentials or roles.

---

## Summary

This session covered:

- Android build failures caused by insufficient memory
- increasing Jenkins agent RAM from 16 GB to 20 GB
- how resource allocation affects build reliability
- infrastructure hosted on VMware virtual machines
- enterprise identity management (IDM)
- creation of special infrastructure accounts
- contacting user support to obtain required access

The main takeaway is that **successful CI/CD operations depend not only on pipeline configuration but also on sufficient system resources and proper access to enterprise infrastructure systems**.

# Yazdan 6 - Vault Access, Jenkins Webhooks, and Bitbucket Integration

https://teams.microsoft.com/l/meetingrecap?driveId=b%21QAnRGb8Jl0eu3NiS-k9viCoNGt4Kod5IiNnbjtEHyRN8Ny48g_jDTL8PU4gpX_cK&driveItemId=01DRD7PCOACST62COL3NCYYQ36FF4OMC4S&sitePath=https%3A%2F%2Fefortuna-my.sharepoint.com%2F%3Av%3A%2Fg%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FEcAUp-0Jy9tFjEN-KXjmC5IB6fYg7r1xJ2WDuhRZLJXa-g&fileUrl=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2FJenkins+6-20251110_140103-Meeting+Recording.mp4%3Fweb%3D1&iCalUid=040000008200E00074C5B7101A82E00800000000180B36A52252DC01000000000000000010000000EFED90453B4C354F91AA478E2001CE1B&threadId=19%3Ameeting_MmJkNTFkMzMtMzhkMC00YTVmLTgxOTUtYzM0MjUwZDczNmVj%40thread.v2&organizerId=1d309b0d-e5cf-4c53-8298-7f4b3a3019ab&tenantId=2acba9fe-1f29-49de-a1ee-45b3b7aff8f5&callId=885fa7cf-d273-4a96-b437-3886c2aa62b7&threadType=Meeting&meetingType=Scheduled&subType=RecapSharingLink_RecapCore


## 1. Context of the session

During this session the discussion focused on:

- how to obtain access to **Vault**
- Jenkins documentation and issue tracking
- how **Bitbucket webhooks trigger Jenkins builds**
- how Jenkins automatically creates webhooks
- practicing webhook configuration

The goal of the session was to better understand **how Jenkins detects repository changes and automatically triggers builds**.

---

## 2. Access to Vault

At the beginning of the meeting the participant asked about access to:

```
Vault
```

Vault is typically used in DevOps environments for managing:

```
Secrets
Credentials
API tokens
Passwords
Certificates
```

Access to Vault usually requires:

```
Role assignment
User approval
Access request through internal process
```

This is because Vault contains sensitive infrastructure data.

---

## 3. Jenkins issue tracker and documentation

The participant encountered a Jenkins issue referenced through a link.

The issue appeared to be from:

```
Jenkins public issue tracker
```

Roman clarified that:

```
The issue is public
```

Meaning it is accessible on the internet and **not part of the company’s internal infrastructure**.

Example Jenkins issue tracker:

```
https://issues.jenkins.io
```

These issues describe known Jenkins bugs or limitations.

---

## 4. Understanding Jenkins webhooks

A large part of the session discussed **webhooks between Bitbucket and Jenkins**.

A webhook allows Bitbucket to notify Jenkins when something changes.

Example workflow:

```
Developer pushes code
        ↓
Bitbucket detects push
        ↓
Bitbucket sends webhook event
        ↓
Jenkins receives event
        ↓
Jenkins triggers build
```

This allows builds to start automatically after code changes.

---

## 5. Webhook configuration confusion

The participant noticed that in older tutorials webhooks were created manually.

Example manual setup:

```
Bitbucket
   → Repository settings
   → Webhooks
   → Add webhook
```

However, modern Jenkins integrations can **create these webhooks automatically**.

---

## 6. Jenkins Bitbucket plugin behavior

Roman demonstrated that when the Jenkins job is configured correctly, the **Bitbucket plugin automatically creates the webhook**.

Configuration example inside Jenkins job:

```
Build when a change is pushed to Bitbucket
```

When this option is enabled:

```
Jenkins
   ↓
Registers webhook in Bitbucket
```

This removes the need to create webhooks manually.

---

## 7. Demonstration of webhook creation

Roman demonstrated the following process:

1. Delete the webhook in Bitbucket.
2. Configure Jenkins job triggers.
3. Save Jenkins configuration.

After saving, Jenkins automatically recreated the webhook.

This confirmed that the webhook is managed by Jenkins.

---

## 8. Jenkins job trigger configuration

The relevant Jenkins configuration section:

```
Job
   → Configure
   → Build Triggers
```

Important trigger option:

```
Build when a change is pushed to Bitbucket
```

When enabled, Jenkins listens for webhook events.

---

## 9. Verifying webhook creation

After saving the Jenkins configuration, the Bitbucket repository showed a new webhook entry.

Example location:

```
Bitbucket
   → Repository settings
   → Webhooks
```

The webhook endpoint typically points to:

```
Jenkins webhook URL
```

This confirms Jenkins successfully registered the webhook.

---

## 10. Testing the webhook

To test the webhook functionality:

```
Push change to repository
```

Example:

```
git commit
git push
```

Expected behavior:

```
Bitbucket sends webhook
        ↓
Jenkins receives event
        ↓
Jenkins job triggers automatically
```

If the build starts, the webhook is working correctly.

---

## 11. Practicing webhook configuration

The participant asked whether it is safe to delete the webhook and recreate it for practice.

Roman confirmed that this is acceptable.

Practice workflow:

```
Delete webhook
        ↓
Enable Jenkins trigger again
        ↓
Save configuration
        ↓
Webhook recreated automatically
```

This is a safe way to understand how Jenkins integrations work.

---

## 12. Repository access limitations

During the session the participant noticed they did not have access to some repositories.

Example repository location mentioned:

```
FEG tools
```

Access to repositories depends on:

```
User permissions
Project roles
Repository visibility
```

Without proper permissions, repositories will not appear in the interface.

---

## 13. Typical CI/CD workflow with Bitbucket and Jenkins

The integration works as follows:

```
Developer pushes code
        ↓
Bitbucket repository updates
        ↓
Bitbucket webhook triggers
        ↓
Jenkins job starts
        ↓
Pipeline executes
```

This is a fundamental mechanism in modern CI/CD systems.

---

## Summary

This session explained:

- how Vault access must be requested
- how Jenkins issues are tracked publicly
- how Bitbucket webhooks trigger Jenkins builds
- how Jenkins automatically creates webhooks via plugins
- how to configure Jenkins job triggers
- how to verify webhook functionality
- how to practice webhook setup safely

The key takeaway is that **Jenkins integrations often automate webhook creation**, simplifying CI/CD setup between repositories and build pipelines.

# Yazdan 7 - Git Branches, Pull Requests, Jenkins Builds, and Disk Usage Investigation

https://teams.microsoft.com/l/meetingrecap?driveId=b%21QAnRGb8Jl0eu3NiS-k9viCoNGt4Kod5IiNnbjtEHyRN8Ny48g_jDTL8PU4gpX_cK&driveItemId=01DRD7PCOACST62COL3NCYYQ36FF4OMC4S&sitePath=https%3A%2F%2Fefortuna-my.sharepoint.com%2F%3Av%3A%2Fg%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FEcAUp-0Jy9tFjEN-KXjmC5IB6fYg7r1xJ2WDuhRZLJXa-g&fileUrl=https%3A%2F%2Fefortuna-my.sharepoint.com%2Fpersonal%2Fkhezripourgharaee_hossein_feg_eu%2FDocuments%2FNahr%C3%A1vky%2FJenkins+6-20251110_140103-Meeting+Recording.mp4%3Fweb%3D1&iCalUid=040000008200E00074C5B7101A82E00800000000180B36A52252DC01000000000000000010000000EFED90453B4C354F91AA478E2001CE1B&threadId=19%3Ameeting_MmJkNTFkMzMtMzhkMC00YTVmLTgxOTUtYzM0MjUwZDczNmVj%40thread.v2&organizerId=1d309b0d-e5cf-4c53-8298-7f4b3a3019ab&tenantId=2acba9fe-1f29-49de-a1ee-45b3b7aff8f5&callId=885fa7cf-d273-4a96-b437-3886c2aa62b7&threadType=Meeting&meetingType=Scheduled&subType=RecapSharingLink_RecapCore

## 1. Context of the session

During this session the discussion focused on:

- Git branches and pull requests
- how branches appear in repository history
- how Jenkins builds are triggered for branches
- large numbers of builds generated by multiple branches
- investigating Jenkins disk usage
- using file comparison (`diff`) to analyze changes over time

The goal of the discussion was to understand **why Jenkins contains many build folders and how to determine whether they are being cleaned up correctly**.

---

## 2. Understanding Git branches in the repository history

Roman explained a diagram showing multiple Git branches.

Example visualization:

```
main branch
   │
   ├── commit
   │
   ├── commit
   │
   └── commit
          \
           \  feature branch (red)
            ├── commit
            └── commit
```

In the diagram:

```
Red line = developer branch
Gray line = pull request merge
```

The pull request represents a request to merge the feature branch into the main branch.

---

## 3. What a Pull Request represents

A **Pull Request (PR)** is a request to merge code changes from one branch into another.

Example workflow:

```
Developer creates branch
        ↓
Developer commits changes
        ↓
Developer opens Pull Request
        ↓
Team reviews code
        ↓
Branch merged into main
```

After merging, the feature branch may be deleted.

---

## 4. Jenkins builds for each branch

In Jenkins CI/CD environments, pipelines may run for:

```
main branch
feature branches
bugfix branches
release branches
```

Example:

```
repo
 ├── main
 ├── feature/login
 ├── feature/api
 └── bugfix/payment
```

Each branch may trigger **its own Jenkins pipeline builds**.

---

## 5. Why Jenkins may contain many build folders

Roman explained that large repositories may contain:

```
many branches
many builds per branch
```

Example scenario:

```
20 branches
×
10 builds per branch
=
200 build directories
```

This can consume large amounts of disk space on the Jenkins server.

---

## 6. When large build numbers are normal

Having many builds is sometimes expected.

Reasons:

```
Multiple active development branches
Frequent commits
CI builds for each commit
```

Therefore, seeing many build directories **does not necessarily mean there is a problem**.

---

## 7. When large build numbers may indicate a problem

Roman mentioned a previous issue where:

```
A pipeline was repeatedly triggering builds
Old build logs were never deleted
```

Example problematic behavior:

```
Pipeline runs repeatedly
        ↓
Build logs accumulate
        ↓
Disk usage grows
```

This situation requires investigation.

---

## 8. Jenkins build cleanup behavior

In properly configured pipelines:

```
Branches are merged
        ↓
Branch pipelines become inactive
        ↓
Old builds eventually removed
```

Some Jenkins setups automatically delete builds after:

```
number of builds
or
number of days
```

This behavior is controlled by **build retention policies**.

---

## 9. Investigating Jenkins storage usage

Roman suggested monitoring the Jenkins build folders over time.

Example directory structure:

```
jenkins_home/
   jobs/
      project-name/
         branches/
            feature-login/
               builds/
            feature-api/
               builds/
```

Each branch may contain multiple build directories.

---

## 10. Comparing folder lists over time

To determine whether Jenkins is cleaning old builds, Roman suggested:

1. Save a list of current directories
2. Wait some time
3. Save another list
4. Compare the two lists

Example command:

```
ls > build_list_day1.txt
```

Later:

```
ls > build_list_day7.txt
```

---

## 11. Using `diff` to analyze changes

The lists can be compared using:

```
diff build_list_day1.txt build_list_day7.txt
```

This reveals:

```
new builds added
old builds removed
unchanged directories
```

This helps determine whether Jenkins is **cleaning up builds properly**.

---

## 12. Typical Jenkins disk usage troubleshooting workflow

Example troubleshooting process:

```
Check Jenkins build folders
        ↓
Identify large number of builds
        ↓
Check branch activity in Bitbucket
        ↓
Check Jenkins retention policies
        ↓
Compare directory lists over time
```

This helps determine whether the behavior is normal or a configuration issue.

---

## 13. Relationship between Bitbucket and Jenkins builds

Jenkins builds are triggered by repository activity.

Example:

```
Developer pushes code
        ↓
Bitbucket webhook triggers Jenkins
        ↓
Pipeline runs
        ↓
Build stored in Jenkins
```

If many branches exist, Jenkins will store builds for each branch.

---

## Summary

This session explained:

- how Git branches appear in repository history
- how pull requests merge branches into main
- how Jenkins runs builds for each branch
- why large numbers of build folders may appear
- when this behavior is normal
- how to investigate Jenkins disk usage
- how to compare directory snapshots using `diff`

The main takeaway is that **large numbers of Jenkins builds are often caused by many active branches, but monitoring disk usage and build cleanup policies is important to prevent storage issues**.