package com.tricellsoftware.timetrackertestappv2;



import com.tricellsoftware.timetrackertestappv2.Fragments.Companies_Fragment;
import com.tricellsoftware.timetrackertestappv2.Fragments.Company_Fragment;
import com.tricellsoftware.timetrackertestapp.databasev2.CompanyTable;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

//This is the main activity (holder) for both the Company Fragment and the Companies_Fragment 
public class CompanyMainActivity extends FragmentActivity implements Companies_Fragment.OnItemSelectedListener{
	
	String s = null;
	
	String _id = null;
	
	SharedPreferences sharedPref;
	boolean sharedPrefFound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_main_layout);
		
		//sharedPref = this.getSharedPreferences(getString(R.string.pref_data_key),this.MODE_PRIVATE);
		
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
	protected void onStart(){
    	super.onStart();
    }
    ///Refreshes the List View
    @Override
	protected void onRestart(){
    	super.onRestart();
    }
    @Override
	protected void onResume(){
    	super.onResume();
    }

    @Override
	protected void onPause(){
    	super.onPause();
    }

}
