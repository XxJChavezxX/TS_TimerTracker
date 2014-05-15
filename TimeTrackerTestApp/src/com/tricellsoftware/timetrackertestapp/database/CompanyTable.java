package com.tricellsoftware.timetrackertestapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CompanyTable {
	//Database Table
	public static final String COMPANY_TABLE = "Companytbl";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COMPANY = "Name";
	public static final String COLUMN_RATE = "Rate";
	public static final String COLUMN_DEFAULT_COMPANY = "DefaultCompany";
	public static final String COLUMN_FK_PROFILEID = "ProfileId";

	
	//Databse creation SQL STATEMENT
	private static final String CREATE_TABLE = "create table "
			+ COMPANY_TABLE
			+" ( "
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_COMPANY + " text not null, "
			+ COLUMN_RATE + " double not null, "
			+ COLUMN_DEFAULT_COMPANY + " bit"
			//+ COLUMN_FK_PROFILEID + "int not null,"
			//+ "foreign key("+ COLUMN_FK_PROFILEID +")" + "references Statustbl("+ProfileTable.COLUMN_ID+")"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database){
		database.execSQL(CREATE_TABLE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
		Log.w(ProfileTable.class.getName(), "Upgrading database from version " + oldVersion + "to" + newVersion 
				+"which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + COMPANY_TABLE);
		onCreate(database);
		
	}
}
