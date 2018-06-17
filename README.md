# azure-webapp-docker-java

A simple java/spring containerized webservice application


This repo was bootstrapped from:
```
https://spring.io/guides/gs/spring-boot-docker/
```

```
curl -v "http://localhost:8080"
curl -v "http://localhost:8080/time"
```

## RESTful Endpoints for CosmosDB Zipcode collection

```
Path                             Method  Functionality       HTTP Status Codes
/cosmosdb/zipcodes/              POST    Creating Objects    201 (Created), 404 (Not Found), 409 (Conflict) if resource already exists.
/cosmosdb/zipcodes/<objectId>    GET     Retrieving Objects  200 (OK), 404 (Not Found)
/cosmosdb/zipcodes/<objectId>    PUT     Updating Objects    200 (OK), 204 (No Content), 404 (Not Found)
/cosmosdb/zipcodes/<objectId>    GET     Query document      200 (OK), 404 (Not Found)
/cosmosdb/zipcodes/<objectId>    DELETE  Deleting Objects    200 (OK), 404 (Not Found)
/cosmosdb/zipcodes/find/<query>  GET     Query documents     200 (OK), 404 (Not Found)
```

See https://gist.github.com/odan/1d2ef018adb3ea5a0d3abb35406d2c65
