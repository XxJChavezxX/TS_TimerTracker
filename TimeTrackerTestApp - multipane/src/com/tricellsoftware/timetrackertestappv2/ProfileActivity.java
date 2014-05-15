package com.tricellsoftware.timetrackertestappv2;


import com.tricellsoftware.timetrackertestapp.DTOsv2.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.businessLogicv2.BusinessLogic;
import com.tricellsoftware.timetrackertestapp.databasev2.ProfileTable;
import com.tricellsoftware.timetrackertestapp.helperv2.TimeHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	
	private EditText Firstname;
	private EditText Lastname;
	private EditText Email;
	
	private BusinessLogic logic;
	private ProfileDTO profile;
	
	private int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			id = extras.getInt(ProfileTable.COLUMN_ID);
		}
		
		//business logic
		logic = new BusinessLogic(this);
		
		//get user by id 1
		profile = logic.getUser(id);
		//action bar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Profile");
		
		Firstname = (EditText) findViewById(R.id.editText1);
		Lastname = (EditText) findViewById(R.id.editText2);
		Email = (EditText) findViewById(R.id.editText3);
		
		if(profile != null){
			Firstname.setText(profile.getFirstName());
			Lastname.setText(profile.getLastName());
			Email.setText(profile.getEmail());
		}
		
		TextView Datetv = (TextView) findViewById(R.id.datetxt);
		String reportDate = TimeHelper.getDate();
		Datetv.setText(reportDate);
		
		Button SaveBttn = (Button) findViewById(R.id.checkbttn);
		//Listening to the button event
				SaveBttn.setOnClickListener(new View.OnClickListener(){
					
					@Override
					public void onClick(View view){
					
						SaveNewItem();
						
					}
				});
		
		
	}
	private void SaveNewItem(){
			
			if(Firstname.length() == 0){
				return;
			}

			String name = Firstname.getText().toString();
			String lastName = Lastname.getText().toString();
			String email = Email.getText().toString();
			
			if(id <= 0){
				
				ProfileDTO newprofile = new ProfileDTO();
				newprofile.setFirstName(name);
				newprofile.setLastName(lastName);
				newprofile.setEmail(email);
				
				newprofile.setStatusID(2); // set fk status id to off
				//new item
				logic.AddNewUser(newprofile);
				//companyUri = getContentResolver().insert(TimeTrackerContentProvider.Content_URI, Values);
				Intent MainScreen = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(MainScreen);
				Toast.makeText(this, "Your profile has been created successfully", Toast.LENGTH_LONG).show();
				
				finish();
				
			}
			else if(profile.getFirstName().equals(name) && profile.getLastName().equals(lastName) && profile.getEmail().equals(email)){
				 finish();
			}
			else {

				profile.setID(id);
				profile.setFirstName(name);
				profile.setLastName(lastName);
				profile.setEmail(email);
				logic.updateProfileById(profile);
				//getContentResolver().update(companyUri, Values, null, null);
				Toast.makeText(this, "Your profile has been updated successfully", Toast.LENGTH_LONG).show();
				
				 finish();
			}
		}
	
}
