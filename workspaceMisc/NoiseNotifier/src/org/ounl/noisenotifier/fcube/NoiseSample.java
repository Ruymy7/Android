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
package org.ounl.noisenotifier.fcube;

import android.content.ContentValues;
import android.database.Cursor;

public class NoiseSample{
	
	public static final String TABLE_NAME = "noisesample";
	
	public static final String KEY_TIMESTAMP= "timestamp";
	public static final String KEY_DECIBELS = "decibels";
	public static final String KEY_DECIBELSAVG = "decibelsavg";
	public static final String KEY_TAG = "tag";
    
	private long lTimestamp;
	private double dDecibels;
	private double dDecibelsavg;
	private String sTag;
	
	
	public NoiseSample(double dDecib, double dDecibavg, long lTimes, String sTag) {
		
		this.lTimestamp = lTimes;
		this.dDecibels = dDecib;
		this.dDecibelsavg = dDecibavg;
		this.sTag = sTag;
	}
	
//	public NoiseSample (Cursor c){
//        
//		this.lTimestamp = Long.parseLong(c.getString(0));
//        this.dDecibels = Double.parseDouble(c.getString(1));        
//        this.iAlertLevel = Integer.parseInt(c.getString(2));
//        		
//	}	
	
	public NoiseSample(Cursor c) {

		this.lTimestamp = c.getInt(0);
		this.dDecibels = c.getDouble(1);
		this.dDecibelsavg = c.getDouble(2);
		this.sTag = c.getString(3);

	}	
	

	
	public static String getCreateTable(){
		
        String sSQL = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_TIMESTAMP + " INTEGER PRIMARY KEY," 
        		+ KEY_DECIBELS + " REAL, "
        		+ KEY_DECIBELSAVG + " REAL, "
                + KEY_TAG + " TEXT" + ")";
		
		return sSQL;
		
	}

	/**
	 * @return the lTimestamp
	 */
	public long getlTimestamp() {
		return lTimestamp;
	}

	/**
	 * @param lTimestamp the lTimestamp to set
	 */
	public void setlTimestamp(long lTimestamp) {
		this.lTimestamp = lTimestamp;
	}

	/**
	 * @return the dDecibels
	 */
	public double getdDecibels() {
		return dDecibels;
	}

	/**
	 * @param dDecibels the dDecibels to set
	 */
	public void setdDecibels(double dDecibels) {
		this.dDecibels = dDecibels;
	}

	/**
	 * @return the sTag
	 */
	public String getTag() {
		return sTag;
	}

	/**
	 * @param sTag the sTag to set
	 */
	public void setTag(String sTag) {
		this.sTag = sTag;
	}
	

	public void loadContentValues(ContentValues cv) {

		 cv.put(KEY_TIMESTAMP, getlTimestamp());
		 cv.put(KEY_DECIBELS, getdDecibels());
		 cv.put(KEY_DECIBELSAVG, getdDecibelsavg());
		 cv.put(KEY_TAG, getTag());
		 			
	}

	/**
	 * @return the dDecibelsavg
	 */
	public double getdDecibelsavg() {
		return dDecibelsavg;
	}

	/**
	 * @param dDecibelsavg the dDecibelsavg to set
	 */
	public void setdDecibelsavg(double dDecibelsavg) {
		this.dDecibelsavg = dDecibelsavg;
	}


}
