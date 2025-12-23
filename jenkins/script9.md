# Applications and tools commonly used alongside Jenkins, and how they integrate into the DevOps ecosystem:

- Bitbucket → source code hosting (Git)

- Vault → secrets and credentials storage

- Nexus → artifact repository

- Quay → container image registry

- SonarQube → code quality scanning

- OWASP → vulnerability lists for security scanning

These are all key components in a CI/CD toolchain.

## Bitbucket

- holds Git repositories

- triggers Jenkins builds using webhooks

- provides branch/source integration

- core tool used daily by developers

**IT term: Webhook** = automated trigger sent from one system (Bitbucket) to another (Jenkins) when events occur.

Example: commit → webhook → Jenkins pipeline runs.

## Vault

- stores secure credentials

- used for Kubernetes/OpenShift authentication

- secrets can be injected into pipelines

**IT term: secret** = password/token/credential used by automation.

Example:
Pipeline asks Vault → receives token → logs into OpenShift.

## Nexus
