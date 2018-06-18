#!/bin/bash

# Execute the 'cjoakim/webapp-docker-java:latest' image with Docker.
# Chris Joakim, Microsoft, 2018/06/18

echo 'docker ps list before starting app:'
docker ps

docker run --env-file ./docker-env.txt -d -p 8080:8080 cjoakim/webapp-docker-java:latest

echo 'docker ps list after starting app:'
docker ps
