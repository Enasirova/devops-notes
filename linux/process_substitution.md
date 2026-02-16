/ means we are indicating htat folder is a directory:

![](images/screenshot-20260213-213221.png)

![](images/screenshot-20260213-213703.png)


process substitutuion allowd us to use the input or the output of a process as a temporary file

`<(command)`

`echo <(ls)` 

so we dont need to output contents of the folder to a file and then compair two files, we can compair contents right away via temproary files:

![](images/screenshot-20260213-214137.png)

here we can see temporary files:
![](images/screenshot-20260213-214157.png)

here we can see numnber of lines:
![](images/screenshot-20260213-214315.png)

we also can create a temporary file as the input to a command

`>(command)`

here we redirect test to the temporary file which whill be the input to the command
![](images/screenshot-20260213-214638.png)


