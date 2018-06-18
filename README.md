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
cosmosdb/zipcodes/<objectId>    GET     Retrieving Objects  200 (OK), 404 (Not Found)
cosmosdb/zipcodes/              POST    Creating Objects    201 (Created), 404 (Not Found), 409 (Conflict) if resource already exists.
cosmosdb/zipcodes/<objectId>    PUT     Updating Objects    200 (OK), 204 (No Content), 404 (Not Found)
cosmosdb/zipcodes/<objectId>    GET     Query document      200 (OK), 404 (Not Found)
cosmosdb/zipcodes/<objectId>    DELETE  Deleting Objects    200 (OK), 404 (Not Found)
cosmosdb/zipcodes/query/        POST    Query documents     200 (OK), 404 (Not Found), expects JSON with 'sql' key
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
./build_jar.sh
```

### Load your CosmosDB

In your CosmosDB in Azure Portal, create a database named **dev** with collection named **airports**.
The collection should have a partition key named **/pk**, and an initial RU value of 10000.

```
./load_airports.sh

tmp/load_airports.txt | grep loaded
```

### Execute the Web App on your Workstation

```
./execute_jar.sh
```

### Create the Docker Image

Be sure to change the name 'cjoakim/webapp-docker-java' in this script to your own name.
```
./build_image.sh
```

### Execute the Docker Image on your Workstation

Be sure to change the name 'cjoakim/webapp-docker-java' in this script to your own name.
```
./execute_image.sh
```


