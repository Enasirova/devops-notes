set command: provides pre-defined config options that we can use

to enable: `set -[feature]`

to disable: `set +[feature]`

features:

* set -x: useful for ex. to see whats behind the alias 
    * xtrace: enables that each command that the shell executes will be printed (allows to debug commands)

    ![](images/screenshot-20260210-194146.png)


    to disable the feature:

    ![](images/screenshot-20260210-194316.png)


# shopt command

* to enable config optoins: 

`shopt -s [optname]`

* to disable config option:

`shopt -u [optname]`

`autocd`:

![](images/screenshot-20260210-195108.png)

`cdspell`:

![](images/screenshot-20260210-195335.png)