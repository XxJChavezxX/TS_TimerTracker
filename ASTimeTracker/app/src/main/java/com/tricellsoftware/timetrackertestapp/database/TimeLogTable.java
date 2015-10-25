package com.tricellsoftware.timetrackertestapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TimeLogTable {
	//Database Table
	public static final String TIMELOG_TABLE = "TimeLogtbl";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DATE = "Date";
	public static final String COLUMN_START_TIME = "StartTime";
	public static final String COLUMN_END_TIME = "EndTime";
	public static final String COLUMN_MINUTES = "Minutes";
	public static final String COLUMN_YEARWEEK = "YearWeek";
	public static final String COLUMN_FK_STATUSID = "StatusId";
	public static final String COLUMN_FK_PROFILEID = "ProfileId";
	public static final String COLUMN_FK_COMPANYID = "CompanyId";


	
	//Databse creation SQL STATEMENT
	private static final String CREATE_TABLE = "create table "
			+ TIMELOG_TABLE
			+"("
			+ COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_DATE + " text not null, "
			+ COLUMN_START_TIME + " text, "
			+ COLUMN_END_TIME + " text, "
			+ COLUMN_MINUTES + " int, "
			+ COLUMN_YEARWEEK + " int, "
			+ COLUMN_FK_STATUSID + " int not null, "
			+ COLUMN_FK_PROFILEID + " int not null, "
			+ COLUMN_FK_COMPANYID + " int, "
			+ "foreign key("+ COLUMN_FK_COMPANYID +")" + "references Companytbl("+CompanyTable.COLUMN_ID+"),"
			+ "foreign key("+ COLUMN_FK_STATUSID +")" + "references Statustbl("+StatusTable.COLUMN_ID+"),"
			+ "foreign key("+ COLUMN_FK_PROFILEID +")" + "references Profiletbl("+ProfileTable.COLUMN_ID+") "
			+ ");";
	
	public static void onCreate(SQLiteDatabase database){
		database.execSQL(CREATE_TABLE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
		Log.w(ProfileTable.class.getName(), "Upgrading database from version " + oldVersion + "to" + newVersion 
				+"which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TIMELOG_TABLE);
		onCreate(database);
		
	}
}
