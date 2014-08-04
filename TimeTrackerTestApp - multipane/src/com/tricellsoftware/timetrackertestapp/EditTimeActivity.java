package com.tricellsoftware.timetrackertestapp;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

import com.tricellsoftware.timetrackertestapp.DTOs.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.Status_Enum;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.database.CompanyTable;
import com.tricellsoftware.timetrackertestapp.database.TimeLogTable;
import com.tricellsoftware.timetrackertestapp.helper.TimeHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditTimeActivity extends Activity {
	
	private BusinessLogic logic;
	private TimeLogDTO timelog;
	List<CompanyDTO> companies; //will hold the list of companies for spinner
	List<String> CompNames;
	int profileId = 1;
	
	private static int id;
	private int _id;
	static String StartDate;
	static String EndDate;
	
	private TimePicker StartPicker;
	private TimePicker EndPicker;
	//private TextView tvDate;
	private TextView clockintv;
	private TextView clockouttv;
	//Time layout items
    private Spinner CompSpinner;
    private Button StartEditBtn;
    private Button EndEditBtn;
    private EditText Starttxt;
    private EditText Endtxt;
    //Date layout items
    private EditText StartDatetxt;
    private EditText EndDatetxt;
    private Button StartDateEditBtn;
    private Button EndDateEditBtn;
    
    
    String StartDateTime;
	String EndDateTime;
    
    
	
	static String TimeFormat	=	"MM/dd/yyyy hh:mm a"; //time format
	
	String newStartTime;
	String newEndTime;
	
	ProgressDialog pd = null;
	
	boolean land;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 // Need to check if Activity has been switched to landscape mode
	    // If yes, finished and go back to the start Activity
		//if screen is large (7 inches)
	    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
	    		//&& (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
	    	  land = true;
		      /** Sending ID back to the Timeloglist fragment if this activity is in landscape mode**/	
		      if(id > 0){
					/** Share id with other activities or fragments by using sharedPref method
					 * **/
		    	  SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.pref_data_key), 0);
		    	  SharedPreferences.Editor editor = sharedPref.edit();
		    	  editor.putString(getString(R.string.timelog_id), String.valueOf(id)).commit();
		    	  //editor.commit();
		      }	
	      //finish();
	      //return;
	    }//checking for the Orientation must be before defining the content view
	    else
	    	land = false;
		setContentView(R.layout.edit_time_layout);
//		
//		if(pd != null){
//			pd = null;
//		}
//		pd = new ProgressDialog(this);
//		pd.show();
//		pd.setMessage("Loading..");
		//action bar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Time Log Time Details");

		
		Bundle extras = getIntent().getExtras();
		
		//business logic
		logic = new BusinessLogic(this);
		
		if(extras != null){
			
			id = extras.getInt(TimeLogTable.COLUMN_ID);
			//id = _id;
			//StartDate = extras.getString("StartDate");
			//EndDate = extras.getString("EndString");
			timelog = logic.getTimeLogByID(String.valueOf(id));
		}
		
		
		companies = logic.getAllCompanies();
		
		
		//get user by id 1
//		StartPicker = (TimePicker) findViewById(R.id.StartTimePicker);
//		EndPicker = (TimePicker) findViewById(R.id.EndTimePicker);
		
		//Initialize buttons
		StartEditBtn = (Button) findViewById(R.id.starteditbttn);
		EndEditBtn = (Button) findViewById(R.id.endeditbttn);
		
		StartDateEditBtn = (Button) findViewById(R.id.startdateeditbtn);
		EndDateEditBtn = (Button) findViewById(R.id.enddateeditbtn);
		
		//Init Edit Texts
		Starttxt = (EditText) findViewById(R.id.startEditText);
		Endtxt = (EditText) findViewById(R.id.endEditText);
		Starttxt.setEnabled(false);
		Endtxt.setEnabled(false);

		StartDatetxt = (EditText) findViewById(R.id.startdateedittxt);
		EndDatetxt = (EditText) findViewById(R.id.enddateedittxt);
//		StartDatetxt.setEnabled(false);
//		EndDatetxt.setEnabled(false);
		
		
		
		/** Spinner set up **/
		CompSpinner = (Spinner) findViewById(R.id.compspinner);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CompanyDTO> adapter = new ArrayAdapter<CompanyDTO>(this, android.R.layout.simple_spinner_item, companies);
		// Apply the adapter to the spinner
		CompSpinner.setAdapter(adapter);
		int index = 0;
		if(timelog != null){
			for(int i = 0; i < companies.size(); i++){
				String compname = adapter.getItem(i).toString();
				String dtoCompName = logic.getCompanyById(timelog.getCompanyId()).getName();
				if(compname.equals(dtoCompName)){
					index = i;
					break;
				}
			}
		}
		CompSpinner.setSelection(index);
		/*** End of Spinner***/
		
		if(timelog != null){
		
//		tvDate = (TextView) findViewById(R.id.dateview);
//		tvDate.setText("Main Date: " + timelog.getDate());
		
//		clockintv = (TextView) findViewById(R.id.clockintv);
//		clockintv.setText("Clock In Date: " + timelog.getStartTime().substring(0,10));
//		clockouttv = (TextView) findViewById(R.id.clockouttv);
//		clockouttv.setText("Clock Out Date: " + timelog.getEndTime().substring(0,10));
		StartDatetxt.setText(timelog.getStartTime().substring(0,10));
		EndDatetxt.setText(timelog.getEndTime().substring(0,10));
		

		Starttxt.setText(timelog.getStartTime().substring(11));
		Endtxt.setText(timelog.getEndTime().substring(11));
		
		//String srt = StartPicker.getCurrentHour().toString();
		
//			//Set hours and minutes to the date picker
//			Calendar c = TimeHelper.setCalendar(timelog.getStartTime());
//			c.getTime();
//			StartPicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY)); // sets hour with am or pm time
//			StartPicker.setCurrentMinute(c.get(Calendar.MINUTE));
//			Calendar cl = TimeHelper.setCalendar(timelog.getEndTime());
//			cl.getTime();
//			EndPicker.setCurrentHour(cl.get(Calendar.HOUR_OF_DAY));// sets hour with am or pm time
//			EndPicker.setCurrentMinute(cl.get(Calendar.MINUTE));
//			//String srt = StartPicker.getCurrentHour().toString();
		}
		else{
			
			Starttxt.setText(TimeHelper.getTime().substring(11));
			Endtxt.setText(TimeHelper.getTime().substring(11));
			
			StartDatetxt.setText(TimeHelper.getDate());
			EndDatetxt.setText(TimeHelper.getDate());
			
			
		}
		

		
		//StartPicker.set/
		
		Button SaveBttn = (Button) findViewById(R.id.checkbttn);
		
		//pd.hide();
		
		//Listening to the button event
		SaveBttn.setOnClickListener(new View.OnClickListener(){
					
			@Override
			public void onClick(View view){
				

				
				if(timelog != null){
					newStartTime = (StartDate == null || StartDate.equals("")) ? timelog.getStartTime() : StartDate; //Get the starttime from timepicker
					newEndTime = (EndDate == null || EndDate.equals("")) ? timelog.getEndTime() : EndDate; //Get the endtime from timepicker
					
					StartDate = "";
					EndDate ="";
				}
				long longDate1 = TimeHelper.getLongFromDate(StartDatetxt.getText().toString());
				long longDate2 = TimeHelper.getLongFromDate(EndDatetxt.getText().toString());
				if(longDate1 <= longDate2){
					validateTimes();
				}
				else
					Toast.makeText(EditTimeActivity.this, "Start Date cannot be greater than End Date", Toast.LENGTH_LONG).show();
						
			}
		});
		//Listening to the spinner
		CompSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				_id = companies.get(position).getID();
				
				//Toast.makeText(getApplicationContext(), "CompanyId: " + String.valueOf(companyId), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		
		//Listening to the Start Edit button
		StartEditBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				//Set hours and minutes to the date picker
				Calendar c = null;
				if(StartDate == null || StartDate.equals("")){
					if(timelog != null)
						c = TimeHelper.setCalendar(timelog.getStartTime());
					else
						c = TimeHelper.setCalendar(StartDatetxt.getText().toString() + " " + Starttxt.getText().toString());
						
				}
				else
					c = TimeHelper.setCalendar(StartDate);
				c.getTime();
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minutes = c.get(Calendar.MINUTE);
				//Init Time Picker Dialog
				TimePickerDialog tdp = new TimePickerDialog(EditTimeActivity.this, new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						// TODO Auto-generated method stub
	
						String date = null;
						if(timelog != null){
							date = timelog.getStartTime();
						}
						else
							date = TimeHelper.getDate();
						//substring is used to only display the hours
						StartDate = TimeHelper.getTimeFromTimePicker(view, date);
						Starttxt.setText(StartDate.substring(11));
					}
				}, hour, minutes, false);
				tdp.show();
				
			}
		});
		//Listening to the End Edit button
		EndEditBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Set Calendar with current date
				Calendar c = null;
				if(EndDate == null || EndDate.equals("")){
					if(timelog != null)
						c = TimeHelper.setCalendar(timelog.getEndTime());
					else
						c = TimeHelper.setCalendar(EndDatetxt.getText().toString()  + " " + Endtxt.getText().toString());
				}
				else
					c = TimeHelper.setCalendar(EndDate);
				c.getTime();
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minutes = c.get(Calendar.MINUTE);
				//Init Time Picker Dialog
				TimePickerDialog tdp = new TimePickerDialog(EditTimeActivity.this, new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						// TODO Auto-generated method stub
						String date = null;
						if(timelog != null){
							date = timelog.getEndTime();
						}
						else
							date = TimeHelper.getTime();
						
						EndDate = TimeHelper.getTimeFromTimePicker(view, date);
						Endtxt.setText(EndDate.substring(11));
					}
				}, hour, minutes, false);
				tdp.show();
			}
		});
		StartDateEditBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Process to get Current Date
	            final Calendar c = Calendar.getInstance();
	            int mYear = c.get(Calendar.YEAR);
	            int mMonth = c.get(Calendar.MONTH);
	            int mDay = c.get(Calendar.DAY_OF_MONTH);
	 
	            // Launch Date Picker Dialog
	            DatePickerDialog dpd = new DatePickerDialog(EditTimeActivity.this,
	                    new DatePickerDialog.OnDateSetListener() {
	 
	                        @Override
	                        public void onDateSet(DatePicker view, int year,
	                                int monthOfYear, int dayOfMonth) {
	                            // Display Selected date in Edit
	                        	String date = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
	                        	date = TimeHelper.formatDate(date);
	                        	StartDatetxt.setText(date);
	                			EndDatetxt.setText(date);

	                			StartDate = date + " " + Starttxt.getText().toString();
	                			
	                			
	                        }
	                    }, mYear, mMonth, mDay);
	            dpd.show();
				
			}
		});
		
		EndDateEditBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Process to get Current Date
	            final Calendar c = Calendar.getInstance();
	            int mYear = c.get(Calendar.YEAR);
	            int mMonth = c.get(Calendar.MONTH);
	            int mDay = c.get(Calendar.DAY_OF_MONTH);
	 
	            // Launch Date Picker Dialog
	            DatePickerDialog dpd = new DatePickerDialog(EditTimeActivity.this,
	                    new DatePickerDialog.OnDateSetListener() {
	 
	                        @Override
	                        public void onDateSet(DatePicker view, int year,
	                                int monthOfYear, int dayOfMonth) {
	                            // Display Selected date in Edit
	                        	String date = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
	                        	date = TimeHelper.formatDate(date);
	                			EndDatetxt.setText(date);
	                			
	                			EndDate = date + " " + Endtxt.getText().toString();
	                        }
	                    }, mYear, mMonth, mDay);
	            dpd.show();
				
			}
		});
		
	}
	//create menu options for the action bar
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
	    		   MenuInflater inflateLayout = getMenuInflater();
	    		   inflateLayout.inflate(R.menu.details_menu, menu);
	    		  return super.onCreateOptionsMenu(menu);
	    } 
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			//Gets the position on the Item selected
			//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			//_id = companies.get((int) info.id).getID();
		   switch (item.getItemId()) {
		   case R.id.action_settings:
		    	  Toast.makeText(this, "Settings was selected", Toast.LENGTH_LONG).show();
		   break;
		   case R.id.action_delete:
			   
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Are you sure you want to delete this record?")
				       .setTitle("Delete");

				// 3. Get the AlertDialog from create()
				
				builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int _id) {
		        	   logic.deleteTimeLogById(id);
		        	   if(logic.Error != null){
						   if(logic.Error.contains("key constraint")){
							   Toast.makeText(getApplicationContext(), "This record is being used somewhere else and cannot be deleted", Toast.LENGTH_LONG).show();
						   }
						   else
							   Toast.makeText(getApplicationContext(), "Unexpected Error Occurred", Toast.LENGTH_LONG).show();
					   }
					   else{
						   Toast.makeText(getApplicationContext(), "Record has been deleted successfully", Toast.LENGTH_LONG).show();
						  
					   }
		    		   finish();
		           }
		       });
				builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		        	   dialog.cancel();
		           }
		       });
				
				// Create the AlertDialog
				AlertDialog dialog = builder.create();

				// show it
				dialog.show();

				 //Toast.makeText(this, "Record has been deleted successfully", Toast.LENGTH_LONG).show();
		   break;
		    	  
		   }
		   return super.onOptionsItemSelected(item);
		 } 
	private void validateTimes()
	{
		/***** Updates existing timelog ******/
		if(timelog != null){
			
			
			if(timelog.getStartTime().equals(newStartTime) && timelog.getEndTime().equals(newEndTime) && timelog.getCompanyId() == _id){
				//Toast.makeText(this, "Times were not updated because no change was made", Toast.LENGTH_LONG).show();
				finish();
			}
			else{
				long lg1 = TimeHelper.getLongFromHour(newStartTime);
				long lg2 = TimeHelper.getLongFromHour(newEndTime);
				//if starttime is less than endtime 
				if(lg1 < lg2){
					updateTimelog(newStartTime, newEndTime);
					Toast.makeText(this, "Timelog was updated successfully!", Toast.LENGTH_LONG).show();
					
					finish();
				}
				else
					Toast.makeText(this, "Start Time cannot be less than End Time, please try again", Toast.LENGTH_LONG).show();
			}
	
		}
		///***** creates a new timelog ******///
		else{
			long lg1 = TimeHelper.getLongFromHour(StartDatetxt.getText().toString() + " " + Starttxt.getText().toString());
			long lg2 = TimeHelper.getLongFromHour(EndDatetxt.getText().toString()  + " " + Endtxt.getText().toString());
			
			if(lg1 < lg2){
				SaveNewTimeLog();
				Toast.makeText(this, "Timelog was created successfully!", Toast.LENGTH_LONG).show();
				
				finish();
			}
			else
				Toast.makeText(this, "Start Time cannot be less than End Time, please try again", Toast.LENGTH_LONG).show();
		}

	}
	private void updateTimelog(String startTime, String endTime){
		/**updates starttime and end time**/
		timelog = new TimeLogDTO();
		
		try {
			timelog.setID(id);
			timelog.setCompanyId(_id);
			timelog.setStartTime(newStartTime);//saves the time format as string
			timelog.setEndTime(newEndTime);
			timelog.setMinutes(TimeHelper.getTimeDiffInMinutes(startTime, endTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//timelog.setProfileID(profile.getID());
		//timelog.setStatusID(ClockType);
		//timelog.setYearWeek(YearWeek);
		logic.updateTimeLogbyID(timelog);
	}
	//saves new timelog record
	private void SaveNewTimeLog(){
		
		String YearWeek = Integer.toString(TimeHelper.getWeekOfYear());
		StartDateTime = StartDatetxt.getText().toString() + " " + Starttxt.getText().toString();
		EndDateTime = EndDatetxt.getText().toString()  + " " + Endtxt.getText().toString();
		
		/**May add the week of the year to query timelogs by week **/
		timelog = new TimeLogDTO();
		timelog.setDate(StartDatetxt.getText().toString()); //saves the date format as string
		timelog.setStartTime(StartDateTime);//saves the time format as string
		timelog.setEndTime(EndDateTime);
		timelog.setProfileID(profileId);
		timelog.setStatusID(Status_Enum.Out.getValue());
		try {
			String mins = TimeHelper.getTimeDiffInMinutes(StartDateTime, EndDateTime);
			timelog.setMinutes(mins);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timelog.setYearWeek(YearWeek);
		timelog.setCompanyId(_id);
		logic.AddNewTimeLog(timelog);
	}

}
