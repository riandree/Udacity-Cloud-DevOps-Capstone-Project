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
                cd backend
                docker build -t todoapp .
                docker tag todoapp:latest 277642653139.dkr.ecr.eu-central-1.amazonaws.com/todoapp:latest
                withAWS(region:'eu-central-1',credentials:'AWS-ECR-LOGIN') {
                  sh 'echo "Logging into Amazon ECR"'
                  def login = ecrLogin()
                  sh ${login}
                  docker push 277642653139.dkr.ecr.eu-central-1.amazonaws.com/todoapp:latest
                }
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
