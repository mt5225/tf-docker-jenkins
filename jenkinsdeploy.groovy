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
    }

    post {
        always {
            cleanWs()
        }
    }
}