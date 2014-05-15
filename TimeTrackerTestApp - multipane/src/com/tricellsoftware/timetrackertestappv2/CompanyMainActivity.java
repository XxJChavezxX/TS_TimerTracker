package com.tricellsoftware.timetrackertestappv2;



import com.tricellsoftware.timetrackertestappv2.Fragments.Companies_Fragment;
import com.tricellsoftware.timetrackertestappv2.Fragments.Company_Fragment;
import com.tricellsoftware.timetrackertestapp.databasev2.CompanyTable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

//This is the main activity (holder) for both the Company Fragment and the Companies_Fragment 
public class CompanyMainActivity extends FragmentActivity implements Companies_Fragment.OnItemSelectedListener{
	
	String s = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_main_layout);
		
	}
	

	@Override
	public void onItemSelected(String _id) {
		// TODO Auto-generated method stub
		  Company_Fragment fm = (Company_Fragment) getSupportFragmentManager().findFragmentById(R.id.company_Fragment);
	        if (fm != null && fm.isInLayout()) {
	          fm.getDataByID(_id);
	        }else{
	        	Intent i = new Intent(getApplicationContext(),
	        	          CompanyActivity.class);
	        		  i.putExtra(CompanyTable.COLUMN_ID, _id);
	        	      startActivity(i);
	        }
	}
//	 /**gets the information from the child screen**/
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//    	super.onActivityResult(requestCode, resultCode, data);
//    	switch(requestCode){
//	    	case 1:{
//	    		if(resultCode == Activity.RESULT_OK){
//	    			s = data.getStringExtra("itemValue");
//	    			Detail_Fragment fm = (Detail_Fragment) getSupportFragmentManager().findFragmentById(R.id.detail_Fragment);
//	    	        if (fm != null && fm.isInLayout()) {
//	    	          fm.setText(s);
//	    	        }
//	    		}
//	    	}
//	    	break;
//    	
//    	}
//    }
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
