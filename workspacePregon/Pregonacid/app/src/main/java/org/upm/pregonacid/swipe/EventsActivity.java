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
package org.upm.pregonacid.swipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.upm.pregonacid.PregonApplication;
import org.upm.pregonacid.R;
import org.upm.pregonacid.db.ListViewEventsAdapter;
import org.upm.pregonacid.db.tables.EventDb;

import java.util.ArrayList;
import java.util.HashMap;


public class EventsActivity extends AppCompatActivity {

	private ArrayList<HashMap> list;
	private ListView lview;
	private Intent intent;
	private ListViewEventsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_section_subjects);


		Toolbar myToolbar = (Toolbar) findViewById(R.id.tToolbar);
		myToolbar.setLogo(R.drawable.ic_launcher);
		myToolbar.setTitle("Pregonacid");
		myToolbar.setSubtitle("de la Sierra");
		setSupportActionBar(myToolbar);

        
		intent = new Intent(this, TimeLineActivity.class);
		lview = (ListView) findViewById(R.id.listviewSubjects);

		lview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				intent.putExtra("POSITION", "" + position);
				startActivity(intent);


			}

		});

	}
	
	
	
	
	@Override	
	protected void onResume(){
		super.onResume();
		
		// Populate SQLlite list
		populateSubjectsFromLocal();

		adapter = new ListViewEventsAdapter(this,list);
		lview.setAdapter(adapter);
		
	}
	
	
	

	/**
	 * Populates list from local database
	 */
	private void populateSubjectsFromLocal() {
		list = new ArrayList<HashMap>();

		PregonApplication pa = (PregonApplication)getApplication();


		for (EventDb t : pa.getDb().getEvents()) {

			// Records for data
			HashMap temp = new HashMap<String, String>();
			temp.put(EventDb.KEY_ID, t.getsId());
			temp.put(EventDb.KEY_TASK_DESC, t.getsTaskDesc());
			temp.put(EventDb.KEY_TASK_ALTERNATIVE_DESC, t.getsTaskAltDesc());
			temp.put(EventDb.KEY_DESC, t.getsDesc());
			temp.put(EventDb.KEY_TASK_TIME_DURATION, t.getlTaskTimeDuration());
			temp.put(EventDb.KEY_TASK_DATE_START, t.getlTaskDateStart());
			temp.put(EventDb.KEY_TASK_ORDER, t.getiTaskOrder());
		
			list.add(temp);
		}
	}
	
	
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, EventsActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
/*
			case R.id.action_refresh:
				Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();

				break;
			case R.id.action_settings:

				Toast.makeText(this, "Action Settings selected", Toast.LENGTH_SHORT).show();
				break;

*/
			default:
				break;
		}


        return super.onOptionsItemSelected(item);
    }

	public void onClickSalir(View v) {
		finish();
		System.exit(0);
	}
	

}