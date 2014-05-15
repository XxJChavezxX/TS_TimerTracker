package com.tricellsoftware.timetrackertestapp.databasev2;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ProfileTable {
	
	//Database Table
	public static final String PROFILE_TABLE = "Profiletbl";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FIRSTNAME = "FirstName";
	public static final String COLUMN_LASTNAME = "LastName";
	public static final String COLUMN_EMAIL = "Email";
	public static final String COLUMN_PHONE = "PhoneNumber";
	public static final String COLUMN_FK_STATUSID = "StatusId";

	
	//Databse creation SQL STATEMENT
	private static final String CREATE_TABLE = "create table "
			+ PROFILE_TABLE
			+"("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_FIRSTNAME + " text not null, "
			+ COLUMN_LASTNAME + " text not null, "
			+ COLUMN_EMAIL + " text not null, "
			+ COLUMN_PHONE + " text, "
			+ COLUMN_FK_STATUSID + " int not null, "
			+ "foreign key("+ COLUMN_FK_STATUSID +")" + "references Statustbl("+StatusTable.COLUMN_ID+")"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database){
		database.execSQL(CREATE_TABLE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
		Log.w(ProfileTable.class.getName(), "Upgrading database from version " + oldVersion + "to" + newVersion 
				+"which will destroy all old data ");
		database.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
		onCreate(database);
		
	}
}
