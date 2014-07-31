package com.tricellsoftware.timetrackertestapp.Fragments;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.app.ActionBar;
import android.app.Activity;
import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.tricellsoftware.timetrackertestapp.CompanyActivity;
import com.tricellsoftware.timetrackertestapp.R;
import com.tricellsoftware.timetrackertestapp.DTOs.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.database.CompanyTable;

public class Companies_Fragment extends ListFragment {
	
	OnItemSelectedListener mListener;
	SimpleCursorAdapter mAdapter;
	ListView listView;
	//private SimpleCursorAdapter adapter;
	
	List<CompanyDTO> companies; 
	private BusinessLogic logic;
	
	private static ArrayAdapter<CompanyDTO> adapter;
	//action bar
	ActionBar actionBar;
	
	private int _id = 0;
	//private int id = 0;
	//private int position;
	//private static int compid = 0;
	
	ProgressDialog pd = null;
	
	Context ctx = null;
	
	//Fragment Variables to modify fragments behavior
	FragmentManager fm = null; 
	Fragment fragment = null;
	
	NoResults_Fragment NR = null;
	
	Company_Fragment newCF;
	
	ListView lv;
	
	View Mainview; //view to add other widgets

	TextView tv; //dynamically added textview
	
	boolean largeScreen;
	
	boolean land; //landscape
	boolean sharedPrefFound = false;
	
	Bundle savedInstanceState;
	
	NoResults_Fragment nr;
	
	SharedPreferences sharedPref;
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Companies_Fragment newInstance(int sectionNumber) {
		Companies_Fragment fragment = new Companies_Fragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	 public static void LoadCompaniesData() {   
		 //adapter.notifyDataSetChanged();
     }

	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		
		  

			
		  
//		//check for data passed by an Activity, this case Company Activity when landscape
//		Bundle args1 = getArguments();
//		if(args1 != null){
//			id = Integer.parseInt(getArguments().getString(CompanyTable.COLUMN_ID));
//			
//		}
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			land = true;
		}
		else
			land = false;
			//land = false;
		
	    View view = inflater.inflate(R.layout.company_rates_fragment,
	        container, false);
	    
	    //ctx = getActivity();
	    
	    
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			land = true;
			
		}
		if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
			largeScreen = true;
		}
	   
		Mainview = view;
	   
	     //init Business logic
		logic = new BusinessLogic(getActivity());
		//Button addNewBttn = (Button) findViewById(R.id.addnewbttn);
		
		//add action bar
		actionBar = getActivity().getActionBar();
		
		
		
		//actionBar.setTitle("Companies");
		setHasOptionsMenu(true);
	     
		//startActivityForResult();

	    return view;
	  }
	  
	  @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
		    
		    _id = 0;
		    //this.savedInstanceState = savedInstanceState;
		    
		    //get list view
			registerForContextMenu(getListView());
		   // lv = (ListView) getView().findViewById(R.id.companies_listview);
//			if(pd != null){
//				pd = null;
//			}
//			pd = new ProgressDialog(getActivity());
//			pd.show();
//			pd.setMessage("Loading..");
			
			tv = (TextView) getView().findViewById(R.id.nofoundtv);
//			
//			if(CompanyActivity.id > 0){
//				id = CompanyActivity.id;
//				 CompanyActivity.id  = 0;
//			}
		    
			/**check for shared preferences, 
			 * We passed the Company id from the Company Activity class and we retrieve by using the getSharedPreferences
			 * method. This was the only way to pass the id from an activity that does not have direct contact with this fragment.
			 * **/
			//Important: currently the shared pref is not clearing up
			sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_data_key), 0);
			if(sharedPref != null){
				if(sharedPref.contains(getString(R.string.company_id)) && land == false)
					_id = sharedPref.getInt(getString(R.string.company_id), 0);
				    sharedPref.getAll();
					sharedPref.edit().remove(getString(R.string.pref_data_key)).clear().commit();
					//sharedPref.edit().remove(getString(R.string.pref_data_key)).clear().commit();
					
					//sharedPrefFound = true;
//					SharedPreferences.Editor editor = sharedPref.edit();
//					editor.remove(getString(R.string.pref_data_key));
//					sharedPref.getAll();
//					editor.clear();
//					editor.commit();
//					sharedPref.getAll();
			}
			//if activity if in port them launch the current timelog in the EditTimeActivity layout



			
			//if id is or less 0 meaning no id was returned, display the no results fragment
			if(_id <= 0){
				//Get Fragment Container if it's present on the Activity
				 if (land){
				
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					//check of the framelayout is present on the Activity associated with this ListFragment
					if(getView().findViewById(R.id.fragment_container) == null){
						//return;
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
		            ft.add(R.id.fragment_container, nr).commit();
				
				 }
				 else{
					 
				 }
			}
			//launch the Company Fragment 
			else{
//	       	 	if(land == false && _id > 0){
////	       	 		
//			  	    Intent i = new Intent(getActivity(), CompanyActivity.class);
//				    //mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
//				    //Uri CompanyUri = Uri.parse(TimeTrackerContentProvider.Content_URI + "/" + id);
//			    	  
//				     i.putExtra(CompanyTable.COLUMN_ID, _id);
//				     //_id = 0;
//				   //  compid = 0;
//				    startActivity(i);
//				    
//				    //_id = 0;
//				    
//				    
//	       	 	}
	       	 	//else{
				
//				  newCF = new Company_Fragment(); // new company fragment
//				  //set data to pass to the company fragment
//	        	  Bundle args = new Bundle();
//	        	  args.putInt(CompanyTable.COLUMN_ID,_id);
//	        	  newCF.setArguments(args);
//	        	  FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//
//		         //Begin fragment transaction to show the new fragment and replace the old one 
//	        	 transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
//	        	 transaction.replace(R.id.fragment_container, newCF);
//			     
//	        	 transaction.commit();
	        	 
	        	 //_id = 0;
	       	 	//}

			}
			//id = 0;


			//pd.hide();
	  }
	  
		@SuppressLint("NewApi")
		public void GetCompaniesData(){
			
			 
			//gets all companies from the db and add them to the listl
			
			companies = logic.getAllCompanies();
			
			if(companies.isEmpty()){
				//text view is dynamically added to display the messag below
				//tv = new TextView(getActivity());
				//tv = (TextView) getView().findViewById(R.id.nofoundtv);
				tv.setText("Companies/Projects have not been added");
				tv.setPaddingRelative(20, 20, 20, 20);
				
				tv.setVisibility(View.VISIBLE);
				//View view = Mainview.findViewById(R.layout.company_rates_fragment);
				//((RelativeLayout)Mainview).addView(tv);
				
				
				
				//cfView.addView(tv);
			}
			else{
				
				tv.setVisibility(View.GONE);
				
			}
			
			if (largeScreen) {
				
	    			//lv = (ListView) getView().findViewById(R.layout.large_company_custom_list);
	    			//if screen is 7 inch use a custom layout to display the list of companies
	    			adapter = new ArrayAdapter<CompanyDTO>(getActivity(), R.layout.large_company_custom_list, companies);
				    setListAdapter(adapter);
				    
				    adapter.notifyDataSetChanged();
		    }
			else{
				
				//use the default list provided by android
				adapter = new ArrayAdapter<CompanyDTO>(getActivity(), android.R.layout.simple_list_item_1, companies);
			    setListAdapter(adapter);
			    
			    adapter.notifyDataSetChanged();
			    
			}
			
			
		    
		}
		//public static void refillData(){
		public static void refreshList(){
			  adapter.notifyDataSetChanged();
		}
		
		  @Override
		  public void onListItemClick(ListView l, View v, int position, long id){
			  super.onListItemClick(l, v, position, id);

			   //get id of the selected item
			  // _id = String.valueOf();
			   //id = companies.get(position).getID();
			   _id = companies.get(position).getID();
			  // compid = (int) id;
			   

			   //i.putExtra(CompanyTable.COLUMN_ID, _id);

				//if screen is large (7 inches)
			    if (land){
			    	
			    	//save company the user was seeing
				    sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_data_key), 0);
				    SharedPreferences.Editor editor = sharedPref.edit();
			    	editor.putInt(getString(R.string.company_id), _id).commit();
			    		//&& (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			    	 //mListener.onItemSelected(String.valueOf(id));
			    	 
			    	// Create fragment and give it an argument specifying the item it should show
		        	  newCF = new Company_Fragment(); // new company fragment
		        	  Bundle args = new Bundle();
		        	  args.putInt(CompanyTable.COLUMN_ID, _id);
		        	  newCF.setArguments(args);
		        	  FragmentTransaction transaction = getFragmentManager().beginTransaction();


			         //Begin fragment transaction to show the new fragment and replace the old one 
		        	 transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
		        	 transaction.replace(R.id.fragment_container, newCF);
				     
		        	 transaction.commit();
			    }
			    else{

			    	
			    	Intent i = new Intent(getActivity(), CompanyActivity.class);
				    //mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
				    //Uri CompanyUri = Uri.parse(TimeTrackerContentProvider.Content_URI + "/" + id);
			    	  
				     i.putExtra(CompanyTable.COLUMN_ID, _id);

				    startActivity(i);
				    //trashes the old id value
				   
			    }
			    //_id = 0;
	          //Toast.makeText(getActivity(), "item: "+ _id, Toast.LENGTH_LONG).show();

		  }
		
	    // Container Activity must implement this interface
	    public interface OnItemSelectedListener {
	        public void onItemSelected(String item);
	    }

	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            mListener = (OnItemSelectedListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement OnItemSelectedListener");
	        }
	    }
	    
	    @Override
		public void onStart(){
	    	super.onStart();
	    	
	    }
	    ///Refreshes the List View
	    @Override
		public void onResume(){
	    	super.onResume();
	    	//pd.show();
	    	GetCompaniesData();
			
	    	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE  && nr == null){// && newCF == null){
				
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				//check of the framelayout is present on the Activity associated with this ListFragment
				if(getView().findViewById(R.id.fragment_container) != null){
					
				}// However, if we're being restored from a previous state,
	            // then we don't need to do anything and should return or else
	            // we could end up with overlapping fragments.
//	            if (savedInstanceState != null) {
//	                return;
//	            }
	            //Creates Company Fragment to be held in the Activity layout using the FrameLayout
	            nr = new NoResults_Fragment();
	            // In case this activity was started with special instructions from an
	            // Intent, pass the Intent's extras to the fragment as arguments
	            //cf.setArguments(getActivity().getIntent().getExtras());
	
	            //Add the fragment to the 'fragment_container' FrameLayout
	            //ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
	            ft.replace(R.id.fragment_container, nr).commit();
			
			 }
	    	//pd.hide();
	    }
	    /**gets the information from the child screen**/
	    @Override
		public void onActivityResult(int requestCode, int resultCode, Intent data){
	    	super.onActivityResult(requestCode, resultCode, data);
	    	switch(requestCode){
		    	case 1:{
		    		if(resultCode == Activity.RESULT_OK){
		    			_id = Integer.parseInt(data.getStringExtra("companyID"));
		    		
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
		public void onCreateContextMenu(ContextMenu menu, View v,
	    		   ContextMenuInfo menuInfo) {
	    		   MenuInflater inflateLayout = getActivity().getMenuInflater();
	    		   inflateLayout.inflate(R.menu.context_menu, menu);
	    		   super.onCreateContextMenu(menu, v, menuInfo);
	    		   
	    } 
		 @Override
		public boolean onContextItemSelected(MenuItem item) {
			//Gets the position on the Item selected
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			_id = companies.get((int) info.id).getID();
		   switch (item.getItemId()) {
		   case R.id.delete:
			   try{

				   logic.deleteCompanyById(String.valueOf(_id));
				   if(logic.Error != null){
					   if(logic.Error.contains("key constraint")){
						   Toast.makeText(getActivity(), "This record is being used somewhere else and cannot be deleted", Toast.LENGTH_LONG).show();
					   }
					   else
						   Toast.makeText(getActivity(), "Unexpected Error Occurred", Toast.LENGTH_LONG).show();
				   }
				   else{
					   Toast.makeText(getActivity(), "Record has been deleted successfully", Toast.LENGTH_LONG).show();
					  
				   }
				   GetCompaniesData();
			    	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){// && newCF == null){
						
						FragmentTransaction ft = getFragmentManager().beginTransaction();
						//check of the framelayout is present on the Activity associated with this ListFragment
						if(getView().findViewById(R.id.fragment_container) != null){
							
						}// However, if we're being restored from a previous state,
			            // then we don't need to do anything and should return or else
			            // we could end up with overlapping fragments.
	//		            if (savedInstanceState != null) {
	//		                return;
	//		            }
			            //Creates Company Fragment to be held in the Activity layout using the FrameLayout
			            nr = new NoResults_Fragment();
			            // In case this activity was started with special instructions from an
			            // Intent, pass the Intent's extras to the fragment as arguments
			            //cf.setArguments(getActivity().getIntent().getExtras());
			
			            //Add the fragment to the 'fragment_container' FrameLayout
			            //ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
			            ft.replace(R.id.fragment_container, nr).commit();
					
					 }
			   }
			   catch(Exception e){
					   Toast.makeText(getActivity(), "Unexpected Error Occurred", Toast.LENGTH_LONG).show();
			   }
		    break;
		      case R.id.view:
		    	  
		  	    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
		  	    	
		  	    	mListener.onItemSelected(String.valueOf(_id));
		  	    }
		  	    else{
			  	    Intent i = new Intent(getActivity(), CompanyActivity.class);
				    //mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
				    //Uri CompanyUri = Uri.parse(TimeTrackerContentProvider.Content_URI + "/" + id);
			    	  
				     i.putExtra(CompanyTable.COLUMN_ID, _id);

				    startActivity(i);
		  	    }
	
		   }
		   return super.onContextItemSelected(item);
		 } 
		//create menu options for the action bar
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    		  MenuInflater inflateLayout = getActivity().getMenuInflater();
	    		  inflateLayout.inflate(R.menu.companies_menu, menu);
	    		  super.onCreateOptionsMenu(menu, inflateLayout);
	    } 
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			//Gets the position on the Item selected
			//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			//_id = companies.get((int) info.id).getID();
		   switch (item.getItemId()) {
		   case R.id.action_settings:
		    	 // Toast.makeText(getActivity(), "Settings was selected", Toast.LENGTH_LONG).show();
		   break;
		   case R.id.AddNew:
			   //launch the Company activity to add a new company
   		    	Intent CompanyScreen = new Intent(getActivity(), CompanyActivity.class);
   		    	startActivity(CompanyScreen);
			       	  
		   }
		   return super.onOptionsItemSelected(item);
		 }


	    @Override
		public void onDestroy(){
	    	super.onDestroy();
	    	
		
	    	
	    	//Clear the data from the shared pref file
	    //	if(sharedPref != null){
	    		//
		       ///if(sharedPrefFound){
		       		//sharedPref.edit().remove(getString(R.string.pref_data_key)).clear().commit();
//			 			/**Clear company value from shared pref**/
//				    	SharedPreferences.Editor editor = sharedPref.edit();
//				    	editor.remove(getString(R.string.pref_data_key));
//				    	sharedPref.getAll();
//				    	editor.clear();
//						editor.commit();
//						sharedPrefFound = false;
	//	       	 }
//	    	}
//	    	//pd.show();
	    	//GetTimeLogData();
	    	//pd.hide();
	    }

}
