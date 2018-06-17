package com.chrisjoakim.springboot1;

import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chrisjoakim.springboot1.dao.CosmosDbDao;
import com.chrisjoakim.springboot1.dao.EnvironmentDao;

/**
 * 
 *
 */

@SpringBootApplication
@RestController
public class Application extends Object {
	
	// Instance variables:
	CosmosDbDao cosmosDao = new CosmosDbDao();
	
	Application() {
		
		super();
	}

	// Endpoints for general web app functions
	
    @RequestMapping(value="/", method=RequestMethod.GET, produces="application/json")
    public HashMap<String, String> home() {
    	return new EnvironmentDao().getHelloInfo();
    }

    @RequestMapping(value="/time", method=RequestMethod.GET, produces="application/json")
    public HashMap<String, String> time() {
    	return new EnvironmentDao().getTimeInfo();
    }
    
    @RequestMapping(value="/env", method=RequestMethod.GET, produces="application/json")
    public HashMap<String, String> environmentVariables() {
    	return new EnvironmentDao().getEnvironmentVariables();
    }

    // Endpoints for Azure CosmosDB Zipcode collection CRUD functions
    // See http://www.baeldung.com/spring-requestmapping
    
    @RequestMapping(
    	value="/cosmosdb/zipcodes/{objectId}",
    	method=RequestMethod.GET,
    	produces="application/json")  
    public String zipcodeGet(@PathVariable("objectId") String id) {
        return "" + System.currentTimeMillis();
    }
    
    @RequestMapping(
    	value="/cosmosdb/zipcodes/",
    	method=RequestMethod.POST,
    	headers="Accept=application/json")
    public String zipcodeCreate() {
        return "" + System.currentTimeMillis();
    }
    
    @RequestMapping(
    	value="/cosmosdb/zipcodes/{objectId}",
    	method=RequestMethod.PUT,
    	headers="Accept=application/json",
    	produces="application/json")
    public String zipcodeUpdate(@PathVariable("objectId") String id) {
        return "" + System.currentTimeMillis();
    }
    
    @RequestMapping(
    	value="/cosmosdb/zipcodes/{objectId}",
    	method=RequestMethod.DELETE)
    public String zipcodeDelete(@PathVariable("objectId") String id) {
        return "" + System.currentTimeMillis();
    }
   
    @RequestMapping(
    	value="/cosmosdb/zipcodes/find/",
    	method=RequestMethod.GET,
    	headers="Accept=application/json",
    	produces="application/json")
    public String zipcodesFind() {
        return "" + System.currentTimeMillis();
    }
    
//    Path                             Method  Functionality       HTTP Status Codes
//    /cosmosdb/zipcodes/              POST    Creating Objects    201 (Created), 404 (Not Found), 409 (Conflict) if resource already exists.
//    /cosmosdb/zipcodes/<objectId>    GET     Retrieving Objects  200 (OK), 404 (Not Found)
//    /cosmosdb/zipcodes/<objectId>    PUT     Updating Objects    200 (OK), 204 (No Content), 404 (Not Found)
//    /cosmosdb/zipcodes/<objectId>    GET     Query document      200 (OK), 404 (Not Found)
//    /cosmosdb/zipcodes/<objectId>    DELETE  Deleting Objects    200 (OK), 404 (Not Found)
//    /cosmosdb/zipcodes/find/<query>  GET     Query documents     200 (OK), 404 (Not Found)
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
