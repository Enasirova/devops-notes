# What is a file?

file - container for storing, accessing and / or managing data

has unique identifier or name. name combined with path = unique location

file has attributes: size, permissions, ownership, timestamps etc

# How is the data stored?

file.txt (referencing inode)

->

inode (referencing actual data on disk): stores metadata (file type, access rihts, number of hardlinks, file sie, last modified date, last access date, where the sdata physically stored?)

-> 

Data on Disk

# Inodes and Files in Unix — Simple Explanation

## Core Idea

In Unix/Linux, a file is actually two things:

1. Data — the contents of the file  
2. Metadata — information about the file

The metadata is stored in a structure called an **inode**.

---

# 1. What an Inode Is

An inode is like a **library card describing a book**.

It stores information about a file such as:

| Metadata in inode | Meaning |
|---|---|
| owner | who owns the file |
| permissions | who can read/write/execute |
| timestamps | when the file was created or modified |
| size | file size |
| pointers | where the file data is stored on disk |

Important:

The inode **does NOT store the filename**.

---

# 2. Where the Filename Is Stored

The filename is stored in the **directory**.

A directory is basically a **table mapping filenames to inode numbers**.

Example directory:

    Documents/

Internally it may look like this:

| filename | inode |
|---|---|
| report.txt | 45123 |
| notes.txt | 45124 |

So the directory connects:

    filename → inode

---

# 3. What Happens When You Open a File

Example command:

    cat report.txt

Steps inside the system:

1. The shell asks the filesystem for the file.
2. The filesystem checks the directory.

    report.txt → inode 45123

3. The kernel reads inode **45123**.
4. The inode contains pointers to disk blocks.
5. The system reads the data from disk.

---

# 4. Visual Structure

    Directory
       |
       | filename → inode number
       v
    inode
       |
       | pointers to disk blocks
       v
    file data

So the chain is:

    filename → inode → data blocks

---

# 5. Why Unix Uses This Design

This design allows features such as **hard links**.

Example:

    fileA
    fileB

Both names can point to the same inode:

    fileA → inode 45123
    fileB → inode 45123

Both filenames reference the same data.

If you delete one:

    rm fileA

The data still exists because:

    fileB → inode 45123

The file disappears only when **all references to the inode are removed**.

---

# 6. How to See Inode Numbers

Use:

    ls -i

Example output:

    45123 report.txt
    45124 notes.txt

The number at the beginning is the **inode number**.

---

# 7. Why a System Can Run Out of Inodes

Sometimes a system shows this error:

    No space left on device

Even though the disk still has free space.

This happens when **all inodes are used**.

It usually occurs when there are **millions of tiny files**.

---

# 8. Simple Analogy

Think of a library:

| Concept | Equivalent |
|---|---|
| directory | index of book titles |
| inode | library card describing a book |
| disk blocks | pages of the book |

Structure:

    title → library card → pages

---

# 9. Key Rule to Remember

In Unix:

    file name ≠ file

The real file is the **inode plus the data blocks**.

The filename is simply a **reference stored in a directory**.

---

# One-Sentence Summary

A filename points to an inode, and the inode points to the actual data on disk.


# How does folder work?

folder 

->

inode: stores metadata


-> 

file.txt


# Files on Unix

everything (almost) is considered a file. for ex. physical drive is a file and it will be represented as a file. 

different kinds of files:

* ordinary files (-)
* directories (d)
* symbolic links (l)
* character device (c)
* block device (b)
* named pipes (p)
* sockets (s)


                1️⃣ Named pipes (p)
                A named pipe is a way for two programs to pass data to each other.
                Think of it like a tube between two processes.
                One program writes into the pipe, the other reads from it.
                Program A  --->  pipe  --->  Program B
                Example
                Create one:
                mkfifo mypipe
                Now check:
                ls -l
                You will see:
                prw-r--r--  mypipe
                The p means pipe.
                How it works
                Terminal 1:
                cat mypipe
                Terminal 2:
                echo hello > mypipe
                Output in terminal 1:
                hello
                So the pipe passed the message between processes.
                2️⃣ Sockets (s)
                A socket file is used for two programs talking to each other, usually on the same machine.
                Many system services use them.
                Example:
                Docker
                PostgreSQL
                MySQL
                systemd
                They create socket files in the filesystem.
                Example:
                /var/run/docker.sock
                Check:
                ls -l /var/run/docker.sock
                You might see:
                srw-rw---- docker.sock
                s means socket.
                How sockets work
                They allow processes to exchange data like this:
                Program A <----> socket <----> Program B
                Unlike pipes, sockets allow two-way communication.
                3️⃣ Key difference
                Feature	Named pipe	Socket
                Direction	one-way	two-way
                Usage	simple process communication	services and daemons
                Created with	mkfifo	created by programs
                Example	bash pipelines	Docker, databases
                4️⃣ Why they appear as files
                Unix has a design principle:
                Everything is a file
                So communication endpoints also appear as files.
                Examples:
                /var/run/docker.sock
                /tmp/mysql.sock
                Programs connect to these files to communicate.
                5️⃣ Example real-world usage
                Docker CLI:
                docker ps
                Actually talks to:
                /var/run/docker.sock
                The CLI sends commands through the socket to the Docker daemon.

we can show the type of a file with the ls command: `ls -l [folder / file]` -> the type then shows up as the firstr character of the first column

![](images/screenshot-20260216-103751.png)

first letter (in my case dash it will be ordinary file):

```bash
~$ ls
'a file.txt'  'a.txt b.txt'   bin       Documents   echo      folder2               foler2_contentx.tx   Pictures   Templates
'a folder'    'b file.txt'    Desktop   Downloads   folder1   foler1_contentx.txt   Music                Public     Videos
~$ ls -l
total 12
-rw-r--r--. 1 naseka naseka    0 Feb 13 15:57 'a file.txt'
drwxr-xr-x. 2 naseka naseka    6 Feb 13 16:34 'a folder'
-rw-r--r--. 1 naseka naseka    0 Feb 13 15:59 'a.txt b.txt'
-rw-r--r--. 1 naseka naseka    0 Feb 13 15:58 'b file.txt'
drwxr-xr-x. 2 naseka naseka   61 Feb  5 14:20  bin
drwxr-xr-x. 2 naseka naseka    6 Jan 29 21:18  Desktop
drwxr-xr-x. 5 naseka naseka 4096 Feb 13 15:35  Documents
drwxr-xr-x. 2 naseka naseka    6 Jan 29 09:53  Downloads
-rw-r--r--. 1 naseka naseka    0 Feb 13 16:22  echo
drwxr-xr-x. 2 naseka naseka   61 Feb 13 17:15  folder1
drwxr-xr-x. 2 naseka naseka   61 Feb 13 17:16  folder2
-rw-r--r--. 1 naseka naseka   34 Feb 13 17:19  foler1_contentx.txt
-rw-r--r--. 1 naseka naseka   34 Feb 13 17:19  foler2_contentx.tx
drwxr-xr-x. 2 naseka naseka    6 Jan 29 09:53  Music
drwxr-xr-x. 2 naseka naseka    6 Jan 29 09:53  Pictures
drwxr-xr-x. 2 naseka naseka    6 Jan 29 09:53  Public
drwxr-xr-x. 2 naseka naseka    6 Jan 29 09:53  Templates
drwxr-xr-x. 2 naseka naseka    6 Jan 29 09:53  Videos
~$ 
```
to also show hidden files: `ls -la`




