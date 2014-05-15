package com.tricellsoftware.timetrackertestapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.tricellsoftware.timetrackertestapp.R;
import com.tricellsoftware.timetrackertestapp.DTOs.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.database.ProfileTable;
import com.tricellsoftware.timetrackertestapp.helper.TimeHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CurrentTimeSheetLog extends Activity {
	
	private BusinessLogic logic;
	TextView txtStartDate;
	TextView txtEndDate;
	TextView totalHours;
	TextView txtCompany;
	TextView txtAmountPaid;
	
	String StartDate;
	String EndDate;
	
	String date;
	
	List<TimeLogDTO> timelogs = new ArrayList();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_timesheetlog);
		
		//action bar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("TimeSheet Log - Summary");
		
		Button timeLogsBttn = (Button) findViewById(R.id.timelogsbtn);
		Button excelBttn = (Button) findViewById(R.id.exportbtn);
		txtStartDate = (TextView)findViewById(R.id.txtStartDate);
		txtEndDate = (TextView)findViewById(R.id.txtEndDate2);
		totalHours = (TextView)findViewById(R.id.txtTotalHours);
		txtCompany = (TextView)findViewById(R.id.txtSCompany);
		txtAmountPaid = (TextView)findViewById(R.id.txtpaid);
		
		try{
	
			GetData();
		
		}
		catch(Exception e){
			Toast.makeText(this, "Error Ocurred" + e.toString(), Toast.LENGTH_LONG).show();
		}
		
		timeLogsBttn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), TimeLogListActivity.class);
				i.putExtra("StartDate", StartDate.substring(4));
				i.putExtra("EndDate", EndDate.substring(4));
				startActivity(i);
			}
			
		});
		excelBttn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), SummaryActivity.class);
				startActivity(i);
			}
			
		});

	}
	//create menu options for the action bar
	public boolean onCreateOptionsMenu(Menu menu) {
    		   MenuInflater inflateLayout = getMenuInflater();
    		   inflateLayout.inflate(R.menu.timesheet_menu, menu);
    		  return super.onCreateOptionsMenu(menu);
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		//Gets the position on the Item selected
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//_id = companies.get((int) info.id).getID();
	   switch (item.getItemId()) {
	   case R.id.action_edit:
			Intent i = new Intent(getApplicationContext(), CalendarViewActivity.class);
			i.putExtra("selectedDate", date);
			startActivityForResult(i, 1);
	   break;
	    	  
	   }
	   return super.onOptionsItemSelected(item);
	 }
	
	 /**gets the information from the child screen**/
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
    	switch(requestCode){
	    	case 1:{
	    		if(resultCode == Activity.RESULT_OK){
	    			date = data.getStringExtra("selectedDate");
	    		
	    		}
	    	}
	    	break;
    	
    	}
    }
    protected void onStart(){
    	super.onStart();
    }
    ///Refreshes the List View
    protected void onRestart(){
    	super.onRestart();
    	GetData();
    }
    protected void onResume(){
    	super.onResume();
    }

    protected void onPause(){
    	super.onPause();
    }
    private void GetData(){
    	
		//business logic
		logic = new BusinessLogic(this);
    	
		CompanyDTO Company = new CompanyDTO();
		Company = logic.getCompanyByDefault("1");
    	
		String[] Dates;
		if(date != null){
			Dates = TimeHelper.getStartDateAndEndDate(date);
		}
		else{
			Dates = TimeHelper.getStartDateAndEndDate(date);
		}
		StartDate = Dates[0];
		EndDate = Dates[1];
		
//		String[] StartDay = StartDate.split(" ");
//		String[] EndDay = EndDate.split(" ");
//		
//		txtStartDate.setText(StartDay[0] + " \n" + StartDay[1]);
//		txtEndDate.setText(EndDay[0] + " \n" + EndDay[1]);
		
		txtStartDate.setText(StartDate);
		txtEndDate.setText(EndDate);
		
		txtCompany.setText(Company.getName());
		//txtCompany.setTextSize(20);
		
		timelogs = logic.getAllTimeLogsByWeek(StartDate.substring(4), EndDate.substring(4)); //logic.getAllTimeLogsByWeek("03/03/2014", "03/09/2014");
		int TotalMinutes = 0;
		for(TimeLogDTO log: timelogs){
			int minutes;
			if(log.getMinutes() == null)
				minutes = 0;//Integer.parseInt(log.getMinutes())
			else{
				minutes =  Integer.parseInt(log.getMinutes());
			}
			
			TotalMinutes = TotalMinutes + minutes;
		}
		totalHours.setText(TimeHelper.displayHoursandMinutes(Integer.toString(TotalMinutes)));
		//Calculate amount paid
		String amountPaid = TimeHelper.calculatepay(TotalMinutes, (float)Company.getRate());
		txtAmountPaid.setText("$: "+amountPaid);
    }

}
