package com.tricellsoftware.timetrackertestappv2;


import com.tricellsoftware.timetrackertestapp.DTOsv2.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.ProfileTable;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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
		
		if (savedInstanceState != null) {
               return;
         }

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
