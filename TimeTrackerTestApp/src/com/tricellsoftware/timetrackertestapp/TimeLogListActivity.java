package com.tricellsoftware.timetrackertestapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tricellsoftware.timetrackertestapp.R;
import com.tricellsoftware.timetrackertestapp.DTOs.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.database.CompanyTable;
import com.tricellsoftware.timetrackertestapp.database.ProfileTable;
import com.tricellsoftware.timetrackertestapp.database.TimeLogTable;
import com.tricellsoftware.timetrackertestapp.helper.CustomArrayAdapter;
import com.tricellsoftware.timetrackertestapp.helper.TimeHelper;

import android.R.menu;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class TimeLogListActivity extends ListActivity {
	
	//ArrayAdapter<TimeLogDTO> adapter;
	List<TimeLogDTO> timelogs;
	CustomArrayAdapter adapter;
	String date;
	
	TextView txtStatus;

	private BusinessLogic logic;
	
	final Context context = this;
	
	long miliseconds = 0;
	//action bar
	private ActionBar actionBar;
	
	private int _id;
	String StartDate;
	String EndDate;
	
	ProgressDialog pd = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		pd = new ProgressDialog(this);
		pd.show();
		pd.setMessage("Loading..");
		logic = new BusinessLogic(this);
		//Button addNewBttn = (Button) findViewById(R.id.addnewbttn);

		
		//action bar
		actionBar = getActionBar();
		actionBar.setTitle("TimeLog");
		actionBar.setDisplayShowCustomEnabled(true);

		GetTimeLogData();
		
		pd.hide();
	}
	
	public void GetTimeLogData(){
		
		

		Bundle extras = getIntent().getExtras();
		
		if(date != null){
			timelogs = logic.getAllTimeLogsByDate(date);
			
		}
		else if(extras != null){
			StartDate = extras.getString("StartDate");
			EndDate = extras.getString("EndDate");
			timelogs = logic.getAllTimeLogsByWeek(StartDate, EndDate);
		}
		else
			timelogs = logic.getAllTimeLogs();
			//timelogs = logic.getAllTimeLogsByWeek(StartDate, EndDate);
			
		
		
		
		adapter = new CustomArrayAdapter(this, R.layout.timelog_list, timelogs);
		
//	    adapter = new ArrayAdapter<TimeLogDTO>(this, R.layout.timelog_list, R.id.txtDatelog, timelogs){
//	    	public View getView(int position, View convertView, ViewGroup parent){
//	    		View view = super.getView(position, convertView, parent);
//	    		
//	    		TimeLogDTO item = getItem(position);
//	    		
//	    		 txtStatus = (TextView)view.findViewById(R.id.txtStatus);
//	    		 txtStatus.setText("OFF");
//	    		
//	    		return view;
//	    	}
//	    };
	    setListAdapter(adapter);
	    
	    adapter.notifyDataSetChanged();
	}
	//create menu options for the action bar
	public boolean onCreateOptionsMenu(Menu menu) {
    		   MenuInflater inflateLayout = getMenuInflater();
    		   inflateLayout.inflate(R.menu.timelog_menu, menu);
    		  return super.onCreateOptionsMenu(menu);
    } 
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	  super.onListItemClick(l, v, position, id);
	  Intent i = new Intent(this, EditTimeActivity.class);
	  
		if(timelogs.get(position).getEndTime().equals("--")){
			Toast.makeText(this, "Please Clock Out before trying to modify the Times", Toast.LENGTH_LONG).show();
		}
		else{
			 _id = timelogs.get(position).getID();
			 i.putExtra(TimeLogTable.COLUMN_ID, _id);
			 //add start time and end time
			 i.putExtra("StartDate", StartDate);
			 i.putExtra("StartDate", EndDate);
			 startActivity(i);
		}
	    //mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
	
	    //Uri CompanyUri = Uri.parse(TimeTrackerContentProvider.Content_URI + "/" + id);
	  
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		//Gets the position on the Item selected
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//_id = companies.get((int) info.id).getID();
	   switch (item.getItemId()) {
	   case R.id.action_settings:
	    	  Toast.makeText(this, "Settings was selected", Toast.LENGTH_LONG).show();
	   break;
	   case R.id.action_search:
		  
			Intent i = new Intent(getApplicationContext(), CalendarViewActivity.class);
			i.putExtra("selectedDate", date);
			startActivityForResult(i, 1);
			
	   break;
	    	  
	   }
	   return super.onOptionsItemSelected(item);
	 } 
	
    protected void onStart(){
    	super.onStart();
    }
    ///Refreshes the List View
    protected void onRestart(){
    	super.onRestart();
    	pd.show();
    	GetTimeLogData();
    	pd.hide();
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

    protected void onResume(){
    	super.onResume();
    }

    protected void onPause(){
    	super.onPause();
    }
}
