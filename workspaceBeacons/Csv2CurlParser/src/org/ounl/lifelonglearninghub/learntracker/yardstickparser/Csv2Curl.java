/*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * Lifelong Learning Hub project 
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.ounl.lifelonglearninghub.learntracker.yardstickparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author BTB
 * 
 */
public class Csv2Curl {

	private static final String WS_URL_YARD = "https://learn-beacon.appspot.com/_ah/api/beaconendpoint/v1/beacon";
	
	private static final String CC_0 = "CC_0";
	private static final String CC_BY = "CC_BY";
	private static final String CC_BY_SA = "CC_BY_SA";
	private static final String CC_BY_ND = "CC_BY_ND";
	private static final String CC_BY_NC = "CC_BY_NC";
	private static final String CC_BY_NC_SA = "CC_BY_NC_SA";
	private static final String CC_BY_NC_ND = "CC_BY_NC_ND";

	//private static final String PATH_FILES = "/Users/BTB/Projects/3LHub/db/ScriptsAppengineDB/";
	private static final String PATH_FILES = "/Users/BTB/Projects/3LHub/Beacons/";
	
	
	public static void main(String[] args) {


		Csv2Curl obj = new Csv2Curl();
		obj.parseYardstick();					


	}
	
	/** 
	 * Parse CSV to the following subject structure
	 */
//	curl --header "Content-Type:application/json" --data "
//	{
//	    'artefact_name': '5675267779461120',
//	    'beacon_id': '5675267779461120',
//	    'beacon_cc': '5675267779461120',
//	    'location_latitude': '5675267779461120',
//	    'location_longitude': '5675267779461120',
//	    'location_country': '5675267779461120',
//	    'layer_attract_distance': '0',
//	    'layer_attract_signal': '0',
//	    'layer_attract_content': '5675267779461120',
//	    'layer_attract_cc': '5675267779461120',
//	    'layer_react_distance': '0',
//	    'layer_react_signal': '0',
//	    'layer_react_content': '5675267779461120',
//	    'layer_react_cc': '5675267779461120',
//	    'layer_interact_distance': '0',
//	    'layer_interact_signal': '0',
//	    'layer_interact_content': '5675267779461120',
//	    'layer_interact_cc': '5675267779461120',
//	    'layer_next_distance': '0',
//	    'layer_next_signal': '0',
//	    'layer_next_content': '5675267779461120',
//	    'layer_next_cc': '5675267779461120',
//	    'layer_exchange_signal': '0',
//	    'layer_exchange_content': '5675267779461120',
//	    'layer_exchange_cc': '5675267779461120'        
//	}
//	" https://learn-beacon.appspot.com/_ah/api/beaconendpoint/v1/beacon



	public void parseYardstick() {		
		String csvFile = PATH_FILES + "inputBeacons.csv";
		String outputFile = PATH_FILES + "outputBeacons.sh";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		BufferedWriter writer = null;

		try {
			File logFile = new File(outputFile);
			logFile.setExecutable(true);
			writer = new BufferedWriter(new FileWriter(logFile));
			
			System.out.println(logFile.getCanonicalPath());

			br = new BufferedReader(new FileReader(csvFile));
			
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] country = line.split(cvsSplitBy);

				String sCommand =
						"curl --header \"Content-Type:application/json\" --data \"  \n" +
						"{ \n" +
	   					"'artefact_name': '" + country[1] + "', \n" +
	   					"'beacon_id': '', \n" +
	   					"'beacon_cc': '"+CC_BY+"', \n" +
	   					"'location_latitude': '" + country[4] + "', \n" +
	   					"'location_longitude': '" + country[3] + "', \n" +
	   					"'location_country': '" + country[5] + "', \n" +
	   					"'layer_attract_distance': '', \n" +
	   					"'layer_attract_signal':  '', \n" +
	   					"'layer_attract_content':  '', \n" +
	   					"'layer_attract_cc':  '', \n" +
	   					"'layer_react_distance':  '', \n" +
	   					"'layer_react_signal':  '', \n" +
	   					"'layer_react_content':  '', \n" +
	   					"'layer_react_cc':  '', \n" +
	   					"'layer_interact_distance':  '10', \n" +
	   					"'layer_interact_signal':  0, \n" +
	   					"'layer_interact_content':  '" + country[2] + "', \n" +
	   					"'layer_interact_cc':  '"+CC_BY+"', \n" +
	   					"'layer_next_distance':  '', \n" +
	   					"'layer_next_signal':  '', \n" +
	   					"'layer_next_content':  '', \n" +
	   					"'layer_next_cc':  '', \n" +
	   					"'layer_exchange_signal':  '', \n" +
	   					"'layer_exchange_content':  '', \n" +
	   					"'layer_exchange_cc':  '' \n" +				
						"} \n" +
					    "\" " + WS_URL_YARD +
						"\n\n";

		            // This will output the full path where the file will be written to...
		            System.out.println(sCommand);
		            
		            writer.append(sCommand);	
				
				
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {
	            }				
			}
		}

		System.out.println("Done! Parsed file "+csvFile+" to curl script: "+outputFile);
	}
	

}