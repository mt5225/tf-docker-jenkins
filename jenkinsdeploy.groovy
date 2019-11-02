pipeline {
    agent {
        docker {
            image 'hashicorp/terraform:0.12.12'
            args '--entrypoint=""'
        }
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

      stage('Init') {
        steps {
                ansiColor('xterm') {
                    sh "terraform init"
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