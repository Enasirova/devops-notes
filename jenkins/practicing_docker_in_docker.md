readme.md -> clean up needed

docker in docker: we have limited 

![](images/screenshot-20260120-093452.png)

we dont know how to test it 
how we test:

![](images/screenshot-20260120-093548.png)

this is just to test that it connects to the container and runs docker run, which prints this message

![](images/screenshot-20260120-093654.png)

developers use it with suit code test containers: it works directly with docker api

we can also run this command to see docker is running:
![](images/screenshot-20260120-093923.png)

## chat gpt explanation till 13:00 min video

![](images/screenshot-20260120-100622.png)
![](images/screenshot-20260120-100804.png)
![](images/screenshot-20260120-100846.png)

## back to roman:

traditioannly: 

![](images/screenshot-20260120-101202.png)

docker communicates with docker daemon via this socket


when ever someone uses docker -> it goes to the local host instead of that docker.socket

![](images/screenshot-20260120-101343.png)


Practice:

ideally in the future: with jnlp, docker in docker and buildah

![](images/screenshot-20260120-101444.png)

in jenkins file to set up instead of now *latest:*" they are aset up as date:

![](images/screenshot-20260120-101654.png)

buildah also:

![](images/screenshot-20260120-101746.png)

and you wil see how you will want to do it

in this example of docker in docker i have created folders with version:
![](images/screenshot-20260120-101839.png)

i think its much easier to do it by hand, because we updated it once a year and becuase a lot of teams depend on it

in readme it will be better to make tags with version prefix, day , month and year:

![](images/screenshot-20260120-102059.png)

and then what he would do in jenkins we almost everywhere have latest, but for ex. creating docker in docker version 28 instead of latest i would put this tag:

![](images/screenshot-20260120-102329.png)

here: 
![](images/screenshot-20260120-102352.png)



because then when you  try to make another builds with other versions ->  they will not collide with the same tag because its always latest. that was the problem, cause we had everywhere latest, so when ever you rebuild it automatically replaces the original. normnally it doesbnt nmatter (in majority of agents). but now it looks like there are some changes in docker which affected test containers.

and even in buildah: new version works little bit different with file system. i dont know why yet. space for experimentionas

You want me to check the pipeline to verify format of writing to check the 3 agents: jnlp

check pipelines in jenkins to verify format: only docker in docker, buildah and jnlp

I still dont have access to quay:

this is ours:

![](images/screenshot-20260120-105230.png)

here is current documentation:

![](images/screenshot-20260120-105315.png)

the hardest thing: to find out stable version

thats how i found it: 

![](images/screenshot-20260120-105420.png)

![](images/screenshot-20260120-105519.png)

this is how i found it:

![](images/screenshot-20260120-105556.png)

i dont think it can be automated.

what can be automated: its to always build latest version. 

but now when we needed to go back, they are already at the version 29 and thats the problematic version. so i had to find latest version of 28

![](images/screenshot-20260120-105808.png)

![](images/screenshot-20260120-105924.png)

then he needed to switch to this revision: 

![](images/screenshot-20260120-110039.png)

then he ran vim:

![](images/screenshot-20260120-110125.png)

now we read docker file of that version:

![](images/screenshot-20260120-113514.png)


from here we are taking version number, cause they have 3 components:

![](images/screenshot-20260120-113919.png)

env docker_version 25.5.2
env docker_buildx_version 0.29.1
env docker_compose_version 2.40.3

so he took those 3 and put them to this dockerilfe:

![](images/screenshot-20260120-113739.png)

this is (i guess docker file content) same as this:

![](images/screenshot-20260120-114005.png)

but they use alpine linux as base image and it works in openshift, but what is supported is to use ubi images:

alpine (file we read in vim):

![](images/screenshot-20260120-114134.png)

ubi images in our dockerifle:

![](images/screenshot-20260120-114235.png)

alpine doesnt have some basic commands. for ex. they dont have TAR command

TAR: command for compressing and decompressing files

![](images/screenshot-20260120-114418.png)

because of this command oc cp doesnt work (it allows you to copy files from local computer into running pod and other directrion. thats why we change it to ubi 9

![](images/screenshot-20260120-114652.png)

and whats happening inside docker file:
![](images/screenshot-20260120-114747.png)


he removed:

there are here testing for different architectures (x86 64 arm etc):

![](images/screenshot-20260120-114820.png)



but we always just run on x86-64:

![](images/screenshot-20260120-114948.png)

but for mac computers.. when you will be building in future docker in docker you need to use switch commands. you need to specify that you want to build image for 86 64 cause otherwise it by default builds for ARM. thats the problem, cause when you uppload it open shift cannot use this image

![](images/screenshot-20260120-115408.png)


here in help for biuld options we can see 

--arch string:

![](images/screenshot-20260120-115511.png)

then he googled for red hat docs and found this:

![](images/screenshot-20260120-115611.png)

another tip: if you want to work with mac in containers (dont use docker desktop)

OrbStack is better than docker desktop for mac.


create new version: docker in docker 28.