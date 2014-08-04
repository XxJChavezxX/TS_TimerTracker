package com.tricellsoftware.timetrackertestapp.businessLogic;


import java.util.ArrayList;
import java.util.List;

import com.tricellsoftware.timetrackertestapp.DTOs.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.database.CompanyTable;
import com.tricellsoftware.timetrackertestapp.database.ProfileTable;
import com.tricellsoftware.timetrackertestapp.database.TimeLogTable;
import com.tricellsoftware.timetrackertestapp.helper.TimeTrackerDBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


public class BusinessLogic {
	
	private TimeTrackerDBHelper database;
	SQLiteDatabase db;
	public String Error;
	
	public BusinessLogic(Context context){
		database = new TimeTrackerDBHelper(context);
	}
	
	public void Open() throws SQLException{
		db = database.getWritableDatabase();
		db.execSQL("PRAGMA foreign_keys=ON;");
		
	}
	public void Close(){
		db.close();
	}
	
	public void addNewCompany(CompanyDTO company){
	  Open();
	  try{
		  ContentValues values = new ContentValues();
		  values.put(CompanyTable.COLUMN_COMPANY, company.getName());
		  values.put(CompanyTable.COLUMN_RATE, company.getRate());
		  values.put(CompanyTable.COLUMN_DEFAULT_COMPANY, company.getIsDefault());
		  
		  db.insert(CompanyTable.COMPANY_TABLE, null, values);
	  }
	 catch(SQLiteException sql){
		  System.err.println("Caught SQLiteException: " + sql.getMessage());
	 }
	  
	  Close();
	}
	
	//return a company by id
	public CompanyDTO getCompanyById(int id){

		String[] projection = {CompanyTable.COLUMN_COMPANY, CompanyTable.COLUMN_RATE, CompanyTable.COLUMN_DEFAULT_COMPANY};
		Open();
		// Cursor cursor = db.query(CompanyTable.COMPANY_TABLE, projection, null, projection, null, null, null, null);
		 CompanyDTO company = new CompanyDTO();
			if(id == 0){
				company.setName("N/A");
				 company.setRate(0.0);
				 company.setIsDefault(false);
				 return company;
			}
		 try{
			 Cursor cursor = db.query(CompanyTable.COMPANY_TABLE, projection, CompanyTable.COLUMN_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null,null);
			 if(cursor != null){
				 cursor.moveToFirst();
				 String name = cursor.getString(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_COMPANY));
			     double rate = cursor.getDouble(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_RATE));
			     boolean isDefault = cursor.getInt(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_DEFAULT_COMPANY))>0;
				 //company.setID(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_ID));
				 company.setName(name);
				 company.setRate(rate);
				 company.setIsDefault(isDefault);
			 }
			 
		 }
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		Close();
		return company;
	}
	//return a company by default is true
		public CompanyDTO getCompanyByDefault(String isdefault){
			String[] projection = {CompanyTable.COLUMN_COMPANY, CompanyTable.COLUMN_RATE, CompanyTable.COLUMN_DEFAULT_COMPANY};
			Open();
			// Cursor cursor = db.query(CompanyTable.COMPANY_TABLE, projection, null, projection, null, null, null, null);
			 CompanyDTO company = new CompanyDTO();
			 
				String Select = "select * from " + CompanyTable.COMPANY_TABLE + 
						" where " + CompanyTable.COLUMN_DEFAULT_COMPANY +" = " + isdefault;
				
			 try{
				 //Cursor cursor = db.query(CompanyTable.COMPANY_TABLE, projection, CompanyTable.COLUMN_DEFAULT_COMPANY + " = ?", new String[] {String.valueOf(isdefault)}, null, null, null,null);
				 Cursor cursor = db.rawQuery(Select, null);
				 if(cursor != null){
					 if(cursor.moveToFirst()){
						 int id = cursor.getInt(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_ID));
						 String name = cursor.getString(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_COMPANY));
					     double rate = cursor.getDouble(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_RATE));
					     boolean isDefault = cursor.getInt(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_DEFAULT_COMPANY))>0;
						 //company.setID(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_ID));
					     company.setID(id);
						 company.setName(name);
						 company.setRate(rate);
						 company.setIsDefault(isDefault);
					 }
					 else{
						 //cursor is empty
					 }
				 }
				 
			 }
			catch(SQLiteException sql){
				 System.err.println("Caught SQLiteException: " + sql.getMessage());
			}
			 Close();
			return company;
		}
	//returns a list of companies
	public List<CompanyDTO> getAllCompanies(){
		Open();
		List<CompanyDTO> companies = new ArrayList<CompanyDTO>();
		//
		try{
			String[] projection = { CompanyTable.COLUMN_ID, CompanyTable.COLUMN_COMPANY, CompanyTable.COLUMN_RATE, CompanyTable.COLUMN_DEFAULT_COMPANY };
			Cursor cursor = db.query(CompanyTable.COMPANY_TABLE, projection, null, null, null, null, null);
			
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				CompanyDTO company = new CompanyDTO();
				int id = Integer.parseInt(cursor.getString(0));
				company.setID(id);
				company.setName(cursor.getString(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_COMPANY)));
				company.setRate(cursor.getInt(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_RATE)));
				company.setIsDefault(cursor.getInt(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_DEFAULT_COMPANY))>0);
				
				companies.add(company);
				cursor.moveToNext();
			}
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}	
		Close();
		return companies;
	}
	//returns a list of companies
	public boolean ValidateIfNameExists(String Name){
		Open();
		List<CompanyDTO> companies = new ArrayList<CompanyDTO>();
		boolean NameExists = false; 
		try{
			String[] projection = { CompanyTable.COLUMN_ID, CompanyTable.COLUMN_COMPANY};
			Cursor cursor = db.query(CompanyTable.COMPANY_TABLE, projection, null, null, null, null, null);
			
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				//CompanyDTO company = new CompanyDTO();
				//int id = Integer.parseInt(cursor.getString(0));
				String CompanyName = cursor.getString(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_COMPANY));
				if(Name.equals(CompanyName)){
					return true;
				}
				else{
					cursor.moveToNext();
					//return false;
				}
				//company.setID(id);
				///company.setName(cursor.getString(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_COMPANY)));
				///company.setRate(cursor.getInt(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_RATE)));
				//.setIsDefault(cursor.getInt(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_DEFAULT_COMPANY))>0);
				///
				//companies.add(company);
				
			}
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}	
		Close();
		return NameExists;
	}
	//updates a company by id
	public int updateCompanyById(CompanyDTO company){
		Open();
		
		int rowsupdated = 0;
		try{
			  ContentValues values = new ContentValues();
			  values.put(CompanyTable.COLUMN_COMPANY, company.getName());
			  values.put(CompanyTable.COLUMN_RATE, company.getRate());
			  values.put(CompanyTable.COLUMN_DEFAULT_COMPANY, company.getIsDefault());
			  
			 rowsupdated = db.update(CompanyTable.COMPANY_TABLE,
						values,
						CompanyTable.COLUMN_ID + "=" + company.getID(),
						null);
			  
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		 Close();
		  
		 return rowsupdated;
	}
	//deletes a company by id
	public void deleteCompanyById(String id){
		Open();
		try{
		  db.delete(CompanyTable.COMPANY_TABLE,
					CompanyTable.COLUMN_ID + " = ?",
					new String[] { id });
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
			 Error = sql.getMessage();
			 
		}
		Close();
	}
	//***************************//
	////Profile Logic
	//**************************//
	public void AddNewUser(ProfileDTO profile){
		  Open();
		  try{
			  ContentValues values = new ContentValues();
			  values.put(ProfileTable.COLUMN_FIRSTNAME, profile.getFirstName());
			  values.put(ProfileTable.COLUMN_LASTNAME, profile.getLastName());
			  values.put(ProfileTable.COLUMN_EMAIL, profile.getEmail());
			  values.put(ProfileTable.COLUMN_FK_STATUSID, profile.getStatusID());
			  
			  db.insert(ProfileTable.PROFILE_TABLE, null, values);
		  }
		  catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		  }
		  
		  Close();
	}
	public ProfileDTO getUser(int id){
		
		String[] projection = {ProfileTable.COLUMN_ID, ProfileTable.COLUMN_FIRSTNAME, ProfileTable.COLUMN_LASTNAME, 
				ProfileTable.COLUMN_EMAIL, ProfileTable.COLUMN_FK_STATUSID, ProfileTable.COLUMN_FK_COMPANYID};
		Open();
		ProfileDTO profile = new ProfileDTO();
		try{
			// Cursor cursor = db.query(CompanyTable.COMPANY_TABLE, projection, null, projection, null, null, null, null);
			
			 Cursor cursor = db.query(ProfileTable.PROFILE_TABLE, projection, ProfileTable.COLUMN_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null,null);
			 int count = cursor.getCount();
			 if(count > 0){
				 cursor.moveToFirst();
				 int _id = Integer.parseInt(cursor.getString(0));
				 profile.setID(_id);
				 String firstname = cursor.getString(cursor.getColumnIndexOrThrow(ProfileTable.COLUMN_FIRSTNAME));
			     String lastname = cursor.getString(cursor.getColumnIndexOrThrow(ProfileTable.COLUMN_LASTNAME));
			     String email = cursor.getString(cursor.getColumnIndexOrThrow(ProfileTable.COLUMN_EMAIL));
			     String status = cursor.getString(cursor.getColumnIndexOrThrow(ProfileTable.COLUMN_FK_STATUSID));
			     int companyid = cursor.getInt(cursor.getColumnIndexOrThrow(ProfileTable.COLUMN_FK_COMPANYID));
			     int statusid = Integer.parseInt(status);
				 //company.setID(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_ID));
			     profile.setFirstName(firstname);
			     profile.setLastName(lastname);
			     profile.setEmail(email);
			     profile.setStatusID(statusid);
			     profile.setCurrentCompany(companyid);
			 }
			 else{
				 Close();
				 return null;
			 }

		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}

		 Close();
		 return profile;
	}
	//updates a company by id
	public int updateProfileById(ProfileDTO profile){
		Open();
		
		int rowsupdated = 0;
		
		try{
		  ContentValues values = new ContentValues();
		  values.put(ProfileTable.COLUMN_FIRSTNAME, profile.getFirstName());
		  values.put(ProfileTable.COLUMN_LASTNAME, profile.getLastName());
		  values.put(ProfileTable.COLUMN_EMAIL, profile.getEmail());
		  values.put(ProfileTable.COLUMN_FK_STATUSID, profile.getStatusID());
		  values.put(ProfileTable.COLUMN_FK_COMPANYID, profile.getCurrentCompany());
		  rowsupdated = db.update(ProfileTable.PROFILE_TABLE,
					values,
					ProfileTable.COLUMN_ID + "=" + profile.getID(),
					null);
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		  
		 Close();
		  
		 return rowsupdated;
	}
	//******************************************************//
	//       TimeLogs Logic                                //
	//*****************************************************//
	public List<TimeLogDTO> getAllTimeLogs(){
		
		Open();
		
		List<TimeLogDTO> timelogs = new ArrayList<TimeLogDTO>();
		
		try{
			String[] projection = { TimeLogTable.COLUMN_ID, TimeLogTable.COLUMN_DATE, TimeLogTable.COLUMN_START_TIME, TimeLogTable.COLUMN_END_TIME, 
					TimeLogTable.COLUMN_MINUTES,TimeLogTable.COLUMN_YEARWEEK, TimeLogTable.COLUMN_FK_PROFILEID, TimeLogTable.COLUMN_FK_STATUSID, TimeLogTable.COLUMN_FK_COMPANYID };
			Cursor cursor = db.query(TimeLogTable.TIMELOG_TABLE, projection, null, null, null, null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				TimeLogDTO timelog = new TimeLogDTO();
				int id = Integer.parseInt(cursor.getString(0));
				timelog.setID(id);
				timelog.setDate(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_DATE)));
				timelog.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_START_TIME)));
				timelog.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_END_TIME)));
				timelog.setMinutes(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_MINUTES)));
				timelog.setYearWeek(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_YEARWEEK)));
				timelog.setProfileID(cursor.getInt(cursor.getColumnIndexOrThrow( TimeLogTable.COLUMN_FK_PROFILEID)));
				timelog.setStatusID(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_STATUSID)));
				timelog.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_COMPANYID)));
				timelogs.add(timelog);
				cursor.moveToNext();
			}
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		//

		Close();
				
		return timelogs;
		
	}
	public void AddNewTimeLog(TimeLogDTO timelog){
		  Open();
		  try{
			  ContentValues values = new ContentValues();
			  values.put(TimeLogTable.COLUMN_DATE, timelog.getDate().toString());
			  values.put(TimeLogTable.COLUMN_START_TIME, timelog.getStartTime().toString());
			  values.put(TimeLogTable.COLUMN_END_TIME, timelog.getEndTime().toString());
			  values.put(TimeLogTable.COLUMN_YEARWEEK, timelog.getYearWeek().toString());
			  values.put(TimeLogTable.COLUMN_FK_PROFILEID, timelog.getProfileID());
			  values.put(TimeLogTable.COLUMN_FK_STATUSID, timelog.getStatusID());
			  values.put(TimeLogTable.COLUMN_FK_COMPANYID, timelog.getCompanyId());
			  values.put(TimeLogTable.COLUMN_MINUTES, timelog.getMinutes());
			  db.insert(TimeLogTable.TIMELOG_TABLE, null, values);
		  }
		  catch(SQLiteException sql){
				 System.err.println("Caught SQLiteException: " + sql.getMessage());
		  }
		  
		  Close();
	}
	//updates a time by id
	public int updateTimeLogStatusID(TimeLogDTO timelog, int statusID){
		Open();
		
		int rowsupdated = 0;
		try{
			ContentValues values = new ContentValues();
			  //values.put(TimeLogTable.COLUMN_DATE, timelog.getDate().toString());
			  //values.put(TimeLogTable.COLUMN_START_TIME, timelog.getStartTime().toString());
			  values.put(TimeLogTable.COLUMN_END_TIME, timelog.getEndTime().toString());
			  values.put(TimeLogTable.COLUMN_MINUTES, timelog.getMinutes());
			  values.put(TimeLogTable.COLUMN_FK_STATUSID, timelog.getStatusID());
			  rowsupdated = db.update(TimeLogTable.TIMELOG_TABLE,
						values,
						TimeLogTable.COLUMN_FK_STATUSID + "=" + statusID,
						null);
			  
			  
			 
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		  
		 Close();
		
		 return rowsupdated;
		
	}
	//updates a company by id
	public int updateTimeLogbyID(TimeLogDTO timelog){
		Open();
		
		int rowsupdated = 0;
		try{
			ContentValues values = new ContentValues();
			  values.put(TimeLogTable.COLUMN_DATE, timelog.getDate().toString());
			  values.put(TimeLogTable.COLUMN_START_TIME, timelog.getStartTime().toString());
			  values.put(TimeLogTable.COLUMN_END_TIME, timelog.getEndTime().toString());
			  values.put(TimeLogTable.COLUMN_MINUTES, timelog.getMinutes());
			  values.put(TimeLogTable.COLUMN_FK_COMPANYID, timelog.getCompanyId());
			  //values.put(TimeLogTable.COLUMN_FK_STATUSID, timelog.getStatusID());
			  rowsupdated = db.update(TimeLogTable.TIMELOG_TABLE,
						values,
						TimeLogTable.COLUMN_ID + "=" + timelog.getID(),
						null);
			  
			  
			 
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		  
		 Close();
		
		 return rowsupdated;
		
	}
public TimeLogDTO getTimeLogbyStatus(int statusID){
	Open();
	String[] projection = { TimeLogTable.COLUMN_ID, TimeLogTable.COLUMN_DATE, TimeLogTable.COLUMN_START_TIME, TimeLogTable.COLUMN_END_TIME, 
			TimeLogTable.COLUMN_MINUTES,TimeLogTable.COLUMN_YEARWEEK, TimeLogTable.COLUMN_FK_PROFILEID, TimeLogTable.COLUMN_FK_STATUSID,TimeLogTable.COLUMN_FK_COMPANYID };

		TimeLogDTO timelog = new TimeLogDTO();
		try{
			// Cursor cursor = db.query(CompanyTable.COMPANY_TABLE, projection, null, projection, null, null, null, null);
			
			 Cursor cursor = db.query(TimeLogTable.TIMELOG_TABLE, projection, TimeLogTable.COLUMN_FK_STATUSID + " = ?", new String[] {String.valueOf(statusID)}, null, null, null,null);
			 int count = cursor.getCount();
			 //cursor.moveToFirst();
			 if(count > 0){
				 cursor.moveToFirst();
				 int _id = Integer.parseInt(cursor.getString(0));
				 timelog.setID(_id);
				 String Date = cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_DATE));
			     String StartTime = cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_START_TIME));
			     String EndTime = cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_END_TIME));
			     String Minutes = cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_MINUTES));
			     String YearWeek = cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_YEARWEEK));
			     int FKStatusID = cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_STATUSID));
			     int FKProfileID = cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_PROFILEID));
			     int FKCompanyID = cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_COMPANYID));
			     
			     timelog.setDate(Date);
			     timelog.setStartTime(StartTime);
			     timelog.setEndTime(EndTime);
			     timelog.setMinutes(Minutes);
			     timelog.setStatusID(FKStatusID);
			     timelog.setProfileID(FKProfileID);
			     timelog.setYearWeek(YearWeek);
			     timelog.setCompanyId(FKCompanyID);
			     cursor.moveToNext();
			 }
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}

		 Close();
		 return timelog;
	}
	//deletes a timelog by id
	public void deleteTimeLogById(int id){
		Open();
		try{
			db.delete(TimeLogTable.TIMELOG_TABLE,
			TimeLogTable.COLUMN_ID + " = ?",
			new String[] { String.valueOf(id)});
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
			 Error = sql.getMessage();
		}
		
		  Close();
	}
	public TimeLogDTO getTimeLogByID(String ID){
		Open();
		
		TimeLogDTO timelog = new TimeLogDTO();
		
		try{
			 
			String[] projection = { TimeLogTable.COLUMN_ID, TimeLogTable.COLUMN_DATE, TimeLogTable.COLUMN_START_TIME, TimeLogTable.COLUMN_END_TIME, 
					TimeLogTable.COLUMN_MINUTES, TimeLogTable.COLUMN_YEARWEEK, TimeLogTable.COLUMN_FK_PROFILEID, TimeLogTable.COLUMN_FK_STATUSID, TimeLogTable.COLUMN_FK_COMPANYID };
			Cursor cursor = db.query(TimeLogTable.TIMELOG_TABLE, projection, TimeLogTable.COLUMN_ID + " = ?", new String[] {ID}, null, null, null,null);
			int count = cursor.getCount();
			 if(count > 0){
				cursor.moveToFirst();
				//TimeLogDTO timelog = new TimeLogDTO();
				//int id = Integer.parseInt(cursor.getString(0));
				timelog.setID(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_ID)));
				timelog.setDate(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_DATE)));
				timelog.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_START_TIME)));
				timelog.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_END_TIME)));
				timelog.setMinutes(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_MINUTES)));
				timelog.setYearWeek(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_YEARWEEK)));
				timelog.setProfileID(cursor.getInt(cursor.getColumnIndexOrThrow( TimeLogTable.COLUMN_FK_PROFILEID)));
				timelog.setStatusID(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_STATUSID)));
				timelog.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_COMPANYID)));
			 }
			 else{
				 return null;
			 }
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		//

		Close();
				
		return timelog;
	}
	public List<TimeLogDTO> getAllTimeLogsByDate(String Date){
		Open();
		
		List<TimeLogDTO> timelogs = new ArrayList<TimeLogDTO>();
		
		try{
			String[] projection = { TimeLogTable.COLUMN_ID, TimeLogTable.COLUMN_DATE, TimeLogTable.COLUMN_START_TIME, TimeLogTable.COLUMN_END_TIME, 
					TimeLogTable.COLUMN_MINUTES, TimeLogTable.COLUMN_YEARWEEK, TimeLogTable.COLUMN_FK_PROFILEID, TimeLogTable.COLUMN_FK_STATUSID,TimeLogTable.COLUMN_FK_COMPANYID };
			Cursor cursor = db.query(TimeLogTable.TIMELOG_TABLE, projection, TimeLogTable.COLUMN_DATE + " = ?", new String[] {Date}, null, null, null,null);
			
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				TimeLogDTO timelog = new TimeLogDTO();
				int id = Integer.parseInt(cursor.getString(0));
				timelog.setID(id);
				timelog.setDate(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_DATE)));
				timelog.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_START_TIME)));
				timelog.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_END_TIME)));
				timelog.setMinutes(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_MINUTES)));
				timelog.setYearWeek(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_YEARWEEK)));
				timelog.setProfileID(cursor.getInt(cursor.getColumnIndexOrThrow( TimeLogTable.COLUMN_FK_PROFILEID)));
				timelog.setStatusID(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_STATUSID)));
				timelog.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_COMPANYID)));
				timelogs.add(timelog);
				cursor.moveToNext();
			}
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		//

		Close();
				
		return timelogs;
	}
	/*****  Get timelogs related by date and current company ******/
	public List<TimeLogDTO> getAllTimeLogsByDate(String Date, String CompanyId){
		Open();
		
		List<TimeLogDTO> timelogs = new ArrayList<TimeLogDTO>();
		
		try{
			String[] projection = { TimeLogTable.COLUMN_ID, TimeLogTable.COLUMN_DATE, TimeLogTable.COLUMN_START_TIME, TimeLogTable.COLUMN_END_TIME, 
					TimeLogTable.COLUMN_MINUTES, TimeLogTable.COLUMN_YEARWEEK, TimeLogTable.COLUMN_FK_PROFILEID, TimeLogTable.COLUMN_FK_STATUSID,TimeLogTable.COLUMN_FK_COMPANYID };
			//Cursor cursor = db.query(TimeLogTable.TIMELOG_TABLE, projection, TimeLogTable.COLUMN_DATE + " = ?", new String[] {Date}, null, null, null,null);
			
			String query = "select * from TimeLogtbl where Date= " + "'"+ Date +"'" + " and " + "CompanyId= " + "'"+ CompanyId+"'";
			Cursor cursor = db.rawQuery(query, null);
			
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				TimeLogDTO timelog = new TimeLogDTO();
				int id = Integer.parseInt(cursor.getString(0));
				timelog.setID(id);
				timelog.setDate(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_DATE)));
				timelog.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_START_TIME)));
				timelog.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_END_TIME)));
				timelog.setMinutes(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_MINUTES)));
				timelog.setYearWeek(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_YEARWEEK)));
				timelog.setProfileID(cursor.getInt(cursor.getColumnIndexOrThrow( TimeLogTable.COLUMN_FK_PROFILEID)));
				timelog.setStatusID(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_STATUSID)));
				timelog.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_COMPANYID)));
				timelogs.add(timelog);
				cursor.moveToNext();
			}
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		//

		Close();
				
		return timelogs;
	}
	public List<TimeLogDTO> getAllTimeLogsByYearWeek(String YearWeek){
		Open();
		
		List<TimeLogDTO> timelogs = new ArrayList<TimeLogDTO>();
		
		try{
			String[] projection = { TimeLogTable.COLUMN_ID, TimeLogTable.COLUMN_DATE, TimeLogTable.COLUMN_START_TIME, TimeLogTable.COLUMN_END_TIME, 
					TimeLogTable.COLUMN_MINUTES, TimeLogTable.COLUMN_YEARWEEK, TimeLogTable.COLUMN_FK_PROFILEID, TimeLogTable.COLUMN_FK_STATUSID, TimeLogTable.COLUMN_FK_COMPANYID };
			Cursor cursor = db.query(TimeLogTable.TIMELOG_TABLE, projection, TimeLogTable.COLUMN_YEARWEEK + " = ?", new String[] {YearWeek}, null, null, null,null);
			
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				TimeLogDTO timelog = new TimeLogDTO();
				int id = Integer.parseInt(cursor.getString(0));
				timelog.setID(id);
				timelog.setDate(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_DATE)));
				timelog.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_START_TIME)));
				timelog.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_END_TIME)));
				timelog.setMinutes(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_MINUTES)));
				timelog.setYearWeek(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_YEARWEEK)));
				timelog.setProfileID(cursor.getInt(cursor.getColumnIndexOrThrow( TimeLogTable.COLUMN_FK_PROFILEID)));
				timelog.setStatusID(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_STATUSID)));
				timelog.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_COMPANYID)));
				timelogs.add(timelog);
				cursor.moveToNext();
			}
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		//

		Close();
				
		return timelogs;
	}
	public List<TimeLogDTO> getAllTimeLogsByWeek(String StartDate, String EndDate){
		Open();
		
		List<TimeLogDTO> timelogs = new ArrayList<TimeLogDTO>();
		
		try{
			String[] projection = { TimeLogTable.COLUMN_ID, TimeLogTable.COLUMN_DATE, TimeLogTable.COLUMN_START_TIME, TimeLogTable.COLUMN_END_TIME, 
					TimeLogTable.COLUMN_MINUTES, TimeLogTable.COLUMN_FK_PROFILEID, TimeLogTable.COLUMN_FK_STATUSID, TimeLogTable.COLUMN_FK_COMPANYID };
//			//Cursor cursor = db.query(TimeLogTable.TIMELOG_TABLE, projection, TimeLogTable.COLUMN_DATE + " = ?", new String[] {Date}, null, null, null,null);
//			String Select = "select " + TimeLogTable.COLUMN_ID + "," + TimeLogTable.COLUMN_DATE + ","+ TimeLogTable.COLUMN_START_TIME + "," + TimeLogTable.COLUMN_END_TIME + "," 
//					+ TimeLogTable.COLUMN_MINUTES + "," + TimeLogTable.COLUMN_FK_PROFILEID + "," + TimeLogTable.COLUMN_FK_STATUSID + " from " + TimeLogTable.TIMELOG_TABLE + 
//					" where " + TimeLogTable.COLUMN_DATE +" between " + EndDate + " and " + StartDate;
			//Cursor cursor = db.query(TimeLogTable.TIMELOG_TABLE, projection, TimeLogTable.COLUMN_DATE + " = ?", new String[] {Date}, null, null, null,null);
			String Select = "select * from " + TimeLogTable.TIMELOG_TABLE + 
					" where " + TimeLogTable.COLUMN_DATE +" <= " + "'"+EndDate+"'" + " and " + TimeLogTable.COLUMN_DATE + " >= " + "'"+StartDate+"'";
			Cursor cursor = db.rawQuery(Select, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				TimeLogDTO timelog = new TimeLogDTO();
				int id = Integer.parseInt(cursor.getString(0));
				timelog.setID(id);
				timelog.setDate(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_DATE)));
				timelog.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_START_TIME)));
				timelog.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_END_TIME)));
				timelog.setMinutes(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_MINUTES)));
				timelog.setProfileID(cursor.getInt(cursor.getColumnIndexOrThrow( TimeLogTable.COLUMN_FK_PROFILEID)));
				timelog.setStatusID(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_STATUSID)));
				timelog.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_COMPANYID)));
				timelog.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_COMPANYID)));
				timelogs.add(timelog);
				cursor.moveToNext();
			}
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		//

		Close();
				
		return timelogs;
	}
	public List<TimeLogDTO> getAllTimeLogsByWeek(String StartDate, String EndDate, String CompanyId){
		Open();
		
		List<TimeLogDTO> timelogs = new ArrayList<TimeLogDTO>();
		
		try{
			String[] projection = { TimeLogTable.COLUMN_ID, TimeLogTable.COLUMN_DATE, TimeLogTable.COLUMN_START_TIME, TimeLogTable.COLUMN_END_TIME, 
					TimeLogTable.COLUMN_MINUTES, TimeLogTable.COLUMN_FK_PROFILEID, TimeLogTable.COLUMN_FK_STATUSID, TimeLogTable.COLUMN_FK_COMPANYID };
//			//Cursor cursor = db.query(TimeLogTable.TIMELOG_TABLE, projection, TimeLogTable.COLUMN_DATE + " = ?", new String[] {Date}, null, null, null,null);
//			String Select = "select " + TimeLogTable.COLUMN_ID + "," + TimeLogTable.COLUMN_DATE + ","+ TimeLogTable.COLUMN_START_TIME + "," + TimeLogTable.COLUMN_END_TIME + "," 
//					+ TimeLogTable.COLUMN_MINUTES + "," + TimeLogTable.COLUMN_FK_PROFILEID + "," + TimeLogTable.COLUMN_FK_STATUSID + " from " + TimeLogTable.TIMELOG_TABLE + 
//					" where " + TimeLogTable.COLUMN_DATE +" between " + EndDate + " and " + StartDate;
			//Cursor cursor = db.query(TimeLogTable.TIMELOG_TABLE, projection, TimeLogTable.COLUMN_DATE + " = ?", new String[] {Date}, null, null, null,null);
			String Select = "select * from " + TimeLogTable.TIMELOG_TABLE + 
					" where " + TimeLogTable.COLUMN_DATE +" <= " + "'"+EndDate+"'" + " and " + TimeLogTable.COLUMN_DATE + " >= " + "'" + StartDate
					+ "' and CompanyId = " + "'"+ CompanyId +"'";
			Cursor cursor = db.rawQuery(Select, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				TimeLogDTO timelog = new TimeLogDTO();
				int id = Integer.parseInt(cursor.getString(0));
				timelog.setID(id);
				timelog.setDate(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_DATE)));
				timelog.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_START_TIME)));
				timelog.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_END_TIME)));
				timelog.setMinutes(cursor.getString(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_MINUTES)));
				timelog.setProfileID(cursor.getInt(cursor.getColumnIndexOrThrow( TimeLogTable.COLUMN_FK_PROFILEID)));
				timelog.setStatusID(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_STATUSID)));
				timelog.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_COMPANYID)));
				timelog.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(TimeLogTable.COLUMN_FK_COMPANYID)));
				timelogs.add(timelog);
				cursor.moveToNext();
			}
		}
		catch(SQLiteException sql){
			 System.err.println("Caught SQLiteException: " + sql.getMessage());
		}
		//

		Close();
				
		return timelogs;
	}
}
