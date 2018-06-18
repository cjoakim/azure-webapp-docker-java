package com.chrisjoakim.springboot1;

/**
 * This interface defines the environment variable names used in this project.
 *
 * @author Chris Joakim, Microsoft
 * @date   2018/06/18
 */

public interface EnvVarNames {

    public static final String AZURE_COSMOSDB_DOCDB_ACCT                = "AZURE_COSMOSDB_DOCDB_ACCT";
    public static final String AZURE_COSMOSDB_DOCDB_COLLNAME            = "AZURE_COSMOSDB_DOCDB_COLLNAME";
    public static final String AZURE_COSMOSDB_DOCDB_CONN_STRING         = "AZURE_COSMOSDB_DOCDB_CONN_STRING";
    public static final String AZURE_COSMOSDB_DOCDB_DBNAME              = "AZURE_COSMOSDB_DOCDB_DBNAME";
    public static final String AZURE_COSMOSDB_DOCDB_KEY                 = "AZURE_COSMOSDB_DOCDB_KEY";
    public static final String AZURE_COSMOSDB_DOCDB_URI                 = "AZURE_COSMOSDB_DOCDB_URI";

    public static final String AZURE_SERVICEBUS_ACCESS_KEY              = "AZURE_SERVICEBUS_ACCESS_KEY";
    public static final String AZURE_SERVICEBUS_CONN_STRING             = "AZURE_SERVICEBUS_CONN_STRING";
    public static final String AZURE_SERVICEBUS_KEY_NAME                = "AZURE_SERVICEBUS_KEY_NAME";
    public static final String AZURE_SERVICEBUS_NAMESPACE               = "AZURE_SERVICEBUS_NAMESPACE";
    public static final String AZURE_SERVICEBUS_QUEUE                   = "AZURE_SERVICEBUS_QUEUE";

    public static final String AZURE_STORAGE_ACCOUNT                    = "AZURE_STORAGE_ACCOUNT";
    public static final String AZURE_STORAGE_CONNECTION_STRING          = "AZURE_STORAGE_CONNECTION_STRING";
    public static final String AZURE_STORAGE_KEY                        = "AZURE_STORAGE_KEY";

    public static final String AZURE_WEBAPP_SECRET                      = "AZURE_WEBAPP_SECRET";
    
    public static final String JAVA_HOME                                = "JAVA_HOME";
    public static final String PATH                                     = "PATH";
    public static final String USER                                     = "USER";
}
