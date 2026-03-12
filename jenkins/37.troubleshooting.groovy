/**
* Web2 Fortunaweb-fe Jenkins deployment pipeline for all environments
*/

fileValues = []
namespace = "web2"

secrets = [
    [ path: 'jenkins/web', secretValues: [[ envVar: 'WEB2_DOCKER_CONFIG', vaultKey: 'docker-config-new' ]]],
    [ path: 'jenkins/web', secretValues: [[ envVar: 'WEB2_K8S_CONFIG', vaultKey: 'kubeconfig-decode' ]]],
    [ path: 'jenkins/web', secretValues: [[ envVar: 'WEB2_K8S_CONFIG_BAREMETAL', vaultKey: 'kubeconfig_bm_decode' ]]]
]

countriesToDeployOcp4 = []
applicationsToDeploy = [:]

brandsMovedToBM = [
    [brand: 'CZ', env: 'stg'],
    [brand: 'CZ', env: 'prd'],
    [brand: 'SK', env: 'stg'],
    [brand: 'SK', env: 'prd'],
    [brand: 'RO', env: 'stg'],
    [brand: 'PL', env: 'stg'],
    [brand: 'CP', env: 'stg'],
    [brand: 'HR', env: 'stg'],
    [brand: 'RO', env: 'prd'],
    [brand: 'CP', env: 'prd'],
    [brand: 'PL', env: 'prd'],
    [brand: 'CZ', env: 'tst'],
    [brand: 'RO', env: 'tst'],
    [brand: 'SK', env: 'tst'],
    [brand: 'PL', env: 'tst'],
    [brand: 'CP', env: 'tst'],
    [brand: 'HR', env: 'tst'],
    [brand: 'HR', env: 'prd']

]

def translateClusterToConfig(cluster) {
    switch(cluster) {
        case "dev": return 'dev'
        case "tst": return 'test'
        case "stg": return 'stage'
        case "prd": return 'prod'
    }
}

static def translateClusterToConfigGoLivePlugin(cluster) {
    switch(cluster) {
        case "dev": return 'dev'
        case "tst": return 'tst'
        case "stg": return 'stg'
        case "prd": return 'prod'
    }
}

def isMovedToBM(brand, env) {
    return brandsMovedToBM.find { it.brand == brand && it.env == env } != null
}

def setupKubeconfig(isBmForCountry) {
    echo "Setting up clusters access configuration Using Bare Metal: ${isBmForCountry}"
    withVault(vaultSecrets: secrets) {
        if (isBmForCountry) {
            sh 'echo "${WEB2_K8S_CONFIG_BAREMETAL}" > ./k8sconfig'
        } else {
            sh 'echo "${WEB2_K8S_CONFIG}" > ./k8sconfig'
        }
    }
}

def runDeployJobs(appName, appCode, imageName, appHelmPath, configDirs, microFrontendName) {
    if (countriesToDeployOcp4.size() > 0) {
        for (country in countriesToDeployOcp4) {
            def isBmForCountry = isMovedToBM(country, params.TARGET_CLUSTER) // Check if country/env for BM migration

            sh "echo Cleaning up images at OCP4"
            setupKubeconfig(isBmForCountry)

            cleanupImages(true, appName, appCode, countriesToDeployOcp4[0], isBmForCountry)
        }
    }
    for (country in countriesToDeployOcp4) {
            def isBmForCountry = isMovedToBM(country, params.TARGET_CLUSTER) // Check if country/env for BM migration
            setupKubeconfig(isBmForCountry)

            sh "echo Deploying to ${country} OCP4 BareMetal: ${isBmForCountry}"
            deployJob(appName, appCode, imageName, country, namespace + '-' + country.toLowerCase(), namespace, appHelmPath, configDirs, microFrontendName, true, isBmForCountry)
        }
}

def deployJob(appName, appCode, imageName, country, deployNamespace, imageNamespace, appHelmPath, configDirs, microFrontendName, isOcp4, isBmForCountry) {
    def targetCluster = getTargetCluster(isOcp4, country, isBmForCountry)
    def targetContext = getTargetContext(deployNamespace, targetCluster)

    switchOcpContext(namespace, deployNamespace, targetCluster, targetContext)
    importImage(namespace, appCode, imageName, imageNamespace, targetContext)
    runHelm(appName, appHelmPath, targetContext, deployNamespace, configDirs, isOcp4, country, microFrontendName)
}

def cleanupImages(isOcp4, appName, appCode, country, isBmForCountry) {
    def targetCluster = getTargetCluster(isOcp4, country, isBmForCountry)
    def deployNamespace = namespace + '-' + country.toLowerCase()
    def targetContext = getTargetContext(deployNamespace, targetCluster)
    switchOcpContext(namespace, deployNamespace, targetCluster, targetContext)

    def imagesToKeep = 30
    sh(script: "oc --namespace ${namespace} describe is ${appCode} | grep -E '^[0-9]+[.][0-9]+[.][0-9]+-' | sed 's/.*://' | sort -n -r | tail -n+${imagesToKeep} | xargs -I {} oc --namespace ${namespace} tag -d ${appCode}:{}", returnStatus: true)
}

def getTargetCluster(isOcp4, country, isBmForCountry) {
  def targetCluster = params.TARGET_CLUSTER == "prd" ? country.toLowerCase() + "prd" : params.TARGET_CLUSTER
  if (isOcp4) {
    if (params.TARGET_CLUSTER == "stg" && !isBmForCountry) {
        targetCluster = targetCluster + "2"
    }
        targetCluster = targetCluster + "-ocp4"
  }
  if (isBmForCountry) {
    targetCluster = targetCluster + "-bm"
  }
  return targetCluster
}

def getTargetContext(deployNamespace, targetCluster) {
    return "${deployNamespace}/${targetCluster}" as Object
}

def switchOcpContext(namespace, deployNamespace, targetCluster, targetContext) {
  try {
    echo "Set context"
    sh "oc config use-context ${targetContext}"
  } catch (Exception e) {
    echo 'Exception occurred when setting context: ' + e.toString()
    echo "Trying to use default account and switch namespace"
    sh "oc config use-context ${namespace}-cz/${targetCluster}"
    sh 'oc config set-context $(oc config current-context)' + " --namespace=${deployNamespace}"
  }
}

def importImage(namespace, appCode, imageName, imageNamespace, targetContext) {
    echo "Import image"
    def importImageCommand = "oc import-image ${namespace}/${appCode}:${env.IMAGE_TAG} --from registry.svc.ifortuna.cz/${imageNamespace}/${imageName}:${env.IMAGE_TAG} --namespace=${namespace} --reference-policy=local --confirm"
    if (params.TARGET_CLUSTER == "prd") {
        importImageCommand = importImageCommand + " --context ${targetContext}"
    }
    sh script: importImageCommand
}

def runHelm(appName, appHelmPath, targetContext, deployNamespace, configDirs, isOcp4, country, microFrontendName) {
    def helmDeployConfigs = []
    def releaseName = appName
    if (microFrontendName) {
        releaseName = releaseName + '-' + microFrontendName
    }
    def helmCommand = 'helm upgrade ' + releaseName + ' ' +  appHelmPath + ' --atomic --install --wait --history-max 10'
    if (params.TARGET_CLUSTER == "prd") {
        helmCommand = helmCommand + " --kube-context ${targetContext}"
    }
    if (isOcp4) {
       helmCommand = helmCommand + " --set image.registry=image-registry.openshift-image-registry.svc:5000"
    } else {
       helmCommand = helmCommand + " --set image.registry=docker-registry.default.svc:5000"
    }
    helmCommand = helmCommand + " --set image.tag=${env.IMAGE_TAG}"
    for (i in configDirs) {
        if (fileExists(i + '/all.yaml')) {
            helmDeployConfigs.push(i + '/all.yaml')
        }
        if (fileExists(i + '/' + translateClusterToConfig(params.TARGET_CLUSTER) + '.yaml')) {
            helmDeployConfigs.push(i + '/' + translateClusterToConfig(params.TARGET_CLUSTER) + '.yaml')
        }
        if (fileExists(i + '/' + country.toLowerCase() + '/all.yaml')) {
            helmDeployConfigs.push(i + '/' + country.toLowerCase() + '/all.yaml')
        }
        if (fileExists(i + '/' + country.toLowerCase() + '/' + translateClusterToConfig(params.TARGET_CLUSTER) +'.yaml')) {
            helmDeployConfigs.push(i + '/' + country.toLowerCase() + '/' + translateClusterToConfig(params.TARGET_CLUSTER) + '.yaml')
        }
    }

    helmCommand = helmCommand + ' --namespace ' + deployNamespace
    helmCommand = helmCommand + ' --values ' + helmDeployConfigs.join(",")
    if (fileValues) {
        helmCommand = helmCommand + ' --set-file ' + fileValues.join(",")
    }

    if (params.DRY_RUN) {
        helmCommand = helmCommand + ' --dry-run'
    }

    helmCommand = helmCommand + ' --timeout 10m'

    echo helmCommand
    sh script: helmCommand
}

def goLivePlugin(appName, country, isOcp4, owner) {
    if (!params.DRY_RUN) {
        env.APW_APPLICATION = appName+'-'+ country
        try {
            apwSetEnvironmentStatus status: 'Deploy'
        } catch (err) {
            echo "GoLive plugin integration failed [$err]"
        }
        try {
            def body = [
                attributes: [
                Owner: owner
                ]
            ]
            apwUpdateEnvironment body: body
            apwSetDeployedVersion version: params.IMAGE + (isOcp4 ? "-ocp4" : ""), buildNumber: currentBuild.displayName, attributes: [
            'Triggered by': env.BUILD_USER_FIRST_NAME + " " + env.BUILD_USER_LAST_NAME
            ]
            apwSetEnvironmentStatus status: 'Up'
        } catch (err) {
            echo "GoLive plugin integration failed [$err]"
        }
    }
}

pipeline {

    agent {
        kubernetes {
            inheritFrom 'buildah'
            defaultContainer 'buildah'
        }
    }

    environment {
        KUBECONFIG = "./k8sconfig"
        APW_JIRA_BASE_URL = 'https://jira.myfortuna.eu'
        APW_JIRA_CREDENTIALS_ID = 'jira-cicd-login'
        JIRA_TOKEN = credentials('jira-cicd-login')
    }

    parameters {
        choice(name: 'TARGET_CLUSTER', choices: ['dev','tst','stg','prd'])

		separator(name: "Application")
        booleanParam(name: 'DEPLOY_FORTUNAWEB_FE', defaultValue: false, description: "Fortunaweb-FE")
        booleanParam(name: 'DEPLOY_COMMUNITY_APPS', defaultValue: false, description: "Community Applications (DO NOT TOUCH UNLESS YOU ARE IN CEA COMMUNITY)")
        booleanParam(name: 'DEPLOY_BETSLIP_CONTAINER', defaultValue: false, description: "BetSlip container")
        booleanParam(name: 'DEPLOY_OFFER_APPLICATION', defaultValue: false, description: "Offer Application")
        booleanParam(name: 'DEPLOY_MYACCOUNT_APPLICATION', defaultValue: false, description: "My Account Application")
        booleanParam(name: 'DEPLOY_PROMOTION_PAGE', defaultValue: false, description: "Promotion Page Application")
        booleanParam(name: 'DEPLOY_SHOP_LOCATOR', defaultValue: false, description: "Shop Locator Application")
        booleanParam(name: 'DEPLOY_CONTACT_FORM', defaultValue: false, description: "Contact Form Application")
        booleanParam(name: 'DEPLOY_ADMIN_APPS', defaultValue: false, description: "CMS Admin Applications")

        separator(name: "Countries")
        booleanParam(name: 'ALL', defaultValue: false)
        booleanParam(name: 'COUNTRY_CZ_OCP4', defaultValue: false)
        booleanParam(name: 'COUNTRY_SK_OCP4', defaultValue: false)
        booleanParam(name: 'COUNTRY_PL_OCP4', defaultValue: false)
        booleanParam(name: 'COUNTRY_RO_OCP4', defaultValue: false)
        booleanParam(name: 'COUNTRY_CP_OCP4', defaultValue: false)
        booleanParam(name: 'COUNTRY_HR_OCP4', defaultValue: false)

        separator(name: "Image")
        imageTag(name: 'IMAGE', credentialId: 'quay-api-token-new', organization: 'web2', image: 'fortunaweb-fe', registry: 'https://registry.svc.ifortuna.cz')

        separator(name: "Deploy Ticket")
        string(name: 'DEPLOY_TICKET', defaultValue: 'none', description: '🏷 Which Jira DEPLOY TICKET will we use?')

        separator(name: "Dry run?")
        booleanParam(defaultValue: false, name: 'DRY_RUN')
    }

    options {
        timeout(time: 20, unit: 'MINUTES')
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        skipDefaultCheckout()
        disableConcurrentBuilds()
    }

    stages {
        stage('Prepare variables') {
            steps {
                script {
                    if (params.COUNTRY_RO_OCP4) {
                        countriesToDeployOcp4.push("RO")
                    }
                    if (params.COUNTRY_PL_OCP4) {
                        countriesToDeployOcp4.push("PL")
                    }
                    if (params.COUNTRY_SK_OCP4) {
                        countriesToDeployOcp4.push("SK")
                    }
                    if (params.COUNTRY_CZ_OCP4) {
                        countriesToDeployOcp4.push("CZ")
                    }
                    if (params.COUNTRY_CP_OCP4) {
                        countriesToDeployOcp4.push("CP")
                    }
                    if (params.COUNTRY_HR_OCP4) {
                        countriesToDeployOcp4.push("HR")
                    }
                    if (params.ALL) {
                        countriesToDeployOcp4=["CZ", "RO", "CP", "SK", "PL", "HR"];
                    }

                    def imageTagSplit = params.IMAGE.split(':')
                    def imageTag = imageTagSplit[imageTagSplit.length - 1]
                    def commitSplit = imageTag.split('-')
                    def commit = commitSplit[commitSplit.length - 1]
                    env.IMAGE_TAG = imageTag
                    env.COMMIT = commit
                    currentBuild.description = "~${currentBuild.getBuildCauses()[0].userId} deployed to " + countriesToDeployOcp4.join(",") + " to OCP4 "+ params.TARGET_CLUSTER + " using image " + imageTag
                }
            }
        }
        stage('🧹 Clean & Checkout Helm chart') {
            steps {
                deleteDir()
                echo "Cleaning directory"
                script {
                    checkout scm: [$class: 'GitSCM',
                                userRemoteConfigs: [[url: "ssh://git@app-bitb-shared.o.dc1.cz.ipa.ifortuna.cz:7999/web/fortunaweb-fe.git", credentialsId: 'bitbucket-global']],
                                branches: [[name: env.COMMIT]],
                                extensions: [[$class: 'SparseCheckoutPaths',  sparseCheckoutPaths:[[$class:'SparseCheckoutPath', path:'/helm']]]]
                    ], poll: false
                }
                //move helm to ./
                sh "mv helm/* ./"
                sh "ls"
            }
        }

        stage('🎫 Check Jira deploy ticket') {
            when {
                expression {
                    return jiraCheckDeployTicket.requiresDeployTicketProd(params.TARGET_CLUSTER)
                }
            }
            steps {
                script {
                    if (jiraCheckDeployTicket.isEmpty(params.DEPLOY_TICKET)) {
                        if (!jiraCheckDeployTicket.isInValidState(params.DEPLOY_TICKET)) {
                            error "Jira DEPLOY ticket ${params.DEPLOY_TICKET} is not in valid state - deploy most likely not approved by Release Management!"
                        }
                    } else {
                        error "You need to provide jira DEPLOY ticket ID to deploy to ${params.TARGET_CLUSTER} environment!"
                    }
                }
            }
        }

        stage('🏗️ Deploy!') {
            parallel {
                stage('🏗️ DEPLOY FORTUNAWEB-FE') {
                    when {
                        expression {
                            return params.DEPLOY_FORTUNAWEB_FE
                        }
                    }
                    steps {
                        script {
                            def configDirs = ["./configs/fortunaweb-fe"]
                            def appName = "fortunaweb-fe"
                            def appCode = "is-fortunaweb-fe"
                            def imageName = "fortunaweb-fe"
                            def appHelmPath = "./fortunaweb-fe"
                            def microFrontendName = null
                            def owner = "SB Betting Frontoffice Team"
                            applicationsToDeploy[appName] = owner
                            runDeployJobs(appName, appCode, imageName, appHelmPath, configDirs, microFrontendName)
                        }
                    }
                }

                stage('🏗️ DEPLOY COMMUNITY APPLICATIONS') {
                    when {
                        expression {
                            return params.DEPLOY_COMMUNITY_APPS
                        }
                    }
                    steps {
                        script {
                            def configDirs = ["./configs/fortunaweb-fe"]
                            def appName = "fortunaweb-fe"
                            def appCode = "is-fortunaweb-fe-community-apps"
                            def imageName = "fortunaweb-fe"
                            def appHelmPath = "./fortunaweb-fe"
                            def microFrontendName = "community-apps"
                            def owner = "SB Betting Community Team"
                            applicationsToDeploy[microFrontendName] = owner
                            runDeployJobs(appName, appCode, imageName, appHelmPath, configDirs, microFrontendName)
                        }
                    }
                }

                stage('🏗️ DEPLOY BETSLIP CONTAINER') {
                    when {
                        expression {
                            return params.DEPLOY_BETSLIP_CONTAINER
                        }
                    }
                    steps {
                        script {
                            def configDirs = ["./configs/fortunaweb-fe"]
                            def appName = "fortunaweb-fe"
                            def appCode = "is-fortunaweb-fe-betslip-container"
                            def imageName = "fortunaweb-fe"
                            def appHelmPath = "./fortunaweb-fe"
                            def microFrontendName = "betslip-container"
                            def owner = "SB Betting Frontoffice Team"
                            applicationsToDeploy[microFrontendName] = owner
                            runDeployJobs(appName, appCode, imageName, appHelmPath, configDirs, microFrontendName)
                        }
                    }
                }

                stage('🏗️ DEPLOY OFFER APPLICATION') {
                    when {
                        expression {
                            return params.DEPLOY_OFFER_APPLICATION
                        }
                    }
                    steps {
                        script {
                            def configDirs = ["./configs/fortunaweb-fe"]
                            def appName = "fortunaweb-fe"
                            def appCode = "is-fortunaweb-fe-offer-application"
                            def imageName = "fortunaweb-fe"
                            def appHelmPath = "./fortunaweb-fe"
                            def microFrontendName = "offer-application"
                            def owner = "SB Offer Frontend Team"
                            applicationsToDeploy[microFrontendName] = owner
                            runDeployJobs(appName, appCode, imageName, appHelmPath, configDirs, microFrontendName)
                        }
                    }
                }

                stage('🏗️ DEPLOY MY ACCOUNT APPLICATION') {
                    when {
                        expression {
                            return params.DEPLOY_MYACCOUNT_APPLICATION
                        }
                    }
                    steps {
                        script {
                            def configDirs = ["./configs/fortunaweb-fe"]
                            def appName = "fortunaweb-fe"
                            def appCode = "is-fortunaweb-fe-my-account"
                            def imageName = "fortunaweb-fe"
                            def appHelmPath = "./fortunaweb-fe"
                            def microFrontendName = "my-account"
                            def owner = "CS Player Account Team"
                            applicationsToDeploy[microFrontendName] = owner
                            runDeployJobs(appName, appCode, imageName, appHelmPath, configDirs, microFrontendName)
                        }
                    }
                }

                stage('🏗️ DEPLOY CONTACT FORM') {
                    when {
                        expression {
                            return params.DEPLOY_CONTACT_FORM
                        }
                    }
                    steps {
                        script {
                            def configDirs = ["./configs/fortunaweb-fe"]
                            def appName = "fortunaweb-fe"
                            def appCode = "is-fortunaweb-fe-contact-form"
                            def imageName = "fortunaweb-fe"
                            def appHelmPath = "./fortunaweb-fe"
                            def microFrontendName = "contact-form"
                            def owner = "SB Platform Team"
                            applicationsToDeploy[microFrontendName] = owner
                            runDeployJobs(appName, appCode, imageName, appHelmPath, configDirs, microFrontendName)
                        }
                    }
                }

                stage('🏗️ DEPLOY SHOP LOCATOR') {
                    when {
                        expression {
                            return params.DEPLOY_SHOP_LOCATOR
                        }
                    }
                    steps {
                        script {
                            def configDirs = ["./configs/fortunaweb-fe"]
                            def appName = "fortunaweb-fe"
                            def appCode = "is-fortunaweb-fe-shop-locator"
                            def imageName = "fortunaweb-fe"
                            def appHelmPath = "./fortunaweb-fe"
                            def microFrontendName = "shop-locator"
                            def owner = "SB Platform Team"
                            applicationsToDeploy[microFrontendName] = owner
                            runDeployJobs(appName, appCode, imageName, appHelmPath, configDirs, microFrontendName)
                        }
                    }
                }

                stage('🏗️ DEPLOY PROMOTION PAGE') {
                    when {
                        expression {
                            return params.DEPLOY_PROMOTION_PAGE
                        }
                    }
                    steps {
                        script {
                            def configDirs = ["./configs/fortunaweb-fe"]
                            def appName = "fortunaweb-fe"
                            def appCode = "is-fortunaweb-fe-cea-apps"
                            def imageName = "fortunaweb-fe"
                            def appHelmPath = "./fortunaweb-fe"
                            def microFrontendName = "cea-apps"
                            def owner = "CEA Marketing Solutions Team"
                            applicationsToDeploy[microFrontendName] = owner
                            runDeployJobs(appName, appCode, imageName, appHelmPath, configDirs, microFrontendName)
                        }
                    }
                }

                stage('🏗️ DEPLOY ADMIN APPS') {
                    when {
                        expression {
                            return params.DEPLOY_ADMIN_APPS
                        }
                    }
                    steps {
                        script {
                            def configDirs = ["./configs/fortunaweb-fe"]
                            def appName = "fortunaweb-fe"
                            def appCode = "is-fortunaweb-fe-admin-apps"
                            def imageName = "fortunaweb-fe"
                            def appHelmPath = "./fortunaweb-fe"
                            def microFrontendName = "admin-apps"
                            def owner = "SB Betting Community Team"
                            applicationsToDeploy[microFrontendName] = owner
                            runDeployJobs(appName, appCode, imageName, appHelmPath, configDirs, microFrontendName)
                        }
                    }
                }

                stage('🏗️ Set GoLive Pipeline') {
                    environment {
                        APW_CATEGORY = translateClusterToConfigGoLivePlugin(params.TARGET_CLUSTER)
                    }
                    steps {
                        script {
                            for (int i = 0; i < countriesToDeployOcp4.size(); i++) {
                                echo "GoLive Plugin to ${countriesToDeployOcp4[i]} OCP4"
                                script {
                                    applicationsToDeploy.each { appName, owner ->
                                        goLivePlugin(appName, countriesToDeployOcp4[i].toLowerCase(), true, owner)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        failure {
            script {
                if (jiraCheckDeployTicket.requiresDeployTicketProd(params.TARGET_CLUSTER)) {
                    wrap([$class: 'BuildUser']) {
                        addJiraComment(ISSUE_ID: params.DEPLOY_TICKET, TEXT: "Deployment [${env.JOB_NAME} [${env.BUILD_NUMBER}]|${env.BUILD_URL}] executed by [~${BUILD_USER_ID}] has failed.")
                    }
                }
            }
        }
    }
}
