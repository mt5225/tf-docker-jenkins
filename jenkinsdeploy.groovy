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
                    def region = "${params.region}"
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
              sh "terraform init -backend-config=\"key=${params.envid}.tfstate\""
          }
        }
      }

      stage('plan') {
          steps{
              dir("roles/${appid}"){
                    script {
                       changesExist = sh( script: "terraform plan -var-file ../../environments/${env.appid}/${params.envid}.tfvars ${env.targetString ?: ''} -detailed-exitcode", returnStatus: true)
                       if(changesExit == 1) {
                           error('Error in terraform apply')
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