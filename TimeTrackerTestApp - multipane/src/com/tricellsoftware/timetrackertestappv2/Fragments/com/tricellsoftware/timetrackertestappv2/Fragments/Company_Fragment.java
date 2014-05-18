package com.tricellsoftware.timetrackertestappv2.Fragments;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tricellsoftware.timetrackertestappv2.R;
import com.tricellsoftware.timetrackertestapp.DTOsv2.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.CompanyTable;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class Company_Fragment extends Fragment {
	

	private NumberPicker nPicker;
	private NumberPicker nPicker2;
	private TextView tView;
	private CheckBox Default;
	private Uri companyUri;
	
	private BusinessLogic logic;
	private CompanyDTO company;
	private CompanyDTO tempCompany;
	//needed for the aletrt box
	
	private int id;
	
	Context ctx = null;
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		 Bundle args = getArguments(); 
		 //Bundle extras = getActivity().getIntent().getExtras();

	    //Get arguments passed from an activity or another fragment
		 if(args != null){
			 id = getArguments().getInt(CompanyTable.COLUMN_ID);
		 }

	    View view = inflater.inflate(R.layout.company_fragment,
	        container, false);
	    ctx = getActivity();
	    
	    return view;
	  }
	  public void getDataByID(String ID){
		 
		  
		  id = Integer.parseInt(ID);
			logic = new BusinessLogic(getActivity());
			
			tView = (TextView) getActivity().findViewById(R.id.txtcompanyname);
			nPicker = (NumberPicker) getActivity().findViewById(R.id.numberPicker1);
			nPicker2 = (NumberPicker) getActivity().findViewById(R.id.NumberPicker01);
			Button SaveBttn = (Button) getActivity().findViewById(R.id.checkbttn);
			Default = (CheckBox) getActivity().findViewById(R.id.chkboxdefault);
			//action bar
			ActionBar actionBar = getActivity().getActionBar();
			actionBar.setTitle("Company Details");
			
//			//mItem = DummyData.Item_Map.get(id);
			nPicker.setMaxValue(100);
			nPicker.setMinValue(8);
			nPicker2.setMaxValue(100);
			nPicker2.setMinValue(0);
			//nPicker2.setWrapSelectorWheel(false);
			//displays two digits on the number piker
			nPicker2.setFormatter(new NumberPicker.Formatter(){ 
					@Override 
					public String format(int value)
					{ 
					return String.format("%02d", value);
					} 
			}); 
			
			
			TextView Datetv = (TextView) getActivity().findViewById(R.id.vDateCompany);
			
			//get an
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy"); // hour //HH:mm:ss
			Date today = Calendar.getInstance().getTime();
			String reportDate = df.format(today);
			Datetv.setText(reportDate);

			//Retriving the id pass from the previous id
			//Intent intent = getIntent();
//			Bundle extras = getActivity().getIntent().getExtras(); //get activity
//			
//			if(extras != null){
//				id = extras.getInt(CompanyTable.COLUMN_ID);
//			}
			
		
			 // Or passed from the other activity
		    if (id > 0) {
		    	//companyUri = extras
		          //.getParcelable(TimeTrackerContentProvider.CONTENT_ITEM_TYPE);
		    	//company = logic.getCompanyByDefault("1");
		    	company = logic.getCompanyById(id);
		    	String rate = String.valueOf(company.getRate());
		    	String[] rates = rate.split("\\.");
		        //fillData(id);
		    	tView.setText(company.getName());
		    	nPicker.setValue(Integer.valueOf(rates[0]));
		    	nPicker2.setValue(Integer.valueOf(rates[1]));
		    	Default.setChecked(company.getIsDefault());
		    	
		    }
		    else{
		    	Default.setChecked(true);
		    	tView.setText("");
		    	nPicker.setValue(8);
		    	nPicker2.setValue(0);
		    	
		    }
		
			//Listening to the button event
			SaveBttn.setOnClickListener(new View.OnClickListener(){
				
				@Override
				public void onClick(View view){
				
					SaveNewItem();
					//Starting a new intent
					//Intent CompanyRatesScreen = new Intent(getApplicationContext(), CompanyRatesActivity.class);
					//startActivity(CompanyRatesScreen);
				}
			});
	  }
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
		    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
	    		//&& (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
		    	getDataByID(String.valueOf(id));
		    }
		   
		    	
	    }//checking for 
		    
	  
	  @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            //mListener = (OnArticleSelectedListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
	        }
	    }

	  
	 private void SaveNewItem(){
			
			if(tView.length() == 0){
				return;
			}
			int rate = nPicker.getValue();
			int rate2 = nPicker2.getValue();
			String total = String.valueOf(rate)+"."+String.valueOf(rate2);
			double totalRate = Double.valueOf(total);
			String name = tView.getText().toString().toUpperCase();

			tempCompany = new CompanyDTO();
			tempCompany.setName(name);
			tempCompany.setRate(totalRate);
			
			if(Default.isChecked())
				tempCompany.setIsDefault(true);
			else
				tempCompany.setIsDefault(false);
			
			
			
			if(id <= 0){
				
				if(logic.ValidateIfNameExists(name) == false){
					/** The following code looks an existing company that set up as default and changes to false**/
					//This object will be use to store the previously set up default company.
					if(tempCompany.getIsDefault()){
						CompanyDTO pCompany = new CompanyDTO();
						pCompany = logic.getCompanyByDefault("1");
						if(tempCompany.getID() != pCompany.getID()){
							pCompany.setIsDefault(false);
							logic.updateCompanyById(pCompany);
							getActivity().finish();
						}
					}
					//new item
					logic.addNewCompany(tempCompany);
					 if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
						 //Toast.makeText(getActivity(), "New Record has been added successfully", Toast.LENGTH_LONG).show();
					      //return;
					    }//checking f
					 else{
						//getActivity().finish();
						//Toast.makeText(getActivity(), "New Record has been added successfully", Toast.LENGTH_LONG).show();
					 }
				}
				else
					Toast.makeText(getActivity(), "Company Name already Exists, please choose a different one", Toast.LENGTH_LONG).show();
			}
			else {
				//if the company that was pull does not match the new/temp company then update it
				boolean chkbox = Default.isChecked();
				if(company.getName().equals(name) && company.getRate() == totalRate && company.getIsDefault() == chkbox){
					getActivity().finish();
				}
				else{
					//getContentResolver().update(companyUri, Values, null, null);
					//Toast.makeText(this, "No change was made to the record", Toast.LENGTH_LONG).show();
					
					//set id of current company
					tempCompany.setID(id);
					
					/** The following code looks an existing company that set up as default and changes to false**/
					//This object will be use to store the previously set up default company.
					if(tempCompany.getIsDefault()){
						CompanyDTO pCompany = new CompanyDTO();
						pCompany = logic.getCompanyByDefault("1");
						if(tempCompany.getID() != pCompany.getID()){
							pCompany.setIsDefault(false);
							logic.updateCompanyById(pCompany);
							getActivity().finish();
						}
					}
					/** End of Section **/
					//update current company
					logic.updateCompanyById(tempCompany);
					
					//getContentResolver().update(companyUri, Values, null, null);
					Toast.makeText(getActivity(), "Selected Record has been updated successfully", Toast.LENGTH_LONG).show();
					
					getActivity().finish();
				}
			
				
			}
		}
		
		//create menu options for the action bar
		public void onCreateOptionsMenu(Menu menu) {
	    		   MenuInflater inflateLayout = getActivity().getMenuInflater();
	    		   inflateLayout.inflate(R.menu.details_menu, menu);
	    		   super.onCreateOptionsMenu(menu, inflateLayout);
	    } 
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			//Gets the position on the Item selected
			//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			//_id = companies.get((int) info.id).getID();
		   switch (item.getItemId()) {
		   case R.id.action_settings:
		    	  Toast.makeText(ctx, "Settings was selected", Toast.LENGTH_LONG).show();
		   break;
		   case R.id.action_delete:
			   
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Are you sure you want to delete this record?")
				       .setTitle("Delete");

				// 3. Get the AlertDialog from create()
				
				builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int _id) {
		        	   logic.deleteCompanyById(String.valueOf(id));
		    		  
		    		   getActivity().finish();
		           }
		       });
				builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		        	   dialog.cancel();
		           }
		       });
				
				// Create the AlertDialog
				AlertDialog dialog = builder.create();

				// show it
				dialog.show();

				 //Toast.makeText(this, "Record has been deleted successfully", Toast.LENGTH_LONG).show();
		   break;
		    	  
		   }
		   return super.onOptionsItemSelected(item);
		 } 
}
