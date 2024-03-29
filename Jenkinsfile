pipeline {
  agent any
  stages {
        stage('Frontend build') {
            steps {
                sh '''
                  cd frontend
                  cp src/staging/prod.js src/staging/config.js # Make client point to 'production' environment
                  yarn install 
                  yarn build     # this needs vue-cli to be available
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
        stage('lint Docker') {
          steps {
            sh '''
              cd backend
              docker run --rm -i hadolint/hadolint < Dockerfile
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
               // AmazonWebServicesCredentialsBinding has issues when embedding calls to kubectl ... so
               // merging the configuration is done in a separate step
              withCredentials([[
                 $class: 'AmazonWebServicesCredentialsBinding',
                 credentialsId: 'ToDoDynamoDBFullAccess',
                 accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                 secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
              ]]) {
                sh '''
                 # inject backend version to deploy and credentials
                 cd k8s
                 cat deployment.yml | sed --expression="s/##VERSION##/${BUILD_ID}/g" |\
                                      sed --expression="s/##AWS_ACCESS_KEY_ID##/${AWS_ACCESS_KEY_ID}/g" |\
                                      sed --expression="s/##AWS_SECRET_ACCESS_KEY##/${AWS_SECRET_ACCESS_KEY}/g" > deploy.yml
                '''
             }
             sh '''
                 # rollout changes to k8s using kubectl
                 cd k8s
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