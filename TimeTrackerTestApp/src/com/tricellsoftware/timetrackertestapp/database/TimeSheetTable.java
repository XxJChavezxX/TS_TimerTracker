package com.tricellsoftware.timetrackertestapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TimeSheetTable {
	
	//Database Table
	public static final String TIMESHEET_TABLE = "TimeSheettbl";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_START_DATE = "StartDate";
	public static final String COLUMN_END_DATE = "EndDate";
	public static final String COLUMN_ = "Hours";

	
	//Databse creation SQL STATEMENT
	private static final String CREATE_TABLE = "create table"
			+ TIMESHEET_TABLE
			+"("
			+ COLUMN_ID + "integer primary key autoincrement,"
			+ COLUMN_START_DATE + "date not null,"
			+ COLUMN_END_DATE + "date not null,"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database){
		database.execSQL(CREATE_TABLE);
	}
	
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
		Log.w(ProfileTable.class.getName(), "Upgrading database from version " + oldVersion + "to" + newVersion 
				+"which will destroy all old data");
		database.execSQL("DROP TABLE ID EXISTS" + TIMESHEET_TABLE);
		onCreate(database);
		
	}

}
