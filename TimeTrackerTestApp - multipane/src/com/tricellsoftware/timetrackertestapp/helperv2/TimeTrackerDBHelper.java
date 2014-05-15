package com.tricellsoftware.timetrackertestapp.helperv2;

import com.tricellsoftware.timetrackertestapp.databasev2.CompanyTable;
import com.tricellsoftware.timetrackertestapp.databasev2.ProfileTable;
import com.tricellsoftware.timetrackertestapp.databasev2.StatusTable;
import com.tricellsoftware.timetrackertestapp.databasev2.TimeLogTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
