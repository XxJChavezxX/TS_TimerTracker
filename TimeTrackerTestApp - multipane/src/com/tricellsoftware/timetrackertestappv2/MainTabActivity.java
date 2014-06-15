package com.tricellsoftware.timetrackertestappv2;


import com.tricellsoftware.timetrackertestapp.DTOsv2.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.ProfileTable;
import com.tricellsoftware.timetrackertestapp.helperv2.SectionsPagerAdapter;
import com.tricellsoftware.timetrackertestappv2.Fragments.Clocks_Fragment;
import com.tricellsoftware.timetrackertestappv2.Fragments.Companies_Fragment;
import com.tricellsoftware.timetrackertestappv2.Fragments.Company_Fragment;
import com.tricellsoftware.timetrackertestappv2.Fragments.Summary_Fragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainTabActivity extends Activity implements ActionBar.TabListener, Companies_Fragment.OnItemSelectedListener {
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	private BusinessLogic logic;
	private ProfileDTO profile;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		if(savedInstanceState != null){
//			return;
//		}
		
		setContentView(R.layout.pager_container);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle("TS Time Tracker");
		
  		//business logic
  		logic = new BusinessLogic(this);
		//access profile data
		profile = logic.getUser(1);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
						
//						//gets current fragment and refreshes the view
//						Fragment fg = returnCurrentFragment(position);
//						
//						if(fg != null){
//							if(fg.getView() != null){
//								fg.onResume();
//							}
//						}
//						if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//							Fragment f1 = returnCurrentFragment(position);
//							if(f1 != null){
//								if(f1.getView() != null){
//									f1.onResume();
//								}
//							}
//						}

						
					}
				});
		mViewPager.setOffscreenPageLimit(1);
		// For each of the sections in the app, add a tab to the action bar.
		
		
		actionBar.addTab(actionBar.newTab()
				.setText("Clock In/Out")
				.setTabListener(this));
				//.setIcon(R.drawable.clockstab));
				//.setCustomView(R.layout.clocktab_layout));
		//actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.WHITE));
		
		actionBar.addTab(actionBar.newTab()
				.setText("Summary")
				.setTabListener(this));
				//.setCustomView(R.layout.summrytab_layout));
		//actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.WHITE));
		
		actionBar.addTab(actionBar.newTab()
				.setText("Companies")
				.setTabListener(this));
				//.setIcon(R.drawable.companytab));
		
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			   Intent ProfileScreen = new Intent(getApplicationContext(), ProfileActivity.class);
			   ProfileScreen.putExtra(ProfileTable.COLUMN_ID, profile.getID());
			   startActivity(ProfileScreen);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// When the given tab is selected, switch to the corresponding page in
				// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
		
		
		
		//mSectionsPagerAdapter.notifyDataSetChanged();
		//this.onResume();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(String item) {
		// TODO Auto-generated method stub
		
	}
	/*** Returns current fragment ***/
	public Fragment returnCurrentFragment(int id){
		
		switch(id){
		case 0:
			Clocks_Fragment fragment = (Clocks_Fragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":0");
			return fragment;
		case 1: 
			Summary_Fragment fragment2 = (Summary_Fragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":1");
			return fragment2;
		case 2:
			Companies_Fragment fragment3 = (Companies_Fragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":2");
			return fragment3;
		}
			
		return null;
	}
	

}
