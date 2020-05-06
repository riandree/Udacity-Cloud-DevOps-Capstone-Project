#!/bin/bash

aws cloudformation update-stack \
       --stack-name todo-webapp \
       --template-body file://infrastructure.yaml \
#       --parameters file://parameters.json \

if [[ "$?" == 0 ]]; then
  echo "Waiting for stack update to complete ..."
  aws cloudformation wait stack-update-complete 
else
  exit 1
fi

echo "updating VPC"

aws cloudformation update-stack --stack-name todo-webapp-vpc --template-body file://amazon-eks-vpc-private-subnets.yaml

if [[ "$?" == 0 ]]; then
  echo "Waiting for stack update to complete ..."
  aws cloudformation wait stack-update-complete 
else
  exit 1
fi

aws cloudformation update-stack --stack-name todo-webapp-eks --template-body file://amazon-eks-cluster.yaml --capabilities CAPABILITY_NAMED_IAM

if [[ "$?" == 0 ]]; then
  echo "Waiting for stack update to complete ..."
  aws cloudformation wait stack-update-complete 
else
  exit 1
fi
