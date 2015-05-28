package com.tricellsoftware.timetrackertestapp.Fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tricellsoftware.timetrackertestapp.DTOs.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.Status_Enum;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.database.ProfileTable;
import com.tricellsoftware.timetrackertestapp.helper.TimeHelper;
import com.tricellsoftware.timetrackertestapp.ClocksActivity;
import com.tricellsoftware.timetrackertestapp.MainTabActivity;
import com.tricellsoftware.timetrackertestapp.R;
import com.tricellsoftware.timetrackertestapp.R.color;
import com.tricellsoftware.timetrackertestapp.SummaryActivity;

import android.app.ActionBar;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Clocks_Fragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	private BusinessLogic logic;
	private ProfileDTO profile;
	private TimeLogDTO timelog;
	
	TextView Name;
	TextView Status;
	TextView clockedtv;
	TextView Timertv;
	
	Date date; //time entry for the timelog
	int ClockType;
	Status_Enum statusEnum;
	
	SimpleDateFormat df; //date format
	String dateformat = "MM/dd/yyyy";
	
	String TimeFormat	=	"MM/dd/yyyy h:mm a"; //time format
	SimpleDateFormat tf;
	
	String startTime = "";
	
	//Timer Variables
	long startTimerTime = 0;
	long prevTime = 0;
	long TotalTime = 0;
	
	private int id;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Clocks_Fragment newInstance(int sectionNumber) {
		
		Clocks_Fragment fragment = new Clocks_Fragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public Clocks_Fragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//action bar
		ActionBar actionBar = getActivity().getActionBar();
		//actionBar.setTitle("Clock in/out");
		

		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.clocks, container,
				false);
		
		

//		TextView textView = (TextView) rootView
//				.findViewById(R.id.section_label);
//		textView.setText(Integer.toString(getArguments().getInt(
//				ARG_SECTION_NUMBER)));
		return rootView;
	}
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	   
	    
	  //find the buttons of the xml layout file
	  		final Button clockInBttn = (Button) getView().findViewById(R.id.clockinbttn);
	  		final Button clockOutBttn = (Button) getView().findViewById(R.id.clockoutbttn);
	  		//final Button timeSheetBttn = (Button) getView().findViewById(R.id.timesheetbttn);
	  		//checks if an id has been passed thru an activity
	  		Bundle extras = getActivity().getIntent().getExtras();
	  		
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
	  		logic = new BusinessLogic(getActivity());
	  		//get user by id 1
	  		profile = logic.getUser(id);
	  		Name = (TextView)getView().findViewById(R.id.dateview);
	  		Status = (TextView)getView().findViewById(R.id.textStatus);
	  		clockedtv = (TextView)getView().findViewById(R.id.clockedtimetv);
	  		
	
	  		Timertv = (TextView)getView().findViewById(R.id.lblTimer); //Re add the timer textview if implemented
	  		//Timertv.setVisibility(View.GONE);
	  		
	  		
//	  		//*******  Set up Timer    *******//
	  		
//	  		final Handler timerHandler = new Handler();
//	  		final Runnable timerRunnable = new Runnable(){
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					long millis = SystemClock.uptimeMillis() - startTimerTime;
//					TotalTime = prevTime + millis;
//					
//					String time = TimeHelper.displayHoursandMinutesSeconds(millis);
//					
//					Timertv.setText("Total Time: " + time);
//					
//					timerHandler.postDelayed(this, 0);
//				}
//	  			
//	  		};
	  		
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
	  				clockedtv.setVisibility(View.GONE);
	  				
	  			}
	  		}
	  		
	  		TextView Datetv = (TextView) getView().findViewById(R.id.vDate);
	  		//set the current date
//	  		df = new SimpleDateFormat(dateformat);
//	  		date = Calendar.getInstance().getTime();
	  		Datetv.setText(TimeHelper.getDate());
	  		
	  		timelog = logic.getTimeLogbyStatus(Status_Enum.In.getValue()); // get single timelog by status
	  		//displays the current clocked in time if not null
	  		if(timelog.getStartTime() != null){
	  			clockedtv.setText("Clocked in at " + timelog.getStartTime().substring(10));
	  			clockedtv.setBackgroundColor(color.lightgrey);
	  			clockedtv.setPadding(20, 20, 20, 20);
	  			
	  		}
	  		//clockedtv.setText("Clocked in at: " + timelog.getStartTime());
	  		//Listening to the button event
	  		clockInBttn.setOnClickListener(new View.OnClickListener(){
	  					
	  			@Override
	  			public void onClick(View view){
	  				
	  				//check if companies have been added
	  				if(profile.getCurrentCompany() == 0)
	  				{
	  					Toast.makeText(getActivity(), "You must add a Company/Project before clocking in", Toast.LENGTH_LONG).show();
	  					return;
	  				}
	  				//***** Start Timer *****//
//	  				Timertv.setVisibility(View.VISIBLE);
//	  				//startTimer(startTimerTime, timerHandler, timerRunnable);
//	  				startTimerTime = SystemClock.uptimeMillis();
//	  				//startTimer = Long.parseLong(TimeHelper.getTime());
//	  				timerHandler.postDelayed(timerRunnable, 0);
	  				
	  			
	  				//SaveNewItem();
	  				view.setBackgroundResource(R.drawable.clockin_default);
	  				clockOutBttn.setEnabled(true); //enables clock out button
	  				view.setEnabled(false); //disables clock in button
	  				clockOutBttn.setBackgroundResource(R.drawable.clockout_focused); //changes the button image to focused
	  				
	  				
	  				
	  				profile.setStatusID(Status_Enum.On.getValue()); // saves the current user state
	  				ClockType = Status_Enum.In.getValue(); // saves the type of clock the user has made
	  				
	  				startTime = TimeHelper.getTime();
	  				SaveNewTimeLog();
	  				logic.updateProfileById(profile); // updates the status of the user
	  				
	  				Status.setText("Status: " + Status_Enum.On.toString());
	  				//displays current time if getStartTime is null
	  				clockedtv.setText("Clocked in at " + startTime.substring(10));
		  			clockedtv.setBackgroundColor(color.lightgrey);
		  			clockedtv.setPadding(20, 20, 20, 20);
	  				clockedtv.setVisibility(View.VISIBLE);
	  				
//	  				if(timelog.getStartTime() == null){
//	  					startTime = TimeHelper.getTime();
//	  					clockedtv.setText("Clocked in at: " + startTime);
//	  				}
	  				
	  				/*** Create notification when clocked in ***/
	  				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity()).setSmallIcon(R.drawable.app_logo_noti)
	  						.setContentTitle("Clocked in at: " + TimeHelper.getTime() + "")
	  						.setContentText("Touch if you would like to Clock Out");
	  						
	  				Intent resultIntent = new Intent(getActivity(), MainTabActivity.class);
	  				resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	  				//Application.getApplicationContext().startActivity(resultIntent);
	  				PendingIntent resultPendingIntent =
	  				    PendingIntent.getActivity(
	  				    getActivity(),
	  				    0,
	  				    resultIntent,
	  				    PendingIntent.FLAG_UPDATE_CURRENT
	  				);
	  				
	  				mBuilder.setContentIntent(resultPendingIntent);
	  				// Sets an ID for the notification
	  				int mNotificationId = 1;
	  				getActivity().getApplicationContext();
					// Gets an instance of the NotificationManager service
	  				NotificationManager mNotifyMgr = 
	  				        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
	  				// Builds the notification and issues it.
	  				mNotifyMgr.notify(mNotificationId, mBuilder.build());
	  				/*** End of Notification  ***/
	  				
	  				
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
	  					int min = Integer.valueOf(TimeHelper.getTimeDiffInMinutes(timelog.getStartTime(), TimeHelper.getTime()));
	  					//int secsleft = 60 - secs;
	  					if(min < 1){ //if mins is less than 5 when user tries to clock out it won't update the timelog, avoiding unnecessary timelog creation
	  						Toast.makeText(getActivity(), "Please allow 1 minute after Clocking In to Clock Out", Toast.LENGTH_LONG).show();
	  						//Toast.makeText(getActivity(), "Please allow 1 minute after Clocking In to Clock Out (" + String.valueOf(secsleft) +" minutes left)", Toast.LENGTH_LONG).show();
	  					}
	  					else{
	  						
//	  		  				//*********** Stop Timer ***********//
//	  						Timertv.setVisibility(View.GONE);
//	  		  				//stopTimer(timerHandler, timerRunnable);
//	  						prevTime += startTimerTime;
//	  		  				timerHandler.removeCallbacks(timerRunnable);
	  						
	  						clockedtv.setVisibility(View.GONE);
	  						profile.setStatusID(Status_Enum.Off.getValue());
	  						ClockType = Status_Enum.Out.getValue();
	  						UpdateTimeLog();
	  						clockInBttn.setBackgroundResource(R.drawable.clockin_focused);
	  						clockInBttn.setEnabled(true);
	  						view.setEnabled(false);
	  						view.setBackgroundResource(R.drawable.clockout_default);
	  						
	  						logic.updateProfileById(profile); // updates the status of the user
	  						
	  						Status.setText("Status: " + Status_Enum.Off.toString());
	  						
	  		  				/*** Create notification when clocked out ***/
	  		  				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity()).setSmallIcon(R.drawable.app_logo_noti)
	  		  						.setContentTitle("Clocked out at: " + TimeHelper.getTime());
	  		  						//.setContentText();
	  		  				
	  		  				//Intent resultIntent = new Intent(getActivity(), MainTabActivity.class);
	  		  				
	  		  				
	  		  				
//	  		  				PendingIntent resultPendingIntent =
//	  		  				    PendingIntent.getActivity(
//	  		  				    getActivity(),
//	  		  				    0,
//	  		  				    resultIntent,
//	  		  				    PendingIntent.FLAG_UPDATE_CURRENT
//	  		  				);
//	  		  				
//	  		  				mBuilder.setContentIntent(resultPendingIntent);
	  		  				// Sets an ID for the notification
	  		  				int mNotificationId = 1;
	  		  				getActivity().getApplicationContext();
	  						// Gets an instance of the NotificationManager service
	  		  				NotificationManager mNotifyMgr = 
	  		  				        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
	  		  				// Builds the notification and issues it.
	  		  				mNotifyMgr.notify(mNotificationId, mBuilder.build());
	  		  				/*** End of Notification  ***/
	  					}
	  				

	  				} catch (ParseException e) {
	  					// TODO Auto-generated catch block
	  					e.printStackTrace();
	  				}//updates the current timelog 
	  			}
	  		});
	  		
	  	
	  		
	  		
	  		
	  		
	  		
	  		//*******  End of Timer    *******//
	  		
//	  		timeSheetBttn.setOnClickListener(new View.OnClickListener() {
//	  			
//	  			@Override
//	  			public void onClick(View v) {
//	  				// TODO Auto-generated method stub
//	  				//Starting a new intent
//	  				Intent i = new Intent(getActivity(), SummaryActivity.class);
//	  				startActivity(i);
////	  				Intent TimelogScreen = new Intent(getApplicationContext(), TimeLogListActivity.class);
////	  				startActivity(TimelogScreen);
//	  			}
//	  		});
	}
//	private void startTimer(long startTimerTime, Handler timerHandler, Runnable timerRunnable){
//		
//			startTimerTime = SystemClock.uptimeMillis();
//			//startTimer = Long.parseLong(TimeHelper.getTime());
//			timerHandler.postDelayed(timerRunnable, 0);
//	}
//	private void stopTimer(Handler timerHandler, Runnable timerRunnable){
//		
//		timerHandler.removeCallbacks(timerRunnable);
//	}
	//saves new timelog record
	private void SaveNewTimeLog(){
		
		String YearWeek = Integer.toString(TimeHelper.getWeekOfYear());
		
		/**May add the week of the year to query timelogs by week **/
		startTime = TimeHelper.getTime();
		timelog = new TimeLogDTO();
		timelog.setDate(TimeHelper.getDate()); //saves the date format as string
		timelog.setStartTime(startTime);//saves the time format as string
		timelog.setEndTime("--");
		timelog.setProfileID(profile.getID());
		timelog.setStatusID(ClockType);
		timelog.setYearWeek(YearWeek);
		timelog.setCompanyId(profile.getCurrentCompany());
		logic.AddNewTimeLog(timelog);
	}
	//saves new timelog record
	private void UpdateTimeLog() throws ParseException{
		
		timelog.setEndTime(TimeHelper.getTime());
		String TotalMillis = TimeHelper.getTimeDiffInMillis(timelog.getStartTime(),timelog.getEndTime());
		timelog.setStatusID(ClockType);
		timelog.setMilliseconds(TotalMillis);
		logic.updateTimeLogStatusID(timelog, Status_Enum.In.getValue());
	}

}
