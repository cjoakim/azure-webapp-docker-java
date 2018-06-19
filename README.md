# azure-webapp-docker-java

A simple java/spring containerized webservice application

This repo was bootstrapped from:
```
https://spring.io/guides/gs/spring-boot-docker/
```

## RESTful Endpoints for CosmosDB Zipcode collection

```
Path                            Method  Functionality       HTTP Status Codes
/                               GET     Hello from Spring   200 (OK)
/time                           GET     Server date & time  200 (OK)
/env/{secret}                   GET     Server env vars     200 (OK)
cosmosdb/airports/<objectId>    GET     Retrieving Objects  200 (OK), 404 (Not Found)
cosmosdb/airports/              POST    Creating Objects    201 (Created), 404 (Not Found), 409 (Conflict) if resource already exists.
cosmosdb/airports/<objectId>    PUT     Updating Objects    200 (OK), 204 (No Content), 404 (Not Found)
cosmosdb/airports/<objectId>    GET     Query document      200 (OK), 404 (Not Found)
cosmosdb/airports/<objectId>    DELETE  Deleting Objects    200 (OK), 404 (Not Found)
cosmosdb/airports/query/        POST    Query documents     200 (OK), 404 (Not Found), expects JSON with 'sql' key
```

See https://gist.github.com/odan/1d2ef018adb3ea5a0d3abb35406d2c65

## Azure Setup

You'll need to provision the following PaaS services:
- Azure CosmosDB with SQL/DocumentDB API
- Azure Container Registry (ACR)
- Azure Container Instance (ACI)
- Azure Kubernetes Service (AKS)

## Workstation Setup

This repository assumes the following:
- macOS or linux workstation
- git installed
- java 8 installed
- maven 3.x installed
- docker installed
- python 3 optional, for http_client
- environment variables are set per the following section

### Environment Variables

The following should be set, and exported, per the values in Azure Portal for your PaaS services.
For example:
```
AZURE_COSMOSDB_DOCDB_ACCT=cjoakimcosmosddb
AZURE_COSMOSDB_DOCDB_COLLNAME=airports
AZURE_COSMOSDB_DOCDB_DBNAME=dev
AZURE_COSMOSDB_DOCDB_KEY=TBGre2S......54qXXA==
AZURE_COSMOSDB_DOCDB_URI=https://cjoakimcosmosddb.documents.azure.com:443/
```

### Compile and Build the Deployable JAR file

```
$ ./build_jar.sh
```

### Load your CosmosDB

In your CosmosDB in Azure Portal, create a database named **dev** with collection named **airports**.
The collection should have a partition key named **/pk**, and an initial RU value of 10000.

```
$ ./load_airports.sh

$ cat tmp/load_airports.txt | grep loaded
```

### Execute the Web App on your Workstation

```
$ ./execute_jar.sh
```

### Create the Docker Image

Be sure to change the name 'cjoakim/webapp-docker-java' in this script to your own name.
```
$ ./build_image.sh
```

### Execute the Docker Image on your Workstation

First, create your **docker-env.txt** file:
```
$ cp docker-env-example.txt docker-env.txt

<edit docker-env.txt to add your own values>
```

```
$ ./execute_image.sh
```

### HTTP Client Program

This repo contains a simple Python 3 HTTP client for the Java Spring Boot app.

```
$ cd http_client
$ ./venv.sh                 (create the python virtual env)
$ source bin/activate       (activate the python virtual env) 

$ python http_client.py time
$ python http_client.py env
$ python http_client.py get_id 87ed3026-9539-4c49-863d-4e54e60a8316
$ python http_client.py get_id bc06f01f-3f86-49ee-86f0-1f3f8639ee2d 
$ python http_client.py get_id eddd56b5-3b4c-4b50-9d56-b53b4c7b50c6 
$ python http_client.py delete_id test eddd56b5-3b4c-4b50-9d56-b53b4c7b50c6
$ python http_client.py post_airport 
$ python http_client.py update_airport 
$ python http_client.py query 
```


## Azure Container Registry

See https://docs.microsoft.com/en-us/azure/container-registry/container-registry-get-started-azure-cli

```
$ az acr login --name cjoakimacr

$ az acr repository list --name cjoakimacr --output table

$ docker tag cjoakim/webapp-docker-java:latest cjoakimacr.azurecr.io/webapp-docker-java:v1
$ docker tag cjoakim/webapp-docker-java:latest cjoakimacr.azurecr.io/webapp-docker-java:v2
$ docker tag cjoakim/webapp-docker-java:latest cjoakimacr.azurecr.io/webapp-docker-java:latest

$ docker push cjoakimacr.azurecr.io/webapp-docker-java:v2

$ az acr repository list --name cjoakimacr --output table
Result
------------------
webapp-docker-java

$ az acr update --name cjoakimacr --admin-enabled true

az container create --resource-group cjoakim-containers --name webapp-docker-java-v3 --image cjoakimacr.azurecr.io/webapp-docker-java:v3 --cpu 1 --memory 1 --registry-username cjoakimacr --registry-password $AZURE_CONTAINER_REGISTRY_USER_PASS --dns-name-label webapp-docker-java --ports 8080 -e 'AZURE_COSMOSDB_DOCDB_ACCT=cjoakimcosmosddb' 'AZURE_COSMOSDB_DOCDB_KEY=TBGre......qXXA==' 'AZURE_COSMOSDB_DOCDB_URI=https://cjoakimcosmosddb.documents.azure.com:443/'

curl -v http://webapp-docker-java.eastus.azurecontainer.io:8080/cosmosdb/airports/0cf7eb67-13d6-4e63-b232-04dd64ff6677

{"country":"Canada","altitude":"108","iata_code":"YAY","_rid":"wboEAJ06JgAHAAAAAAAAAA==","city":"St. Anthony","timezone_num":"-3.5","latitude":"51.391944","epoch":"1529336500223","_attachments":"attachments/","timezone_code":"America/St_Johns","name":"St Anthony","location":{"coordinates":[-56.083056,51.391944],"type":"Point"},"pk":"YAY","id":"0cf7eb67-13d6-4e63-b232-04dd64ff6677","_self":"dbs/wboEAA==/colls/wboEAJ06JgA=/docs/wboEAJ06JgAHAAAAAAAAAA==/","_etag":"\"99009efb-0000-0000-0000-5b27d2b40000\"","longitude":"-56.083056","_ts":1529336500}

```


