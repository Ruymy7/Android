/*******************************************************************************
 * Copyright (C) 2015 Open University of The Netherlands
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
package org.ounl.lifelonglearninghub.nfcecology.fcube.commands;

import java.net.MalformedURLException;
import java.net.URL;

import org.ounl.lifelonglearninghub.nfcecology.fcube.config.FeedbackCubeConfig;


public class FCMelody1 implements IFeedbackCubeCommnads{
	/**
	 * > PUT /speaker/beep/ HTTP/1.1         : Plays meolody
	 */	
	private static final String WS_PATH = "/speaker/melody/1/";
	private String ipAdress = "";
	
	
	public FCMelody1(String sIp){
		ipAdress = sIp;
	}
	
	private String getCommand(){		
		
		return FeedbackCubeConfig.URL_PREFIX + ipAdress + WS_PATH; 
	}
	

	public URL getUrlCommand(){
		try {
			return new URL(getCommand());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString(){
		return "COMMAND OFF: URL["+getUrlCommand().toString()+"] COMMAND["+getCommand()+"] HAS PARAMS:["+hasParams()+"] PARAMS:["+getParams()+"] METHOD:["+getHttpMethod()+"]";
	}

	@Override
	public boolean hasParams() {
		return false;
	}

	
	@Override
	public String getParams() {
		return "";
	}
	
	@Override
	public String getHttpMethod() {		
		return IFeedbackCubeCommnads.HTTP_METHOD_PUT;
	}

	@Override
	public String getAction() {
		return ACTION_MELODY1;
	}

	@Override
	public String getWSPath() {
		return WS_PATH;
	}
	

}
