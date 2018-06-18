package com.chrisjoakim.springboot1.dao;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisjoakim.springboot1.AppConfig;
import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.FeedOptions;
import com.microsoft.azure.documentdb.FeedResponse;
import com.microsoft.azure.documentdb.PartitionKey;
import com.microsoft.azure.documentdb.RequestOptions;
import com.microsoft.azure.documentdb.ResourceResponse;

/**
 * This class performs all CRUD operations vs CosmosDB for the application.
 * It uses the Java Sync SDK for SQL API of Azure Cosmos DB;
 * @see https://github.com/Azure/azure-documentdb-java
 * 
 * Related links:
 * @see https://docs.microsoft.com/en-us/azure/cosmos-db/sql-api-java-application
 * @see https://docs.microsoft.com/en-us/azure/cosmos-db/sql-api-sql-query
 * @see https://github.com/Azure/azure-documentdb-java/blob/master/documentdb-examples/src/test/java/com/microsoft/azure/documentdb/examples/DocumentQuerySamples.java
 *
 * There is also a newer Async SDK for CosmosDB, based on RxJava, see the following links for this SDK:
 * @see https://github.com/ReactiveX/RxJava
 * @see https://azure.microsoft.com/en-us/blog/announcing-new-async-java-sdk-for-azure-cosmosdb/
 * @see https://github.com/Azure/azure-cosmosdb-java
 *
 * @author Chris Joakim, Microsoft
 * @date   2018/06/18
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

    public ResourceResponse<Document> deleteDocument(String dbName, String collName, String pk, String docId) throws DocumentClientException {

        String docLink = this.docLink(dbName, collName, docId);
        RequestOptions options = new RequestOptions();
        options.setPartitionKey(new PartitionKey(pk));
        return this.client.deleteDocument(docLink, options);
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
            d = AppConfig.getDocDbDatabaseName();
        }
        if (c == null) {
            c = AppConfig.getDocDbCollName();
        }
        return "dbs/" + d + "/colls/" + c;
    }

    protected String docLink(String dbName, String collName, String docId) {

        String clink = this.collLink(dbName, collName);
        return clink + "/docs/" + docId;
    }
    
    protected String serviceEndpoint() {

        return "https://" + AppConfig.getDocDbAcct() + ".documents.azure.com";
    }
}
