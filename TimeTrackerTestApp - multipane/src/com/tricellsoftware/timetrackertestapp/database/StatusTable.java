package com.tricellsoftware.timetrackertestapp.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StatusTable {
	//Database Table
	public static final String STATUS_TABLE = "Statustbl";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_STATUS = "Status";

	
	//Databse creation SQL STATEMENT
	private static final String CREATE_TABLE = "create table "
			+ STATUS_TABLE
			+"("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_STATUS + " text not null"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database){
		database.execSQL(CREATE_TABLE);
		
		AddStatus("ON", database);
		AddStatus("OFF", database);
		AddStatus("IN", database);
		AddStatus("OUT", database);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
		Log.w(ProfileTable.class.getName(), "Upgrading database from version " + oldVersion + "to" + newVersion 
				+"which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + STATUS_TABLE);
		onCreate(database);
		
	}
	
	private static void AddStatus(String status, SQLiteDatabase database){
		  ContentValues values = new ContentValues();
		  values.put(StatusTable.COLUMN_STATUS, status);
		  database.insert(StatusTable.STATUS_TABLE, null, values);
	}
}
