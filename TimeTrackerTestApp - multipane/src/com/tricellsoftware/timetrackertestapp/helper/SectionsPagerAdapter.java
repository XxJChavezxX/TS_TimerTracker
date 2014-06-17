package com.tricellsoftware.timetrackertestapp.helper;

import java.util.Locale;

import com.tricellsoftware.timetrackertestapp.CompanyMainActivity;
import com.tricellsoftware.timetrackertestapp.Fragments.Clocks_Fragment;
import com.tricellsoftware.timetrackertestapp.Fragments.Companies_Fragment;
import com.tricellsoftware.timetrackertestapp.Fragments.CompanyMain_Fragment;
import com.tricellsoftware.timetrackertestapp.Fragments.Summary_Fragment;





import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter{
	
	private static final String ARG_SECTION_NUMBER = "section_number";

	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		switch(position){
		case 0: 
			//Clocks_Fragment fragment = new Clocks_Fragment();
			//return fragment;
			return Clocks_Fragment.newInstance(position);
		case 1:
			//Summary_Fragment fragment1 = new Summary_Fragment();
			//eturn fragment1;
			return Summary_Fragment.newInstance(position);
		case 2: 
			//Companies_Fragment fragment2 = new Companies_Fragment();
			//return fragment2;
			return Companies_Fragment.newInstance(position);
			
			//FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();

		}
		return null;
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return "Tab 1"; //getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return "Tab 2";//getString(R.string.title_section2).toUpperCase(l);
		case 2:
			return "Tab 3"; //getString(R.string.title_section3).toUpperCase(l);
		}
		return null;
	}
}