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
package org.upm.pregon.almonaciddelasierra.swipe;

import java.util.ArrayList;
import java.util.List;

import org.upm.pregon.almonaciddelasierra.PregonApplication;
import org.upm.pregon.almonaciddelasierra.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


public class TimeLineActivity extends FragmentActivity {
	
	private String CLASSNAME = this.getClass().getName();

	private EventFragmentStatePagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
    private List<EventFragment> lDayFragments;
    PregonApplication pa;
    

    private int iCurrentPos = 0;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_demo);
        
        pa = (PregonApplication)getApplication();        

        lDayFragments = new ArrayList<EventFragment>();
        
        Bundle extras = getIntent().getExtras();   
        if (extras != null) {
            String sPosition =  extras.getString("POSITION");
            Integer oiPosition = new Integer(sPosition);
            iCurrentPos = oiPosition.intValue();
        }                        
        
        for (int i = 0; i < pa.getDb().getEvents().size(); i++) {
        	EventFragment dayF = new EventFragment();
        	
            Bundle args = new Bundle();
            args.putInt(EventFragment.ARG_POSITION, i);
            args.putString(EventFragment.ARG_TOP_TEXT, pa.getEvents().get(i).getsTaskDesc());
            args.putString(EventFragment.ARG_LONG_TEXT, pa.getEvents().get(i).getsTaskAltDesc());

            dayF.setArguments(args);
            
            lDayFragments.add(dayF);
            
		}
        
        
        // Navigation onOptionsItemSelected
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);        

        
    }
    
	@Override	
	protected void onResume(){
		super.onResume();
		Log.d(CLASSNAME, "onResume TimeLineActivity.");		
		
        mDemoCollectionPagerAdapter = new EventFragmentStatePagerAdapter(getSupportFragmentManager(), lDayFragments, pa.getEvents());        
        mDemoCollectionPagerAdapter.notifyDataSetChanged();        
        
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mViewPager.setCurrentItem(iCurrentPos);
       		
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
        }
        return super.onOptionsItemSelected(item);
    }

    
    
    
	public void onClickSwitchAction(View v) {
		Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
		whatsappIntent.setType("text/plain");
		whatsappIntent.setPackage("com.whatsapp");
		whatsappIntent.putExtra(Intent.EXTRA_TEXT,
				"The text you wanted to share");
		try {
			startActivity(whatsappIntent);
		} catch (android.content.ActivityNotFoundException ex) {
			// This message should be returned to the user
			System.out.println("Whatsapp have not been installed.");
		}

	}


}
