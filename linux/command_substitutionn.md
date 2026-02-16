allows to execute the command and use the output as an expansion 
`echo "$(cat file.txt)"` it will execute a subshell (new bash process) and collect the output for another command

single quotes dont work with substitution

the size of my home directory: `echo 'The size of my home directory is:'"$(du -sh ~")`

backtick `

