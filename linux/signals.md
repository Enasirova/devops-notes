# what are the signals

signals can be sent to processes and they will interrupt the process flow at a convenient time

but you can imagine it will interrupt process immediately

Its a mechanism to asynchroniously notify a process of an event

the OS: is responsible for delivering the signal to the processs; maintains a signal queue, so we can send a signal to every process


# what kind of messages/signals are there?

* terminal source (example: SIGINT -> means Interrupt the process)
* shell (example: SIGHUP -> means Hangup the processs. for ex. i have download ongoing download in terminal and i clsoe the terminal)
* window manager (example SIGWINCH -> window change)
* other: (SIGTERM termomate process)
* Kernel (SIGKILL = kill process, SIGILL = illegal instruction)

# SIGINT

for ex. we start slow download in terminal. if i try to type, i am still inside download program. i am not in controle of my terminal. SIGING -> would say to a program "hey i want you to stop". Program might still ignore the signal though

CTRL+C = this has sent SIGING signal to wget program which was downloading. Then wget finished smth necessary and stopped gracefully. 

