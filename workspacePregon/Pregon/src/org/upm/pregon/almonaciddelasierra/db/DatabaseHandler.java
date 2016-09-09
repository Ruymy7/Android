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
package org.upm.pregon.almonaciddelasierra.db;

import java.util.ArrayList;
import java.util.List;

import org.upm.pregon.almonaciddelasierra.db.tables.EventDb;
import org.upm.pregon.almonaciddelasierra.db.ws.dataobjects.EventDO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private String CLASSNAME = this.getClass().getName();
	
	//private static final int DATABASE_VERSION = 1;
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "bernauersdatabase.db";
	private SQLiteDatabase sqliteDB = null;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		sqliteDB = this.getWritableDatabase();
		sqliteDB.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		sqliteDB = db;
		
		Log.d(CLASSNAME, "Creating table subjects...");
		db.execSQL(EventDb.getCreateTable());
		
		Log.d(CLASSNAME, "Tables created!!");
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + EventDb.TABLE_NAME);
		
		onCreate(db);
	}



	/**
	 * Insert subject into db
	 * 
	 * @param o
	 */
	public void addEvent(EventDb o) {

		sqliteDB = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		o.loadContentValues(values);
		try {
			sqliteDB.insert(EventDb.TABLE_NAME, null, values);
		} catch (Exception e) {
			Log.e("DatabaseHandler", "Error inserting subject " + e.toString()
					+ "");
			e.printStackTrace();
		}
		sqliteDB.close();
	}

	/**
	 * Insert list of subjects into db
	 * 
	 * @param goals
	 */
	public void addSubject(List<EventDb> subjects) {

		for (int i = 0; i < subjects.size(); i++) {
			addEvent(subjects.get(i));
		}

	}

	
	/**
	 * Insert list of subject data objects into local db
	 * 
	 * @param goals
	 */
	public void addListEventsDO(List<EventDO> events) {

		for (int i = 0; i < events.size(); i++) {
			addEvent(events.get(i).toSqliteObject());
		}

	}
	
	
	
	/**
	 * Get subjects from database ordered by order
	 * 
	 * @return
	 */
	public List<EventDb> getEvents() {
		List<EventDb> list = new ArrayList<EventDb>();
		String selectQuery = "SELECT  * FROM " + EventDb.TABLE_NAME + " ORDER BY "+EventDb.KEY_TASK_ORDER;
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				EventDb o = new EventDb(cursor);
				list.add(o);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return list;
	}

	
	
	/**
	 * Loads default course into local database whenever there is no connection to backend
	 * 
	 */
	public List<EventDb> addDefaultSubjects(){
		
		List<EventDb> lSDb = new ArrayList<EventDb>();
		
		
		EventDb sdb0 = new EventDb("1000", "Default Spanish Course","Gramatica", "Self study", 1479945600000L, 18000000L, 1, 0);
		EventDb sdb1 = new EventDb("1001", "Default Spanish Course","Escuchar", "Self study", 1480118400000L, 10800000L, 1, 1);
		EventDb sdb2 = new EventDb("1002", "Default Spanish Course","Hablar", "Self study", 1480291200000L, 7200000L, 1, 2);
		EventDb sdb3 = new EventDb("1003", "Default Spanish Course","Escribir", "Self study", 1480377600000L, 14400000L, 1, 3);
		EventDb sdb4 = new EventDb("1004", "Default Spanish Course","Leccion 1", "Lecture", 1480550400000L, 18000000L, 1, 4);
		EventDb sdb5 = new EventDb("1005", "Default Spanish Course","Leccion 2", "Lecture", 1480809600000L, 7200000L, 1, 5);
		EventDb sdb6 = new EventDb("1006", "Default Spanish Course","Leccion 3", "Lecture", 1481068800000L, 7200000L, 1, 6);
		EventDb sdb7 = new EventDb("1007", "Default Spanish Course","Leccion 4", "Lecture", 1481155200000L, 7200000L, 1, 7);
		EventDb sdb8 = new EventDb("1008", "Default Spanish Course","Leccion 5", "Lecture", 1481241600000L, 7200000L, 1, 8);
		EventDb sdb9 = new EventDb("1009", "Default Spanish Course","Examen final", "Evaluation", 1481328000000L, 7200000L, 1, 9);

		
//		EventDb sdb0 = new EventDb("DSC", "Default Spanish Course","Gramatica", "Self study", 1479945600000L, 18000000L, 1, 0);
//		EventDb sdb1 = new EventDb("DSC", "Default Spanish Course","Escuchar", "Self study", 1480118400000L, 10800000L, 1, 1);
//		EventDb sdb2 = new EventDb("DSC", "Default Spanish Course","Hablar", "Self study", 1480291200000L, 7200000L, 1, 2);
//		EventDb sdb3 = new EventDb("DSC", "Default Spanish Course","Escribir", "Self study", 1480377600000L, 14400000L, 1, 3);
//		EventDb sdb4 = new EventDb("DSC", "Default Spanish Course","Leccion 1", "Lecture", 1480550400000L, 18000000L, 1, 4);
//		EventDb sdb5 = new EventDb("DSC", "Default Spanish Course","Leccion 2", "Lecture", 1480809600000L, 7200000L, 1, 5);
//		EventDb sdb6 = new EventDb("DSC", "Default Spanish Course","Leccion 3", "Lecture", 1481068800000L, 7200000L, 1, 6);
//		EventDb sdb7 = new EventDb("DSC", "Default Spanish Course","Leccion 4", "Lecture", 1481155200000L, 7200000L, 1, 7);
//		EventDb sdb8 = new EventDb("DSC", "Default Spanish Course","Leccion 5", "Lecture", 1481241600000L, 7200000L, 1, 8);
//		EventDb sdb9 = new EventDb("DSC", "Default Spanish Course","Examen final", "Evaluation", 1481328000000L, 7200000L, 1, 9);		
		
		lSDb.add(sdb0);
		lSDb.add(sdb1);
		lSDb.add(sdb2);
		lSDb.add(sdb3);
		lSDb.add(sdb4);
		lSDb.add(sdb5);
		lSDb.add(sdb6);
		lSDb.add(sdb7);
		lSDb.add(sdb8);
		lSDb.add(sdb9);
		
		addSubject(lSDb);
		
		return lSDb;
		
		
		
	}


}