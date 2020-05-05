# Udacity-Cloud-DevOps-Capstone-Project

As an example a small ToDo-List application called 'Andre´s TODO' has been developed
which is accessible under [andrerieck.de](http://www.andrerieck.de) 

#### Technologies used 

- ***CI/CD*** Jenkins
- ***Frontend*** *Vue.js* with the *Element* UI-library. Vue app is deployed by the Jenkins pipeline to an S3-Bucket from which the static resources are served.
- ***Backend*** : REST Services provided via a *Spring-Boot* backend deployed to an *EKS managed K8S cluster*. 
- ***Container orchestration platform*** AWS EKS managed K8S cluster.
- ***Container registry*** *AWS ECR*
- ***Persistence*** *AWS DynamoDB*
- ***User Management*** *AWS Cognito*
K8S Cluster
- ***infrastructure provisionsing*** AWS cloudformation

#### overview over AWS infrastructure

#### CI/CD Pipeline

The CI/CD pipeline has the steps as show by the following image and is defined using a *Jenkinsfile*

![Image of CI/CD pipeline](./doc/pipeline.jpg)

The ***Jenkins*** instance has been provisioned manually on an EC2 instance.
The following tools need to be available on then jenkins instance :

* yarn
* node  
* vue-cli
* maven
* openjdk 14
* docker
* aws cli
* kubectl  

## Infrastructure setup

![Architecture overview](./doc/archOverview.png)





The Infrastructure is deployed using Cloud-formation



Project Description



## configuring kubectl for a new EKS Cluster

aws sts get-caller-identity

aws eks --region region-code update-kubeconfig --name cluster_name
--> aws eks --region eu-central-1 update-kubeconfig --name prod

muss vorhanden sein : kubectl describe configmap -n kube-system aws-auth

IAM User zum Cluster hinzufügen :
rieck5~$ kubectl edit -n kube-system configmap/aws-auth

user einfügen unter 'mapUsers' : 

apiVersion: v1
data:
  mapRoles: |
    - groups:
      - system:bootstrappers
      - system:nodes
      rolearn: arn:aws:iam::277642653139:role/todo-webapp-NodeInstanceRole-60LK21PIZL03
      username: system:node:{{EC2PrivateDNSName}}
  mapUsers: |
    - userarn: arn:aws:iam::277642653139:user/jenkins-nanodegree
      username: pipeline
      groups:
        - system:masters

--> Jenkins kubectl muss entsprechend konfiguriert werden mittels 
rieck5~/udacity/Udacity-Cloud-DevOps-Capstone-Project$ aws eks --region eu-central-1 update-kubeconfig --name prod
während der entsprechende User in der aws cli dort aktiv ist
