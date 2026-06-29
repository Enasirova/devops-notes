bash performs word splitting on our input

`touch a.txt b.txt`: here this command will be slitted into 3 different words: touch a.txt and b.txt. the first entry is the program. a.txt first parameter/argument and b.xts is the second parameter/argument

![](images/screenshot-20260213-155400.png)

any character listed in IFS: tab, new line and space
![](images/screenshot-20260213-155506.png)

sequesnces of ifs characters are treated as one delimieter (for ex. two spaces will be united into one)

we can disable word splitting behaviour via wrapping parts of the command into quotes, so we can specify whitespaces in the file names:

touch a file.txt
touch 'a file.txt'
touch "a file.txt"

![](images/screenshot-20260213-155925.png)

```bash
~$ touch 'a.txt b.txt'
```

![](images/screenshot-20260213-160133.png)

