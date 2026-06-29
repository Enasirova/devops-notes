# ping and jobs

```bash
ping -c 25 google.com #Sends 25 ping packets to google.com, then stops and prints packet loss and latency statistics.
jobs #Shows background and stopped jobs in the current shell
wait #Waits for background jobs to finish
wait 123 #waits for process with ID123 (waits till it changes its state)
wait %1 #waits for job with id 1
wait; echo "jobs finished" #will print jobs finished after all jobs are done
fg %1 #Brings job number 1 to the foreground.
bg %1 #Use this when you want to continue interacting with a stopped or background job directly. Resumes stopped job number 1 in the background.
kill %1 #Terminates job number 1.
```

`ctrl + Z => pauses the job`

examples:

```bash
~$ ping -c 30 google.com > /dev/null &
[1] 7187
~$ ping -c 30 google.com > /dev/null &
[2] 7189
~$ ping -c 30 google.com > /dev/null &
[3] 7191
~$ jobs
[1]   Running                 ping -c 30 google.com > /dev/null &
[2]-  Running                 ping -c 30 google.com > /dev/null &
[3]+  Running                 ping -c 30 google.com > /dev/null &
~$ wait
^C
~$ jobs
[1]   Running                 ping -c 30 google.com > /dev/null &
[2]-  Running                 ping -c 30 google.com > /dev/null &
[3]+  Running                 ping -c 30 google.com > /dev/null &
~$ fg %1
bash: fg: job has terminated
[1]   Done                    ping -c 30 google.com > /dev/null
[2]-  Done                    ping -c 30 google.com > /dev/null
[3]+  Done                    ping -c 30 google.com > /dev/null
~$ fg %3
bash: fg: %3: no such job
~$ jobs
~$ ping -c 30 google.com > /dev/null &
[1] 7266
~$ ping -c 30 google.com > /dev/null &
[2] 7268
~$ ping -c 30 google.com > /dev/null &
[3] 7270
~$ ping -c 30 google.com > /dev/null &
[4] 7272
~$ ping -c 30 google.com > /dev/null &
[5] 7276
~$ jobs
[1]   Running                 ping -c 30 google.com > /dev/null &
[2]   Running                 ping -c 30 google.com > /dev/null &
[3]   Running                 ping -c 30 google.com > /dev/null &
[4]-  Running                 ping -c 30 google.com > /dev/null &
[5]+  Running                 ping -c 30 google.com > /dev/null &
~$ fg %5
ping -c 30 google.com > /dev/null
^Z
[5]+  Stopped                 ping -c 30 google.com > /dev/null
~$ jobs
[1]   Done                    ping -c 30 google.com > /dev/null
[2]   Done                    ping -c 30 google.com > /dev/null
[3]   Done                    ping -c 30 google.com > /dev/null
[4]-  Done                    ping -c 30 google.com > /dev/null
[5]+  Stopped                 ping -c 30 google.com > /dev/null
~$ bg %5
[5]+ ping -c 30 google.com > /dev/null &
~$ jobs
[5]+  Running                 ping -c 30 google.com > /dev/null &
~$ 
```

```bash
~$ jobs
[1]   Running                 ping -c 25 google.com > /dev/null &
[2]-  Running                 ping -c 25 google.com > /dev/null &
[3]+  Running                 ping -c 25 google.com > /dev/null &
~$ wait; echo "jobs finished"
[1]   Done                    ping -c 25 google.com > /dev/null
[2]-  Done                    ping -c 25 google.com > /dev/null
[3]+  Done                    ping -c 25 google.com > /dev/null
jobs finished
```

# tput


tput is a command that asks: “What special character sequence should I send to this terminal to do something?”

`tput bold`

![](images/screenshot-20260626-123905.png)

`tput bel` 

```bash
~$ ping -c 50 google.com > /dev/null & 
[1] 25125
~$ wait; tput bel; echo "done" #when command is finished, it will ring a sound and print done
[1]+  Done                    ping -c 50 google.com > /dev/null
done
~$ 
```


# reset

`resest` if you type it in terminal and press enter -> eveyrthing will be cleared and reset to default state

