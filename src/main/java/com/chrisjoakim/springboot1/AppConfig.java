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

    public static synchronized Map<String, String> envVars() {

        return System.getenv();
    }

    public static synchronized String envVar(String name) {

        return envVars().get(name);
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
    
    public static String getDocDbDefaultDbName() {

        return System.getenv(AZURE_COSMOSDB_DOCDB_DBNAME);
    }

    public static String getDocDbDefaultCollName() {

        return System.getenv(AZURE_COSMOSDB_DOCDB_COLLNAME);
    }

    public static synchronized String storageConnectionString() {

        String s = System.getenv(AZURE_STORAGE_CONNECTION_STRING);
        return s;
    }
}
