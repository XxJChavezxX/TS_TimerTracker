package com.tricellsoftware.timetrackertestapp.DTOsv2;

public enum Status_Enum {
	On("ON", 1),
	Off("OFF", 2),
	In("IN", 3),
	Out("OUT", 4);
	
	 private String stringValue;
	    private int intValue;
	    private Status_Enum(String toString, int value) {
	        stringValue = toString;
	        intValue = value;
	    }

	    @Override
	    public String toString() {
	        return stringValue;
	    }
	    
	    public int getValue(){
	    	return intValue;
	    }

}
