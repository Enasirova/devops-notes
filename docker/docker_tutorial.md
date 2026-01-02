Basic concepts of OS:
Ubuntu, Edora etc

they all have OS Kernel (Linux)
you have common Kernel across all OSs. So OSs share Kernel.
Docker can run all OSs if they are all have Linux Kernel.

What OS doesnt share same Kernel: Windows. You require Docker on Windows server.
If you run Linux container on Windows (in reality its Linux container on Linux VM).

Containers vs Virtual Machines:
Hardware Infrastracture -> OS -> Docker -> Container (with app, lib and deps in every container) Container Container

VM:
Hardware Infrastructure -> Hypervisor -> VM (application, libs, deps, os) VM VM

VMs take ages to boot.

When you have large environments we can benefit from VMs and Containers.

Docker Hub = public repository, i
