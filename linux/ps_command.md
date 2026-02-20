ps = process status

alsmost same on mac and linux

`ps` on linux:

* shows all processes of current. terminal (tty)

![](images/screenshot-20260219-163202.png)

if we open extra bashe:

![](images/screenshot-20260219-163242.png)

how to displa ALL processes 

`ps -A` or `ps -e` processes from all user all sessions

`ps -f` switch to full format listing (table with also user, terminal and parent process PPID)

![](images/screenshot-20260219-163505.png)

`ps -p 1234 1235` only shows 1234 and 12345

![](images/screenshot-20260219-163746.png)


`ps --forest` to see the process tree:

![](images/screenshot-20260219-163943.png)

`ps -l` show in long format:

![](images/screenshot-20260219-164013.png)

