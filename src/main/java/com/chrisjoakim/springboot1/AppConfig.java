package com.chrisjoakim.springboot1;

import java.util.Map;

/**
 * This class returns application configuration values, typically specified as environment variables.
 *
 * See https://12factor.net
 * See https://12factor.net/config
 *
 * @author Chris Joakim, Microsoft
 * @date   2018/06/18
 */

public class AppConfig implements EnvVarNames {
	
	// Constants:
	public static String DEFAULT_COSMOSDB_DOCDB_DBNAME   = "dev";
	public static String DEFAULT_COSMOSDB_DOCDB_COLLNAME = "airports";
	
    public static synchronized Map<String, String> envVars() {

        return System.getenv();
    }

    public static synchronized String envVar(String name) {

        return envVars().get(name);
    }
    
    public static synchronized String envVar(String name, String defaultValue) {

        String value = envVars().get(name);
        
        if (value != null) {
        	return value;
        }
        else {
        	return defaultValue;
        }
    }
    
    public static String getDocDbAcct() {

        return System.getenv(AZURE_COSMOSDB_DOCDB_ACCT);
    }

    public static String getDocDbKey() {

        return System.getenv(AZURE_COSMOSDB_DOCDB_KEY);
    }

    public static String getDocDbUri() {

        return System.getenv(AZURE_COSMOSDB_DOCDB_URI);
    }
    
    public static String getDocDbDatabaseName() {

        return envVar(AZURE_COSMOSDB_DOCDB_DBNAME, DEFAULT_COSMOSDB_DOCDB_DBNAME);
    }

    public static String getDocDbCollName() {

        return envVar(AZURE_COSMOSDB_DOCDB_COLLNAME, DEFAULT_COSMOSDB_DOCDB_COLLNAME);
    }

    public static synchronized String storageConnectionString() {

        String s = System.getenv(AZURE_STORAGE_CONNECTION_STRING);
        return s;
    }
    
    public static String getWebappSecret() {

        return System.getenv(AZURE_WEBAPP_SECRET);
    }
    
    public static void main(String[] args) {
    	
    	// This method is for ad-hoc testing purposed only
    	
    	System.out.println(AppConfig.getWebappSecret());
    	System.out.println(AppConfig.getDocDbDatabaseName());
    	System.out.println(AppConfig.getDocDbCollName());
    }
    
}
