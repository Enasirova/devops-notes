Fedora (not super stable yet) -> CentOS Stream -> RHEL (extremely stable and reliable)

CentOS Stream is a rolling preview of RHEL

RHEL = red hat enterprise linux

# .rpm package format

.rpm its arichive that contain all the files and configuration required to install a software 

we could download .rpm files manually for ex. from https://mirror.stream.centos.org

* we will see file folder for our centos version (for ex. 9-stream/) -> we go inside and we see many folders:

![](images/screenshot-20260303-134612.png)


either AppStream/ or BaseOS/

we go to BaseOS/ -> 

![](images/screenshot-20260303-134642.png)

ours is aarch64:
![](images/screenshot-20260303-134704.png)


os -> repodata/ (in this folder we have all files our package manager can download)

![](images/screenshot-20260303-134803.png)

but we need individual packages in os -> packages. here we will see individual rpm packages:

![](images/screenshot-20260303-134849.png)


we click on zsh-5.8-9:

![](images/screenshot-20260303-134957.png)


# to inspect rpm package?

`rpm -qpl [file].rpm`

we do everything in centos vm (browser, download etc and the terminal):

![](images/screenshot-20260303-135538.png)

then we could see all those files would be installed on our system if we installed that file.

why rpm -qpl? use helper: rpm --help

![](images/screenshot-20260303-135747.png)

# if we want to isntall

`rpm -i [file]`


![](images/screenshot-20260303-135928.png)


# if we want to uninstall

here we ran into dependancy issue
![](images/screenshot-20260303-140014.png)


how to inunstall

![](images/screenshot-20260303-140036.png)






but in general we should avoid downloading manually

