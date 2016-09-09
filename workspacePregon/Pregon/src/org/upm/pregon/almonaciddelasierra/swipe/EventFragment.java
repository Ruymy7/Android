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

import org.upm.pregon.almonaciddelasierra.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class EventFragment extends Fragment {
	
	private String CLASSNAME = this.getClass().getName();

	public static final String ARG_POSITION = "position";
	
	public static final String ARG_TITLE = "title";
	public static final String ARG_TOP_TEXT = "toptext";
	public static final String ARG_LONG_TEXT = "longtext";
	

	private View rootView;
	private int mPosition = 0;
	private String mTitle = "";
	private String mTopText = "";
	private String mLongText = "";
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_collection_object, container, false);
		
		Bundle args = getArguments();
		mPosition = args.getInt(ARG_POSITION);
		mTitle = (String)args.getString(ARG_TITLE);
		mTopText = (String)args.getString(ARG_TOP_TEXT);
		mLongText = (String)args.getString(ARG_LONG_TEXT);
		
		
		inflateLayout();
				
		Log.d(CLASSNAME, "onCreateView EventFragment item position["+mPosition+"] title["+mTitle+"]  TopText["+mTopText+"]  LongText["+mLongText+"].");
				
		
		return rootView;
		
	}
	

	
	public void inflateLayout(){
		
		TextView tTop = (TextView) rootView.findViewById(R.id.tvTopText);
		tTop.setText(mTopText);

		TextView t = (TextView) rootView.findViewById(R.id.tvLongText);
		t.setText(mLongText);

	}
	
	
	public int getPosition(){
		return mPosition;
	}
	
	

	
}