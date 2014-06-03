package com.tricellsoftware.timetrackertestappv2.Fragments;

import java.util.List;



import com.tricellsoftware.timetrackertestapp.DTOsv2.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.TimeLogTable;
import com.tricellsoftware.timetrackertestapp.helperv2.CustomArrayAdapter;
import com.tricellsoftware.timetrackertestappv2.CalendarViewActivity;
import com.tricellsoftware.timetrackertestappv2.CompanyActivity;
import com.tricellsoftware.timetrackertestappv2.EditTimeActivity;
import com.tricellsoftware.timetrackertestappv2.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TimeLogs_Fragment extends ListFragment {
	//ArrayAdapter<TimeLogDTO> adapter;
	List<TimeLogDTO> timelogs;
	CustomArrayAdapter adapter;
	private String date;
	
	OnLogSelectedListener mListener;
	
	TextView txtStatus;

	private BusinessLogic logic;
	
	long miliseconds = 0;
	//action bar
	private ActionBar actionBar;
	
	private int _id = 0;
	private static int timeid = 0;
	String StartDate;
	String EndDate;
	
	//Fragment Variables to modify fragments behavior
	FragmentManager fm = null; 
	Fragment fragment = null;
	
	NoResults_Fragment NR = null;
	
	ProgressDialog pd = null;
	TimeLogDetails_Fragment newTF; //new details fragment
	
	Context context = null;
	
	View Mainview; //view to add other widgets
	
	TextView tv; //dynamically added textview
	
	boolean land; //landscape
	boolean sharedPrefFound; 
	
	SharedPreferences sharedPref;
	
	NoResults_Fragment nr;
	
	ListView lv;

	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	    
	  
	   context = getActivity();
	
	     //init Business logic
		logic = new BusinessLogic(getActivity());
		//Button addNewBttn = (Button) findViewById(R.id.addnewbttn);

		//add action bar
		actionBar = getActivity().getActionBar();
		actionBar.setTitle("Time Logs");
		
		setHasOptionsMenu(true);
		
	
	 }
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		  
			if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
				land = true;
			}
			else
				land = false;
			
	    View view = inflater.inflate(R.layout.list_view_container, //loads the view list container that holder the custom array adapter, the adapter loads the fragment
	        container, false);
	    
	    Mainview = view;
	   
	    return view;
	  }
	  @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
		    //get list view
		    registerForContextMenu(getListView());
		    
		    if(land == false && timeid > 0){
		    	 Intent i = new Intent(getActivity(), EditTimeActivity.class);
		    	
				 i.putExtra(TimeLogTable.COLUMN_ID, timeid);
				 //add start time and end time
				 i.putExtra("StartDate", StartDate);
				 i.putExtra("StartDate", EndDate);
				 startActivity(i);
				 timeid = 0;
		    }
		    
		
		    
//			//time log id is being shared by a static field which we use in this list fragment.
//			if(EditTimeActivity.id > 0){
//				//pass the value to the id of this fragment
//				_id = EditTimeActivity.id;
//				//reset the timelog id back to 0
//				EditTimeActivity.id  = 0;
//			}
			/**check for shared preferences, 
			 * We passed the Timelog id from the EditTimelog Activity class and we retrieve by using the getSharedPreferences
			 * method. This was the only way to pass the id from an activity that does not have direct contact with this fragment.
			 * **/
			SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.pref_data_key),0);
			if(sharedPref != null){
				if(sharedPref.contains(getString(R.string.timelog_id)) && land){
					_id = Integer.parseInt(sharedPref.getString(getString(R.string.timelog_id), ""));
					sharedPref.edit().remove(getString(R.string.pref_data_key)).clear().commit();
				}
				
			}
			
			if(_id <= 0) {  
			  //Get Fragment Container if it's present on the Activity
				 if (land){
				
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					//check of the framelayout is present on the Activity associated with this ListFragment
					if(getView().findViewById(R.id.fragment_timelog_container) == null){
						return;
					}// However, if we're being restored from a previous state,
		            // then we don't need to do anything and should return or else
		            // we could end up with overlapping fragments.
		            if (savedInstanceState != null) {
		                return;
		            }
		            //Creates Company Fragment to be held in the Activity layout using the FrameLayout
		            NoResults_Fragment nr = new NoResults_Fragment();
		            // In case this activity was started with special instructions from an
		            // Intent, pass the Intent's extras to the fragment as arguments
		            //cf.setArguments(getActivity().getIntent().getExtras());
		
		            //Add the fragment to the 'fragment_container' FrameLayout
		            ft.add(R.id.fragment_timelog_container, nr).commit();
				
				 }
				 else{
					 
				 }
			}
			else{
		    	// Create fragment and give it an argument specifying the item it should show
		    	  newTF = new TimeLogDetails_Fragment(); // new company fragment
	        	  Bundle args = new Bundle();
	        	  args.putInt(TimeLogTable.COLUMN_ID, _id);
	        	  newTF.setArguments(args);
	        	  FragmentTransaction transaction = getFragmentManager().beginTransaction();


		         //Begin fragment transaction to show the new fragment and replace the old one 
	        	 transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
	        	 transaction.replace(R.id.fragment_timelog_container, newTF);
			     
	        	 transaction.commit();

			}
			
			//GetTimeLogData();
//			//clear data from shared pref file
//	       	 if(sharedPrefFound){
//		 			/**Clear Timelog value from shared pref**/
//			    	SharedPreferences.Editor editor = sharedPref.edit();
//			    	editor.clear();
//					editor.commit();
//					sharedPrefFound = false;
//	       	 }
		   
	  }
	    // Container Activity must implement this interface
	    public interface OnLogSelectedListener {
	        public void onLogSelected(String item);
	    }

	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            mListener = (OnLogSelectedListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement OnItemSelectedListener");
	        }
	    }
	  
		@SuppressLint("NewApi")
		public void GetTimeLogData(){

			Bundle extras = getActivity().getIntent().getExtras();
			
			if(date != null){
				timelogs = logic.getAllTimeLogsByDate(date);
				
			}
			else if(extras != null){
				StartDate = extras.getString("StartDate");
				EndDate = extras.getString("EndDate");
				timelogs = logic.getAllTimeLogsByWeek(StartDate, EndDate);
				
				if(timelogs.size() <= 0){
					tv = new TextView(getActivity());
					
					tv.setText("No Time Logs have been created");
					tv.setPaddingRelative(20, 20, 20, 20);
					
					//View view = Mainview.findViewById(R.layout.company_rates_fragment);
					((LinearLayout)Mainview).addView(tv);
					
					//cfView.addView(tv);
				}
				else{
					
					if(tv != null){
						tv.setVisibility(View.GONE);
						((LinearLayout)Mainview).removeView((View)tv.getParent());
					}
				}
			}
			else{
				timelogs = logic.getAllTimeLogs();
			
			}
				//timelogs = logic.getAllTimeLogsByWeek(StartDate, EndDate);

			
			adapter = new CustomArrayAdapter(context, R.layout.timelog_customlist_fragment, timelogs);
			adapter.notifyDataSetChanged();
			
		    setListAdapter(adapter);
		    
		    
		    
		    //reset id
		   // _id = 0;
		}
		
		
		//create menu options for the action bar
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    		   MenuInflater inflateLayout = getActivity().getMenuInflater();
	    		   inflateLayout.inflate(R.menu.timelog_menu, menu);
	    		  super.onCreateOptionsMenu(menu, inflateLayout);
	    } 
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
		  super.onListItemClick(l, v, position, id);

		  if(timelogs.get(position).getEndTime().equals("--")){
				Toast.makeText(getActivity(), "Please Clock Out before trying to modify the Times", Toast.LENGTH_LONG).show();
			}
		   else{
		  
			//get id of the selected item
			   _id = timelogs.get(position).getID();
			   //i.putExtra(CompanyTable.COLUMN_ID, _id);
			   timeid = _id;
				//if screen is large (7 inches)
			    if (land){
			    		//&& (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			    	 //mListener.onLogSelected(String.valueOf(_id));
			    	 
			    	// Create fragment and give it an argument specifying the item it should show
			    	  newTF = new TimeLogDetails_Fragment(); // new company fragment
		        	  Bundle args = new Bundle();
		        	  args.putInt(TimeLogTable.COLUMN_ID, _id);
		        	  newTF.setArguments(args);
		        	  FragmentTransaction transaction = getFragmentManager().beginTransaction();
	
	
			         //Begin fragment transaction to show the new fragment and replace the old one 
		        	 transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
		        	 transaction.replace(R.id.fragment_timelog_container, newTF);
				     
		        	 transaction.commit();
			    }
			    else{
					  Intent i = new Intent(getActivity(), EditTimeActivity.class);
					  
						if(timelogs.get(position).getEndTime().equals("--")){
							Toast.makeText(getActivity(), "Please Clock Out before trying to modify the Times", Toast.LENGTH_LONG).show();
						}
						else{
							 //_id = timelogs.get(position).getID();
							 i.putExtra(TimeLogTable.COLUMN_ID, _id);
							 //add start time and end time
							 i.putExtra("StartDate", StartDate);
							 i.putExtra("StartDate", EndDate);
							 startActivity(i);
						}
			    }
			  }
         //Toast.makeText(getActivity(), "item: "+ _id, Toast.LENGTH_LONG).show();
		  
		  

		    //mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
		
		    //Uri CompanyUri = Uri.parse(TimeTrackerContentProvider.Content_URI + "/" + id);
		  
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			//Gets the position on the Item selected
			//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			//_id = companies.get((int) info.id).getID();
		   switch (item.getItemId()) {
		   case R.id.action_settings:
		    	  Toast.makeText(getActivity(), "Settings was selected", Toast.LENGTH_LONG).show();
		   break;
		   case R.id.action_search:
			  
				Intent i = new Intent(getActivity(), CalendarViewActivity.class);
				i.putExtra("selectedDate", date);
				startActivityForResult(i, 1);
				
				//_id = 0;
				
		   break;
		    	  
		   }
		   return super.onOptionsItemSelected(item);
		 } 
		
	    @Override
		public void onStart(){
	    	super.onStart();
	    }
	    ///Refreshes the List View
	    @SuppressLint("NewApi")
		@Override
		public void onResume(){
	    	super.onResume();
	    	//pd.show();
	    	GetTimeLogData();
	    	if (land &&  nr == null || land && timelogs.isEmpty()){// && newCF == null){
				
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				//check of the framelayout is present on the Activity associated with this ListFragment
				if(getView().findViewById(R.id.fragment_timelog_container) != null){
					
				}// However, if we're being restored from a previous state,
	            // then we don't need to do anything and should return or else
	            // we could end up with overlapping fragments.
	            //if (savedInstanceState != null) {
	          //      return;
	          //  }
	            //Creates Company Fragment to be held in the Activity layout using the FrameLayout
	            nr = new NoResults_Fragment();
	            // In case this activity was started with special instructions from an
	            // Intent, pass the Intent's extras to the fragment as arguments
	            //cf.setArguments(getActivity().getIntent().getExtras());
	
	            //Add the fragment to the 'fragment_container' FrameLayout
	            //ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
	            ft.replace(R.id.fragment_timelog_container, nr).commit();
			
			 }
	    	if(date != null && timelogs.isEmpty() && tv == null){
				
				tv = new TextView(getActivity());
				
				tv.setText("No Time Logs have been found for date: " + date);
				tv.setPaddingRelative(20, 20, 20, 20);
				
				//View view = Mainview.findViewById(R.layout.company_rates_fragment);
				((LinearLayout)Mainview).addView(tv);
				
				//cfView.addView(tv);
				
	    	}
	    	//pd.hide();
	    }
	    /**gets the information from the child screen**/
	    @SuppressLint("NewApi")
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data){
	    	super.onActivityResult(requestCode, resultCode, data);
	    	switch(requestCode){
		    	case 1:{
		    		if(resultCode == Activity.RESULT_OK){
		    			date = data.getStringExtra("selectedDate");
		    			if(tv != null){
							tv.setVisibility(View.GONE);
							((LinearLayout)Mainview).removeView((View)tv.getParent());
						}
		    			else{

		    			}
		    		
		    		}
		    	}
		    	break;
	    	
	    	}
	    }


	    @Override
		public void onPause(){
	    	super.onPause();
	    }
	    @Override
		public void onDestroy(){
	    	super.onDestroy();
//	    	
//	    	if(sharedPref != null){
//		       	 if(sharedPrefFound){
//			 			/**Clear Timelog value from shared pref**/
//				    	SharedPreferences.Editor editor = sharedPref.edit();
//				    	editor.clear();
//						editor.commit();
//						sharedPrefFound = false;
//		       	 }
//	    	}
	    	//pd.show();
	    	//GetTimeLogData();
	    	//pd.hide();
	    }
}
