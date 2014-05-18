package com.tricellsoftware.timetrackertestappv2.Fragments;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
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

import com.tricellsoftware.timetrackertestappv2.CompanyActivity;
import com.tricellsoftware.timetrackertestappv2.R;
import com.tricellsoftware.timetrackertestapp.DTOsv2.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.CompanyTable;

public class Companies_Fragment extends ListFragment {
	
	OnItemSelectedListener mListener;
	SimpleCursorAdapter mAdapter;
	ListView listView;
	//private SimpleCursorAdapter adapter;
	
	List<CompanyDTO> companies; 
	private BusinessLogic logic;
	
	ArrayAdapter<CompanyDTO> adapter;
	//action bar
	ActionBar actionBar;
	
	private String _id;
	private int id;
	private int position;
	
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
	
	SharedPreferences sharedPref;
	
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
		
	    View view = inflater.inflate(R.layout.company_rates_fragment,
	        container, false);
	    
	    //ctx = getActivity();
	    
	    
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE 
	    		&& (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
			largeScreen = true;
		}
	   
		Mainview = view;
	   
	     //init Business logic
		logic = new BusinessLogic(getActivity());
		//Button addNewBttn = (Button) findViewById(R.id.addnewbttn);
		
		//add action bar
		actionBar = getActivity().getActionBar();
		actionBar.setTitle("Companies");
		setHasOptionsMenu(true);
	     

	    return view;
	  }
	  
	  @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
		    //get list view
			registerForContextMenu(getListView());
			
//			if(pd != null){
//				pd = null;
//			}
//			pd = new ProgressDialog(getActivity());
//			pd.show();
//			pd.setMessage("Loading..");
			
			
//			
//			if(CompanyActivity.id > 0){
//				id = CompanyActivity.id;
//				 CompanyActivity.id  = 0;
//			}
		    
			/**check for shared preferences, 
			 * We passed the Company id from the Company Activity class and we retrieve by using the getSharedPreferences
			 * method. This was the only way to pass the id from an activity that does not have direct contact with this fragment.
			 * **/
			sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_data_key),getActivity().MODE_PRIVATE);
			if(sharedPref != null){
				if(sharedPref.contains(getString(R.string.company_id)) && land)
					id = sharedPref.getInt(getString(R.string.company_id), 0);
					sharedPref.edit().remove(getString(R.string.company_id)).clear().commit();
					
					//sharedPrefFound = true;
//					SharedPreferences.Editor editor = sharedPref.edit();
//					editor.remove(getString(R.string.pref_data_key));
//					sharedPref.getAll();
//					editor.clear();
//					editor.commit();
//					sharedPref.getAll();
			}


			
			//if id is or less 0 meaning no id was returned, display the no results fragment
			if(id <= 0){
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
			}
			//launch the Company Fragment 
			else{
				
				  newCF = new Company_Fragment(); // new company fragment
				  //set data to pass to the company fragment
	        	  Bundle args = new Bundle();
	        	  args.putInt(CompanyTable.COLUMN_ID,id);
	        	  newCF.setArguments(args);
	        	  FragmentTransaction transaction = getFragmentManager().beginTransaction();


		         //Begin fragment transaction to show the new fragment and replace the old one 
	        	 transaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
	        	 transaction.replace(R.id.fragment_container, newCF);
			     
	        	 transaction.commit();	 

			}
			id = 0;
			

			//pd.hide();
	  }
	  
		@SuppressLint("NewApi")
		public void GetCompaniesData(){
			
			 
			//gets all companies from the db and add them to the listl
			
			companies = logic.getAllCompanies();
			
			if(companies.size() <= 0){
				//text view is dynamically added to display the messag below
				tv = new TextView(ctx);
				
				tv.setText("No Companies/Projects have been added");
				tv.setPaddingRelative(20, 20, 20, 20);
				
				//View view = Mainview.findViewById(R.layout.company_rates_fragment);
				((RelativeLayout)Mainview).addView(tv);
				
				//cfView.addView(tv);
			}
			else{
				
				//Mainview = getListView();
				 
				if(tv != null){
					//tv.setVisibility(View.INVISIBLE);
					tv.setVisibility(View.GONE);
					((RelativeLayout)Mainview).removeView((View)tv);
					
				}
				
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
		
		  @Override
		  public void onListItemClick(ListView l, View v, int position, long id){
			  super.onListItemClick(l, v, position, id);
			  if(sharedPref != null){
				  sharedPref.edit().remove(getString(R.string.pref_data_key)).clear().commit();
			  }
			   //get id of the selected item
			   _id = String.valueOf(companies.get(position).getID());
			   //i.putExtra(CompanyTable.COLUMN_ID, _id);

				//if screen is large (7 inches)
			    if (land){
			    		//&& (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			    	 mListener.onItemSelected(String.valueOf(_id));
			    	 
			    	// Create fragment and give it an argument specifying the item it should show
		        	  newCF = new Company_Fragment(); // new company fragment
		        	  Bundle args = new Bundle();
		        	  args.putInt(CompanyTable.COLUMN_ID, Integer.parseInt(_id));
		        	  newCF.setArguments(args);
		        	  FragmentTransaction transaction = getFragmentManager().beginTransaction();


			         //Begin fragment transaction to show the new fragment and replace the old one 
		        	 transaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
		        	 transaction.replace(R.id.fragment_container, newCF);
				     
		        	 transaction.commit();
			    }
			    else{
			    	Intent i = new Intent(getActivity(), CompanyActivity.class);
				    //mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
				    //Uri CompanyUri = Uri.parse(TimeTrackerContentProvider.Content_URI + "/" + id);
			    	  
				     i.putExtra(CompanyTable.COLUMN_ID, _id);

				    startActivity(i);
			    }
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
			
	    	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && newCF == null){
				
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				//check of the framelayout is present on the Activity associated with this ListFragment
				if(getView().findViewById(R.id.fragment_container) != null){
					
				}// However, if we're being restored from a previous state,
	            // then we don't need to do anything and should return or else
	            // we could end up with overlapping fragments.
	            //if (savedInstanceState != null) {
	          //      return;
	          //  }
	            //Creates Company Fragment to be held in the Activity layout using the FrameLayout
	            NoResults_Fragment nr = new NoResults_Fragment();
	            // In case this activity was started with special instructions from an
	            // Intent, pass the Intent's extras to the fragment as arguments
	            //cf.setArguments(getActivity().getIntent().getExtras());
	
	            //Add the fragment to the 'fragment_container' FrameLayout
	            ft.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
	            ft.replace(R.id.fragment_container, nr).commit();
			
			 }
	    	//pd.hide();
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
			_id = String.valueOf(companies.get((int) info.id).getID());
		   switch (item.getItemId()) {
		   case R.id.delete:
			   logic.deleteCompanyById(_id);
			   Toast.makeText(getActivity(), "Record has been deleted successfully", Toast.LENGTH_LONG).show();
			   GetCompaniesData();
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
		    	  Toast.makeText(getActivity(), "Settings was selected", Toast.LENGTH_LONG).show();
		   break;
		   case R.id.AddNew:
			   
				 // Need to check if Activity has been switched to landscape mode
			    // If yes, finished and go back to the start Activity
			    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			    	//mListener.onItemSelected(String.valueOf(0));
			    	
			    	//creates an empty fragment to add a new company
		        	  Company_Fragment CF = new Company_Fragment(); // new company fragment
		        	  FragmentTransaction transaction = getFragmentManager().beginTransaction();


			         //Begin fragment transaction to show the new fragment and replace the old one 
		        	 transaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
		        	 transaction.replace(R.id.fragment_container, CF);
				     
		        	 transaction.commit();
			    	
			    	
			      //return;
			    }//checking for the Orientation must be before defining the content view
			    else{
			    	Intent CompanyScreen = new Intent(getActivity(), CompanyActivity.class);
			    	startActivity(CompanyScreen);
			   
			    }
			   
			   //search text box on the action bar
//		   case R.id.action_search:
//				LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			   View v = inflator.inflate(R.layout.search_custom, null);
//		   
//			   //Toast.makeText(this, "Search was selected", Toast.LENGTH_LONG).show();
//			  // actionBar.getThemedContext();
//			   //actionBar.
//			   actionBar.setCustomView(v);
//			   
//			  // actionBar.hide();
	//	
//				/** Get the edit text from the action view */
//			   TextView txtSearch = (TextView) v.findViewById(R.id.editText1);
//			   //txtSearch.setBackgroundColor(Color.WHITE);
//			   txtSearch.setTextColor(Color.WHITE);
//		   break;
		    	  
		   }
		   return super.onOptionsItemSelected(item);
		 }

	    @Override
		public void onDestroy(){
	    	super.onDestroy();
	    	
		
	    	
	    	//Clear the data from the shared pref file
	    	if(sharedPref != null){
	    		
		       	 if(sharedPrefFound){
		       		//sharedPref.edit().remove(getString(R.string.pref_data_key)).clear().commit();
//			 			/**Clear company value from shared pref**/
//				    	SharedPreferences.Editor editor = sharedPref.edit();
//				    	editor.remove(getString(R.string.pref_data_key));
//				    	sharedPref.getAll();
//				    	editor.clear();
//						editor.commit();
//						sharedPrefFound = false;
		       	 }
	    	}
//	    	//pd.show();
	    	//GetTimeLogData();
	    	//pd.hide();
	    }

}
