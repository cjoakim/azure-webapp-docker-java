package com.chrisjoakim.springboot1.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisjoakim.springboot1.AppConfig;
import com.chrisjoakim.springboot1.dao.CosmosDbDao;
import com.chrisjoakim.utils.CommandLineArgs;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.mail.util.MailLogger;

// com.chrisjoakim.springboot1.utils.CosmosDbUtil

public class CosmosDbUtil {

    // Class variables
    private static final Logger logger = LoggerFactory.getLogger(CosmosDbUtil.class);

    
	public CosmosDbUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		CommandLineArgs clArgs = new CommandLineArgs(args);
        String function = clArgs.stringArg("--function", "none");
        logger.warn("Main.main; function: " + function);
        
        String  infile   = clArgs.stringArg("--infile",  "data/public/world_airports_flat.json");
        boolean useDb    = clArgs.booleanArg("--use-db",  false);
        String  dbName   = clArgs.stringArg("--dbname",   AppConfig.getDocDbDefaultDbName());
        String  collName = clArgs.stringArg("--collname", AppConfig.getDocDbDefaultCollName());
        long    pauseMs  = clArgs.longArg("--pause", 50);
        
        switch(function) {
        case "load_airports":
        	loadAirports(infile, dbName, collName, pauseMs, useDb);
        	break;
        }

    }

    private static void loadAirports(String infile, String dbName, String collName, long pauseMs, boolean useDb) {
    
    	CosmosDbDao dao = null;
    	
		try {
			dao = new CosmosDbDao();
		}
		catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
    	
    	try {
        	HashMap<String, Object> iataCodes = new HashMap<String, Object>();

			List<String> airports = readFileLines(infile);
			
	        for (int i = 0; i < airports.size(); i++) {
	        	String line = airports.get(i);
	        	logger.warn("processLine: " + line);
	        	Map<String, Object> airport = parseJsonLineToMap(line);
	        	
				String iata = ((String) airport.getOrDefault("iata_code", "")).trim();
				if (iata.length() > 2) {
					airport.put("pk", iata);
					airport.put("id", UUID.randomUUID().toString());
					airport.put("epoch", "" + Instant.now().toEpochMilli());
					
					if (iataCodes.containsKey(iata)) {
						logger.warn("duplicate iata code: " + iata);
					}
					else {
						logger.warn("loading: " + airport);
						//Document doc = dao.insertDocument(String dbName, String collName, Object obj) 
					}
				}
	        }
	        
	        
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	finally {
    		logger.warn("closing dao");
			dao.close();
    		logger.warn("dao closed");
		}
    }
    

	private static List<String> readFileLines(String filename) throws Exception {
		
		List<String> lines = new ArrayList<String>();
		
		try (Stream<String> stream = Files.lines(Paths.get(filename))) {
			stream.forEach(lines::add);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	private static Map<String, Object> parseJsonLineToMap(String line) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, Object>> typeRef 
			= new TypeReference<HashMap<String, Object>>() {};
		return mapper.readValue(line, typeRef);
	}
	
}
