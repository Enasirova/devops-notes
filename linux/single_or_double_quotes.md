`cat $PWD/*.txt`: no qiotes -> all available shell expansions are being applied

![](images/screenshot-20260213-163049.png)


single quotes: all expansions are disabled, word splitting is disabled:

`echo '$PWD/*.txt'`

![](images/screenshot-20260213-163216.png)

Double quots: most expansions are disabled. variable explansion worked, but file expansion not worked:

![](images/screenshot-20260213-163331.png)

quotes in bash: only define how bash will expand/split the command, they do nothing else!

![](images/screenshot-20260213-163612.png)

or this: `'echo' 'hello world'`

![](images/screenshot-20260213-163700.png)

here we disabled word splitting:

![](images/screenshot-20260213-163726.png)

