package com.tricellsoftware.timetrackertestappv2.Fragments;

import java.text.ParseException;
import java.util.Calendar;

import com.tricellsoftware.timetrackertestapp.DTOsv2.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.CompanyTable;
import com.tricellsoftware.timetrackertestapp.databasev2.TimeLogTable;
import com.tricellsoftware.timetrackertestapp.helperv2.TimeHelper;
import com.tricellsoftware.timetrackertestappv2.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class TimeLogDetails_Fragment extends Fragment {
	
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
	
	//action bar
    ActionBar actionBar;
	
	//Fragment Variables to modify fragments behavior
	FragmentManager fm = null; 
	Fragment fragment = null;
	
	NoResults_Fragment NR = null;
	
	ProgressDialog pd = null;
	
	boolean land; //landscape
	boolean sharedPrefFound; 
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	     
	  
	  // context = getActivity();
	
	     //init Business logic
		logic = new BusinessLogic(getActivity());
		//Button addNewBttn = (Button) findViewById(R.id.addnewbttn);

		//add action bar
		actionBar = getActivity().getActionBar();
		actionBar.setTitle("Time Log Details");
//		if(pd != null){
//			pd = null;
//		}
//		pd = new ProgressDialog(getActivity());
		//pd.show();
		//pd.setMessage("Loading..");


		
	 }
	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		  
		  if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			  land = true;
		  else
			  land = false;
		  
	    View view = inflater.inflate(R.layout.timelog_details_fragment,
	        container, false);
	    // setHasOptionsMenu(true);

	    return view;
	  }
	  @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
		    
		    
		    if(land){
			   
				Bundle extras = getArguments(); 
			    
				if(extras != null){
					id = getArguments().getInt(TimeLogTable.COLUMN_ID);
					//StartDate = extras.getString("StartDate");
					//EndDate = extras.getString("EndString");
	
		    	
				//business logic
				
				timelog = logic.getTimeLogByID(String.valueOf(id));
				//get user by id 1
				StartPicker = (TimePicker) getActivity().findViewById(R.id.StartTimePicker);
				EndPicker = (TimePicker) getActivity().findViewById(R.id.EndTimePicker);
				
	//			tvDate = (TextView) findViewById(R.id.dateview);
	//			tvDate.setText("Main Date: " + timelog.getDate());
				
				clockintv = (TextView) getActivity().findViewById(R.id.clockintv);
				clockintv.setText("Clock In Date: " + timelog.getStartTime().substring(0,10));
				clockouttv = (TextView) getActivity().findViewById(R.id.clockouttv);
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
				
				Button SaveBttn = (Button) getActivity().findViewById(R.id.checkbttn);
				
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
		    }
	  }
	  @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            //mListener = (OnArticleSelectedListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
	        }
	    }
	  
		private void validateTimes(){
			
			if(timelog.getStartTime().equals(newStartTime) && timelog.getEndTime().equals(newEndTime)){
				//Toast.makeText(this, "Times were not updated because no change was made", Toast.LENGTH_LONG).show();
				getActivity().finish();
			}
			else{
				long lg1 = TimeHelper.getLongFromHour(newStartTime);
				long lg2 = TimeHelper.getLongFromHour(newEndTime);
				//if starttime is less than endtime 
				if(lg1 < lg2){
					updateTimelog(newStartTime, newEndTime);
					Toast.makeText(getActivity(), "Times were updated sucessfully!!", Toast.LENGTH_LONG).show();
					
					getActivity().finish();
				}
				else
					Toast.makeText(getActivity(), "Start Time cannot be less than End Time, please try again", Toast.LENGTH_LONG).show();
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
