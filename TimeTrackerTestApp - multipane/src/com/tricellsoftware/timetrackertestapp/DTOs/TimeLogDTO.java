package com.tricellsoftware.timetrackertestapp.DTOs;

public class TimeLogDTO {
	
	private int id;
	private String DateTime;
	private String StartTime;
	private String EndTime;
	private String Milliseconds;
	private String YearWeek;
	//private Date endTime;
	private int profileId;
	private int statusId;
	private int companyId;

//	public int profileid; // may be included
	public TimeLogDTO(){
	}
	
	public int getID(){
		return id;
	}
	public void setID(int id){
		this.id = id;
	}
	public void setDate(String time){
		this.DateTime = time;
	}
	public void setStartTime(String time){
		this.StartTime = time;
	}
	public void setEndTime(String time){
		this.EndTime = time;
	}
	public void setMilliseconds(String Milliseconds){
		this.Milliseconds = Milliseconds;
	}
	public void setStatusID(int statusId){
		this.statusId = statusId;
	}
	public void setProfileID(int profileId){
		this.profileId = profileId;
	}
	public String getDate(){
		return DateTime;
	}
	public String getStartTime(){
		return StartTime;
	}
	public String getEndTime(){
		return EndTime;
	}
	public String getMilliseconds(){
		return Milliseconds;
	}
	public int getStatusID(){
		return statusId;
	}
	public int getProfileID(){
		return profileId;
	}
	
	public String getYearWeek() {
		return YearWeek;
	}

	public void setYearWeek(String yearWeek) {
		YearWeek = yearWeek;
	}
	
	
	public TimeLogDTO(int id, String DateTime, String StartTime, String EndTime, String Milliseconds, String YearWeek, int profileId, int statusId){
		this.id = id;
		this.DateTime = DateTime;
		this.StartTime = StartTime;
		this.EndTime = EndTime;
		this.Milliseconds = Milliseconds;
		//this.endTime = endTime;
		this.profileId = profileId;
		this.statusId = statusId;
		this.YearWeek = YearWeek;
		
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}


}
