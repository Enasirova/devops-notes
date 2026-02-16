# terminal types

VT100 - AS|| - terminal, produced between 1978 and 1983
actual computer would send text to this terminal and the terminal would just display it. 

so it used to be what terminal is right now.

variable `TERM` -> abilities of bash

![](images/screenshot-20260211-102343.png)

there are other terminals:
* text only (no colors)
* etc.
* command `toe -a`to see all of them:

![](images/screenshot-20260211-102440.png)

even this old one is there:

![](images/screenshot-20260211-102531.png)

# terminal escape sequences

`echo -e "\e[30;40m":`

-e means allow specific or escaped characters for ex.
*  \n :"hello\nbash" with -e it will print it ton different lines
* -e: whats following now its not normal text, its escape secence which will change the behaviour of our terminal

![](images/screenshot-20260211-102918.png)

30: black foreground
40: black background
36: cyan foreground
41: red background
there many other colors

`echo -e "\e[36;41m":`

![](images/screenshot-20260211-103059.png)

# additional secequeces

`infocmp`: what your terminal supports (but in reality not all of them):

![](images/screenshot-20260211-103447.png)

here we can see how bold is:

![](images/screenshot-20260211-103715.png)

so we use it in the command:
![](images/screenshot-20260213-150005.png)

to reset:

![](images/screenshot-20260211-103836.png)

but the sequenes have name, so i can use their names with another program `tput`:
![](images/screenshot-20260211-103911.png)



