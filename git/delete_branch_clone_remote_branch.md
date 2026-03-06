

```bash
naseka@CZMB94D536 jacktime-service % git branch
* jenkins_test
  master
naseka@CZMB94D536 jacktime-service % git checkout master # switch away from the branch you are going to delete
Switched to branch 'master'
Your branch is up to date with 'origin/master'.
naseka@CZMB94D536 jacktime-service % git branch
  jenkins_test
* master
naseka@CZMB94D536 jacktime-service % git branch -d jenkins_test # delete branch locally
Deleted branch jenkins_test (was 61bdf2b).
naseka@CZMB94D536 jacktime-service % git branch
* master
naseka@CZMB94D536 jacktime-service % git branch -r                  # view all remote branches                        
  origin/AI-test-code-review
  origin/HEAD -> origin/master
  origin/SB-4698-jt-filter-withoutodds-unnecessary-calls-on-updateprices
  origin/feature/DEV-92791-jts-client-side-load-balancer-on-betcore-client
  origin/feature/SB-2732-Old_offer_cleaning
  origin/master
  origin/sonar
naseka@CZMB94D536 jacktime-service % git checkout -b jenkins_test origin/AI-test-code-review  # cloned remote branch to new local branch          
branch 'jenkins_test' set up to track 'origin/AI-test-code-review'.
Switched to a new branch 'jenkins_test'
naseka@CZMB94D536 jacktime-service % git status
On branch jenkins_test
Your branch is up to date with 'origin/AI-test-code-review'.

nothing to commit, working tree clean
naseka@CZMB94D536 jacktime-service % git branch
* jenkins_test
  master
naseka@CZMB94D536 jacktime-service % git push -u origin jenkins_test # pushed new local branch
Total 0 (delta 0), reused 0 (delta 0), pack-reused 0 (from 0)
remote: 
remote: Create pull request for jenkins_test:
remote:   https://app-bitbtest-shared.o.dc1.cz.ipa.ifortuna.cz/projects/MW/repos/jacktime-service/pull-requests?create&sourceBranch=refs%2Fheads%2Fjenkins_test
remote: 
To https://app-bitbtest-shared.o.dc1.cz.ipa.ifortuna.cz/scm/mw/jacktime-service.git
 * [new branch]      jenkins_test -> jenkins_test
branch 'jenkins_test' set up to track 'origin/jenkins_test'.
naseka@CZMB94D536 jacktime-service % 

```