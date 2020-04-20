pipeline {
  agent any
  //environment {
  //      AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
  //      AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
  //}
  stages {
  //      stage('xLint') {
  //          steps {
  //              echo 'Testing..'
  //          }
  //      }
        stage('Frontend build') {
            steps {
                echo "Running frontend build with id ${env.BUILD_ID} on ${env.JENKINS_URL}"
                sh '''
                  cd frontend
                  yarn install 
                  yarn build   # this needs vue-cli to be available
                ''' 
            }
        }
        stage('Backend build') {
            steps {
                echo "Running backend build with id ${env.BUILD_ID} on ${env.JENKINS_URL}"
                sh '''
                  cd backend
                  mvn clean package
                ''' 
            }
        }
        stage('Docker') {
             steps {
                 sh 'pwd && ls -la'
                 sh 'cd frontend && ls -la'
             }
        }
        stage('Deploy Frontend') {
          steps {
             withAWS(region:'eu-central-1',credentials:'aws-static') {
               sh 'echo "Uploading content with AWS credentials"'
               s3Upload(payloadSigningEnabled: true, 
                     includePathPattern:'**/*', workingDir:'frontend/dist',
                     bucket:'de.rieck.todoapp')
             }
          }   
        }
//        stage('Deploy') {
//            steps {
//                echo 'Deploying....'
//            }
//        }
  }
}
