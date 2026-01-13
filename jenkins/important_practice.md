# Jenkins Configuration as Code (JCasC)

## WORKFLOW

```bash
cd /var/lib/jenkins

cp jenkins.yaml jenkins.yaml.20250602   # backup
vim jenkins.yaml                        # edit
yamllint jenkins.yaml                   # check yaml syntax (optional)
diff jenkins.yaml jenkins.yaml.20250602 # review changes
systemctl restart jenkins               # apply
```

**IT term: YAML** = a human-readable file used to store settings.

**IT term: JCasC** = plugin allowing Jenkins configuration to be stored as text files.

Jenkins.yaml contains:

- secrets (example: JIRA connection)

- permissions

  - there is also permission metrics in GUI
    he puts "S-FEG-OCP_prod_p_jenkins_edit" to search for in configuration file
    then he adds this entry to the file on the server

    ![](images/screenshot-20260106-184751.png)

    ![](images/screenshot-20260106-184824.png)

    so we do it in GUI -> then we add it also in YAML, which is stored on the server

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

## Connect as my user to Jenkins server (SSH)

```bash
ssh naseka@jenkins01-ocp01-shared.m.dc1.ipa.ifortuna.cz
```

if succesfull i see:

```
naseka@jenkins01-ocp01-shared:~$
```

## then I temporarily (if allowed) become root:

```bash
sudo -i
```

then prompt changes to:

```
root@jenkins01-ocp01-shared:~#
```

```pgsql
    root@jenkins01-ocp01-shared.m.dc1.ipa.ifortuna.cz
    │    │
    │    └── hostname (server name)
    └─────── user (root = administrator)

```

So:

- jenkins01-ocp01-shared... = the Jenkins server
- root = administrator account

## Go to Jenkins configuration directory

```bash
cd /var/lib/jenkins
```

To see what’s inside:

```
ls
```

Example output:

```bash
jobs/
plugins/
secrets/
jenkins.yaml
```

## View the YAML file (safe, read-only)

```bash
cat jenkins.yaml
```

or (better for beginners):

```bash
less jenkins.yaml
```

Tips for less:

- scroll → arrow keys
- quit → q

## Create a backup copy of YAML file:

```bash
cp jenkins.yaml jenkins.yaml.20250602
```

- cp = copy
- jenkins.yaml = source file
- jenkins.yaml.20250602 = backup with date

## Edit the YAML file

Option A: `nano` (BEST for beginners)

```bash
nano jenkins.yaml
```

Inside nano:

- edit text normally
- save → CTRL + O, then Enter
- exit → CTRL + X

Option B: vim (you saw instructor use this)

```bash
vim jenkins.yaml
```

Minimal vim survival kit:

- press i → start editing
- edit text
- press ESC
- type :wq
- press Enter
- That’s it.

### Search for authorizationStrategy inside vim:

Inside `vim` you type:

```
/authorizationStrategy
```

- `/` = search forward
- `authorizationStrategy is the text we are looking for

Exit search mode: press `ESC`

### Type new group to admin name roles:

We copy pasted the group name from GUI configuration as a file into our Yaml on the server:

![](images/screenshot-20260107-143707.png)

## Check YAML syntax (optional)

```bash
yamllint jenkins.yaml
```

- If no output → good
- If errors → fix indentation
- (YAML is very sensitive to spaces!)

## Review changes (compairing back up with actual yaml file)

```bash
diff jenkins.yaml jenkins.yaml.20250602 # review changes
```

## Tell Jenkins to reload configuration (not needed in our case)

not needed here, cause GUI changes will be applied till the restart. Restart will happen if we do some plugin, upgrades etc)

Restart Jenkins (most common)

```bash
systemctl restart jenkins
```

Check status:

```bash
systemctl status jenkins
```

## Typical real workflow:

```bash
cd /var/lib/jenkins

cp jenkins.yaml jenkins.yaml.20250602   # backup
vim jenkins.yaml                        # edit
yamllint jenkins.yaml                   # check yaml syntax (optional)
diff jenkins.yaml jenkins.yaml.20250602 # review changes
systemctl restart jenkins               # apply
```

# OpenShift OC command-line tool (OpenShift CLI)
## Commands:

```bash
oc login ...token.. #token is taken from here: https://console-openshift-console.apps.ocp01-shared.m.dc1.cz.ipa.ifortuna.cz
oc get projects
oc project jenkins-shared
oc get pods # to see table with pods

```

# To see everything related to Jenkins (jobs, plugins etc):

```bash
ls -la /var/lib/jenkins/
```


# to experiment/test jenkins updates:

  Jenkins is regularly backed up by copying its data to separate folders, and these backups can be downloaded and used locally to safely experiment, test changes, or even intentionally break Jenkins without risking production. For local testing, Jenkins YAML configuration must be adjusted so it does not connect to real OpenShift or external systems. The key message is that updates and risky changes should always be tested on a local or test setup first, never directly on the production Jenkins.

  
  

# back up functional plugins

`cp -ar plugins plugins.$(date +%Y%m%d)`

# undestand installs, updates and stability: 
`cat /etc/yum.repos.d/jenkins.repo` 
    shows where Jenkins packages are downloaded from, which is crucial for understanding installs, updates, and stability.

# to instal jenkins

```bash
yum install jenkins
```

# agents are built here

## flow:

1. Admin changes Dockerfile (repo on bitbucket: https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/OS/repos/ocp4_jenkins_docker_slaves/browse)

2. Then admin pushes the changes to bitbucket

3. This triggers jenkins CI job to build image here: https://ci.svc.ifortuna.cz/job/Openshift/job/ocp4_jenkins_docker_slaves/job/master/

4. CI job  in jenkins builds image

5. Image is pushed to internal registry (Quay)

6. Jenkins (via OpenShift plugin) requests agent
OpenShift runs pod from that image (image was built above and recipe for the image correspond to folder names (which are agent names): buildah, slave-base-java11/ etc)

            Jenkins requests an agent → OpenShift runs a pod
            ✔️ Correct, and this is the key mental model
            When a pipeline runs:
            Jenkins pipeline says:
            agent { label 'slave-base-java11' }
            Jenkins Kubernetes/OpenShift plugin:
            maps label → image
            OpenShift:
            creates a pod
            pod uses that image
            That pod is the Jenkins agent


![](images/screenshot-20260113-154711.png)

https://ci.svc.ifortuna.cz/job/Openshift/job/ocp4_jenkins_docker_slaves/job/master/


the repository corresponds to this demo pic:

-> every agent has its own folder

        * these are Jenkins agent image definitions:

            buildah/
            dockerindocker/
            jnlp/
            OCP4setup/
            slave-android-java21/
            slave-base-java11/



![](images/screenshot-20260113-154530.png)

the repo itself is on bitbucket:
https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/OS/repos/ocp4_jenkins_docker_slaves/browse

![](images/screenshot-20260113-155824.png)

* That repository is used to build Docker images for Jenkins agents that will later run in OpenShift (OCP4).

Inside image definitions folder (for ex. `slave-base-java11`)

![](images/screenshot-20260113-160628.png)

```csharp
slave-base-java11/
├── Dockerfile
├── ca-bundle.crt
├── cz_ipa_new_2.crt
├── fan_ca_bundle.crt
├── ftn-ca.pem
├── vault-pki-nonprod-chain.crt
├── vault-pki-prod-chain.crt
├── jenkins-agent
```

* shortly (more below): 
```css
Git repository
└── Agent image folder
    ├── Dockerfile      ← how to build the image
    ├── certs           ← trust company services
    └── agent binary    ← connect to Jenkins
```

## Dockerfile = recipe for the agent image

                This is the most important file.
                It literally says:
                “Take base image X
                Install Java 11
                Copy certificates
                Install Jenkins agent
                Configure environment”
                When Jenkins admins say:
                “we build Jenkins agents”
                They mean:
                “we run docker build using this Dockerfile”

## .crt/.pem files = company security certificates "this agent trusts company infrastructure"

*.crt
*.pem

                Are TLS / SSL certificates.
                Why needed?
                Because inside your company:
                Git
                Nexus
                Vault
                Internal APIs
                👉 use internal CAs, not public ones.
                Without these certs:
                git clone ❌
                mvn download ❌
                curl https://internal-api ❌
                So these files are copied into the image so that:
                “This agent trusts company infrastructure”

## jenkins-agent file
                This is usually:
                a binary
                or a startup script
                Its job:
                connect the container back to the Jenkins controller
                register as an agent
                Without it:
                👉 container runs, but Jenkins cannot use it