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
package org.upm.pregonacid.db.tables;

import android.content.ContentValues;
import android.database.Cursor;

import org.upm.pregonacid.db.ws.dataobjects.EventDO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDb implements ITables {

	public static final String TABLE_NAME = "subject";


	public static final String KEY_ID = "id";
	public static final String KEY_DESC = "subject_desc";
	public static final String KEY_TASK_DESC = "subject_task_desc";
	public static final String KEY_TASK_ALTERNATIVE_DESC = "subject_task_alternative_desc";
	public static final String KEY_TASK_DATE_START = "subject_task_date_start";
	public static final String KEY_TASK_TIME_DURATION = "subject_task_time_duration";
	public static final String KEY_TASK_LEVEL = "subject_task_level";
	public static final String KEY_TASK_ORDER = "subject_task_order";

	private String sId;
	private String sDesc;
	private String sTaskDesc;
	private String sTaskAltDesc;
	private long lTaskDateStart;
	private long lTaskTimeDuration;
	private int iTaskLevel;
	private int iTaskOrder;

	public EventDb(String sId, String sDesc, String sTaskDesc, String sTaskAltDesc,
			long lTaskDateStart, long lTaskTimeDuration, 
			int iTaskLevel, int iTaskOrder) {
		super();
		this.sId = sId;
		this.sDesc = sDesc;
		this.sTaskDesc = sTaskDesc;
		this.sTaskAltDesc = sTaskAltDesc;
		this.lTaskDateStart = lTaskDateStart;
		this.lTaskTimeDuration = lTaskTimeDuration;
		this.iTaskLevel = iTaskLevel;
		this.iTaskOrder = iTaskOrder;
	}

	public EventDb(Cursor c) {

		this.sId = c.getString(0);
		this.sDesc = c.getString(1);
		this.sTaskDesc = c.getString(2);
		this.sTaskAltDesc = c.getString(3);
		this.lTaskDateStart = Long.parseLong(c.getString(4));
		this.lTaskTimeDuration = Long.parseLong(c.getString(5));
		this.iTaskLevel = Integer.parseInt(c.getString(6));
		this.iTaskOrder = Integer.parseInt(c.getString(7));

	}
	
	public EventDb(EventDO edo) {

		this.sId = edo.getId();
		this.sDesc = edo.getSubject_desc();
		this.sTaskDesc = edo.getSubject_task_desc();
		this.sTaskAltDesc = "pepito juano";
		
		try{
			this.lTaskDateStart = Long.valueOf(edo.getSubject_task_level());
		}catch(Exception e){
			this.lTaskDateStart = new Date().getTime();
		}		


		try{
			this.lTaskTimeDuration = Long.valueOf(edo.getSubject_task_time_duration());
		}catch(Exception e){
			this.lTaskTimeDuration = 44l;
		}

		
		try{
			Integer oI = Integer.valueOf(edo.getSubject_task_level());
			this.iTaskLevel = oI.intValue();
		}catch(Exception e){
			this.iTaskLevel = 0;
		}

		
		try{
			Integer oI = Integer.valueOf(edo.getSubject_task_order());
			this.iTaskOrder = oI.intValue();
		}catch(Exception e){
			this.iTaskOrder = 0;
		}		


	}	

	/**
	 * 
	 * @return duration in minutes
	 */
	public int getTaskDurationInMins() {
		

		int minutes = (int) ((lTaskTimeDuration / 1000) / 60);
		return minutes;
	}

	public static String getCreateTable() {

		String sSQL = "CREATE TABLE " + TABLE_NAME + "(" 
				+ KEY_ID + " integer primary key autoincrement," 
				+ KEY_DESC + " TEXT, " 
				+ KEY_TASK_DESC + " TEXT, " 
				+ KEY_TASK_ALTERNATIVE_DESC + " TEXT, " 
				+ KEY_TASK_DATE_START + " INTEGER,"
				+ KEY_TASK_TIME_DURATION + " INTEGER," 
				+ KEY_TASK_LEVEL + " INTEGER," 
				+ KEY_TASK_ORDER + " INTEGER" 
				+ ")";

		return sSQL;

	}

	public void loadContentValues(ContentValues cv) {

		cv.put(KEY_ID, sId);
		cv.put(KEY_DESC, sDesc);
		cv.put(KEY_TASK_DESC, sTaskDesc);
		cv.put(KEY_TASK_ALTERNATIVE_DESC, sTaskAltDesc);
		cv.put(KEY_TASK_DATE_START, lTaskDateStart);
		cv.put(KEY_TASK_TIME_DURATION, lTaskTimeDuration);
		cv.put(KEY_TASK_LEVEL, iTaskLevel);
		cv.put(KEY_TASK_ORDER, iTaskOrder);

	}

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public String getsDesc() {
		return sDesc;
	}

	public void setsDesc(String sDesc) {
		this.sDesc = sDesc;
	}

	public String getsTaskDesc() {
		return sTaskDesc;
	}

	public void setsTaskDesc(String sTaskDesc) {
		this.sTaskDesc = sTaskDesc;
	}

	public long getlTaskDateStart() {
		return lTaskDateStart;
	}

	public String getFormattedTaskDateStart() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date dDateStart = new Date(lTaskDateStart);

		return df.format(dDateStart);
	}

	public void setlTaskDateStart(long lTaskDateStart) {
		this.lTaskDateStart = lTaskDateStart;
	}

	public long getlTaskTimeDuration() {
		return lTaskTimeDuration;
	}

	public void setlTaskTimeDuration(long lTaskTimeDuration) {
		this.lTaskTimeDuration = lTaskTimeDuration;
	}

	public int getiTaskLevel() {
		return iTaskLevel;
	}

	public void setiTaskLevel(int iTaskLevel) {
		this.iTaskLevel = iTaskLevel;
	}

	public int getiTaskOrder() {
		return iTaskOrder;
	}

	public void setiTaskOrder(int iTaskOrder) {
		this.iTaskOrder = iTaskOrder;
	}

	public String getsTaskAltDesc() {
		return sTaskAltDesc;
	}

	public void setsTaskAltDesc(String sTaskAltDesc) {
		this.sTaskAltDesc = sTaskAltDesc;
	}

}
