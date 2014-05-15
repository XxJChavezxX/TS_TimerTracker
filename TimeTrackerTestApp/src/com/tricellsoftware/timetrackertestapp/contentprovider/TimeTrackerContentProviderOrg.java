package com.tricellsoftware.timetrackertestapp.contentprovider;


import java.util.Arrays;
import java.util.HashSet;

import com.tricellsoftware.timetrackertestapp.database.CompanyTable;
import com.tricellsoftware.timetrackertestapp.database.ProfileTable;
import com.tricellsoftware.timetrackertestapp.helper.TimeTrackerDBHelper;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class TimeTrackerContentProviderOrg extends ContentProvider{
	
	private TimeTrackerDBHelper database;
	
	private SQLiteDatabase db;
	//path to the content provider
	private static String AUTHORITY = "com.example.timetrackertestapp.contentproviderOrg";
	
	private static final int Items = 10; //number of items to return for the urimatcher
	private static final int Item_id = 20; //item to return for the urimatcher
	//base path
	private static String BASE_PATH = "Companytbl";
	
	public static final Uri Content_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/item";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/items";
		      
	//needed to check if a single or several items are returned
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static{
		sUriMatcher.addURI(AUTHORITY, BASE_PATH, Items);
		sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", Item_id); // <- change the number to an actual id to return details
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int uriType = sUriMatcher.match(uri);
		//open db connection
		SQLiteDatabase db = database.getWritableDatabase();
		//keep track of rows deleted
		int rowsDeleted = 0;		
		switch(uriType){
		case Items:
			rowsDeleted = db.delete(CompanyTable.COMPANY_TABLE, selection,
			          selectionArgs);
			break;
		case Item_id:
			String id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)){
				rowsDeleted = db.delete(CompanyTable.COMPANY_TABLE, CompanyTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			
			break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);	
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		int uriType = sUriMatcher.match(uri);
		//open db connection
		db = database.getWritableDatabase();
		//keep track of rows deleted
		int rowsDeleted = 0;
		long id = 0;
		
		switch(uriType){
		case Items:
			id = db.insert(CompanyTable.COMPANY_TABLE, null, values);
			break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);	
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		database = new TimeTrackerDBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

	    // Uisng SQLiteQueryBuilder instead of query() method
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	    
	 // check if the caller has requested a column which does not exists
	    checkColumns(projection);
	    
	    // Set the table
	    queryBuilder.setTables(CompanyTable.COMPANY_TABLE);
	 // Set the table
	    //queryBuilder.setTables(ProfileTable.PROFILE_TABLE);
	    
	    
	    int uriType = sUriMatcher.match(uri);
	    switch(uriType){
	    case Items:
	    	break;
	    case Item_id:
	    	  // adding the ID to the original query
	        queryBuilder.appendWhere(CompanyTable.COLUMN_ID + "="
	            + uri.getLastPathSegment());
	        break;
	        
	        default:
	        	throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    //access databse
	    SQLiteDatabase db = database.getWritableDatabase();
	    Cursor cursor = queryBuilder.query(db, projection, selection,
	        selectionArgs, null, null, sortOrder);
	    // make sure that potential listeners are getting notified
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);
	    
	 // make sure that potential listeners are getting notified
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);
		// TODO Auto-generated method stub
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int uriType = sUriMatcher.match(uri);
		//open db connection
		SQLiteDatabase db = database.getWritableDatabase();
		//keep track of rows deleted
		int rowsUpdated = 0;		
		switch(uriType){
		case Items:
			rowsUpdated = db.update(CompanyTable.COMPANY_TABLE, 
					values,
					selection,
			        selectionArgs);
			break;
		case Item_id:
			String id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)){
				rowsUpdated = db.update(CompanyTable.COMPANY_TABLE,
						values,
						CompanyTable.COLUMN_ID + "=" + id,
						null);
			}
			else{
				rowsUpdated = db.update(CompanyTable.COMPANY_TABLE,
						values,
						CompanyTable.COLUMN_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			
			break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);	
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}
	
	 private void checkColumns(String[] projection) {
		    String[] available = { CompanyTable.COLUMN_COMPANY,
		        CompanyTable.COLUMN_RATE, CompanyTable.COLUMN_DEFAULT_COMPANY,
		        CompanyTable.COLUMN_ID };
		    if (projection != null) {
		      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
		      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
		      // check if all columns which are requested are available
		      if (!availableColumns.containsAll(requestedColumns)) {
		        throw new IllegalArgumentException("Unknown columns in projection");
		      }
		    }
		  }

}
