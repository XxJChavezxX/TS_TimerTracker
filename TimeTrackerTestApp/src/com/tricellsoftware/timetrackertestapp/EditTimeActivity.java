package com.tricellsoftware.timetrackertestapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tricellsoftware.timetrackertestapp.R;
import com.tricellsoftware.timetrackertestapp.DTOs.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.database.ProfileTable;
import com.tricellsoftware.timetrackertestapp.helper.TimeHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditTimeActivity extends Activity {
	
	private BusinessLogic logic;
	private TimeLogDTO timelog;
	
	private int id;
	String StartDate;
	String EndDate;
	
	private TimePicker StartPicker;
	private TimePicker EndPicker;
	//private TextView tvDate;
	private TextView clockintv;
	private TextView clockouttv;
	
	static String TimeFormat	=	"MM/dd/yyyy hh:mm a"; //time format
	
	String newStartTime;
	String newEndTime;
	
	ProgressDialog pd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_time_layout);
		
		pd = new ProgressDialog(this);
		pd.show();
		pd.setMessage("Loading..");
		//action bar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Edit Time");

		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			id = extras.getInt(ProfileTable.COLUMN_ID);
			//StartDate = extras.getString("StartDate");
			//EndDate = extras.getString("EndString");
		}
		
		//business logic
		logic = new BusinessLogic(this);
		
		timelog = logic.getTimeLogByID(String.valueOf(id));
		//get user by id 1
		StartPicker = (TimePicker) findViewById(R.id.StartTimePicker);
		EndPicker = (TimePicker) findViewById(R.id.EndTimePicker);
		
//		tvDate = (TextView) findViewById(R.id.dateview);
//		tvDate.setText("Main Date: " + timelog.getDate());
		
		clockintv = (TextView) findViewById(R.id.clockintv);
		clockintv.setText("Clock In Date: " + timelog.getStartTime().substring(0,10));
		clockouttv = (TextView) findViewById(R.id.clockouttv);
		clockouttv.setText("Clock Out Date: " + timelog.getEndTime().substring(0,10));
		
		//Set hours and minutes to the date picker
		Calendar c = TimeHelper.setCalendar(timelog.getStartTime());
		c.getTime();
		StartPicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY)); // sets hour with am or pm time
		StartPicker.setCurrentMinute(c.get(Calendar.MINUTE));
		Calendar cl = TimeHelper.setCalendar(timelog.getEndTime());
		cl.getTime();
		EndPicker.setCurrentHour(cl.get(Calendar.HOUR_OF_DAY));// sets hour with am or pm time
		EndPicker.setCurrentMinute(cl.get(Calendar.MINUTE));
		//String srt = StartPicker.getCurrentHour().toString();
		
		//StartPicker.set/
		
		Button SaveBttn = (Button) findViewById(R.id.checkbttn);
		
		pd.hide();
		
		//Listening to the button event
		SaveBttn.setOnClickListener(new View.OnClickListener(){
					
			public void onClick(View view){
						
				newStartTime = TimeHelper.getTimeFromTimePicker(StartPicker, timelog.getStartTime()); //Get the starttime from timepicker
				newEndTime = TimeHelper.getTimeFromTimePicker(EndPicker, timelog.getEndTime()); //Get the endtime from timepicker
				
				validateTimes();        
						
			}
		});
		
		
	}
	private void validateTimes(){
		
		if(timelog.getStartTime().equals(newStartTime) && timelog.getEndTime().equals(newEndTime)){
			//Toast.makeText(this, "Times were not updated because no change was made", Toast.LENGTH_LONG).show();
			finish();
		}
		else{
			long lg1 = TimeHelper.getLongFromHour(newStartTime);
			long lg2 = TimeHelper.getLongFromHour(newEndTime);
			//if starttime is less than endtime 
			if(lg1 < lg2){
				updateTimelog(newStartTime, newEndTime);
				Toast.makeText(this, "Times were updated sucessfully!!", Toast.LENGTH_LONG).show();
				
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

}
