package com.tricellsoftware.timetrackertestapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tricellsoftware.timetrackertestapp.R;
import com.tricellsoftware.timetrackertestapp.DTOs.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.database.CompanyTable;
import com.tricellsoftware.timetrackertestapp.database.ProfileTable;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {
	
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    
	private BusinessLogic logic;
	private ProfileDTO profile;
	
	public int profileID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		//action bar
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		
		//business logic //db helper
		logic = new BusinessLogic(this);
		
		profile = logic.getUser(1);//search for user by id 1
		//if not null then redirect the user to the main activiy
		if(profile != null){ 
			
	        new Handler().postDelayed(new Runnable() {
	          	 
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
	                
	                i.putExtra(ProfileTable.COLUMN_ID, profile.getID());
	   			 
	                startActivity(i);
	 
	                // close this activity
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
		
			 
		}
		else{ //else take the user to the profile screen
	        new Handler().postDelayed(new Runnable() {
	          	 
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	                Intent i = new Intent(SplashScreenActivity.this, ProfileActivity.class);
	  
	   			 
	                startActivity(i);
	 
	                // close this activity
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
			
		}

	}

}
