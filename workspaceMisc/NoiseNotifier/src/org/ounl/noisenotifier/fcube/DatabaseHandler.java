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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private String CLASSNAME = this.getClass().getSimpleName();

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "noise.db";
	private SQLiteDatabase sqliteDB = null;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		sqliteDB = this.getWritableDatabase();
		sqliteDB.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		sqliteDB = db;

		Log.d(CLASSNAME, "Creating table NoiseSample ...");
		db.execSQL(NoiseSample.getCreateTable());

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + NoiseSample.TABLE_NAME);

		onCreate(db);
	}

	/**
	 * Insert NoiseSample into db
	 * 
	 * @param o
	 */
	public void addNoiseSample(NoiseSample o) {

		sqliteDB = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		o.loadContentValues(values);
		try {
			sqliteDB.insert(NoiseSample.TABLE_NAME, null, values);
		} catch (Exception e) {
			Log.e(CLASSNAME, "Error inserting NoiseSample " + e.toString() + "");
			e.printStackTrace();
		}
		sqliteDB.close();
	}

	/**
	 * Insert list of NoiseSampless into db
	 * 
	 * @param NoiseSampless
	 */
	public void addNoiseSamples(List<NoiseSample> noiseSamples) {

		for (int i = 0; i < noiseSamples.size(); i++) {
			addNoiseSample(noiseSamples.get(i));
		}

	}

	/**
	 * Get NoiseSampless from database
	 * 
	 * @return
	 */
	public List<NoiseSample> getNoiseSamples() {
		List<NoiseSample> list = new ArrayList<NoiseSample>();
		String selectQuery = "SELECT  * FROM " + NoiseSample.TABLE_NAME;
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				NoiseSample o = new NoiseSample(cursor);
				list.add(o);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return list;
	}



}