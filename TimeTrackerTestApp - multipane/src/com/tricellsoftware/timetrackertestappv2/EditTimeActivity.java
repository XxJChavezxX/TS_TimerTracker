package com.tricellsoftware.timetrackertestappv2;

import java.text.ParseException;
import java.util.Calendar;

import com.tricellsoftware.timetrackertestapp.DTOsv2.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.CompanyTable;
import com.tricellsoftware.timetrackertestapp.databasev2.TimeLogTable;
import com.tricellsoftware.timetrackertestapp.helperv2.TimeHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditTimeActivity extends Activity {
	
	private BusinessLogic logic;
	private TimeLogDTO timelog;
	
	public static int id;
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
//		      /** Sending ID back to the Timeloglist fragment if this activity is in landscape mode**/	
//		      if(id > 0){
//					/** Share id with other activities or fragments by using sharedPref method
//					 * **/
//		    	  SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.pref_data_key), this.MODE_PRIVATE);
//		    	  SharedPreferences.Editor editor = sharedPref.edit();
//		    	  editor.putString(getString(R.string.timelog_id), String.valueOf(id));
//		    	  editor.commit();
//		      }	
	      finish();
	      return;
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
		
		if(extras != null){
			id = extras.getInt(TimeLogTable.COLUMN_ID);
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
		
		//pd.hide();
		
		//Listening to the button event
		SaveBttn.setOnClickListener(new View.OnClickListener(){
					
			@Override
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
