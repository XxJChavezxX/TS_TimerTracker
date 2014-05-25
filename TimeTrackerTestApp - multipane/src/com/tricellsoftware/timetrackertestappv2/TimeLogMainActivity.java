package com.tricellsoftware.timetrackertestappv2;

import com.tricellsoftware.timetrackertestappv2.Fragments.TimeLogDetails_Fragment;
import com.tricellsoftware.timetrackertestappv2.Fragments.TimeLogs_Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

//This is the main activity(holder) for both TimelogsDetails_Fragment and the TimelogList_Fragment
public class TimeLogMainActivity extends FragmentActivity implements TimeLogs_Fragment.OnLogSelectedListener{
	
	String s = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timelogs_main_layout);
		
	}
	

	@Override
	public void onLogSelected(String _id) {
		 //TODO Auto-generated method stub
		  TimeLogDetails_Fragment fm = (TimeLogDetails_Fragment) getFragmentManager().findFragmentById(R.id.timelogDetails_Fragment);
	        if (fm != null && fm.isInLayout()) {
	         // fm.getDataByID(_id);
	        }else{
	        	Intent i = new Intent(getApplicationContext(),
	        	          EditTimeActivity.class);
	        		  //i.putExtra(CompanyTable.COLUMN_ID, _id);
	        	      startActivity(i);
	        }
	}

}
