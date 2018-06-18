#!/bin/bash

# Compile the Java code and produce the deployable jar file with Maven.
# Chris Joakim, Microsoft, 2018/06/18

rm target/*.jar

mvn clean compile package

cp target/spring-boot-docker1-0.1.0.jar app.jar
ls -al *.jar
