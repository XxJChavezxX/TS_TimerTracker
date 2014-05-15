package com.tricellsoftware.timetrackertestapp.helper;

import com.tricellsoftware.timetrackertestapp.database.CompanyTable;
import com.tricellsoftware.timetrackertestapp.database.ProfileTable;
import com.tricellsoftware.timetrackertestapp.database.StatusTable;
import com.tricellsoftware.timetrackertestapp.database.TimeLogTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimeTrackerDBHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "timetracker.db";
	private static final int DATABASE_VERSION = 30;

	public TimeTrackerDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(DATABASE_NAME, "Current db version is " + db.getVersion());
		// TODO Auto-generated method stub
		//create tables
		ProfileTable.onCreate(db);
		CompanyTable.onCreate(db);
		TimeLogTable.onCreate(db);
		StatusTable.onCreate(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(DATABASE_NAME, "Current db version is " + db.getVersion());
	     if (oldVersion < newVersion) {

	        		ProfileTable.onUpgrade(db, oldVersion, newVersion);
	        		CompanyTable.onUpgrade(db, oldVersion, newVersion);
	        		TimeLogTable.onUpgrade(db, oldVersion, newVersion);
	        		StatusTable.onUpgrade(db, oldVersion, newVersion);
	            
	        }

		

	}

}
