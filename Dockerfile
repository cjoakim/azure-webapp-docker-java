FROM openjdk:8-jdk-alpine
MAINTAINER Chris Joakim

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

VOLUME /tmp

COPY target/spring-boot-docker1-0.1.0.jar /usr/src/app/app.jar

EXPOSE 8080
CMD [ "java", "-jar", "app.jar" ]

# Docker commands:
# docker build -t cjoakim/webapp-docker-java . 
# docker run -d -p 3000:3000 cjoakim/webapp-docker-java:latest
# docker run -d -p 80:3000 cjoakim/webapp-docker-java:latest
# docker run -e MONGODB_URI=$MONGODB_AZURE_URI -d -p 80:3000 cjoakim/webapp-docker-java:latest 
# docker ps
# docker stop -t 2 86b125ed43e5  (where 86b125ed43e5 is the CONTAINER ID from 'docker ps')
# docker push cjoakim/webapp-docker-java:latest

#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","spring-boot-docker1-0.1.0.jar"]
