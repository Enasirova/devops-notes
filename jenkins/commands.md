```bash

docker run hello-world

cd

git clone repolink

cd reponame

ls

cd insall-jenkins-docker

docker compose up -d # -d means detached mode

docker compose down

cat build/computer.txt
#: read  (its eyes that read) and display contents - shows everything inside computer.txt

cat file1.txt file2.txt
# Shows contents of both files, one after another

cat > newfile.txt
# Type content, then press Ctrl+D to save.
#cat reads from stdin (keyboard)
#> redirects output to newfile.txt
#File is created or overwritten (if file already exists, it will entirely overwrite it!)
#Press Ctrl + D → sends EOF (End Of File), saving and exiting

cat >> existing.txt
# Add content to the end of an existing file
#>> appends instead of overwriting

echo #: write text (its a pen), write to a file (overwrite contents of the file > or append >>)

echo "Hello, World!"
# Output: Hello, World!

echo "Mainboard" > build/computer.txt
# Creates or overwrites computer.txt with "Mainboard"

echo "Display" >> build/computer.txt
# Adds "Display" to the end of computer.txt without removing existing content

echo "The filename is: $FILENAME"
# Shows the value of the FILENAME variable

touch build/computer.txt
# Creates file if missing (or updates timestamp):

mkdir -p build
# Creates directory + does not error if it already exists. Useful for pipelines that run repeatedly.


```

```groovy

// in script means its a shell command inside ‘…’
sh

sh ‘’’

put multiple commands to shell liek this

next command

next command

‘’’
```

mkdir foldername: creates a directory

touch parentfolder/newfilename: creates a file

echo “test” >> build/computer.txt: apending text test to the file

mkdir -p bulid: -p parameter says create a directory if it doesnt already exist

post {} same level as stages

alwats{} meaning doesnt matter if success or not

success {} meaning when job is successful

cleanWs() → clean worksspace

archiveArtifacts artifacts: ‘build/\*\*’ → in case of success we save artifacts to build directory folders. artifacts are separated from workshoace. jenkins copies specified files from workspace to its artifact storage, so when cleanup of workspace happens, artifact storage is preserved.

`build/**` means "archive everything inside the build directory and all its subdirectories, no matter how deeply nested."

- `build/*.txt` - Only .txt files directly in the build directory (not in subdirectories)
- `build/**/*.txt` - All .txt files in build and all subdirectories
- `build/**/reports/*.xml` - All .xml files in any "reports" folder under build

PID = Process ID
To see processes command in terminal:

```bash
ps
```

![](images/screenshot-20251225-142831.png)
