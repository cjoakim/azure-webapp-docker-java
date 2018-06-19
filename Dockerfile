FROM openjdk:8-jdk-alpine
MAINTAINER Chris Joakim

RUN mkdir -p /app

WORKDIR /app

COPY app.jar /app/app.jar

# The Spring Boot Web App will listen on port 8080.
EXPOSE 8080

# This is the entry-point command for the container instances.
CMD [ "java", "-jar", "app.jar" ]



# Example Docker commands:
# docker build -t cjoakim/webapp-docker-java . 
# docker run -d -p 3000:3000 cjoakim/webapp-docker-java:latest
# docker run -d -p 80:3000 cjoakim/webapp-docker-java:latest
# docker run -e MONGODB_URI=$MONGODB_AZURE_URI -d -p 80:3000 cjoakim/webapp-docker-java:latest 
# docker ps
# docker stop -t 2 86b125ed43e5  (where 86b125ed43e5 is the CONTAINER ID from 'docker ps')
# docker push cjoakim/webapp-docker-java:latest

# az acr login --name cjoakimacr
# az acr repository list --name cjoakimacr --output table
# docker tag cjoakim/webapp-docker-java:latest cjoakimacr.azurecr.io/webapp-docker-java:latest
# docker push cjoakimacr.azurecr.io/webapp-docker-java:latest

# Environment variables in the running container from the 'openjdk:8-jdk-alpine' base image:
# JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
# PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin

