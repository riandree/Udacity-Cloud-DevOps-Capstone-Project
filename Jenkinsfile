pipeline {
  agent any
  stages {
        stage('Frontend build') {
            steps {
                echo "Running frontend build with id ${env.BUILD_ID} on ${env.JENKINS_URL}"
                sh '''
                  cd frontend
                  cp src/staging/prod.js src/staging/config.js # Make client point to 'production' environment
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
                sh '''
                  cd backend
                  docker build -t todoapp .
                  docker tag todoapp:latest 277642653139.dkr.ecr.eu-central-1.amazonaws.com/todoapp:${BUILD_ID}
                  aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 277642653139.dkr.ecr.eu-central-1.amazonaws.com/todoapp
                  docker push 277642653139.dkr.ecr.eu-central-1.amazonaws.com/todoapp:${BUILD_ID}
                '''
              }
        }
        stage('rolling deploy Backend') {
          steps {
            sh '''
               cd k8s
               cat deployment.yml | sed --expression='s/##SIGNING_KEY##/todo/g' | sed --expression="s/##VERSION##/${BUILD_ID}/g" > deploy.yml
               cat deploy.yml
               kubectl apply -f deploy.yml 
            '''
          }  
        }
        stage('Deploy Frontend') {
          steps {
             withAWS(region:'eu-central-1',credentials:'aws-static') {
               sh 'echo "Uploading content with AWS credentials"'
               s3Upload(payloadSigningEnabled: true, 
                     includePathPattern:'**/*', workingDir:'frontend/dist',
                     bucket:'www.andrerieck.de')
             }
          }  
        }
  }
}