package com.tricellsoftware.timetrackertestapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tricellsoftware.timetrackertestapp.R;
import com.tricellsoftware.timetrackertestapp.DTOs.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.Status_Enum;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.database.ProfileTable;
import com.tricellsoftware.timetrackertestapp.helper.TimeHelper;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ClocksActivity extends Activity {
	
	private BusinessLogic logic;
	private ProfileDTO profile;
	private TimeLogDTO timelog;
	
	TextView Name;
	TextView Status;
	
	
	Date date; //time entry for the timelog
	int ClockType;
	Status_Enum statusEnum;
	
	SimpleDateFormat df; //date format
	String dateformat = "MM/dd/yyyy";
	
	String TimeFormat	=	"MM/dd/yyyy h:mm a"; //time format
	SimpleDateFormat tf;
	
	String time;
	
	private int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clocks);

		//action bar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Clock in/out");
		
		//find the buttons of the xml layout file
		final Button clockInBttn = (Button) findViewById(R.id.clockinbttn);
		final Button clockOutBttn = (Button) findViewById(R.id.clockoutbttn);
		final Button timeSheetBttn = (Button) findViewById(R.id.timesheetbttn);
		//checks if an id has been passed thru an activity
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			id = extras.getInt(ProfileTable.COLUMN_ID);
			if(id <= 0){
				id = 1;
			}
		}
		else{
			id = 1;
		}
		
		//business logic
		logic = new BusinessLogic(this);
		//get user by id 1
		profile = logic.getUser(id);
		Name = (TextView)findViewById(R.id.dateview);
		Status = (TextView)findViewById(R.id.textStatus);

		if(id > 0){
			Name.setText("Welcome " + profile.getFirstName());
			
			//Sets the current state of the user 
			int fkStatus = profile.getStatusID(); // stores the fk value of the profile table
			if(fkStatus == Status_Enum.On.getValue()){
				
				Status.setText("Status: " + Status_Enum.On.toString());
				//sets the background images on the buttons
				clockOutBttn.setBackgroundResource(R.drawable.clockout_focused);
				clockInBttn.setBackgroundResource(R.drawable.clockin_default);
				clockInBttn.setEnabled(false);
			}
			else{
				Status.setText("Status: " + Status_Enum.Off.toString());
				clockInBttn.setBackgroundResource(R.drawable.clockin_focused);
				clockOutBttn.setEnabled(false);
			}
		}
		
		TextView Datetv = (TextView) findViewById(R.id.vDate);
		//set the current date
//		df = new SimpleDateFormat(dateformat);
//		date = Calendar.getInstance().getTime();
		Datetv.setText(TimeHelper.getDate());
		
		timelog = logic.getTimeLogbyStatus(Status_Enum.In.getValue()); // get single timelog by status
		
		//Listening to the button event
		clockInBttn.setOnClickListener(new View.OnClickListener(){
					
			@Override
			public void onClick(View view){
			
				//SaveNewItem();
				view.setBackgroundResource(R.drawable.clockin_default);
				clockOutBttn.setEnabled(true); //enables clock out button
				view.setEnabled(false); //disables clock in button
				clockOutBttn.setBackgroundResource(R.drawable.clockout_focused); //changes the button image to focused
				
				profile.setStatusID(Status_Enum.On.getValue()); // saves the current user state
				ClockType = Status_Enum.In.getValue(); // saves the type of clock the user has made

				SaveNewTimeLog();
				logic.updateProfileById(profile); // updates the status of the user
				
				Status.setText("Status: " + Status_Enum.On.toString());
			}
		});
		//Listening to the button event
		clockOutBttn.setOnClickListener(new View.OnClickListener(){
					
			@Override
			public void onClick(View view){
			
				//SaveNewItem();	
				//tf = new SimpleDateFormat(TimeFormat);
				//date = Calendar.getInstance().getTime();
				//time = tf.format(date);

				
				try {
					int mins = Integer.valueOf(TimeHelper.getTimeDiffInMinutes(timelog.getStartTime(), TimeHelper.getTime()));
					int minsleft = 5 - mins;
					if(mins < 5){ //if mins is less than 5 when user tries to clock out it won't update the timelog, avoiding unnecessary timelog creation
						Toast.makeText(ClocksActivity.this, "Please allow 5 minutes after Clocking In to Clock Out (" + String.valueOf(minsleft) +" minutes left)", Toast.LENGTH_LONG).show();
					}
					else{
						profile.setStatusID(Status_Enum.Off.getValue());
						ClockType = Status_Enum.Out.getValue();
						UpdateTimeLog();
						clockInBttn.setBackgroundResource(R.drawable.clockin_focused);
						clockInBttn.setEnabled(true);
						view.setEnabled(false);
						view.setBackgroundResource(R.drawable.clockout_default);
						
						logic.updateProfileById(profile); // updates the status of the user
						
						Status.setText("Status: " + Status_Enum.Off.toString());
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//updates the current timelog 
				

			}
		});
		timeSheetBttn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Starting a new intent
				Intent i = new Intent(getApplicationContext(), SummaryActivity.class);
				startActivity(i);
//				Intent TimelogScreen = new Intent(getApplicationContext(), TimeLogListActivity.class);
//				startActivity(TimelogScreen);
			}
		});
		
	}
	//saves new timelog record
	private void SaveNewTimeLog(){
		
		String YearWeek = Integer.toString(TimeHelper.getWeekOfYear());
		
		/**May add the week of the year to query timelogs by week **/
		timelog = new TimeLogDTO();
		timelog.setDate(TimeHelper.getDate()); //saves the date format as string
		timelog.setStartTime(TimeHelper.getTime());//saves the time format as string
		timelog.setEndTime("--");
		timelog.setProfileID(profile.getID());
		timelog.setStatusID(ClockType);
		timelog.setYearWeek(YearWeek);
		logic.AddNewTimeLog(timelog);
	}
	//saves new timelog record
	private void UpdateTimeLog() throws ParseException{
		
		timelog.setEndTime(TimeHelper.getTime());
		String TotalMinutes = TimeHelper.getTimeDiffInMinutes(timelog.getStartTime(),timelog.getEndTime());
		timelog.setStatusID(ClockType);
		timelog.setMinutes(TotalMinutes);
		logic.updateTimeLogStatusID(timelog, Status_Enum.In.getValue());
	}



}
