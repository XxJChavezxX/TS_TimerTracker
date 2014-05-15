package com.tricellsoftware.timetrackertestapp.DTOsv2;

public class ProfileDTO {
	
	//table fields
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private int statusid;
	
	public ProfileDTO(){
	}
	
	public int getID(){
		return id;
	}
	public void setID(int id){
		this.id = id;
	}
	public void setFirstName(String name){
		this.firstName = name;
	}
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public void setStatusID(int statusid){
		this.statusid = statusid;
	}
	public String getFirstName(){
		return firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public String getEmail(){
		return email;
	}
	public int getStatusID(){
		return statusid;
	}

	
	public ProfileDTO(String firstName, String lastName, String email, int statusid)//, String phone)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.statusid = statusid;
	}

}
