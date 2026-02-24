killall firefox -> will find all processes called firefox and kill them

killall -s [SIGNAL] firefox

killall -s SIGINT firefox

# on mac:

`killall -s SIGINT firefox` -> will not work

`killall -SIGINT firefox` -> it will work
