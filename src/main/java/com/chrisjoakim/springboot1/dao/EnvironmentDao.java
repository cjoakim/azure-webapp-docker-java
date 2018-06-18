package com.chrisjoakim.springboot1.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.chrisjoakim.springboot1.EnvVarNames;

/**
 * Simple DAO class used by the web layer to obtain configuration values.
 *
 * See https://12factor.net
 * See https://12factor.net/config
 *
 * @author Chris Joakim, Microsoft
 * @date   2018/06/18
 */

public class EnvironmentDao extends Object implements EnvVarNames {

	public EnvironmentDao() {

		super();
	}
	
	public HashMap<String, String> getHelloInfo() {
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("message", "hello from Spring Boot web app");	
		return hash;
	}
	
	public HashMap<String, String> getTimeInfo() {
		
		Date d = new Date();
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("date", d.toLocaleString());
		hash.put("gmt", d.toGMTString());
		hash.put("epoch", new Long(d.getTime()).toString());
		return hash;
	}
	
	public HashMap<String, String> getEnvironmentVariables() {
		
		Date d = new Date();
		HashMap<String, String> hash = new HashMap<String, String>();
		
		for (String name : azureEnvVars()) {
			hash.put(name, "" + System.getenv(name));
		}
		for (String name : otherEnvVars()) {
			hash.put(name, "" + System.getenv(name));
		}
		return hash;
	}
	
	public ArrayList<String> azureEnvVars() {
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(AZURE_COSMOSDB_DOCDB_ACCT);
		list.add(AZURE_COSMOSDB_DOCDB_DBNAME);
		list.add(AZURE_COSMOSDB_DOCDB_KEY);
		list.add(AZURE_COSMOSDB_DOCDB_URI);
		return list;
	}
	
	public ArrayList<String> otherEnvVars() {
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(JAVA_HOME);
		list.add(PATH);
		list.add(USER);
		return list;
	}
}
