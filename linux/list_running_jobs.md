# how to have overview of background jobs

`jobs` it will print all the jobs that are currently running

we are discarting out put here to /dev/null:

![](images/screenshot-20260224-101749.png)

# how to bring background job to foreground

`fg [%job-ID]`

if its just one job, then `fg` is enough

![](images/screenshot-20260224-102012.png)

if i just run fg -> i will put + command will be in hte foreground:

![](images/screenshot-20260224-102112.png)

![](images/screenshot-20260224-102204.png)

# important

only foreground jobs can receive keyvoard input

also means htat ctrl c will not work for background jobs (so signals will not work for backgorund jobs)

