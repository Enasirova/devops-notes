# You can chain multiple commands

![](images/screenshot-20260202-160905.png)

![](images/screenshot-20260202-161048.png)

# practicing:

![](images/screenshot-20260202-163925.png)

# tee

with combination of a pipe and the tee you can create standard output and write it to a file at the same time

![](images/screenshot-20260202-164226.png)

![](images/screenshot-20260202-164345.png)

![](images/screenshot-20260202-164439.png)

# ping program
ping checks whether another computer is reachable over the network and how fast it responds.

ping = “Are you there, and how long do you take to answer?”

![](images/screenshot-20260202-164605.png)

![](images/screenshot-20260202-164821.png)

![](images/screenshot-20260202-164920.png)

# sort

sort does NOT modify the original file.
It reads the file, sorts the lines, and prints the result to stdout.

sorts by default in alphabetical order

![](images/screenshot-20260202-165218.png)

![](images/screenshot-20260202-165343.png)

here it will be sorted by last name:

![](images/screenshot-20260202-165414.png)

# unique

here we removed dupliccates: if we dont sort data, then we will that duplicates are not removed. unique only rmeoves duplicates which are next to each other.

![](images/screenshot-20260202-165511.png)

or i could use sort -u

![](images/screenshot-20260202-165621.png)

sometimes we dont want to remove duplicates, we just want to know duplicates

here we listed duplicate entries:

![](images/screenshot-20260202-165717.png)

# grep

by default its using basic regular expressions

for now we disable regular expressions for now via `-F` parameters

## grep and binary data

we should not use grep for binary

![](images/screenshot-20260202-205313.png)


# ip addr show

Shows all network interfaces on the system and their IP addresses.

![](images/screenshot-20260203-131346.png)

![](images/screenshot-20260203-131411.png)

![](images/screenshot-20260203-131428.png)



![](images/screenshot-20260202-205517.png)

![](images/screenshot-20260202-205552.png)

# strings

## tr = translate

replace characters:

![](images/screenshot-20260202-205754.png)

![](images/screenshot-20260202-205827.png)

![](images/screenshot-20260202-210005.png)

![](images/screenshot-20260202-210301.png)

## rev
reversed order of characters

![](images/screenshot-20260202-210336.png)


# cut: it cuts the string based on the pipe position
allows to process and extract data from a file or standard input
`cut -b`

## uptime

![](images/screenshot-20260203-132029.png)

![](images/screenshot-20260202-210452.png)

first 10 bites will be shown:

![](images/screenshot-20260202-210526.png)


-b = bytes
-c = characters. some characters have 2 bytes (diactrics for ex)

## cutting by fields

`-d` is a delimeter parameter

here we can see that first field is blank asecond field has time

![](images/screenshot-20260203-132341.png)


![](images/screenshot-20260202-210915.png)

![](images/screenshot-20260202-210956.png)

# sed

sed allows us to easily execute commands on a file or on sdin

they can: delete or insert lines; replace lines or part of lines

`s/[pattern]/[replacement]/[flags]:`

`-g` -> all characters. 


![](images/screenshot-20260202-211244.png)

# task: find data in the log file

i ran wget command to have the log file in my vm

then this chaining of commands answered the task.

command:
![](images/screenshot-20260203-134600.png)

questions which were answered:

![](images/screenshot-20260203-134619.png)


