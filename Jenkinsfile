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
  //      stage('Unittest') {
  //          steps {
  //              // Alternative zu Credentials im Environment
  //              withCredentials(bindings: [sshUserPrivateKey(credentialsId: 'jenkins-ssh-key-for-abc', \
  //                                                           keyFileVariable: 'SSH_KEY_FOR_ABC')]) {
  //                // 
  //              }
  //              echo 'Building..'
  //          }
  //      }
        stage('Build') {
            // when {
            //  expression {
            //    currentBuild.result == null || currentBuild.result == 'SUCCESS' 
            //  }
            //}
            steps {
                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
   //             echo 'Testing..'
                sh '''
                cd frontend
                yarn install 
                yarn build   # this needs vue-cli to be available
                ''' 
   //             archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true 
   //             junit '**/target/*.xml'     junit captures and associates the JUnit XML files matching the inclusion pattern (**/target/*.xml).  Pluigin notwendig
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
