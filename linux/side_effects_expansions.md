
process is always the same:

first, the command is being expanded, then word splitting is applied, qutoes are bing removed, command being executed

`touch '-al'` -> wont work, cause -al is understood as a paramter
this is ok: `touch ./-al`

`ls *`-> prints all files in my directory. bit if ther is file called -al, then ls behaviour would be modifed... 

sometimes file names are understood as parameters!!!

`ls ./*` -> this is good always

always use double quotes in variables! or word splitting will occur!
`touch "${PWD}/file.txt"`

! refer to filenmaes in the same directory as `./file.txt`
! always use the quoting sytle that is as restricve as possible: use sibgle qutoes when possible and if not possible  -> use double quotes. 