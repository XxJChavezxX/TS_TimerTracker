package com.tricellsoftware.timetrackertestapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;





import com.tricellsoftware.timetrackertestapp.DTOs.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.helper.FileCreatorHelper;
import com.tricellsoftware.timetrackertestapp.helper.SendEmailHelper;
import com.tricellsoftware.timetrackertestapp.helper.TimeHelper;

public class SummaryActivity extends Activity {
	
	private BusinessLogic logic;
	TextView txtStartDate;
	TextView txtEndDate;
	TextView totalHours;
	TextView txtCompany;
	TextView txtRate;
	TextView txtAmountPaid;
	
	String StartDate;
	String EndDate;
	
	String date;
	CompanyDTO Company = null;
	List<TimeLogDTO> timelogs = new ArrayList();
	
	//createfile classes
	FileCreatorHelper fc = null;
	SendEmailHelper se = null;
	
	ProgressDialog pd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary_layout);
		
		
//		pd = new ProgressDialog(this);
//		pd.show();
//		pd.setMessage("Loading..");
		//action bar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("TimeSheet Log - Summary");
		
		Button timeLogsBttn = (Button) findViewById(R.id.timelogsbtn);
		Button exportBttn = (Button) findViewById(R.id.exportbtn);
		txtStartDate = (TextView)findViewById(R.id.txtStartDate);
		txtEndDate = (TextView)findViewById(R.id.txtEndDate2);
		totalHours = (TextView)findViewById(R.id.txtTotalHours);
		txtCompany = (TextView)findViewById(R.id.txtSCompany);
		txtAmountPaid = (TextView)findViewById(R.id.txtpaid);
		txtRate = (TextView)findViewById(R.id.textSRate);
		try{
	
			GetData();
		
		}
		catch(Exception e){
			Toast.makeText(this, "Error Ocurred" + e.toString(), Toast.LENGTH_LONG).show();
		}
		//pd.hide();
		timeLogsBttn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), TimeLogMainActivity.class);
				i.putExtra("StartDate", StartDate.substring(4));
				i.putExtra("EndDate", EndDate.substring(4));
				startActivity(i);
			}
			
		});
		exportBttn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			ProfileDTO profile = logic.getUser(1); //get profile info
				fc = new FileCreatorHelper();
				se = new SendEmailHelper();
				String filePath = fc.CreateFile(getApplicationContext(), profile, Company, date);
				se.SendEmailWithExcelFile(SummaryActivity.this, profile.getEmail(), 
					"TimeSheet Report", "This document attached to the email is a detailed view of the time clocks", filePath);
			}
			
		});

	}
	//create menu options for the action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    		   MenuInflater inflateLayout = getMenuInflater();
    		   inflateLayout.inflate(R.menu.timesheet_menu, menu);
    		  return super.onCreateOptionsMenu(menu);
    }
	
	@Override
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
    @Override
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
    @Override
	protected void onStart(){
    	super.onStart();
    }
    ///Refreshes the List View
    @Override
	protected void onRestart(){
    	super.onRestart();
//    	pd.show();
    	GetData();
//    	pd.hide();
    }
    @Override
	protected void onResume(){
    	super.onResume();
    }

    @Override
	protected void onPause(){
    	super.onPause();
    }
    private void GetData(){
    	
		//business logic
		logic = new BusinessLogic(this);
    	
		Company = new CompanyDTO();
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
		
		txtStartDate.setText(StartDate);
		txtEndDate.setText(EndDate);
		if(Company.getName() == null){
			txtCompany.setText("N/A");
			Toast.makeText(this, "Please select a Company in order to calculate the Estimated Pay", Toast.LENGTH_LONG).show();
		}
		else
			txtCompany.setText(Company.getName());
		if(Company.getRate() <= 0){
			txtRate.setText("N/A");
		}
		else
			txtRate.setText("$: "+String.valueOf(Company.getRate()));
			
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
		if(amountPaid.equals("0")){
			txtAmountPaid.setText("N/A");
		}
		else
			txtAmountPaid.setText("$: " + amountPaid);
    }

}
