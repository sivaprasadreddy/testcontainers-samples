#!/bin/bash

awslocal s3 mb s3://testbucket
echo "List of S3 buckets:"
echo "-------------------------------"
awslocal s3 ls

awslocal sqs create-queue --queue-name test_queue
echo "List of SQS Queues:"
echo "-------------------------------"
awslocal sqs list-queues

awslocal dynamodb create-table \
    --table-name person \
    --key-schema AttributeName=id,KeyType=HASH AttributeName=name,KeyType=RANGE \
    --attribute-definitions AttributeName=id,AttributeType=S AttributeName=name,AttributeType=S \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

echo "List of DynamoDB tables:"
echo "-------------------------------"
awslocal dynamodb list-tables
