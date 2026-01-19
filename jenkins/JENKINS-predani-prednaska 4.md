---
marp: true
paginate: true
---
<style>
section::after {
  content: attr(data-marpit-pagination) '/' attr(data-marpit-pagination-total);
}
</style>

# Jenkins handover

TODO: java keytool certifikaty, System Admin e-mail address zmenit na novou

---

## How to view in VSCode

- add `Marp for VS Code` extension to vscode
- preview with `Ctrl+Shift+V`

---

## Setup for access

- ~~AD group `S-FEG-OCP_prod_p_jenkins_edit`~~ for OCP Team only
- set new AD group for admin account in jenkins
- nexus access to upload artifacts, Kuba
- S-FEG-NET-JENKINS - new group for new admins to access servers
- S-FEG-OCP_prod_p_jenkins_edit - access to OCP namespaces

---

## S-FEG-NET-JENKINS AD group
contains access to:
```
jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz   # master server
el8builder01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz   # testing jenkins
app02-jenkins-ocp01-p.azure.feg.bet   # DR jenkins in azure
```

ports:
```
80
8080
443
22
```

---

SSH to servers:
```
ssh -l ${USER}@ad.ifortuna.cz jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz
ssh -l ${USER}@ad.ifortuna.cz el8builder01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz
ssh -l ${USER}@ad.ifortuna.cz app02-jenkins-ocp01-p.azure.feg.bet
```

---

## Documentation official

- official documentation https://www.jenkins.io/doc/
- links on plugins site https://ci.svc.ifortuna.cz/manage/pluginManager/
- https://www.youtube.com/@CloudBeesTV
- https://www.youtube.com/watch?v=A2dEsu9dRUc&list=PLvBBnHmZuNQJeznYL2F-MpZYBUeLIXYEe

---

## Documentation FEG specific

- tips for developers https://cnfl.myfortuna.eu/spaces/DEV/pages/203717678/Jenkins+with+OCP4+backend
- tips for agents https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/OS/repos/ocp4_jenkins_docker_slaves/README.md

---

## How is Jenkins used in FEG

- building apps, images and pushing into quay (registry.svc.ifortuna.cz)
- deployment of apps (JIRA connector, managed by other team)
- QA tests
- build of jenkins own agents https://ci.svc.ifortuna.cz/job/Openshift/job/ocp4_jenkins_docker_slaves/job/master/

---

## Web GUI

- https://ci.svc.ifortuna.cz/
- definitely create you own space for testing (i use romantest)

---

## Web GUI - Dashboard

- menu
- build queue: which jobs are waiting for assignment to agent/node
- build executor status: running jobs
- folders

---

## Web GUI - New Item

- create folder
- Multibranch pipeline
- Freestyle pipeline

---

## Web GUI - Multibranch pipeline

- most used style of pipeline
- show and tell

---

## Web GUI - Freestyle pipeline

- show and tell

---

## Web GUI - Manage Jenkins - System

- home directory
- number of executors
- jenkins URL
- SonarQube settings (sonar-test is for testing of SonarQube)
- Bitbucket Endpoints
- Vault Plugin
- Jira
- Quay Image Tag Parameter Plugin (we will change this later)
- Global Trusted Pipeline Libraries
- Git plugin

---

## Web GUI - Manage Jenkins - System - Global Trusted Pipeline Libraries 1/2

- https://www.jenkins.io/doc/book/pipeline/shared-libraries/
- shared libraries for everyone to use
- pulled to all builds
- https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/OS/repos/jenkins-shared-library/browse
- https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/OS/repos/jenkins-shared-library/browse/vars
- used because of `Method Code Too Large`

---

## Web GUI - Manage Jenkins - System - Global Trusted Pipeline Libraries 2/2

- caused by limitations in Java (https://docs.cloudbees.com/docs/cloudbees-ci-kb/latest/troubleshooting-guides/method-code-too-large-error)
- some teams have their own, for example MiddleWare https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/MW/repos/jenkins-pipelines/browse/vars

---

## Web GUI - Manage Jenkins - Tools

- Git path

---

## Web GUI - Manage Jenkins - Plugins

- biggest pain in Jenkins
- beautiful example of dependency hell
- main concern when updating Jenkins
- always backup functional plugins `cp -ar plugins plugins.$(date +%Y%m%d)`
- almost always some changes in configuration
- when plugin is obsolete we just remove it
- best practice is never add another plugin (but even folders works as plugin, so some are needed)

---

## Web GUI - Manage Jenkins - Plugins - Troubleshooting example

- notes/JENKINS-plugins.md
- notes/JENKINS-pluginslog.md

---

## Web GUI - Manage Jenkins - Clouds


- clouds -> kubernetes -> Pod Templates (show and tell)
- dependency on jnlp
- Container template -> Docker image, always use images stream
- if you setup registry.svc.ifortuna.cz you will overload quay
- even developers should upload builded image to quay, then setup imagestream and then use image for deployment from imagestream

---

## Web GUI - Manage Jenkins - Nodes 1/2

- nodes
- connection via SSH
- owner of remote server needs to generate ssh key pair (as jenkins)
  ```bash
  ssh-keygen
  ls .ssh
  # id_ed25519  id_ed25519.pub
  ```
- owner of remote server needs to add content of id_ed25519.pub into `~/.ssh/authorized_keys`

---

## Web GUI - Manage Jenkins - Nodes 2/2

- add client cert content (id_ed25519) to credentials in jenkins (example: https://ci.svc.ifortuna.cz/manage/credentials/store/system/domain/_/credential/jenkins%20(MacOS-QA2%20username%20and%20ssh%20key)/)
- setup these credentials as Credentials in node configuration (example: https://ci.svc.ifortuna.cz/computer/macos%2Dqa2/configure)

---

## Web GUI - Manage Jenkins - Configuration as Code 1/2

- https://www.jenkins.io/projects/jcasc/
- CasC on web vs jenkins.yaml on master server
- Caution, you can't use configuration file from GUI as jenkins.yaml directly on master server (mentioned in documentation of JCasC plugin)
- when making changes, you must manually copy sections from GUI file to jenkins.yaml on server
- good practice to backup file with `sudo su - && cd /var/lib/jenkins/ && cp jenkins.yaml jenkins.yaml.$(date +%Y%m%d)` before making changes
- By default, the Configuration as Code Plugin cancels Jenkins startup when a deprecated section is detected

---

## Web GUI - Manage Jenkins - Configuration as Code 2/2

- button Apply new configuration - when making changes, copy to jenkins.yaml, hit apply and check if changes are loaded as expected
- button View Configuration
- By default, the Configuration as Code Plugin cancels Jenkins startup when a deprecated section is detected.
- you can change by this entry:
  ```yaml
  jenkins:
    configuration-as-code:
      deprecated: warn
  ```
- but then you must search for deprecated entries

---

## Web GUI - Manage Jenkins - Configuration as Code vs configuration in GUI

- mainly for testing
- always take changes from Jenkins Configuration as Code onlive version and put them into /var/lig/jenkins/jenkins.yaml
- always click `Apply` and `Save`

---

## Web GUI - Manage Jenkins - Configuration as Code vs configuration in GUI - hands on

- Quay Image Tag Parameter Plugin
- change wrong URL to `registry.svc.ifortuna.cz`

---

## Web GUI - Manage Jenkins - Security

- Authentication
- LDAP

---

## Web GUI - Manage Jenkins - Credentials

- storage of username + passwords
- ssh keys
- new entry is hidden behind Domain `(global)` -> `Add Credentials`
- don't forget to copy new entry to `jenkins.yaml`
- trick to show stored credentials will soon be revealed

---

## Web GUI - Manage Jenkins - Manage and Assign Roles

- role is definition of access rights
- `pattern` is place, where `role` can do assigned tasks

---

## Web GUI - Manage Jenkins - Manage and Assign Roles - Assign Roles

- if ldap (bitbucket_) group has admin or reader rights
- which `roles` can `ldap group` use

---

## Web GUI - Manage Jenkins - In-process Script Approval

- not acitvely checked
- sometimes developer needs to allow potentially unsafe things, but they usualy know what they are doing

---

## Web GUI - Manage Jenkins - Troubleshooting - Manage Old Data

- according to documentation safe to run

---

## Web GUI - Manage Jenkins - Tools and Actions - Script Console

- how to decode stored passwords?
- https://ci.svc.ifortuna.cz/manage/script
- `println(hudson.util.Secret.decrypt("{XXX=}"))`
- need to have `/var/lib/jenkins/secrets/master.key` for this installation
- you can't decode passwords on new installation of Jenkins without `master.key`

---

## Jenkins hardware

- master
- openshift
- nodes
- test

---

### Master

- master https://ci.svc.ifortuna.cz/
- you can encounter address without certificate http://jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz:8080 (some developers still use this URL)
- `ssh -l ${USER}@ad.ifortuna.cz jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz`
- in vmware, CZDC1-INTERNAL

---

### Master - Jenkins

- jenkins is installed from jenkins repo `/etc/yum.repos.d/jenkins.repo`
  ```bash
  [jenkins]
  name=Jenkins-stable
  baseurl=http://pkg.jenkins.io/redhat-stable
  gpgcheck=0
  proxy=http://czdcm-proxy-infra.lx.ifortuna.cz:3128
  ```
- `systemctl status jenkins`

---
### Master - Nginx

- jenkins is setup to listen on 8080
- https is handled by nginx
- `cat /etc/nginx/conf.d/jenkins.conf`
- `systemctl status nginx`
- certificates are stored in `/etc/pki/jenkins/` (nginx configuration)
- valid until `Thu, 05 Mar 2026 15:35:00 GMT`

---

### Master - certificates

```bash
keytool -import -alias caftn -keystore /var/lib/jenkins/keystore/cacerts -file /home/ad.ifortuna.cz/prorom/certyjenkins/CA.pem 
keytool -import -alias caftn -keystore /var/lib/jenkins/keystore/cacerts -file /home/ad.ifortuna.cz/prorom/certyjenkins/fortunaCA.cer 
keytool -import -alias caftn -keystore /var/lib/jenkins/keystore/cacerts -file /home/ad.ifortuna.cz/prorom/certyjenkins/FortunaRootCA_2032.crt 
keytool -import -alias ftnad -keystore /var/lib/jenkins/keystore/cacerts -file /home/ad.ifortuna.cz/prorom/certyjenkins/ftnAD.crt 
keytool -list -v -keystore cacerts | grep -i alias
keytool -import -alias bitbucket -keystore /var/lib/jenkins/keystore/cacerts -file /home/ad.ifortuna.cz/prorom/certyjenkins/lb01-atlas-shared.o.dc1.cz.ipa.ifortuna.cz.pem 
keytool -import -alias bitbucket3 -keystore /var/lib/jenkins/keystore/cacerts -file /home/ad.ifortuna.cz/prorom/certyjenkins/lb01-atlas-shared.o.dc1.cz.ipa.ifortuna.cz.pem
```

---

### Master - /opt

- `/opt` filesystem is on LVM
- PV `/dev/sdd   vg1_data  lvm2 a--  <900.00g <30.00g`
- VG `vg1_data    1   1   0 wz--n- <900.00g <30.00g`
- LV `lv_opt              vg1_data  -wi-ao---- 870.00g`

---

### Master - enlarge /opt

- enlarge disk in vmware
- update in OS
    ```bash
    echo 1 > /sys/class/block/sdd/device/rescan
    pvresize /dev/sdd
    lvextend -r -L +150G /dev/mapper/vg1_data-lv_opt
    df -h /opt/
    ```

---

### Openshift

- management cluster, namespace `jenkins-shared`
  - GUI - https://console-openshift-console.apps.ocp01-shared.m.dc1.cz.ipa.ifortuna.cz/k8s/ns/jenkins-shared/core~v1~Pod
  - CLI - `oc login -u ${USER} https://api.ocp01-shared.m.dc1.cz.ipa.ifortuna.cz:6443`, `oc project jenkins-shared`
- configuration in https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/OS/repos/ocp4_jenkins_docker_slaves/browse/OCP4setup
- README.md

---

### Openshift - Build of agents 1/2

- README.md
- Kubernetes plugin https://plugins.jenkins.io/kubernetes/
- repo https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/OS/repos/ocp4_jenkins_docker_slaves
- how to build: push changes, go to https://ci.svc.ifortuna.cz/job/Openshift/job/ocp4_jenkins_docker_slaves/job/master/
- show and explain Jenkinsfile
- logic of triggers
- how to add another agent
- when build fails, there is list when agent was modified https://registry.svc.ifortuna.cz/organization/ocp4-jenkins-slaves

---

### Openshift - Build of agents 2/2

- possible solution for https://docs.cloudbees.com/docs/cloudbees-ci-kb/latest/troubleshooting-guides/method-code-too-large-error
- use repo as pipeline library
- romantest/vars/celapipeline.groovy
- romantest/JenkinsfileUpravaAgentaYaml rename to Jenkinsfile
- romantest -> Configuration -> Pipeline Libraries (load same repo as pipeline library)

---

### Openshift - Special agents 1/2

- README.md
- jnlp, why, how, wrong official documentation
  ```bash
  podman pull registry.svc.ifortuna.cz/ocp4-jenkins-slaves/jnlp:latest
  podman pull registry.svc.ifortuna.cz/ocp4-jenkins-slaves/jnlp:updatedjenkinsfile
  podman image tag registry.svc.ifortuna.cz/ocp4-jenkins-slaves/jnlp:latest registry.svc.ifortuna.cz/ocp4-jenkins-slaves/jnlp:backup
  # podman push backup, account application token
  podman login -u='$app' -p='XXX' registry.svc.ifortuna.cz
  podman push registry.svc.ifortuna.cz/ocp4-jenkins-slaves/jnlp:backup
  # podman push new latest
  podman image tag registry.svc.ifortuna.cz/ocp4-jenkins-slaves/jnlp:updatedjenkinsfile registry.svc.ifortuna.cz/ocp4-jenkins-slaves/jnlp:latest
  podman push registry.svc.ifortuna.cz/ocp4-jenkins-slaves/jnlp:latest
  ```
- buildah
- dockerindocker (mainly because of testcontainers)
- rest of agents

---

### Openshift - Special agents 2/2

- agents have common storage, ideal way of working is: use small container with node22 for this, java21 fot these steps and so on, all containers in pod can access /home/jenkins/workspace
- some agents have huge memory reservations for better placement on node, memory hungry builds were often OOMKilled

---

### Openshift - ImageStream

- creation in README.md
- automatic check of images in quay
- for new agent you need to create new image stream

---

### Openshift - PVC

- usage in README.md
- mainly cache for maven, npm, nvd, rush
- beware different node versions needs different cache volumes
- yaml files for creation `./OCP4setup/`

---

### Nodes

- nodes (mac, windows, linux)
- connected via SSH (yes, Windows too)
- limited connection over Web GUI
- not under our management
- https://ci.svc.ifortuna.cz/manage/computer/

---

### Test

- to test jenkins updates
- to test system updates
- create snapshot before changes
- https://ci-test.m.dc1.cz.ipa.ifortuna.cz
- test server el8builder `ssh -l ${USER}@ad.ifortuna.cz el8builder01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz`
- in vmware, CZDC1-INTERNAL
- in openshift `oc project jenkins-test`

---

## Add certificate to Jenkins

- java apps have their own certificate store
- import new intermediate certificate
  ```bash
  scp 'CA01-CZDC1-O.ad.ifortuna.cz_FORTUNA CA(6).cer' prorom\@ad.ifortuna.cz@el8builder01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz:/home/ad.ifortuna.cz/prorom/
  keytool -list -v -keystore /var/lib/jenkins/keystore/cacerts | grep ftn
  keytool -list -v -keystore /var/lib/jenkins/keystore/cacerts -alias ftncert3
  keytool -import -trustcacerts -alias ftncert4 -keystore /var/lib/jenkins/keystore/cacerts -file '/home/ad.ifortuna.cz/prorom/CA01-CZDC1-O.ad.ifortuna.cz_FORTUNA CA(6).cer'
  keytool -list -v -keystore /var/lib/jenkins/keystore/cacerts -alias ftncert4
  ```

---

## Upgrade of Jenkins - theory 1/2

- https://www.jenkins.io/doc/upgrade-guide/
- plan outage, jenkins is needed while releases and is used 24/7, jobs scheduled by cron
- plugins can be copied over to test (or local laptop) and experiment
- jenkins.war
- majority of problems after upgrade is from plugin configuration and unsatisfied dependencies
- errors are in journal `journalctl -eu jenkins`
- don't forget to edit jenkins.yaml
- always make snapshot in vmware before update!!!

---

## Upgrade of Jenkins - theory 2/2

1. upgrade plugins first
   - http://el8builder01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz:8080/manage/pluginManager/
   - select all
   - button `Update`
   - `systemctl restart jenkins`
2. upgrade Jenkins
3. upgrade any remaining plugins

---

## Upgrade of Jenkins - practice 1/11

upgrade from `2.452.3` to `2.504.1`
https://www.jenkins.io/doc/upgrade-guide/2.462/

changes:
- The "Disable project" link has been removed from the project (job) page. A project can be disabled from its "Configure" page.
- Upgrade Spring Security, Spring Framework, and servlet containers - Spring Security 6, Spring Framework 6, and Jakarta EE 9

---

## Upgrade of Jenkins - practice 2/11

- Removing configurability of Jenkins agent protocols list
  ```yaml
  jenkins:
    agentProtocols:
    - "JNLP4-connect"
    - "Ping"
    authorizationStrategy:
  ```
---

## Upgrade of Jenkins - practice 3/11

- in this case we found out that webhooks stopped working after update
- https://issues.jenkins.io/browse/JENKINS-75735
- https://github.com/jenkinsci/bitbucket-branch-source-plugin/blob/master/docs/USER_GUIDE.adoc#webhooks-registering
- https://medium.com/@minaxijoshi3101/downgrade-a-plugin-version-in-jenkins-without-internet-28580603732b
- we decided to downgrade plugin to working version `936.1.1`

---

## Upgrade of Jenkins - practice 4/11
plan:
- plan outage for 1 hour, DEPLOY Infra change
- update plugins
- `systemctl stop jenkins`
- edit jenkins.yaml
    ```yaml
  jenkins:
          #agentProtocols:
          #- "JNLP4-connect"
          #- "Ping"
    authorizationStrategy:
  ```

---

## Upgrade of Jenkins - practice 5/11

- update ldap.jpi, jenkins and system
  ```bash
  cd /var/lib/jenkins/plugins/
  rm -rf ldap*
  https_proxy=http://czdcm-proxy-infra.lx.ifortuna.cz:3128 curl -o ldap.hpi -fSL https://updates.jenkins.io/latest/ldap.hpi
  chown jenkins:jenkins ldap.hpi
  dnf update
  systemctl start jenkins
  ```
- remove nginx-plus
  ```bash
  dnf remove nginx-plus
  rm /etc/yum.repos.d/nginx-plus.repo
  dnf install nginx
  systemctl enable --now nginx
  ```

---

## Upgrade of Jenkins - practice 6/11

- `systemctl start jenkins`
- update plugins in GUI (if any)
- `systemctl restart jenkins`
- repeat until all plugins are updated

---

## Upgrade of Jenkins - practice 7/11
- downgrade of `cloudbees-bitbucket-branch-source`
- https://github.com/jenkinsci/bitbucket-branch-source-plugin/blob/master/docs/USER_GUIDE.adoc#webhooks-registering
- https://issues.jenkins.io/browse/JENKINS-75735
- downgrade guide https://medium.com/@minaxijoshi3101/downgrade-a-plugin-version-in-jenkins-without-internet-28580603732b

---

## Upgrade of Jenkins - practice 8/11
- There will be a major refactoring in the plugin roadmap regarding webhooks, effectively creating extension points so that external plugins can provide their own implementation to process the incoming payload
- Why plugin from "Move Work Forward" company yes and other no? Because historical reason, 7-8 years ago they provided the first implementation of webhook when Bitbucket Server did not and it was for FREE.
- I can not remove that support because was documented but the implementation (specific for that plugin) will be move out the bitbucket-branch-source plugin into a separate plugin and left open to adoption.

---

## Upgrade of Jenkins - practice 9/11
- bitbucket-branch-source
  ```bash
  systemctl stop jenkins
  cd /var/lib/jenkins/plugins/
  rm -rf /var/lib/jenkins/plugins/cloudbees-bitbucket-branch-source*
  https_proxy=http://czdcm-proxy-infra.lx.ifortuna.cz:3128 curl -o cloudbees-bitbucket-branch-source.hpi -fSL https://updates.jenkins.io/download/plugins/cloudbees-bitbucket-branch-source/936.1.1/cloudbees-bitbucket-branch-source.hpi
  chown jenkins:jenkins cloudbees-bitbucket-branch-source.hpi
  systemctl start jenkins
  ```
- blue ocean
  ```bash
  systemctl stop jenkins
  cd /var/lib/jenkins/plugins/
  rm -rf blueocean blueocean.jpi blueocean.bak
  https_proxy=http://czdcm-proxy-infra.lx.ifortuna.cz:3128 curl -o blueocean.hpi -fSL https://updates.jenkins.io/download/plugins/blueocean/1.27.19/blueocean.hpi
  chown jenkins:jenkins blueocean.hpi
  systemctl start jenkins
  ```
- blueocean-bitbucket-pipeline
  ```bash
  rm -rf blueocean-bitbucket-pipeline*
  https_proxy=http://czdcm-proxy-infra.lx.ifortuna.cz:3128 curl -o blueocean-bitbucket-pipeline.hpi -fSL https://updates.jenkins.io/download/plugins/blueocean-bitbucket-pipeline/1.27.19/blueocean-bitbucket-pipeline.hpi
  chown jenkins:jenkins blueocean-bitbucket-pipeline.hpi
  systemctl start jenkins
  ```

---

## Upgrade of Jenkins - practice 10/11
- reboot server  
  ```bash
  reboot
  ```
- Manage Old Data
  https://ci.svc.ifortuna.cz/manage/administrativeMonitor/OldData/manage
- end of web page, button `Discard Unreadable Data`

---

## Upgrade of Jenkins - practice 11/11
- made persistent with script in `/var/lib/jenkins/init.groovy.d/cspsetup.groovy`
- ~~on built-in node https://ci.svc.ifortuna.cz/computer/(built-in)/script~~
- ~~run this:~~
  ```bash
  System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "default-src 'unsafe-eval' 'wasm-unsafe-eval' 'unsafe-inline'; media-src 'self' data:; img-src 'self' data:")
  ```

---

## Upgrade of Jenkins - tips

- SNAPSHOT!!!
- you can copy existing plugin to local machine, different server and test on them
  ```bash
  cp -ar plugins plugins.$(date +%Y%m%d)
  tar zcvf pluginsprorom.tar.gz plugins.02052025
  chown prorom@ad.ifortuna.cz pluginsprorom.tar.gz
  ```
- on local machine
  ```bash
  scp prorom\@ad.ifortuna.cz@jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz:/opt/jenkins/pluginsprorom.tar.gz ./
  ```
- older versions of jenkins packages https://archives.jenkins-ci.org/redhat-stable/

---

## External apps used with Jenkins

- Bitbucket
- Vault - C&T team, Michaela Vycudilíková
- Nexus - C&T team, Jakub Rektoris
- Quay - C&T team, Aleš Kratochvíl
- SonarQube - C&T team, Štěpán David
- OWASP - 3rd party plugin
- https://lnd.svc.ifortuna.cz

---

### Bitbucket

- show webhooks in bitbucket
- git clone in pipeline
  ```groovy
  stages {
    stage('Checkout Source'){
      steps {
        checkout scm: [
          $class: 'GitSCM',
          userRemoteConfigs: [[url: "ssh://git@app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz:7999/os/prorom-sonar-test.git",
          credentialsId: 'bitbucket-global']],
          branches: [[name: "master"]]], poll: false
      }
    }
  }
  ```
- SSH is over port 7999!

---

### Vault

- https://vault-shared.svc.ifortuna.cz/
- login to openshift clusters for deploy of apps via kubeconfig
- deploy
- setup secretu v pipeline
  ```groovy
  def secrets = [[
    $class: 'VaultSecret',
    path: 'jenkins/mw',
    secretValues: [
      [$class: 'VaultSecretValue', envVar: 'MY_DOCKER_CONFIG', vaultKey: 'docker-config'],
      [$class: 'VaultSecretValue', envVar: 'QUAY_API_TOKEN', vaultKey: 'quay-api-token']
    ]
  ]]
  ```

---

### Nexus

- https://nexus.svc.ifortuna.cz/
- upload of artifacts
- beware there is different URL outside of cluster and for agents in cluster
- https://cnfl.myfortuna.eu/pages/viewpage.action?pageId=203717678#JenkinswithOCP4backend-SonarandNexussetup

---

### Quay

- https://registry.svc.ifortuna.cz/
- images
- pull a push images
- robot accounts

---

### SonarQube

- https://sonar-sonar-prod.m.dc1.cz.ipa.ifortuna.cz/projects
- tokens in Jenkins configuration (jenkins.yaml)
- mainly for developers

---

### OWASP

- https://owasp.org/
- uses list of vulnerabilities
- https://www.cisa.gov/sites/default/files/feeds/known_exploited_vulnerabilities.json
- proxied by nexus because of rate limiting (nexus -> Browse -> cisa -> .json)
- nobody in FEG wants to create certificate/key for api

---

## Inspiration for pipelines

- jenkins shared libraries https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/OS/repos/jenkins-shared-library/browse/vars
- pipelines from Marek Přibyl team https://app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz/projects/MW/repos/jenkins-pipelines/browse/vars

---

## Backup

- daily job in vmware via veeam
- manual backup before changing jenkins.yaml `cp jenkins.yaml jenkins.yaml.$(date +%Y%m%d)`
- backup plugins `cp -ar plugins plugins.$(date +%Y%m%d)`

---

## Disaster Recovery

- https://cnfl.myfortuna.eu/spaces/DEV/pages/344102612/DR+for+Jenkins+and+Tools

---

## Troubleshooting

- put `sleep` to pipeline and `oc rsh -c kontejner pod`
- romantest, `sh ''' '''` a `sh '" "'` a `sh " ' ' "`
- proxy
- my notes JENKINS-troubleshooting.md, find examples
- Teams channel `sb-devops` - solved problems

---

## Troubleshooting - Easy Local Lab

- clean environment, for example plugin update troubleshooting
  ```bash
  podman run -d --rm -p 8080:8080 -p 50000:50000 -v jenkinsvol:/var/jenkins_home --name=jenkins docker.io/jenkins/jenkins:lts
  # get initialAdminPassword
  podman exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
  ```
- using existing jenkins.yaml (needs changes like remove ldap, setup admin with password)
  ```bash
  podman volume create jenkinsvol
  podman run -d --rm -p 8080:8080 -p 50000:50000 -v jenkinsvol:/var/jenkins_home -v ./jenkins.yaml:/var/jenkins_home/jenkins.yaml --name=jenkins docker.io/jenkins/jenkins:lts
  podman exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
  ```
- run `jenkins.war` in folder

---

## Troubleshooting - Proxy usage in pipelines

- in cluster vs outside of cluster URLs vs services
- proxy in management: `http://czdcm-proxy-infra.lx.ifortuna.cz`, `http://10.0.68.20`, port `3128`
- sometimes error with proxy from another environment (dev, test, ...)
- URLs must also be allowed from proxy to internet
  ```bash
  export HTTP_PROXY=http://10.0.68.20:3128
  export HTTPS_PROXY=http://10.0.68.20:3128
  export NO_PROXY=${NEXUS_NO_PROXY},${SONAR_NO_PROXY}
  ```
- yes `HTTPS` connections go over `HTTP` to proxy

---

## Troubleshooting - CPU and RAM usage of agents

- https://grafana-thanos-shared.apps.ocp01-shared.m.dc1.cz.ipa.ifortuna.cz/d/OdFOFFNIk/jenkins-agents?orgId=1&from=1747659691680&to=1747660591680
- select time and refresh page
- select pod
- select container

---

## Troubleshooting - /opt/jenkins/workspace cleanup

- remove files older than 90 days from workspace directory
`find /opt/jenkins/workspace -type d -mtime +90 -exec rm -rf {} \;`
- `No such file or directory` is ok output

---

## HTML publisher plugin

- custom CSP rules
- documentation: https://www.jenkins.io/doc/book/security/configuring-content-security-policy/
- example: https://ci.svc.ifortuna.cz/job/QA/job/Mobile/job/iOS/job/iOS_SB_Local/HTML_20Report/
- made persistent with `/var/lib/jenkins/init.groovy.d/cspsetup.groovy`
- ~~temporary setup, run this here https://ci.svc.ifortuna.cz/computer/(built-in)/script~~
```bash
System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "default-src 'unsafe-eval' 'wasm-unsafe-eval' 'unsafe-inline'; media-src 'self' data:; img-src 'self' data:")
```

persistent setup (beware, AI generated):

```bash
JENKINS_ARGS="--argumentsRealm.passwd.jenkins=yourpassword --argumentsRealm.roles.user=admin --httpPort=8080 --csp=default-src 'unsafe-eval' 'wasm-unsafe-eval' 'unsafe-inline'; img-src 'self' data:"
```
---

## Unresolved problems

- sometimes login to web gui is very slow, don't know why, ldap settings were optimized, but this sometimes occur

---

## To Do for new team 1/3
- make these settings for html report plugin persistent

  ```bash
  System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "default-src 'unsafe-eval' 'wasm-unsafe-eval' 'unsafe-inline'; media-src 'self' data:; img-src 'self' data:")
  ```
- hint
  ```bash
  JENKINS_ARGS="--argumentsRealm.passwd.jenkins=yourpassword --argumentsRealm.roles.user=admin --httpPort=8080 --csp=default-src 'unsafe-eval' 'wasm-unsafe-eval' 'unsafe-inline'; img-src 'self' data:"
  ```
---

## To Do for new team 2/3
- resolve Bitbucket webhooks after updating, affected plugins are these:
  - cloudbees-bitbucket-branch-source.hpi
  - blueocean.hpi
  - blueocean-bitbucket-pipeline.hpi

---

## To Do for new team 3/3
- Future proof solution should be to use Native BitBucket Webhooks
- on test you can test what happens when you try to change



