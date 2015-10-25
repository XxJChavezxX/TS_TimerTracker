package com.tricellsoftware.timetrackertestapp;

import com.tricellsoftware.timetrackertestapp.R;
import com.tricellsoftware.timetrackertestapp.helper.TimeHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class CalendarViewActivity extends Activity{
	
	long miliseconds;
	String date;

	
	boolean land; 
	//action bar
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view);
		
		
		//action bar
		actionBar = getActionBar();
		actionBar.setTitle("Filter By Date");
		actionBar.setDisplayShowCustomEnabled(true);
		
		
		final CalendarView calendar = (CalendarView) findViewById(R.id.calendar1);
		Button SaveBttn = (Button) findViewById(R.id.checkbttn);
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			date = extras.getString("selectedDate");
			if(date != null)
				miliseconds = TimeHelper.getLongFromDate(date);
		}
		
		if(miliseconds > 0){
			
			calendar.setDate(miliseconds);
		}
		
		calendar.setOnDateChangeListener( new OnDateChangeListener(){

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				 
				// TODO Auto-generated method stub
					miliseconds = calendar.getDate();
					date = TimeHelper.getDateFromLong(miliseconds);
			}
			
		});
		//Listening to the button event
		SaveBttn.setOnClickListener(new View.OnClickListener(){				
			@Override
			public void onClick(View view){
				
				//dialog.cancel();
				Intent i = new Intent();
				i.putExtra("selectedDate", date);
				setResult(Activity.RESULT_OK, i); //pass information to the previous activity
				
//				/** Share date with other activities or fragments by using sharedPref method
//				 * **/
//	    	  SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.pref_data_key), 0);
//	    	  SharedPreferences.Editor editor = sharedPref.edit();
//	    	  editor.putString(getString(R.string.selected_date), date);
//	    	  editor.commit();
				
		      finish();
				//startActivity(i);
				//GetTimeLogData();
			}
		});
	}
	
	
}
