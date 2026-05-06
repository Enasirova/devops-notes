# Shell Configuration on Jenkins VM: User vs Jenkins Environment

## Current Session Context

Example SSH session:

    [0 naseka@ad.ifortuna.cz@jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz ~]$ pwd
    /home/ad.ifortuna.cz/naseka

Listing files in the home directory:

    [0 naseka@ad.ifortuna.cz@jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz ~]$ ls -a
    .  ..  .bash_history  .bash_logout  .bash_profile  .bashrc  .jenkins  .viminfo

This shows the **home directory of user `naseka` on the Jenkins VM**.

---

# 1. These configuration files belong only to user `naseka`

Location:

    /home/ad.ifortuna.cz/naseka/

Files:

    .bash_history
    .bash_logout
    .bash_profile
    .bashrc
    .jenkins
    .viminfo

These files control the **shell environment only for this specific user**.

Example login:

    ssh naseka@jenkins01-ocp01-shared

When this user logs in, the shell reads:

    /home/ad.ifortuna.cz/naseka/.bash_profile
    /home/ad.ifortuna.cz/naseka/.bashrc

---

# 2. Other users have their own configuration

Each Linux user has a separate home directory and separate shell configuration.

Examples:

| User | Config location |
|-----|-----|
| naseka | /home/ad.ifortuna.cz/naseka/.bashrc |
| jenkins | /home/jenkins/.bashrc |
| root | /root/.bashrc |

These environments are **completely independent**.

---

# 3. Jenkins pipelines usually run as the `jenkins` user

Even if you SSH as:

    naseka

Jenkins builds normally run as:

    jenkins

Therefore anything configured in:

    /home/ad.ifortuna.cz/naseka/.bashrc

**does NOT affect Jenkins builds.**

Example problem:

You add:

    export PATH=/opt/maven/bin:$PATH

to your `.bashrc`.

It works in SSH:

    mvn -version

But Jenkins still fails with:

    mvn: command not found

because Jenkins is using a different user environment.

---

# 4. System-wide shell configuration

Some configuration files apply to all users:

    /etc/profile
    /etc/bashrc
    /etc/environment

These affect every user unless overridden by user-specific files.

---

# 5. Useful commands when debugging Jenkins environment

Check which user Jenkins runs as:

    ps aux | grep jenkins
    id jenkins

Inside a Jenkins pipeline you can inspect the environment:

    sh '''
    echo "USER=$(whoami)"
    echo "HOME=$HOME"
    echo "SHELL=$SHELL"
    env | sort
    '''

---

# Summary

The files:

    .bash_profile
    .bashrc

inside:

    /home/ad.ifortuna.cz/naseka

configure the shell **only for the user `naseka`**, not for Jenkins or other system users.