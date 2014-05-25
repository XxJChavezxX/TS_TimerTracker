package com.tricellsoftware.timetrackertestappv2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tricellsoftware.timetrackertestapp.DTOsv2.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.DTOsv2.DummyData;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.CompanyTable;
import com.tricellsoftware.timetrackertestappv2.Fragments.Companies_Fragment;
import com.tricellsoftware.timetrackertestappv2.Fragments.Company_Fragment;
import com.tricellsoftware.timetrackertestappv2.Fragments.Companies_Fragment.OnItemSelectedListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.NumberPicker;
import android.widget.TextView;

public class CompanyActivity extends FragmentActivity implements Companies_Fragment.OnItemSelectedListener{
	
	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyData.DummyItem mItem;
	private NumberPicker nPicker;
	private NumberPicker nPicker2;
	private TextView tView;
	private CheckBox Default;
	private Uri companyUri;
	
	private BusinessLogic logic;
	private CompanyDTO company;
	private CompanyDTO tempCompany;
	//needed for the aletrt box
	final Context context = this;
	
	private ScrollView sv;
	
	public static int id;
	private String _id;
	
	boolean land; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		// Need to check if Activity has been switched to landscape mode
	    // If yes, finished and go back to the start Activity
		//if screen is large (7 inches)
	    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
	    		//&& (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
	    	land = true;
	      /** Sending ID back to the Companies fragment if this activity is in landscape mode**/	
	      if(id > 0){
				/** Share id with other activities or fragments by using sharedPref method
				 * **/
	    	  SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.pref_data_key), 0);
	    	  SharedPreferences.Editor editor = sharedPref.edit();
	    	  editor.putInt(getString(R.string.company_id), id);
	    	  editor.commit();
	    	  
			Intent i = new Intent(this, CompanyMainActivity.class);
			i.putExtra("companyID", id);
			setResult(Activity.RESULT_OK, i); //pass information to the previous activity
			
	      }
	      
      	  /** Kills the Activity **/
	      finish();
	    //  return;
	    }//checking for the Orientation must be before defining the content view
	    else
	    	land = false;
		setContentView(R.layout.company_layout_port);
		
		logic = new BusinessLogic(this);
		
		if(land == false){
			//sv = (ScrollView)findViewById(R.id.scrollView1);
		}
		
		tView = (TextView)findViewById(R.id.txtcompanyname);
		nPicker = (NumberPicker)findViewById(R.id.numberPicker1);
		nPicker2 = (NumberPicker)findViewById(R.id.NumberPicker01);
		Button SaveBttn = (Button) findViewById(R.id.checkbttn);
		Default = (CheckBox) findViewById(R.id.chkboxdefault);
		//action bar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Company Details");
		
//		//mItem = DummyData.Item_Map.get(id);
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
		
		
		TextView Datetv = (TextView) findViewById(R.id.vDateCompany);
		
		//get an
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy"); // hour //HH:mm:ss
		Date today = Calendar.getInstance().getTime();
		String reportDate = df.format(today);
		Datetv.setText(reportDate);

		//Retriving the id pass from the previous id
		//Intent intent = getIntent();
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			_id = extras.getString(CompanyTable.COLUMN_ID);
			id = Integer.parseInt(_id);
		}
		
	
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
//	
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
						//finish();
					}
				}
				//new item
				logic.addNewCompany(tempCompany);
				//companyUri = getContentResolver().insert(TimeTrackerContentProvider.Content_URI, Values);
				Toast.makeText(this, "New Company/Project: " + tempCompany.getName() + " has been added successfully", Toast.LENGTH_LONG).show();
				finish();
			}
			else
				Toast.makeText(this, "Company Name already Exists, please choose a different one", Toast.LENGTH_LONG).show();
		}
		else {
			//if the company that was pull does not match the new/temp company then update it
			boolean chkbox = Default.isChecked();
			if(company.getName().equals(name) && company.getRate() == totalRate && company.getIsDefault() == chkbox){
				finish();
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
						finish();
					}
				}
				/** End of Section **/
				//update current company
				logic.updateCompanyById(tempCompany);
				
				//getContentResolver().update(companyUri, Values, null, null);
				Toast.makeText(this, "The seleted Company: " + tempCompany.getName() + " has been updated successfully", Toast.LENGTH_LONG).show();
				
				finish();
			}
		
			
		}
	}
	
	//create menu options for the action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    		   MenuInflater inflateLayout = getMenuInflater();
    		   inflateLayout.inflate(R.menu.details_menu, menu);
    		  return super.onCreateOptionsMenu(menu);
    } 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Gets the position on the Item selected
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//_id = companies.get((int) info.id).getID();
	   switch (item.getItemId()) {
	   case R.id.action_settings:
	    	  Toast.makeText(this, "Settings was selected", Toast.LENGTH_LONG).show();
	   break;
	   case R.id.action_delete:
		   
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(context);

			// 2. Chain together various setter methods to set the dialog characteristics
			builder.setMessage("Are you sure you want to delete this record?")
			       .setTitle("Delete");

			// 3. Get the AlertDialog from create()
			
			builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	           @Override
			public void onClick(DialogInterface dialog, int _id) {
	        	   logic.deleteCompanyById(String.valueOf(id));
	    		   finish();
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
	
	private void fillData(Uri uri) {
		    String[] projection = { CompanyTable.COLUMN_COMPANY,
		    		CompanyTable.COLUMN_RATE, CompanyTable.COLUMN_DEFAULT_COMPANY };
		    Cursor cursor = getContentResolver().query(uri, projection, null, null,
		        null);
		    if (cursor != null) {
		      cursor.moveToFirst();
		      String name = cursor.getString(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_COMPANY));
		      double rate = cursor.getInt(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_RATE));
		      
		      tView.setText(name);
		      nPicker.setValue((int)rate);

		      // always close the cursor
		      cursor.close();
		    }
	 }
	@Override
	public void onItemSelected(String item) {
		// TODO Auto-generated method stub
		
	}
//	public static int returnCompID(){
//		return id;
//	}
	

}
