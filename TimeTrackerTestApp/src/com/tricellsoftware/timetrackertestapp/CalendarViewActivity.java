package com.tricellsoftware.timetrackertestapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tricellsoftware.timetrackertestapp.R;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.database.CompanyTable;
import com.tricellsoftware.timetrackertestapp.helper.TimeHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.NumberPicker;
import android.widget.TextView;

public class CalendarViewActivity extends Activity{
	
	long miliseconds;
	String date;
	
	//action bar
	private ActionBar actionBar;
	
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
			public void onClick(View view){
				
				//dialog.cancel();
				Intent i = new Intent(getApplicationContext(), TimeLogListActivity.class);
				i.putExtra("selectedDate", date);
				setResult(Activity.RESULT_OK, i); //pass information to the previous activity
				finish();
				//startActivity(i);
				//GetTimeLogData();
			}
		});
	}
	
	
}
