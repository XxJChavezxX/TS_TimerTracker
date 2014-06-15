package com.tricellsoftware.timetrackertestappv2.Fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tricellsoftware.timetrackertestapp.DTOsv2.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.DTOsv2.Status_Enum;
import com.tricellsoftware.timetrackertestapp.DTOsv2.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.ProfileTable;
import com.tricellsoftware.timetrackertestapp.helperv2.TimeHelper;
import com.tricellsoftware.timetrackertestappv2.ClocksActivity;
import com.tricellsoftware.timetrackertestappv2.MainTabActivity;
import com.tricellsoftware.timetrackertestappv2.R;
import com.tricellsoftware.timetrackertestappv2.SummaryActivity;

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
	
	
	Date date; //time entry for the timelog
	int ClockType;
	Status_Enum statusEnum;
	
	SimpleDateFormat df; //date format
	String dateformat = "MM/dd/yyyy";
	
	String TimeFormat	=	"MM/dd/yyyy h:mm a"; //time format
	SimpleDateFormat tf;
	
	String time;
	
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
	  		
	  		TextView Datetv = (TextView) getView().findViewById(R.id.vDate);
	  		//set the current date
//	  		df = new SimpleDateFormat(dateformat);
//	  		date = Calendar.getInstance().getTime();
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
	  				
	  				/*** Create notification when clocked in ***/
	  				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity()).setSmallIcon(R.drawable.noti_icon)
	  						.setContentTitle("Clocked in at: " + TimeHelper.getTime())
	  						.setContentText("Click here if you would like to Clock Out");
	  				
	  				Intent resultIntent = new Intent(getActivity(), MainTabActivity.class);
	  				resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK 
	  						| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
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
	  					int mins = Integer.valueOf(TimeHelper.getTimeDiffInMinutes(timelog.getStartTime(), TimeHelper.getTime()));
	  					int minsleft = 5 - mins;
	  					if(mins < 5){ //if mins is less than 5 when user tries to clock out it won't update the timelog, avoiding unnecessary timelog creation
	  						Toast.makeText(getActivity(), "Please allow 5 minutes after Clocking In to Clock Out (" + String.valueOf(minsleft) +" minutes left)", Toast.LENGTH_LONG).show();
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
	  						
	  		  				/*** Create notification when clocked out ***/
	  		  				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity()).setSmallIcon(R.drawable.noti_icon)
	  		  						.setContentTitle("Clocked out at: " + TimeHelper.getTime());
	  		  				
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
