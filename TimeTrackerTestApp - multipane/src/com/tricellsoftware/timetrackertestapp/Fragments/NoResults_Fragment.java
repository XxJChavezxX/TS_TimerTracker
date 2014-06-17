package com.tricellsoftware.timetrackertestapp.Fragments;

import com.tricellsoftware.timetrackertestapp.R;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoResults_Fragment extends Fragment {
	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.no_results_fragment,
	        container, false);
	    
	   // ctx = getActivity();
	    
	    return view;
	  }

}
