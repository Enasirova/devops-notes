# verify user exists
```bash
id jenkins # expected: uid=503(jenkins) gid=20(staff) groups=20(staff)
```

```bash
dscl . -list /Users | grep jenkins # grep for jenkins
dscl . -read /Users/jenkins # inspect user details - This shows UID, home directory, shell, etc.
```


# verify home directory

```bash
ls -ld /Users/jenkins # expected: drwxr-xr-x  ... jenkins staff ... /Users/jenkins
```

# verify ssh directory

```bash
ls -la /Users/jenkins/.ssh # expected: authorized_keys
```

## permissions should be strict

```bash
ls -ld /Users/jenkins/.ssh # expected: drwx------  jenkins staff
```

# verify authorized key exists

```bash
cat /Users/jenkins/.ssh/authorized_keys # You should see the public key from the Jenkins controller. Expected: ssh-ed25519 AAAAC3... jenkins-controller
```

# verify permissions (very important for ssh)

```bash
ls -l /Users/jenkins/.ssh # expected: -rw------- authorized_keys
```

## correct permissions:

            /Users/jenkins           755
            /Users/jenkins/.ssh      700
            authorized_keys          600


# Verify Shell login works locally

```bash
ssh jenkins@localhost # expected: The authenticity of host 'localhost' can't be established password for jenkins: OR Enter passphrase for key:
```

wrong: 

            Permission denied

            Unknown user jenkins


## verify ssh enabled on the mac:

```bash
sudo systemsetup -getremotelogin
```

# final test from Jenkins controller

```bash
ssh -i jenkins_key jenkins@MAC_IP
```

# common agent problems:

1️⃣ .ssh permissions wrong
2️⃣ authorized_keys owned by wrong user
3️⃣ home directory missing
4️⃣ SSH disabled in macOS settings
5️⃣ wrong username used in Jenkins node config


# Jenkins SSH Troubleshooting — authorized_keys Ownership (Mac)

A very common SSH problem when connecting Jenkins to a Mac agent is wrong ownership of the `authorized_keys` file.

SSH is extremely strict about file ownership and permissions.  
If the ownership is wrong, SSH will ignore the file completely and authentication will fail.

---

## 1. Verify ownership of authorized_keys

Run on the Mac:

ls -l /Users/jenkins/.ssh

Correct output should look similar to:

-rw-------  1 jenkins  staff  ... authorized_keys

Important columns:

| Column | Expected |
|------|------|
| Owner | jenkins |
| Group | usually staff |

---

## 2. Verify `.ssh` directory ownership

Run:

ls -ld /Users/jenkins/.ssh

Expected output:

drwx------  jenkins staff ...

---

## 3. Verify home directory ownership

Run:

ls -ld /Users/jenkins

Expected output:

drwxr-xr-x  jenkins staff ...

---

## 4. Fix ownership (if incorrect)

If you see something like:

root staff authorized_keys

then fix it with:

sudo chown jenkins:staff /Users/jenkins/.ssh/authorized_keys

Often it is safest to fix the entire directory:

sudo chown -R jenkins:staff /Users/jenkins/.ssh

---

## 5. Verify permissions (SSH also checks this)

Run:

ls -ld /Users/jenkins/.ssh  
ls -l /Users/jenkins/.ssh

Correct permissions should be:

| Path | Permission |
|-----|-----|
| /Users/jenkins | 755 |
| /Users/jenkins/.ssh | 700 |
| authorized_keys | 600 |

If necessary, fix them:

chmod 700 /Users/jenkins/.ssh  
chmod 600 /Users/jenkins/.ssh/authorized_keys

---

## 6. Why SSH rejects wrong ownership

SSH protects against situations where another user could modify authentication keys.

Example risk scenario:

authorized_keys owned by root or another user

Someone else could add their own key and log in as `jenkins`.

Therefore SSH enforces:

authorized_keys must be owned by the target user

---

## 7. Useful debugging command

If SSH login fails, run on the Mac while attempting connection:

sudo log stream --predicate 'process == "sshd"'

You may see messages like:

Authentication refused: bad ownership or modes for file

This immediately indicates permission or ownership problems.

---

## Summary

Most Jenkins → Mac SSH connection problems come from:

- wrong file owner
- wrong file permissions
- incorrect home directory configuration
- missing `.ssh` directory
- SSH service disabled

Correct setup ensures Jenkins can authenticate using the key stored in:

/Users/jenkins/.ssh/authorized_keys