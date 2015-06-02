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

	private static final String WS_URL_YARD = "https://lifelong-learning-hub.appspot.com/_ah/api/subjectendpoint/v1/subject";
	private static final String WS_URL_USER = "https://lifelong-learning-hub.appspot.com/_ah/api/userendpoint/v1/user";

	private static final String PARAM_SUBJECTS = "S";
	private static final String PARAM_USERS = "U";

	private static final String COURSE = "OUNL_N35231";
	private static final String PATH_FILES = "/Users/BTB/Projects/3LHub/db/ScriptsAppengineDB/";
	
	
	public static void main(String[] args) {

		String sParam = "";
		try{
			sParam = args[0];
			
			if(sParam.contains(PARAM_SUBJECTS) || sParam.contains(PARAM_USERS)){
				
				if(sParam.contains(PARAM_SUBJECTS)){
					Csv2Curl obj = new Csv2Curl();
					obj.parseYardstick();					
				}
				if(sParam.contains(PARAM_USERS)){
					Csv2Curl obj = new Csv2Curl();
					obj.parseUsers();					
				}
				
			}else{
				System.out.println("Usage java Csv2Curl -S | -U | -SU");
			}
			
		}catch (Exception e){
			System.out.println("Usage java Csv2Curl -S | -U | -SU");
		}
		

	}
	
	/** 
	 * Parse CSV to the following subject structure
	 */
//		curl --header "Content-Type:application/json" --data "
//		{
//		    'subject_desc': 'N35231',
//		    'subject_task_desc': 'Introduction Lecture GIS course',
//		    'subject_task_alternative_desc': 'Block 0 [Lecture]',
//		    'subject_task_date_start': 1415836800000,
//		    'subject_task_time_duration': 7200000,
//		    'subject_task_level': '0',
//		    'subject_task_order': '0'
//		    
//		}
//		" https://lifelong-learning-hub.appspot.com/_ah/api/subjectendpoint/v1/subject
	 

	public void parseYardstick() {		
		String csvFile = PATH_FILES + "inputY.csv";
		String outputFile = PATH_FILES + "Output_Yardstick_"+COURSE+".sh";
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
			
			int iOrder = 0;
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] country = line.split(cvsSplitBy);

				String sCommand =
						"curl --header \"Content-Type:application/json\" --data \"  \n" +
						"{ \n" +
					    "'subject_desc': '"+COURSE+"',  \n" +
					    "'subject_task_desc': '" + country[0] + "', \n" +
					    "'subject_task_alternative_desc': '" + country[3] + "', \n" +
					    "'subject_task_date_start': " + country[4] + ", \n" +
					    "'subject_task_time_duration': " + country[5] + ", \n" +
					    "'subject_task_level': '0', \n" +
					    "'subject_task_order': '"+iOrder+"' \n" +				
						"} \n" +
					    "\" " + WS_URL_YARD +
						"\n\n";

		            // This will output the full path where the file will be written to...
		            System.out.println(sCommand);
		            

		            
		            writer.append(sCommand);	
		            iOrder++;
				
				
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
	
	

	/**
	 * Parse CSV file to the following user curl scritp
	 */
//	curl --header "Content-Type:application/json" --data "
//	{
//		'user_name':'Annemiek Bloemendaal',
//		'subject_desc':'N35231',
//		'user_type':'300'
//	}
//	" https://lifelong-learning-hub.appspot.com/_ah/api/userendpoint/v1/user
	public void parseUsers() {		
		String csvFile = PATH_FILES + "inputU.csv";
		String outputFile = PATH_FILES + "Output_Users_"+COURSE+".sh";
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
					    "'user_name': '" + country[0] + "', \n" +
					    "'subject_desc': '" + COURSE + "', \n" +
					    "'user_type': '" + country[1] + "' \n" +		
						"} \n" +
					    "\" " + WS_URL_USER +
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