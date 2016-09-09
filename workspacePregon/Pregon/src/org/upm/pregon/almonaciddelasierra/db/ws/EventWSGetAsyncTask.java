/*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * LearnTracker project 
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
package org.upm.pregon.almonaciddelasierra.db.ws;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.upm.pregon.almonaciddelasierra.db.ws.dataobjects.EventDO;
import org.upm.pregon.almonaciddelasierra.db.ws.dataobjects.EventDOList;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class EventWSGetAsyncTask extends
		AsyncTask<String, Integer, List<EventDO>> {
	
	private String CLASSNAME = this.getClass().getName();

	public List<EventDO> listSubjects(String url) {

		InputStream is = null;

		try {
			Log.d(CLASSNAME, " Querying to backend " + url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);

			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
				return null;
			}

			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(CLASSNAME, "Error in http connection " + e.toString());
		}



		Reader reader = new InputStreamReader(is);
		Gson gson = new Gson();
		EventDOList r = gson.fromJson(reader, EventDOList.class);
		List<EventDO> subjects = r.subjects;


		return subjects;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected List<EventDO> doInBackground(String... sParam) {

		Log.d(CLASSNAME, "Starting "+CLASSNAME+" task in background...");		
		List<EventDO> sOutList = new ArrayList<EventDO>();

		try {

			Log.e(CLASSNAME, "WS GET EVENT PATH [" + sParam[0].toString() + "]");
			sOutList = listSubjects(sParam[0].toString());
			Log.e(CLASSNAME, toString(sOutList));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sOutList;
	}

	@Override
	protected void onPostExecute(List<EventDO> result) {
		super.onPostExecute(result);

	}


	
    /**
     * Returns list of subjects ordered by task_order
     * @return
     */
    public List<EventDO> orderedSubjects(List<EventDO> subjects){
    	
    	EventDO[] ordered = new EventDO[subjects.size()];

    	for (EventDO subjectDO : subjects) {
    		Integer oI = new Integer(subjectDO.getSubject_task_order());
			ordered[oI.intValue()] = subjectDO;					
		}    	
    	
    	return Arrays.asList(ordered);
    }
    
    /**
     * Print items in debug mode
     * @param list
     */
    public String toString(List<EventDO> list){
    	
    	String sResult = "";
    	for (EventDO subjectDO : list) {
    		sResult+=subjectDO.toString();
		}
    	
    	return sResult;
    	
    }		
	

}
