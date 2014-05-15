package com.tricellsoftware.timetrackertestapp.DTOsv2;

public class CompanyDTO {
	
	private int id;
	private String name;
	private double rate;
	private boolean isDefault;
	
	public CompanyDTO(){
	}
	
	public int getID(){
		return id;
	}
	public void setID(int id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setRate(double rate){
		this.rate = rate;
	}
	public void setIsDefault(boolean isDefault){
		this.isDefault = isDefault;
	}
	public String getName(){
		return name;
	}
	public double getRate(){
		return rate;
	}
	public boolean getIsDefault(){
		return isDefault;
	}
	public CompanyDTO(String name, double rate, boolean isDefault){
		this.name = name;
		this.rate = rate;
		this.isDefault = isDefault;
	}
	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return name;
	  }
}
