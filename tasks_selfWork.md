# INFRA-40058

Tasks which are no longer running in Jenkins or on node macosbuild3 are still blocking executors.

What usually fixes it (high level)
One of these:
Disconnect + reconnect the node
Restart the agent
Restart Jenkins
“Kill” stuck executors in Jenkins UI
⚠️ This is NOT a pipeline or code problem.
It’s node / executor state corruption.

Out of all transcripts you shared, ONLY ONE is directly relevant.

Node = computer
Executor = how many jobs that computer can run at the same time

# Docker in Docker agent
at the end should be version tag, not :latest
cause there was a problem when there is more than one