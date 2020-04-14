#!/bin/bash

aws cloudformation create-stack \
       --stack-name todo-webapp \
       --template-body file://infrastructure.yml
#       --parameters file://parameters.json \
#       --capabilities CAPABILITY_NAMED_IAM

if [[ "$?" == 0 ]]; then
  echo "Waiting for stack creation to complete ..."
  aws cloudformation wait stack-create-complete 
fi
