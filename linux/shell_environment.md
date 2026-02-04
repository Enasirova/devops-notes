# what is a shell

shell = outer layer of operating system

it takes commands from the user and translates them into a form tha tthe kernel can understand

it can  also display things to the terminal


CLI = tex based interface that allows users to interact with systems by typing commands

often shell refers to cli

linux shell = text base interfaces, that allows us to work on devices that dont support a gui

so we usually mean terminal is a shell

# Environment variables

used to store config info and settings

they influence the shell and program behaviour

convention: env variables are written in uppercase letters

env variables vs bash variables (not written in uppercase)

list env variables

`env`:

![](images/screenshot-20260203-184434.png)

`echo "${PWD}"`

$ means to access the variable

we put doublequotes 

![](images/screenshot-20260203-184637.png)

better with curly braces, to make sure to see whats exactly variable is:

![](images/screenshot-20260203-184742.png)


# HOME variable

stores the current user's home directory path
![](images/screenshot-20260203-185052.png)

# PWD

# OLDPWD (old working directory)

# USER
![](images/screenshot-20260203-185248.png)

# set env variables

`export VAR=value`

![](images/screenshot-20260203-185700.png)

# rewrite the variable

`variablename='new value'`

important!! no spaces around =

whitespace matters in bash

![](images/screenshot-20260203-190152.png)

# Delete env variable

`unset VAR`

for ex. i created city variable 

![](images/screenshot-20260203-190619.png)

and then removed it:

![](images/screenshot-20260203-190636.png)