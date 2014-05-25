package com.tricellsoftware.timetrackertestappv2.Fragments;

import com.tricellsoftware.timetrackertestappv2.CompanyMainActivity;
import com.tricellsoftware.timetrackertestappv2.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CompanyMain_Fragment extends Fragment implements Companies_Fragment.OnItemSelectedListener{
	
	String s = null;
	
	String _id = null;
	
	SharedPreferences sharedPref;
	boolean sharedPrefFound = false;
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static CompanyMain_Fragment newInstance(int sectionNumber) {
		CompanyMain_Fragment fragment = new CompanyMain_Fragment();
		//Companies_Fragment fg = new Companies_Fragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public CompanyMain_Fragment() {
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.company_main_layout);
		
//		if (savedInstanceState != null) {
//            return;
//      }

		//sharedPref = this.getSharedPreferences(getString(R.string.pref_data_key),this.MODE_PRIVATE);
	    //Intent i = new Intent(this, CompanyActivity.class);
	    //i.putExtra("companyID", string);
	    //startActivityForResult(getApplicationContext(), Activity.RESULT_OK);
		
//		Bundle extras = getIntent().getExtras();
//		
//		if(extras != null){
//			String id = extras.getString("companyID");	
//			System.out.println(id);
//		}
	}
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
		if(savedInstanceState != null)
			return;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.company_main_layout, container,
				false);
//		TextView textView = (TextView) rootView
//				.findViewById(R.id.section_label);
//		textView.setText(Integer.toString(getArguments().getInt(
//				ARG_SECTION_NUMBER)));
		return rootView;
	}

	@Override
	public void onItemSelected(String _id) {
		// TODO Auto-generated method stub
//		  Company_Fragment fm = (Company_Fragment) getSupportFragmentManager().findFragmentById(R.id.company_Fragment);
//	        if (fm != null && fm.isInLayout()) {
//	          fm.getDataByID(_id);
//	        }else{
//	        	Intent i = new Intent(getApplicationContext(),
//	        	          CompanyActivity.class);
//	        		  i.putExtra(CompanyTable.COLUMN_ID, _id);
//	        	      startActivity(i);
//	        }
	}
//	@Override
//	public void onBackPressed(){
//    	if(sharedPref != null){
//	       	 if(sharedPrefFound){
//		 			/**Clear company value from shared pref**/
//			    	SharedPreferences.Editor editor = sharedPref.edit();
//			    	editor.clear();
//					editor.commit();
//					sharedPrefFound = false;
//	       	 }
//    	}
//		super.onBackPressed();
//	}
    @Override
	public void onStart(){
    	super.onStart();
    }
    ///Refreshes the List View
    @Override
	public void onResume(){
    	super.onResume();
    }
    /**gets the information from the child screen**/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
    	switch(requestCode){
	    	case 1:{
	    		if(resultCode == Activity.RESULT_OK){
	    			_id = data.getStringExtra("companyID");
	    		
	    		}
	    	}
	    	break;
    	
    	}
    }

    @Override
	public void onPause(){
    	super.onPause();
    }
}
