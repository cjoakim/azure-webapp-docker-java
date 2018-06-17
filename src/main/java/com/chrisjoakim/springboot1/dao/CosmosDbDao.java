package com.chrisjoakim.springboot1.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisjoakim.springboot1.AppConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.FeedOptions;
import com.microsoft.azure.documentdb.FeedResponse;
import com.microsoft.azure.documentdb.RequestOptions;

/**
 * This class performs all CRUD operations vs CosmosDB for the application.
 * TODO - revisit this class
 *
 * See https://docs.microsoft.com/en-us/azure/cosmos-db/sql-api-java-application
 * See https://docs.microsoft.com/en-us/azure/cosmos-db/sql-api-sql-query
 * See https://github.com/Azure/azure-documentdb-java/blob/master/documentdb-examples/src/test/java/com/microsoft/azure/documentdb/examples/DocumentQuerySamples.java
 *
 * @author Chris Joakim, Microsoft
 * @date   2018/05/14
 */

public class CosmosDbDao {

    // Class variables
    private static final Logger logger = LoggerFactory.getLogger(CosmosDbDao.class);

    // Instance variables:
    private DocumentClient client = null;


    public CosmosDbDao() throws Exception {

    	super();
        this.client = new DocumentClient(
                serviceEndpoint(),
                AppConfig.getDocDbKey(),
                new ConnectionPolicy(),
                ConsistencyLevel.Session);
    }

    public Document insertDocument(String dbName, String collName, Object obj) throws DocumentClientException {

        String collLink = this.collLink(dbName, collName);
        return this.client.createDocument(collLink, obj, new RequestOptions(), false).getResource();
    }

    public Document upsertDocument(String dbName, String collName, Object obj) throws DocumentClientException {

        String collLink = this.collLink(dbName, collName);
        return this.client.upsertDocument(collLink, obj, new RequestOptions(), false).getResource();
    }

    public FeedResponse<Document> queryAsDocuments(String dbName, String collName, String sql) {

        String collLink = this.collLink(dbName, collName);
        FeedOptions options = new FeedOptions();
        options.setEnableCrossPartitionQuery(true);
        return this.client.queryDocuments(collLink, sql, options);
    }

    public ArrayList<String> queryAsJsonList(String dbName, String collName, String sql) {

        FeedResponse<Document> queryResults = queryAsDocuments(dbName, collName, sql);
        ArrayList<String> jsonStrings = new ArrayList<String>();

        for (Document doc : queryResults.getQueryIterable()) {
            if (doc != null) {
                jsonStrings.add(doc.toJson());
            }
            // doc is an instance of com.microsoft.azure.documentdb.Document
        }
        return jsonStrings;
    }
    
    public void close() {
    	
    	if (this.client != null) {
    		this.client.close();
    	}
    }

    protected String collLink(String dbName, String collName) {

        String d = dbName;
        String c = collName;

        if (d == null) {
            d = AppConfig.getDocDbDefaultDbName();
        }
        if (c == null) {
            c = AppConfig.getDocDbDefaultCollName();
        }
        return "dbs/" + d + "/colls/" + c;
    }

    protected String serviceEndpoint() {

        return "https://" + AppConfig.getDocDbAcct() + ".documents.azure.com";
    }

    
}