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
package org.upm.pregon.almonaciddelasierra;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.upm.pregon.almonaciddelasierra.db.DatabaseHandler;
import org.upm.pregon.almonaciddelasierra.db.tables.EventDb;
import org.upm.pregon.almonaciddelasierra.db.ws.EventWSGetAsyncTask;
import org.upm.pregon.almonaciddelasierra.db.ws.dataobjects.EventDO;
import org.upm.pregon.almonaciddelasierra.swipe.EventsActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LoadingScreenActivity extends Activity {
	
	private String CLASSNAME = this.getClass().getName();
	
	PregonApplication pa;
	List<EventDb> listEventsDb;	
	
// Make http request periodically	
//	http://stackoverflow.com/questions/20501225/using-service-to-run-background-and-create-notification
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
				
		pa = (PregonApplication)getApplication();
		
		// Load properties file from asset folder
		try {
			pa.setConfig(loadConfigProperties(Constants.CONFIG_PROPERTIES_FILE));				
		} catch (IOException e) {
			Log.e(CLASSNAME, "Could not load properties file "+e.getMessage());
			e.printStackTrace();
		}
		
		// Init data base handler
		DatabaseHandler db = new DatabaseHandler(this);
		pa = (PregonApplication)getApplicationContext();
		pa.setDb(db);
		
		
		startService(new Intent(this, MyService.class));
		

	}
	
	@Override
	protected void onResume() {
		super.onResume();


		listEventsDb = pa.getDb().getEvents();
		if (listEventsDb.size() == 0) {
			// Load data from backend into local db and session
			populateEventsFromBackend("N35231");
		} else {
			// Load into session
			pa.setEvents(listEventsDb);
		}	
			

		// Fill in form data
		TextView tvUserId = (TextView) findViewById(R.id.tvPropUserId);
		TextView tvCourseId = (TextView) findViewById(R.id.tvPropCourseId);
		TextView tvVers = (TextView) findViewById(R.id.tvPropVersion);								
		tvUserId.setText("User: Miguel Porlán Chendo");
		tvCourseId.setText("Course: Nivel 2");
		tvVers.setText("Version: Nivel 3");		

	}
	

		
	
	/**
	 * Populates list from backend database. 
	 * 1) Save list into session
	 * 2) Save list into local database
	 * 
	 * This method is only executed once when configuration is loaded for first time
	 */
	private void populateEventsFromBackend(String sCoursId) {

		Log.d(CLASSNAME, "Loading events "+sCoursId+" .");		
		List<EventDO> lista;		
		EventWSGetAsyncTask wsat = new EventWSGetAsyncTask();
		
		try {
			
			String sURL = pa.getConfig().getProperty(Constants.CP_WS_GET_EVENTS_PATH);
			sURL+=sCoursId;			
			
			lista = wsat.execute(sURL).get();
			
			if (lista != null){
				Log.d(CLASSNAME, "Backend returned list with "+lista.size()+" events.");
				
				// Save events into local database
				pa.db.addListEventsDO(lista);
								
				// Save activities into session
				pa.setEventsDO(lista);				
				
				if(lista.size() < 1){
					Log.e(CLASSNAME, "Event list is empty");
					Toast.makeText(getApplicationContext(),
							"Backend returned empty list of events.", Toast.LENGTH_LONG)
							.show();
					
//					// Subjcts could not be loaded from backend. Load fake data
//					// Init database with fake data
//					List<EventDb> ldb = Session.getSingleInstance().getDatabaseHandler().addDefaultSubjects();
//					// Init session with fake data
//					Session.getSingleInstance().initActivitiesDb(ldb);
					
				}
				
			}else{
				Log.e(CLASSNAME, "Backend returned empty list of subjects.");
			}
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
				
	}
	
	

	public void onClickSwipeButton(View v) {
		Intent intent = new Intent(this, EventsActivity.class);
		startActivity(intent);
	}
	
	public void onClickMenu(View v) {
		Log.e(CLASSNAME, "User clicked Menu icon");		
	}	

	
	public void onClickUser(View v) {
		Log.e(CLASSNAME, "User clicked User icon ");		
	}	

	
	
	/**
	 * Load properties
	 * 
	 * @return list of poperties loaded
	 * @throws IOException
	 */
	private Properties loadConfigProperties(String sFile) throws IOException {
		String[] fileList = { sFile };
		Properties prop = new Properties();
		for (int i = fileList.length - 1; i >= 0; i--) {
			String file = fileList[i];
			try {
				InputStream fileStream = getAssets().open(file);
				prop.load(fileStream);
				fileStream.close();
			} catch (FileNotFoundException e) {
				Log.e(CLASSNAME, "Ignoring missing property file " + file);
			}
		}
		return prop;
	}
	
	
}