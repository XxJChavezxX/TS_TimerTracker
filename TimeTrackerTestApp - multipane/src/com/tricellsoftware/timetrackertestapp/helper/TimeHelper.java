package com.tricellsoftware.timetrackertestapp.helper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.widget.TimePicker;


public class TimeHelper {
	static Date date; //time entry for the timelog

	static SimpleDateFormat df; //date format
	static String dateformat = "MM/dd/yyyy";
	
	static SimpleDateFormat tf;
	String time;
	static String TimeFormat2	=	"hh:mm:ss a"; //time format
	static String TimeFormat	=	"MM/dd/yyyy hh:mm:ss a"; //time format
	
	
	static int WeekOfYear;
	
	static long diff;
	
	public static String getTimeDiffInMinutes(String startTime, String endTime) throws ParseException{
		
		Date StartDateTime = new SimpleDateFormat(TimeFormat,Locale.US).parse(startTime);
		Date EndDateTime = new SimpleDateFormat(TimeFormat,Locale.US).parse(endTime);
		
		if(StartDateTime.getTime() > EndDateTime.getTime()){
			//diff = StartDateTime.getTime() - EndDateTime.getTime();
			diff = 0;
			
		}
		else {
			diff = EndDateTime.getTime() - StartDateTime.getTime();	
		    
		}
//		long diffSeconds = diff / 1000 % 60;
//		long diffMinutes = diff / (60* 1000) % 60;
//		long diffHours = diff / (60 * 60 * 1000);
		
		long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
		long minutes = TimeUnit.SECONDS.toMinutes(seconds);
		long hours = TimeUnit.MINUTES.toHours(minutes);

		//date = new Date(hours);
		//if(minutes >= 60)
			//minutes = 0;
		//return tf.format(date);
		return String.valueOf(minutes); //String.format("%01d:%02d", hours, minutes);//String.valueOf(hours) +":"+ String.valueOf(minutes);
		
	}
	public static String getTimeDiffInMillis(String startTime, String endTime) throws ParseException{
		
		Date StartDateTime = new SimpleDateFormat(TimeFormat,Locale.US).parse(startTime);
		Date EndDateTime = new SimpleDateFormat(TimeFormat,Locale.US).parse(endTime);
		
		if(StartDateTime.getTime() > EndDateTime.getTime()){
			//diff = StartDateTime.getTime() - EndDateTime.getTime();
			diff = 0;
			
		}
		else {
			diff = EndDateTime.getTime() - StartDateTime.getTime();	
		    
		}
//		long diffSeconds = diff / 1000 % 60;
//		long diffMinutes = diff / (60* 1000) % 60;
//		long diffHours = diff / (60 * 60 * 1000);
		
		long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
		long minutes = TimeUnit.SECONDS.toMinutes(seconds);
		long hours = TimeUnit.MINUTES.toHours(minutes);
		//variable will hold the milisecinds
		long millis = diff;
		//date = new Date(hours);
		//if(minutes >= 60)
			//minutes = 0;
		//return tf.format(date);
		return String.valueOf(millis); //String.format("%01d:%02d", hours, minutes);//String.valueOf(hours) +":"+ String.valueOf(minutes);
		
	}
	public static String getTimeDiffInSecs(String startTime, String endTime) throws ParseException{
		
		Date StartDateTime = new SimpleDateFormat(TimeFormat,Locale.US).parse(startTime);
		Date EndDateTime = new SimpleDateFormat(TimeFormat,Locale.US).parse(endTime);
		
		if(StartDateTime.getTime() > EndDateTime.getTime()){
			diff = StartDateTime.getTime() - EndDateTime.getTime();
		}
		else
			diff = EndDateTime.getTime() - StartDateTime.getTime();	
//		long diffSeconds = diff / 1000 % 60;
//		long diffMinutes = diff / (60* 1000) % 60;
//		long diffHours = diff / (60 * 60 * 1000);
		
		long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
		long minutes = TimeUnit.SECONDS.toMinutes(seconds);
		long hours = TimeUnit.MINUTES.toHours(minutes);
		//date = new Date(hours);
		//if(minutes >= 60)
			//minutes = 0;
		//return tf.format(date);
		return String.valueOf(seconds); //String.format("%01d:%02d", hours, minutes);//String.valueOf(hours) +":"+ String.valueOf(minutes);
		
	}
	public static String displayHoursandMinutes(String Minutes){
		
		if(Minutes == null){
			return "--";
		}
		long minutes = Integer.parseInt(Minutes);
		long hours = TimeUnit.MINUTES.toHours(minutes);
		long seconds = TimeUnit.MINUTES.toSeconds(minutes);
		if(minutes >= 60){
			minutes = minutes - (hours * 60);
		}
		seconds = seconds % 60;
		
		return String.format("%01d:%02d:%02d", hours, minutes, seconds);
	}
	public static String displayHoursandMinutesSecondSecondMethod(long millis){
		
//		if(Minutes == null){
//			return "--";
//		}
		long secs = TimeUnit.MILLISECONDS.toSeconds(millis);
		long minutes = TimeUnit.SECONDS.toMinutes(secs);
		long hours = TimeUnit.MINUTES.toHours(minutes);
		
		if(minutes >= 60){
			minutes = minutes - (hours * 60);
		}
		
		return String.format("%02d:%02d:%02d", hours, minutes, secs);
	}
	public static String displayHoursandMinutesSeconds(long millis){
		
		if(millis <= 0){
			return "--";
		}
		int secs = (int) (millis / 1000);
		int minutes = secs / 60;
		//long hours = minutes / 60;

		int hour = minutes / 60;
		secs = secs % 60;
		minutes = minutes % 60;
	
		
		return String.format("%02d:%02d:%02d", hour, minutes, secs);
	}
	public static String getDate(){
		df = new SimpleDateFormat(dateformat, Locale.US);
		date = Calendar.getInstance().getTime();
		return df.format(date);
		
		
	}
	public static String getDateAndTime(){
		df = new SimpleDateFormat(TimeFormat, Locale.US);
		date = Calendar.getInstance().getTime();
		return df.format(date);
		
		
	}
	public static int getWeekOfYear(){
		
		Calendar cl = new GregorianCalendar();
		//df = new SimpleDateFormat(dateformat, Locale.US);
		WeekOfYear = cl.get(Calendar.WEEK_OF_YEAR);
		return WeekOfYear;
		
	}
	public static String getTime(){
		
		tf = new SimpleDateFormat(TimeFormat, Locale.US);
		date = Calendar.getInstance().getTime();
		return tf.format(date);

		
	}
	public static String getDateFromLong(long miliseconds){
		
		df = new SimpleDateFormat(dateformat, Locale.US);
		date = new Date(miliseconds);
		return df.format(date);
	}
	public static long getLongFromDate(String Date){
		long miliseconds;
		Date date = null;
		try {
			date = new SimpleDateFormat(dateformat, Locale.US).parse(Date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		miliseconds = date.getTime();
		
		return miliseconds;
	}
	public static long getLongFromHour(String Hour){
		long miliseconds;
		Date date = null;
		try {
			date = new SimpleDateFormat(TimeFormat).parse(Hour);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		miliseconds = date.getTime();
		
		return miliseconds;
	}
	//sets the calendar to a specific date 
	public static Calendar setCalendar(String date){
		//get calendar set to current date and Time
		Calendar cal = Calendar.getInstance();
		Date dt = null;
		if(date != null){
			try {
				dt = new SimpleDateFormat(TimeFormat).parse(date);
				cal.setTime(dt);
				//cal.getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
			return null;
		//Date EndDate = cal.getTime();
		//String date2 = new SimpleDateFormat(TimeFormat).format(EndDate);
		//System.out.println(date2);
		return cal;
	}
	public static String[] getStartDateAndEndDate(String Date){
		
		//get calendar set to current date and Time
		Calendar cal = Calendar.getInstance();
		DateFormat df2 = new SimpleDateFormat("EEE MM/dd/yyyy");
		Date StartDate = null;
		
		if(Date != null){
			try {
				//creates Date from String Date
				StartDate = df.parse(Date);
				cal.setTime(StartDate); //sets the Time from the Date
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			//set the calendar to Monday of the current week
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			StartDate = cal.getTime(); //gets current Date
		}
		
		//Prints date in the specified format
		cal.add(Calendar.DATE, 6);
		Date EndDate = cal.getTime();
		//System.out.println(df.format(EndDate));
		
		String[] Dates = {df2.format(StartDate), df2.format(EndDate)};
		
		return Dates;
	}
	/**
	 * @return amount paid by minutes
	 **/
	public static String calculatepay(double minutes, float rate){
		
		float time = (float) ((minutes)/60);
		
		return new DecimalFormat("#.##").format(time * rate);
	}
	public static ArrayList<String> spamSevenDatesByStartDate(String Date){
		
		//get calendar set to current date and Time
		Calendar cal = Calendar.getInstance();
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date StartDate = null;
		
		if(Date != null){
			try {
				//creates Date from String Date
				StartDate = df.parse(Date);
				cal.setTime(StartDate); //sets the Time from the Date
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			//set the calendar to Monday of the current week
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			StartDate = cal.getTime(); //gets current Date
		}
		ArrayList<String> Dates = new ArrayList();
		DateFormat df2 = new SimpleDateFormat("EEEE MM/dd/yyyy");
		//spans 7 days out of the week
		for(int i = 0; i < 7; i++){
			String date = df2.format(cal.getTime());
			//System.out.println(date);
			cal.add(Calendar.DATE, 1);
			
			Dates.add(date);
		}
		//Prints date in the specified format
		//cal.add(Calendar.DATE, 6);
		//Date EndDate = cal.getTime();
		//System.out.println(df.format(EndDate));
		
		//String[] Dates = {df2.format(StartDate), df2.format(EndDate)};
		
		return Dates;
	}
	public static String formatDate(String Date){
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date formattedDate = null;
		try {
			formattedDate = df.parse(Date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String date = df.format(formattedDate);
		
		return date;
		
	}
	public static String getTimeFromTimePicker(TimePicker tp, String Date){
		
		Calendar cal = setCalendar(Date);
		cal.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
		cal.set(Calendar.MINUTE, tp.getCurrentMinute());
		cal.set(Calendar.SECOND, 0);
		//cal.set(Calendar.SECOND, tp.getcur());
		Date date = cal.getTime();
        String time = new SimpleDateFormat(TimeFormat).format(date);
        
		return time;
		
	}
	public static Date getDateFromTime(String time){
		SimpleDateFormat df = new SimpleDateFormat("hh:mm");
		Date d = null;
		try {
			d = df.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		//get calendar set to current date and Time
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(d);
//		cal.add(Calendar.HOUR, 0);
//		
//		Date finalDate = cal.getTime();
//        time = new SimpleDateFormat(TimeFormat).format(finalDate);
        
		return d;
	}


}
