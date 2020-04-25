# Udacity-Cloud-DevOps-Capstone-Project

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
