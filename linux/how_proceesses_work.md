# a process = 
* an instance of a program
* independent execution unit with its own resources:
    * CPU & Memory resources
        * cpu can only work on one process at a time per core. OS gives the cpu to pgogram a for a moment, then to program b etc. so CPU resource means how much time OS scheduler gives CPU to process a program

        * memory = RAM. 
            * space to store variables, program code, stack, heap, loaded libraries.

    * Opened files, network connections
* is managed by the kernel (lowest level of the OS)

each process has:
* process id (pid)
* a user under which this process runs under
* a state (running, waiting, stopped, zombie)
* various other properties

all organized in hierarchy