package com.tricellsoftware.timetrackertestappv2;

import com.tricellsoftware.timetrackertestapp.DTOsv2.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.ProfileTable;
import com.tricellsoftware.timetrackertestapp.helperv2.TimeHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private BusinessLogic logic;
	private ProfileDTO profile;
	
	TextView Name;
	
	private int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		//action bar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Home");
		
		//checks if an id has been passed thru an activity
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			id = extras.getInt(ProfileTable.COLUMN_ID);
			if(id <= 0){
				id = 1;
			}
		}
		else{
			id = 1;
		}
		//business logic
		logic = new BusinessLogic(this);
		//get user by id 1
		profile = logic.getUser(id);
		Name = (TextView)findViewById(R.id.welcomelbl);
		if(id > 0){
			Name.setText("Welcome " + profile.getFirstName());
		}
		
		Button clockBttn = (Button) findViewById(R.id.clocksbttn);
		Button crBttn = (Button) findViewById(R.id.hourlyratesbttn);
		Button logsBttn = (Button) findViewById(R.id.timesheetbttn);
		
		TextView Datetv = (TextView) findViewById(R.id.tvDate);
		//set the current date
//		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//		Date today = Calendar.getInstance().getTime();
		String reportDate = TimeHelper.getDate();
		Datetv.setText(reportDate);
		
		
		//Listening to the button event
		clockBttn.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View view){
				//Starting a new intent
				Intent ClocksScreen = new Intent(getApplicationContext(), ClocksActivity.class);
				
				startActivity(ClocksScreen);
			}
		});
		//Listening to the button event
		crBttn.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View view){
				//Starting a new intent
				Intent CompanyRatesScreen = new Intent(getApplicationContext(), CompanyMainActivity.class);
				startActivity(CompanyRatesScreen);
			}
		});
		
		logsBttn.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View view){
				//Starting a new intent
				Intent i = new Intent(getApplicationContext(), SummaryActivity.class);
				startActivity(i);
			}
		});
		
	}
   @Override
protected void onStart(){
    	super.onStart();
    }
    ///Refreshes the List View
    @Override
	protected void onRestart(){
    	super.onRestart();
		//get user by id 1
		profile = logic.getUser(id);
		Name = (TextView)findViewById(R.id.welcomelbl);
		if(id > 0){
			Name.setText("Welcome " + profile.getFirstName());
		}
    }

    @Override
	protected void onResume(){
    	super.onResume();
    }

    @Override
	protected void onPause(){
    	super.onPause();
    }
	//create menu options for the action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    		   MenuInflater inflateLayout = getMenuInflater();
    		   inflateLayout.inflate(R.menu.main, menu);
    		  return super.onCreateOptionsMenu(menu);
    } 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Gets the position on the Item selected
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//_id = companies.get((int) info.id).getID();
	   switch (item.getItemId()) {
//	   case R.id.Home:
//		   Toast.makeText(this, "Home was selected", Toast.LENGTH_LONG).show();
//	   break;
//	   case R.id.Companies:
//	    	  Toast.makeText(this, "Companies was selected", Toast.LENGTH_LONG).show();
//	   break;
//	   case R.id.Clocks:
//	    	  Toast.makeText(this, "Clocks was selected", Toast.LENGTH_LONG).show();
//	   break;
//	   case R.id.TimeLog:
//	    	  Toast.makeText(this, "Time Logs was selected", Toast.LENGTH_LONG).show();
//	   break;
	   case R.id.action_settings:
		   Intent ProfileScreen = new Intent(getApplicationContext(), ProfileActivity.class);
		   ProfileScreen.putExtra(ProfileTable.COLUMN_ID, profile.getID());
			startActivity(ProfileScreen);
	    	  //Toast.makeText(this, "Settings was selected", Toast.LENGTH_LONG).show();
	   break;
	   case R.id.AddNew:
	    	  Toast.makeText(this, "Add new was selected", Toast.LENGTH_LONG).show();
	   break;
	    	  
	   }
	   return super.onOptionsItemSelected(item);
	 } 
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
