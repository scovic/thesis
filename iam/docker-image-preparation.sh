#!/bin/bash

mvn clean && mvn package

docker rm iam-service && docker image rm iam-service
docker build -t iam-service .