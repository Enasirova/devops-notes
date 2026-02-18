# inmportant ddeices pseudo

## /dev/null

when reading returns EOF (end of file)
when writing Discards the information

output is discarded:

![](images/screenshot-20260217-101209.png)

just shows end of file (nothing orinted):

![](images/screenshot-20260217-101300.png)

## /dev/random

produces a stream of random numbers
only produces random data, as long as enough "environmental noise" is available

the idea is that we have pure random data, but they are not generated randomly, its data from us

this can run forever:

![](images/screenshot-20260217-101438.png)

then we can run this comand to show first 10 lines of dev/random (binaries) - this is used for cryptographic opearations:

![](images/screenshot-20260217-101556.png)

## /dev/urandom


it will always produce data (if no more environmental noise available -> it will produce data), otherwise same as random

## /dev/stdin, /dev/stdout, /dev/stderr

input, output, errors

![](images/screenshot-20260217-101754.png)

when ever we use for ex echo comand -> it will use stdout file, then it makes sure its shown on my terminal. even if we dont explicitly say > /dev/stdout -> it will still send data there technically when ever output is shown on the terminal


