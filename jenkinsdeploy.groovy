pipeline {
    agent {
        docker {
            image 'hashicorp/terraform:0.12.9'
            args '--entrypoint=""'
        }
    }

    options {
      timeout(15)
      timestamps()
      ansiColor('xterm')
      disableConcurrentBuilds()
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
}