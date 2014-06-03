package com.tricellsoftware.timetrackertestappv2.Fragments;



import java.util.ArrayList;
import java.util.List;

import com.tricellsoftware.timetrackertestapp.DTOsv2.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.DTOsv2.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.DTOsv2.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.helperv2.FileCreatorHelper;
import com.tricellsoftware.timetrackertestapp.helperv2.SendEmailHelper;
import com.tricellsoftware.timetrackertestapp.helperv2.TimeHelper;
import com.tricellsoftware.timetrackertestappv2.CalendarViewActivity;
import com.tricellsoftware.timetrackertestappv2.R;
import com.tricellsoftware.timetrackertestappv2.SummaryActivity;
import com.tricellsoftware.timetrackertestappv2.TimeLogMainActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Summary_Fragment extends Fragment {
	
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
	
	private boolean infoChanged;
	
	View MainView;
	
	ProgressDialog pd = null;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Summary_Fragment newInstance(int sectionNumber) {
		Summary_Fragment fragment = new Summary_Fragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public Summary_Fragment() {
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		ActionBar actionBar = getActivity().getActionBar();
		//actionBar.setTitle("TimeSheet Log - Summary");
		setHasOptionsMenu(true);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.summary_layout, container,
				false);
//		TextView textView = (TextView) rootView
//				.findViewById(R.id.section_label);
//		textView.setText(Integer.toString(getArguments().getInt(
//				ARG_SECTION_NUMBER)));
		
		MainView = rootView;
		return rootView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
		

	    Button timeLogsBttn = (Button)getView().findViewById(R.id.timelogsbtn);
		Button exportBttn = (Button)getView().findViewById(R.id.exportbtn);
		txtStartDate = (TextView)getView().findViewById(R.id.txtStartDate);
		txtEndDate = (TextView)getView().findViewById(R.id.txtEndDate2);
		totalHours = (TextView)getView().findViewById(R.id.txtTotalHours);
		txtCompany = (TextView)getView().findViewById(R.id.txtSCompany);
		txtAmountPaid = (TextView)getView().findViewById(R.id.txtpaid);
		txtRate = (TextView)getView().findViewById(R.id.textSRate);
		try{
	
			GetData();
		
		}
		catch(Exception e){
			Toast.makeText(getActivity(), "Error Ocurred" + e.toString(), Toast.LENGTH_LONG).show();
		}
		//pd.hide();
		timeLogsBttn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), TimeLogMainActivity.class);
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
				String filePath = fc.CreateFile(getActivity(), profile, Company, date);
				se.SendEmailWithExcelFile(getActivity(), profile.getEmail(), 
					"TimeSheet Report", "This document attached to the email is a detailed view of the time clocks", filePath);
			}
			
		});

	}
	//create menu options for the action bar
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    		  MenuInflater inflateLayout = getActivity().getMenuInflater();
    		  inflateLayout.inflate(R.menu.timesheet_menu, menu);
    		  super.onCreateOptionsMenu(menu, inflateLayout);
    } 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Gets the position on the Item selected
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//_id = companies.get((int) info.id).getID();
	   switch (item.getItemId()) {
	   case R.id.action_edit:
			Intent i = new Intent(getActivity(), CalendarViewActivity.class);
			i.putExtra("selectedDate", date);
			startActivityForResult(i, 1);
	   break;
	    	  
	   }
	   return super.onOptionsItemSelected(item);
	 }
	
	 /**gets the information from the child screen**/
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
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
	public void onStart(){
    	super.onStart();
    }
    ///Refreshes the List View
    @Override
	public void onResume(){
    	super.onResume();
    	if(MainView != null){
    		GetData();
    	}
    	
    }

    @Override
	public void onPause(){
    	super.onPause();
    }
    private void GetData(){
    	
		//business logic
		logic = new BusinessLogic(getActivity());
    	
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
			Toast.makeText(getActivity(), "Please select a Company in order to calculate the Estimated Pay", Toast.LENGTH_LONG).show();
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
