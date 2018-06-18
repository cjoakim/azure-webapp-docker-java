package com.chrisjoakim.springboot1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chrisjoakim.springboot1.dao.CosmosDbDao;
import com.chrisjoakim.springboot1.dao.EnvironmentDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.ResourceResponse;

/**
 * Entry-point for this Spring Boot Web Application.
 * All HTTP endpoints for this App are currently defined in this class.
 * 
 * @author Chris Joakim, Microsoft
 * @date   2018/06/18
 */

@SpringBootApplication
@RestController
public class Application extends Object {
	
    // Class variables
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    		
	// Instance variables:
	private String dbName = AppConfig.getDocDbDefaultDbName();
	private String collName = AppConfig.getDocDbDefaultCollName();
	private CosmosDbDao cosmosDbDao = null;
	
	Application() {
		
		super();
		
		try {
			this.cosmosDbDao = new CosmosDbDao();
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("EXCEPTION WHEN CREATING CosmosDbDao INSTANCE, PROGRAM TERMINATING.");
			System.exit(1);
		}
	}

	// Endpoints for general web app functions:
	
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

    // Endpoints for Azure CosmosDB Zipcode collection CRUD functions:
    // See http://www.baeldung.com/spring-requestmapping
    //
    // Path                            Method  Functionality       HTTP Status Codes
    // cosmosdb/zipcodes/<objectId>    GET     Retrieving Objects  200 (OK), 404 (Not Found)
    // cosmosdb/zipcodes/              POST    Creating Objects    201 (Created), 404 (Not Found), 409 (Conflict) if resource already exists.
    // cosmosdb/zipcodes/<objectId>    PUT     Updating Objects    200 (OK), 204 (No Content), 404 (Not Found)
    // cosmosdb/zipcodes/<objectId>    GET     Query document      200 (OK), 404 (Not Found)
    // cosmosdb/zipcodes/<objectId>    DELETE  Deleting Objects    200 (OK), 404 (Not Found)
    // cosmosdb/zipcodes/query/        POST    Query documents     200 (OK), 404 (Not Found), expects JSON with 'sql' key
    
    @RequestMapping(value="/cosmosdb/zipcodes/{objectId}", method=RequestMethod.GET, produces="application/json")  
    public ResponseEntity<?> zipcodeGet(@PathVariable("objectId") String id) {
    	
    	String sql = String.format("select * from c where c.id = \"%s\"", id);
    	logger.warn("zipcodeGet; sql: " + sql);
    	
    	ArrayList<String> docs = cosmosDbDao.queryAsJsonList(dbName, collName, sql);
    	
    	if (docs.size() > 0) {
    		return new ResponseEntity<String>(docs.get(0), HttpStatus.OK); 
    	}
    	else {
    		return new ResponseEntity<String>("{}", HttpStatus.NOT_FOUND); 
    	}
    }
    
    @RequestMapping(value="/cosmosdb/zipcodes/", method=RequestMethod.POST, headers="Accept=application/json")
    public ResponseEntity<?> zipcodeCreate(@RequestBody Map<String, Object> body) {
    	
    	try {
        	logger.warn("zipcodeCreate; body: " + body);
			Document doc = cosmosDbDao.insertDocument(dbName, collName, body);
        	logger.warn("zipcodeCreate; doc: " + doc);
        	
        	// TODO - consider adding duplicate detection logic and return 409 (Conflict)
        	
			if (doc != null) {
				return new ResponseEntity<String>(doc.toJson(), HttpStatus.CREATED); 
			}
			else {
				return new ResponseEntity<String>("{}", HttpStatus.NOT_FOUND); 
			}
		}
    	catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{}", HttpStatus.INTERNAL_SERVER_ERROR); 
		}
    }
    
    @RequestMapping(value="/cosmosdb/zipcodes/{objectId}", method=RequestMethod.PUT, headers="Accept=application/json", produces="application/json")
    public ResponseEntity<?> zipcodeUpdate(@RequestBody Map<String, Object> body) {

    	try {
        	logger.warn("zipcodeUpdate; body: " + body);
			Document doc = cosmosDbDao.upsertDocument(dbName, collName, body);
        	logger.warn("zipcodeUpdate; doc: " + doc);
			return new ResponseEntity<String>(doc.toJson(), HttpStatus.OK); 
		}
    	catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{}", HttpStatus.INTERNAL_SERVER_ERROR); 
		}
    }
    
    @RequestMapping(value="/cosmosdb/zipcodes/{pk}/{objectId}", method=RequestMethod.DELETE)
    public ResponseEntity<?> zipcodeDelete(@PathVariable("pk") String pk, @PathVariable("objectId") String id) {

    	try {
			logger.warn(String.format("zipcodeDelete; pk: %s id: %s", pk, id));
			ResourceResponse<Document> resp = cosmosDbDao.deleteDocument(dbName, collName, pk, id);
			logger.warn(resp.toString());
			return new ResponseEntity<String>("{}", HttpStatus.OK); 
		}
    	catch (DocumentClientException e) {
    		return new ResponseEntity<String>("{}", HttpStatus.NOT_FOUND); 
		}
    }
   
    @RequestMapping(value="/cosmosdb/zipcodes/query/", method=RequestMethod.POST, headers="Accept=application/json", produces="application/json")
    public ResponseEntity<?> zipcodesQuery(@RequestBody Map<String, String> body) {

    	try {
			logger.warn(String.format("zipcodesFind; body: %s", body));
			String sql = body.get("sql");
			logger.warn(String.format("zipcodesFind; sql: %s", sql));
			ArrayList<String> docs = cosmosDbDao.queryAsJsonList(dbName, collName, sql);

			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(docs);
			return new ResponseEntity<String>(json, HttpStatus.OK); 
		}
    	catch (Exception e) {
    		return new ResponseEntity<String>("{}", HttpStatus.NOT_FOUND); 
		}
    }
    
	protected Map<String, Object> parseJsonBody(String json) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, Object>> typeRef 
			= new TypeReference<HashMap<String, Object>>() {};
		return mapper.readValue(json, typeRef);
	}
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
