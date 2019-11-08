pipeline {
    agent {
        docker {
            image 'hashicorp/terraform:0.12.12'
            args '--entrypoint=""'
        }
    }

    parameters {
        choice choices: ['petstore-qa-usw2', 'petstore-dev-usw2', 'petstore-prod-usw2', 'petstore-dr-usw2'], description: '', name: 'envid'
        choice choices: ['us-west-2', 'us-east-2'], description: '', name: 'region'
        choice choices: ['Plan', 'Apply', 'Destroy'], description: '', name: 'action'
        string defaultValue: '', description: '', name: 'target', trim: true
    }

    options {
      timeout(15)
      timestamps()
      ansiColor('xterm')
      disableConcurrentBuilds()
    }

    environment {
        AWS_ACCESS_KEY_ID     = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
    }

    stages {
      stage('create variables') {
        steps {
                script {
                    env.appid = params.envid.split('-')[0]
                    def target = "${params.target}"
                    env.targetString = ""
                    if (target != '') {
                        target.split(",").each { moduleName ->
                            env.targetString += "-target ${moduleName} "
                        }
                    }
                    def changesExist = -1
                }
            }
        }

      stage('Validate') {
        steps {
            sh 'terraform validate'
        }
      }
    
      stage('Lint') {
        steps {
            sh 'terraform fmt -recursive'
        }
      }

      // init stage
      // Jenkins runs the `terraform init` command which prepares all the terraform code and initializes the remote state
      stage('Init') {
        steps{
          dir("roles/${appid}") {
              sh "terraform init -backend-config=\"key=${params.envid}.tfstate\" -backend-config=\"bucket=${params.envid}\" -backend-config=\"region=${params.region}\""
          }
        }
      }

      stage('plan') {
          steps{
              dir("roles/${appid}"){
                    script {
                       changesExist = sh( script: "terraform plan -var-file ../../environments/${env.appid}/${params.envid}.tfvars ${env.targetString ?: ''} -detailed-exitcode", returnStatus: true)
                       if(changesExist == 1) {  //error planning, exist
                           error('Error in terraform apply')
                       }
                    }
              }
          }
        }

      stage('apply') {
          //pre-conditions to apply change 
          when {
              allOf {
                  expression { return (changesExist == 2 || params.action == 'Destroy') }// 0 is no changes, 1 is error, 2 is changes
                  branch 'master'  //only apply changes in master branch
                  expression { return ( params.action == 'Apply' || params.action == 'Destroy') }  //only if user explicitly to do so
              }
          }
          steps{
            script {
              // TODO: notification to slack channel
              def approval = input(submitterParameter: 'submitter', message: 'Should we continue?')   
            }
            dir("roles/${appid}"){
              script {
                  if(params.action == 'Destroy'){
                        sh "terraform destroy -input=false -auto-approve -force -parallelism 25 -var-file ../../environments/${env.appid}/${params.envid}.tfvars ${env.targetString ?: ''}"
                      } else {
                        sh "terraform apply -input=false -auto-approve -parallelism 25 -var-file ../../environments/${env.appid}/${params.envid}.tfvars ${env.targetString ?: ''}"
                      }
                }
            }
          }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}