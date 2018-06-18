package com.chrisjoakim.springboot1.utils;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisjoakim.io.FileUtil;
import com.chrisjoakim.springboot1.AppConfig;
import com.chrisjoakim.springboot1.dao.CosmosDbDao;
import com.chrisjoakim.utils.CommandLineArgs;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.documentdb.Document;

/**
 * This class is used to perform batch/bulk/ad-hoc CosmosDB operations.
 * See 'load_airports.sh' where this program is executed.
 * 
 * @author Chris Joakim, Microsoft
 * @date   2018/06/18
 */

public class CosmosDbUtil {

    // Class variables:
    private static final Logger logger = LoggerFactory.getLogger(CosmosDbUtil.class);
	private static CosmosDbDao dao = null;
    
	public CosmosDbUtil() {
		
		super();
	}

	public static void main(String[] args) {

		CommandLineArgs clArgs = new CommandLineArgs(args);
        String function = clArgs.stringArg("--function", "none");
        logger.warn("main; function: " + function);
        
        String  infile   = clArgs.stringArg("--infile",  "data/public/world_airports_flat.json");
        String  dbName   = clArgs.stringArg("--dbname",   AppConfig.getDocDbDefaultDbName());
        String  collName = clArgs.stringArg("--collname", AppConfig.getDocDbDefaultCollName());
        long    pauseMs  = clArgs.longArg("--pauseMs", 10);
        
        switch(function) {
	        case "load_airports":
	        	loadAirports(infile, dbName, collName, pauseMs);
	        	break;
	        default:
	            logger.warn("main; unknown function: " + function);
	            break;
        }
    }

    private static void loadAirports(String infile, String dbName, String collName, long pauseMs) {
    	
    	try {
    		createDao();
        	HashMap<String, Object> iataCodes = new HashMap<String, Object>();
			List<String> airports = new FileUtil().readFileLines(infile);
			
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
						Document doc = dao.insertDocument(dbName, collName, airport);
						logger.warn("loaded: " + doc);
						pause(pauseMs);
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
	
    private static void createDao() {
    	
    	if (dao != null) {
    		try {
    			dao = new CosmosDbDao();
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    			logger.error("EXCEPTION WHEN CREATING CosmosDbDao INSTANCE, PROGRAM TERMINATING.");
    			System.exit(1);
    		}
    	}
    }
    
	private static Map<String, Object> parseJsonLineToMap(String line) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, Object>> typeRef 
			= new TypeReference<HashMap<String, Object>>() {};
		return mapper.readValue(line, typeRef);
	}

    private static void pause(long ms) {

        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            // ignore
        }
    }
}
