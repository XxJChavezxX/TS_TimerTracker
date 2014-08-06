package com.tricellsoftware.timetrackertestapp.Fragments;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import com.tricellsoftware.timetrackertestapp.DTOs.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.Status_Enum;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.database.CompanyTable;
import com.tricellsoftware.timetrackertestapp.database.TimeLogTable;
import com.tricellsoftware.timetrackertestapp.helper.CustomArrayAdapter;
import com.tricellsoftware.timetrackertestapp.helper.TimeHelper;
import com.tricellsoftware.timetrackertestapp.EditTimeActivity;
import com.tricellsoftware.timetrackertestapp.R;

import android.R.menu;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class TimeLogDetails_Fragment extends Fragment {
	
	private BusinessLogic logic;
	private TimeLogDTO timelog;
	List<CompanyDTO> companies; //will hold the list of companies for spinner
	List<String> CompNames;
	
	private int id;
	String StartDate;
	String EndDate;
	
	static String FStartDate;
	static String FEndDate;
	
	private TimePicker StartPicker;
	private TimePicker EndPicker;
	//private TextView tvDate;
	private TextView clockintv;
	private TextView clockouttv;
	int profileId = 1;
	
	static String TimeFormat	=	"MM/dd/yyyy hh:mm a"; //time format
	
	String newStartTime;
	String newEndTime;
	private int compId;
	
	//action bar
    ActionBar actionBar;
	
	//Fragment Variables to modify fragments behavior
	FragmentManager fm = null; 
	Fragment fragment = null;
	
	NoResults_Fragment NR = null;
	
	ProgressDialog pd = null;
	
	//The following are needed to update the list on the timelogs fragment
	CustomArrayAdapter adapter;
	List<TimeLogDTO> timelogs;
	ListView lv;
	Bundle activityextras;
	private TextView totalHourstv;
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
	
	boolean land; //landscape
	boolean sharedPrefFound; 
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	     
	  
	  // context = getActivity();
	  	setHasOptionsMenu(true);
	
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
		    
		    try{
		
		    	
			    activityextras = getActivity().getIntent().getExtras();
			    
			    if(land){
				   
					Bundle extras = getArguments(); 
				    
					if(extras != null){
						id = getArguments().getInt(TimeLogTable.COLUMN_ID);
						StartDate = extras.getString("StartDate");
						EndDate = extras.getString("EndDate");
		
			    	
					//business logic
					
					timelog = logic.getTimeLogByID(String.valueOf(id));
					
					companies = logic.getAllCompanies();
					
					totalHourstv = (TextView) getActivity().findViewById(R.id.totalHourstxt);
					//get user by id 1
//					StartPicker = (TimePicker) getActivity().findViewById(R.id.StartTimePicker);
//					EndPicker = (TimePicker) getActivity().findViewById(R.id.EndTimePicker);
					
					//Initialize buttons
					StartEditBtn = (Button) getActivity().findViewById(R.id.starteditbttn);
					EndEditBtn = (Button) getActivity().findViewById(R.id.endeditbttn);
					
					//Init Edit Texts
					Starttxt = (EditText) getActivity().findViewById(R.id.startEditText);
					Endtxt = (EditText) getActivity().findViewById(R.id.endEditText);
					
					/** Spinner set up **/
					CompSpinner = (Spinner) getActivity().findViewById(R.id.compspinner);
					
		//			tvDate = (TextView) findViewById(R.id.dateview);
		//			tvDate.setText("Main Date: " + timelog.getDate());
					
//					clockintv = (TextView) getActivity().findViewById(R.id.clockintv);
//					clockintv.setText("Clock In Date: " + timelog.getStartTime().substring(0,10));
//					clockouttv = (TextView) getActivity().findViewById(R.id.clockouttv);
//					clockouttv.setText("Clock Out Date: " + timelog.getEndTime().substring(0,10));
					
					if(timelog != null){
	
						Starttxt.setText(timelog.getStartTime().substring(11));
	
						Endtxt.setText(timelog.getEndTime().substring(11));
						
					}
					
	//				//Set hours and minutes to the date picker
	//				Calendar c = TimeHelper.setCalendar(timelog.getStartTime());
	//				c.getTime();
	//				StartPicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY)); // sets hour with am or pm time
	//				StartPicker.setCurrentMinute(c.get(Calendar.MINUTE));
	//				Calendar cl = TimeHelper.setCalendar(timelog.getEndTime());
	//				cl.getTime();
	//				EndPicker.setCurrentHour(cl.get(Calendar.HOUR_OF_DAY));// sets hour with am or pm time
	//				EndPicker.setCurrentMinute(cl.get(Calendar.MINUTE));
	//				//String srt = StartPicker.getCurrentHour().toString();
					
					//look for the list view realted to the Activity on the timelogs_fragment
			    	lv = (ListView) getActivity().findViewById(android.R.id.list);
					//StartPicker.set/
					//Initialize buttons
					StartEditBtn = (Button) getActivity().findViewById(R.id.starteditbttn);
					EndEditBtn = (Button) getActivity().findViewById(R.id.endeditbttn);
					
					//Init Edit Texts
					Starttxt = (EditText) getActivity().findViewById(R.id.startEditText);
					Endtxt = (EditText) getActivity().findViewById(R.id.endEditText);
					
					StartDatetxt = (EditText) getActivity().findViewById(R.id.startdateedittxt);
					EndDatetxt = (EditText) getActivity().findViewById(R.id.enddateedittxt);
					StartDateEditBtn = (Button) getActivity().findViewById(R.id.startdateeditbtn);
					EndDateEditBtn = (Button) getActivity().findViewById(R.id.enddateeditbtn);
					
			    	/** Spinner set up **/
					CompSpinner = (Spinner) getActivity().findViewById(R.id.compspinner);
					
					// Create an ArrayAdapter using the string array and a default spinner layout
					ArrayAdapter<CompanyDTO> adapter = new ArrayAdapter<CompanyDTO>(getActivity(), android.R.layout.simple_spinner_item, companies);
					// Apply the adapter to the spinner
					CompSpinner.setAdapter(adapter);
					int index = 0;
					for(int i = 0; i < companies.size(); i++){
						String compname = adapter.getItem(i).toString();
						String dtoCompName = logic.getCompanyById(timelog.getCompanyId()).getName();
						if(compname.equals(dtoCompName)){
							index = i;
							break;
						}
					}
					CompSpinner.setSelection(index);
					
					/*** End of Spinner***/
					
					Button SaveBttn = (Button) getActivity().findViewById(R.id.checkbttn);
					
					
					if(timelog != null){
						
//						tvDate = (TextView) findViewById(R.id.dateview);
//						tvDate.setText("Main Date: " + timelog.getDate());
						
//						clockintv = (TextView) findViewById(R.id.clockintv);
//						clockintv.setText("Clock In Date: " + timelog.getStartTime().substring(0,10));
//						clockouttv = (TextView) findViewById(R.id.clockouttv);
//						clockouttv.setText("Clock Out Date: " + timelog.getEndTime().substring(0,10));
						StartDatetxt.setText(timelog.getStartTime().substring(0,10));
						EndDatetxt.setText(timelog.getEndTime().substring(0,10));
						

						Starttxt.setText(timelog.getStartTime().substring(11));
						Endtxt.setText(timelog.getEndTime().substring(11));
						
						totalHourstv.setText("Total Time: " + TimeHelper.displayHoursandMinutes(timelog.getMinutes()));
						
						//String srt = StartPicker.getCurrentHour().toString();
						
//							//Set hours and minutes to the date picker
//							Calendar c = TimeHelper.setCalendar(timelog.getStartTime());
//							c.getTime();
//							StartPicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY)); // sets hour with am or pm time
//							StartPicker.setCurrentMinute(c.get(Calendar.MINUTE));
//							Calendar cl = TimeHelper.setCalendar(timelog.getEndTime());
//							cl.getTime();
//							EndPicker.setCurrentHour(cl.get(Calendar.HOUR_OF_DAY));// sets hour with am or pm time
//							EndPicker.setCurrentMinute(cl.get(Calendar.MINUTE));
//							//String srt = StartPicker.getCurrentHour().toString();
						}
						else{
							
							Starttxt.setText(TimeHelper.getTime().substring(11));
							Endtxt.setText(TimeHelper.getTime().substring(11));
							
							StartDatetxt.setText(TimeHelper.getDate());
							EndDatetxt.setText(TimeHelper.getDate());
							
							totalHourstv.setText("Total Time: N/A");
						}
					
					//pd.hide();
					
					//Listening to the button event
					SaveBttn.setOnClickListener(new View.OnClickListener(){
								
						@Override
						public void onClick(View view){
								
							if(timelog != null){
								newStartTime = (FStartDate == null || FStartDate.equals("")) ? timelog.getStartTime() : FStartDate; //Get the starttime from timepicker
								newEndTime = (FEndDate == null || FEndDate.equals("")) ? timelog.getEndTime() : FEndDate; //Get the endtime from timepicker
								
								FStartDate = "";
								FEndDate ="";
								
								validateTimes();    
							}
							long longDate1 = TimeHelper.getLongFromDate(StartDatetxt.getText().toString());
							long longDate2 = TimeHelper.getLongFromDate(EndDatetxt.getText().toString());
							if(longDate1 <= longDate2){
								validateTimes();
							}
							else
								Toast.makeText(getActivity(), "Start Date cannot be greater than End Date", Toast.LENGTH_LONG).show();
	
						}
					});
					//Listening to the spinner
					CompSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
	
						@Override
						public void onItemSelected(AdapterView<?> parent, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							compId = companies.get(position).getID();
							
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
							Calendar c;
							if(FStartDate == null || FStartDate.equals("")){
								c = TimeHelper.setCalendar(timelog.getStartTime());
							}
							else
								c = TimeHelper.setCalendar(FStartDate);
							c.getTime();
							int hour = c.get(Calendar.HOUR_OF_DAY);
							int minutes = c.get(Calendar.MINUTE);
							//Init Time Picker Dialog
							TimePickerDialog tdp = new TimePickerDialog(getActivity(), new OnTimeSetListener() {
								
								@Override
								public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
									// TODO Auto-generated method stub
									//** Set Start Time Txt **//
									String date = null;
									if(timelog != null){
										date = timelog.getStartTime();
									}
									else
										date = TimeHelper.getDate();
									//substring is used to only display the hours
									FStartDate = TimeHelper.getTimeFromTimePicker(view, date);
									Starttxt.setText(FStartDate.substring(11));
									
									//update total hours text view
									String minutes = null;
									try {
										minutes = TimeHelper.getTimeDiffInMinutes(FStartDate, EndDatetxt.getText().toString()  + " " + Endtxt.getText().toString());
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									totalHourstv.setText("Total Time: " + TimeHelper.displayHoursandMinutes(minutes));
									
									//clear static values
									
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
							Calendar c;
							if(FEndDate == null || FEndDate.equals("")){
								c = TimeHelper.setCalendar(timelog.getEndTime());
							}
							else
								c = TimeHelper.setCalendar(FEndDate);
							c.getTime();
							int hour = c.get(Calendar.HOUR_OF_DAY);
							int minutes = c.get(Calendar.MINUTE);
							//Init Time Picker Dialog
							TimePickerDialog tdp = new TimePickerDialog(getActivity(), new OnTimeSetListener() {
								
								@Override
								public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
									// TODO Auto-generated method stub
									//** Set End Time Txt **//
									String date = null;
									if(timelog != null){
										date = timelog.getEndTime();
									}
									else
										date = TimeHelper.getTime();
									
									FEndDate = TimeHelper.getTimeFromTimePicker(view, date);
									Endtxt.setText(FEndDate.substring(11));

									
									//update total hours text view
									String minutes = null;
									try {
										minutes = TimeHelper.getTimeDiffInMinutes(FEndDate, StartDatetxt.getText().toString() + " " + Starttxt.getText().toString());
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									totalHourstv.setText("Total Time: " + TimeHelper.displayHoursandMinutes(minutes));
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
					            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
					                    new DatePickerDialog.OnDateSetListener() {
					 
					                        @Override
					                        public void onDateSet(DatePicker view, int year,
					                                int monthOfYear, int dayOfMonth) {
					                            // Display Selected date in Edit
					                        	String date = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
					                        	date = TimeHelper.formatDate(date);
					                        	StartDatetxt.setText(date);
					                			EndDatetxt.setText(date);

					                			FStartDate = date + " " + Starttxt.getText().toString();
					                			FEndDate = EndDatetxt.getText().toString()  + " " + Endtxt.getText().toString();
					                			
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
					            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
					                    new DatePickerDialog.OnDateSetListener() {
					 
					                        @Override
					                        public void onDateSet(DatePicker view, int year,
					                                int monthOfYear, int dayOfMonth) {
					                            // Display Selected date in Edit
					                        	String date = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
					                        	date = TimeHelper.formatDate(date);
					                			EndDatetxt.setText(date);
					                			
					                			FEndDate = date + " " + Endtxt.getText().toString();
					                			FStartDate = StartDatetxt.getText().toString() + " " + Starttxt.getText().toString();
					                        }
					                    }, mYear, mMonth, mDay);
					            dpd.show();
								
							}
						});
					
				
					}
			    }
			    else{
			    	 
			    }
			    
			    
			}
			catch(Exception e){
				Toast.makeText(getActivity(), "Unexpcted Error..." + e.getMessage(), Toast.LENGTH_LONG).show();
			}
	  
		
	}
	  
//	//create menu options for the action bar
//			@Override
//			public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//				if(land){
//					MenuInflater inflateLayout = getActivity().getMenuInflater();
//	    		    inflateLayout.inflate(R.menu.details_menu, menu);
//	    		    super.onCreateOptionsMenu(menu, inflateLayout);
//				}
//		    } 
//			@Override
//			public boolean onOptionsItemSelected(MenuItem item) {
//				//Gets the position on the Item selected
//				//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//				//_id = companies.get((int) info.id).getID();
//			   switch (item.getItemId()) {
//			   case R.id.action_settings:
//			    	  Toast.makeText(getActivity(), "Settings was selected", Toast.LENGTH_LONG).show();
//			   break;
////			   case R.id.action_delete:
////				   
////					// 1. Instantiate an AlertDialog.Builder with its constructor
////					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////
////					// 2. Chain together various setter methods to set the dialog characteristics
////					builder.setMessage("Are you sure you want to delete this record?")
////					       .setTitle("Delete");
////
////					// 3. Get the AlertDialog from create()
////					
////					builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
////			           @Override
////					public void onClick(DialogInterface dialog, int _id) {
////			        	   logic.deleteTimeLogById(id);
////			        	   if(logic.Error != null){
////							   if(logic.Error.contains("key constraint")){
////								   Toast.makeText(getActivity(), "This record is being used somewhere else and cannot be deleted", Toast.LENGTH_LONG).show();
////							   }
////							   else
////								   Toast.makeText(getActivity(), "Unexpected Error Occurred", Toast.LENGTH_LONG).show();
////						   }
////						   else{
////							   Toast.makeText(getActivity(), "Record has been deleted successfully", Toast.LENGTH_LONG).show();
////							   RefreshCompaniesList(lv, adapter);
////						   }
////			        	  
////			        	   
////			        	   //*********  Removes deleted timelog *********//
////							FragmentTransaction ft = getFragmentManager().beginTransaction();
////							//check of the framelayout is present on the Activity associated with this ListFragment
////							if(getView().findViewById(R.id.fragment_timelog_container) != null){
////								return;
////							}// However, if we're being restored from a previous state,
////				            // then we don't need to do anything and should return or else
////				            // we could end up with overlapping fragments.
//////				            if (savedInstanceState != null) {
//////				                return;
//////				            }
////				            //Creates Company Fragment to be held in the Activity layout using the FrameLayout
////				            NoResults_Fragment nr = new NoResults_Fragment();
////				            // In case this activity was started with special instructions from an
////				            // Intent, pass the Intent's extras to the fragment as arguments
////				            //cf.setArguments(getActivity().getIntent().getExtras());
////				
////				            //Add the fragment to the 'fragment_container' FrameLayout
////				            ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
////				            ft.replace(R.id.fragment_timelog_container, nr).commit();
////			           }
////			       });
////					builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
////			           @Override
////					public void onClick(DialogInterface dialog, int id) {
////			               // User cancelled the dialog
////			        	   dialog.cancel();
////			           }
////			       });
////					
////					// Create the AlertDialog
////					AlertDialog dialog = builder.create();
////
////					// show it
////					dialog.show();
////
////					 //Toast.makeText(this, "Record has been deleted successfully", Toast.LENGTH_LONG).show();
////			   break;
//			    	  
//			   }
//			   return super.onOptionsItemSelected(item);
//			 } 
	  
	  
	  
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
			
			/***** Updates existing timelog ******/
			if(timelog != null){
				if(timelog.getStartTime().equals(newStartTime) && timelog.getEndTime().equals(newEndTime) && timelog.getCompanyId() == compId){
					//Toast.makeText(this, "Times were not updated because no change was made", Toast.LENGTH_LONG).show();
					//getActivity().finish();
					
				}
				else{
					long lg1 = TimeHelper.getLongFromHour(newStartTime);
					long lg2 = TimeHelper.getLongFromHour(newEndTime);
					//if starttime is less than endtime 
					if(lg1 < lg2){
						updateTimelog(newStartTime, newEndTime);
						Toast.makeText(getActivity(), "Timelog was updated successfully!", Toast.LENGTH_LONG).show();
						RefreshCompaniesList(lv, adapter);
						//getActivity().recreate();
						//getActivity().finish();
					}
					else
						Toast.makeText(getActivity(), "Start Time cannot be less than End Time, please try again", Toast.LENGTH_LONG).show();
				}
			}
			///***** creates a new timelog ******///
			else{
				long lg1 = TimeHelper.getLongFromHour(StartDatetxt.getText().toString() + " " + Starttxt.getText().toString());
				long lg2 = TimeHelper.getLongFromHour(EndDatetxt.getText().toString()  + " " + Endtxt.getText().toString());
				
				if(lg1 < lg2){
					SaveNewTimeLog();
					Toast.makeText(getActivity(), "Timelog was created successfully!", Toast.LENGTH_LONG).show();
					
					RefreshCompaniesList(lv, adapter);
				}
				else
					Toast.makeText(getActivity(), "Start Time cannot be less than End Time, please try again", Toast.LENGTH_LONG).show();
			}
				
		}
		//Refreshes the list related to this activity located on the Timelogs Fragment 
		private void RefreshCompaniesList(ListView lv, CustomArrayAdapter adapter){
			this.lv = lv;
			this.adapter = adapter;
			
			//StartDate = activityextras.getString("StartDate");
			//EndDate = activityextras.getString("EndDate");
			timelogs = logic.getAllTimeLogsByWeek(StartDate, EndDate);
			
			adapter = new CustomArrayAdapter(getActivity(), android.R.id.list, timelogs);
			lv.setAdapter(adapter);
		    
		    adapter.notifyDataSetChanged();
		    
		    
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
			timelog.setCompanyId(compId);
			logic.AddNewTimeLog(timelog);
		}
		private void updateTimelog(String startTime, String endTime){
			/**updates starttime and end time**/
			timelog = new TimeLogDTO();
			
			try {
				timelog.setID(id);
				timelog.setDate(StartDatetxt.getText().toString());
				timelog.setCompanyId(compId);
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
