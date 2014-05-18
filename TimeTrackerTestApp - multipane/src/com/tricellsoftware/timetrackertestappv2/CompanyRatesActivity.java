package com.tricellsoftware.timetrackertestappv2;

import java.util.List;


import com.tricellsoftware.timetrackertestapp.DTOsv2.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//this activity was replaced by the TimelogList_Fragment
public class CompanyRatesActivity extends ListActivity{ //implements LoaderManager.LoaderCallbacks<Cursor>{
	
	SimpleCursorAdapter mAdapter;
	ListView listView;
	//private SimpleCursorAdapter adapter;
	
	List<CompanyDTO> companies; 
	private BusinessLogic logic;
	
	ArrayAdapter<CompanyDTO> adapter;
	//action bar
	ActionBar actionBar;
	
	private int _id;
	private int position;
	
	ProgressDialog pd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.company_rates_fragment);
		registerForContextMenu(getListView());
//		pd = new ProgressDialog(this);
//		pd.show();
//		pd.setMessage("Loading..");
		
		
		
		logic = new BusinessLogic(this);
		//Button addNewBttn = (Button) findViewById(R.id.addnewbttn);
		
		//action bar
		actionBar = getActionBar();
		actionBar.setTitle("Companies");
		
		//GetCompaniesData();
		//pd.hide();
		
	}
//	// Opens the second activity if an entry is clicked
//	  @Override
//	  protected void onListItemClick(ListView l, View v, int position, long id) {
//	    super.onListItemClick(l, v, position, id);
//	    Intent i = new Intent(this, CompanyActivity.class);
//	    //mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
//	    
//	    //Uri CompanyUri = Uri.parse(TimeTrackerContentProvider.Content_URI + "/" + id);
//	    _id = companies.get(position).getID();
//	    i.putExtra(CompanyTable.COLUMN_ID, _id);
//
//	    startActivity(i);
//	  }
//	public void GetCompaniesData(){
//		
//		//gets all companies from the db and add them to the list
//		companies = logic.getAllCompanies();
//		
//	    adapter = new ArrayAdapter<CompanyDTO>(this, android.R.layout.simple_list_item_1, companies);
//	    setListAdapter(adapter);
//	    
//	    adapter.notifyDataSetChanged();
//	}
//	
//    protected void onStart(){
//    	super.onStart();
//    }
//    ///Refreshes the List View
//    protected void onRestart(){
//    	super.onRestart();
//    	pd.show();
//    	GetCompaniesData();
//    	pd.hide();
//    }
//
//    protected void onResume(){
//    	super.onResume();
//    }
//
//    protected void onPause(){
//    	super.onPause();
//    }
//
//    public void onCreateContextMenu(ContextMenu menu, View v,
//    		   ContextMenuInfo menuInfo) {
//    		   MenuInflater inflateLayout = getMenuInflater();
//    		   inflateLayout.inflate(R.menu.context_menu, menu);
//    		  super.onCreateContextMenu(menu, v, menuInfo);
//    } 
//	 public boolean onContextItemSelected(MenuItem item) {
//		//Gets the position on the Item selected
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//		_id = companies.get((int) info.id).getID();
//	   switch (item.getItemId()) {
//	   case R.id.delete:
//		   logic.deleteCompanyById(_id);
//		   Toast.makeText(this, "Record has been deleted successfully", Toast.LENGTH_LONG).show();
//		   GetCompaniesData();
//	    break;
//	      case R.id.view:
//	  	    Intent i = new Intent(this, CompanyActivity.class);
//		    //mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
//		    //Uri CompanyUri = Uri.parse(TimeTrackerContentProvider.Content_URI + "/" + id);
//		    
//		    i.putExtra(CompanyTable.COLUMN_ID, _id);
//
//		    startActivity(i);
//	   }
//	   return super.onContextItemSelected(item);
//	 } 
//	//create menu options for the action bar
//	public boolean onCreateOptionsMenu(Menu menu) {
//    		   MenuInflater inflateLayout = getMenuInflater();
//    		   inflateLayout.inflate(R.menu.companies_menu, menu);
//    		  return super.onCreateOptionsMenu(menu);
//    } 
//	public boolean onOptionsItemSelected(MenuItem item) {
//		//Gets the position on the Item selected
//		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//		//_id = companies.get((int) info.id).getID();
//	   switch (item.getItemId()) {
//	   case R.id.action_settings:
//	    	  Toast.makeText(this, "Settings was selected", Toast.LENGTH_LONG).show();
//	   break;
//	   case R.id.AddNew:
//		   Intent CompanyScreen = new Intent(getApplicationContext(), CompanyActivity.class);
//		   startActivity(CompanyScreen);
//		   //search text box on the action bar
////	   case R.id.action_search:
////			LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////		   View v = inflator.inflate(R.layout.search_custom, null);
////	   
////		   //Toast.makeText(this, "Search was selected", Toast.LENGTH_LONG).show();
////		  // actionBar.getThemedContext();
////		   //actionBar.
////		   actionBar.setCustomView(v);
////		   
////		  // actionBar.hide();
////	
////			/** Get the edit text from the action view */
////		   TextView txtSearch = (TextView) v.findViewById(R.id.editText1);
////		   //txtSearch.setBackgroundColor(Color.WHITE);
////		   txtSearch.setTextColor(Color.WHITE);
////	   break;
//	    	  
//	   }
//	   return super.onOptionsItemSelected(item);
//	 } 
//	
//	@Override
//	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//		String[] projection = { CompanyTable.COLUMN_ID, CompanyTable.COLUMN_COMPANY };
//		CursorLoader cursorLoader = new CursorLoader(this, TimeTrackerContentProvider.Content_URI, projection, null, null, null);
//		// TODO Auto-generated method stub
//		return cursorLoader;
//	}
//	@Override
//	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
//		// TODO Auto-generated method stub
//		//GetCompaniesData();
//		
//
//		//adapter.swapCursor(data);
//	}
//	@Override
//	public void onLoaderReset(Loader<Cursor> loader) {
//		// TODO Auto-generated method stub
//		//GetCompaniesData();
//		
//
//		// data is not available anymore, delete reference
//	   // adapter.swapCursor(null);
//	}

}
