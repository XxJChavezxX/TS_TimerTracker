package com.tricellsoftware.timetrackertestapp.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.tricellsoftware.timetrackertestapp.DTOs.CompanyDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.ProfileDTO;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;
import com.tricellsoftware.timetrackertestapp.businessLogic.BusinessLogic;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileCreatorHelper {
	private BusinessLogic logic = null;
	Context context;
	List<TimeLogDTO> timelogs = null;
	ArrayList<String> Dates = null;
	public String CreateFile(Context context, ProfileDTO profile, CompanyDTO company, String date){
		
		this.context = context;
		String FileName = "TimeSheet.xls";
		File filePathDirectory = null;
		if(isExternalStorageWritable()){
			
			File sdCard = Environment.getExternalStorageDirectory();//gets sd card
			filePathDirectory = new File (sdCard.getAbsolutePath() + "/TimeTracker/Files"); //adds the Timetracker/Files directory
			if(!filePathDirectory.exists()){
				filePathDirectory.mkdirs(); // creates directory is it does not exist
			}
	   		File file = new File(filePathDirectory, FileName);
			try {
				//file = context.openFileOutput(FileName,Context.MODE_PRIVATE);
				logic = new BusinessLogic(context);
				logic.getUser(1);
				
				createFile(file, profile, company, date, String.valueOf(profile.getCurrentCompany()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
		return filePathDirectory+"/"+FileName;
	}
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	    	 Log.d("Carburant", "Sdcard can read/write !!" ); 
	        return true;
	    }
	    return false;
	}
	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public void createFile(File file, ProfileDTO profile, CompanyDTO company, String date, String companyid) throws IOException{
		
		 try {
				
				
				Dates = TimeHelper.spamSevenDatesByStartDate(date);
				/**Create jxl and Timelogs Lists to create a jxl xls file**/
	            WritableWorkbook workBook = Workbook.createWorkbook(file);
	 
	            WritableSheet sheet = workBook.createSheet(
	                    "Sheet1", 0);
	            
	            
	            WritableFont fontCell = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
	            fontCell.setColour(Colour.WHITE);
	            WritableCellFormat fontCellformat = new WritableCellFormat (fontCell);
	            fontCellformat.setBackground(Colour.GRAY_50);
	            fontCellformat.setAlignment(Alignment.CENTRE);
	            fontCellformat.setBorder(Border.ALL, BorderLineStyle.THIN);
	            
	            WritableFont fontCell2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
	            //fontCell2.setColour(Colour.AQUA);
	            WritableCellFormat fontCellformat2 = new WritableCellFormat (fontCell2);
	           // fontCellformat2.setBackground(Colour.WHITE);
	            fontCellformat2.setAlignment(Alignment.CENTRE);
	            fontCellformat2.setBorder(Border.ALL, BorderLineStyle.THIN);
	            
	            WritableCellFormat fontCellformat3 = new WritableCellFormat (fontCell2);
		        fontCellformat3.setBackground(Colour.LIME);
		        fontCellformat3.setAlignment(Alignment.CENTRE);
		        fontCellformat3.setBorder(Border.ALL, BorderLineStyle.THIN);

		        
	            //Add the created Cells to the sheet
	            int height = 400;
	            int width = 20;
	            int width2 = 15;
	    		//spans 7 days out of the week
    			int breakTime = 0;
    			int totalTime = 0;
	            int totalHours = 0;
				String endTime = null;
				String startTime = null;
				String strTotalTime;
	            int mins = 0;
	    		for (int i = 0; i <= Dates.size()-1; i++) {
	    			String CurrentDate = Dates.get(i);
	    			String currentDay[] = CurrentDate.split(" ");
	    			
	    			String Day = currentDay[0];
	    			String Date = currentDay[1];
	    			int row = i+1;
	    			/**Get Timelogs by date**/
	    			timelogs = new ArrayList<TimeLogDTO>();
	    			timelogs = logic.getAllTimeLogsByDate(Date, companyid);
	    			int count = timelogs.size();
	    			Label Daylbl = new Label(row, 0, Day, fontCellformat);
	    			Label Datelbl = new Label(row, 1, Date, fontCellformat2);
	    			Label StartTimelbl = null;
	    			Label EndTimelbl = null;
	    			Label Breakslbl = new Label(row, 4, String.valueOf(breakTime), fontCellformat2);
    				Label Totallbl = new Label(row, 6, String.valueOf(totalTime), fontCellformat2);
    				String empty = "--";
    				/**Span information depending on number of timelogs**/
	    			if(count == 0) //if there's no timelog
	    			{
	    				StartTimelbl = new Label(row, 2, "", fontCellformat2);
	    				EndTimelbl = new Label(row, 3, "", fontCellformat2);
	    				mins = 0;
	    				totalTime = 0;
	    				Breakslbl = new Label(row, 4, empty, fontCellformat2);//TimeHelper.displayHoursandMinutes(String.valueOf(breakTime)), fontCellformat2);
	    				Totallbl = new Label(row, 6, empty, fontCellformat2);//TimeHelper.displayHoursandMinutes(String.valueOf(totalTime)), fontCellformat2);
	    			}	
	    			else if(count > 1){ //if there's more than one 

	    				StartTimelbl = new Label(row, 2, timelogs.get(0).getStartTime(), fontCellformat2);
	    				EndTimelbl = new Label(row, 3, timelogs.get(count-1).getEndTime(), fontCellformat2);
	    				mins = 0;
	    				totalTime = 0;
	    				breakTime = 0;
	    				String nextlogEndTime = null;
	    				for(int j = 0; j <= count-1; j++){ //for each timelog get the EndTime of one log and the StartTime of the next log to calculate break time
		    				try {
		    					if(j < count-1){ 
		    						endTime = timelogs.get(j).getEndTime();
		    						startTime = timelogs.get(j+1).getStartTime();
		    						nextlogEndTime = timelogs.get(j+1).getEndTime(); //stores next log end time, should be "--", which is expected
		    						if(!endTime.equals(empty))
		    						{
		    							breakTime = breakTime + Integer.parseInt(TimeHelper.getTimeDiffInMinutes(endTime, startTime));
		    						}
		    						
		    					}
			    				mins = Integer.parseInt(timelogs.get(j).getMinutes());
			    				totalTime = totalTime + mins;
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    				}
	    				if(nextlogEndTime.equals(empty)){
	    					totalTime = 0;
	    					Breakslbl = new Label(row, 4, empty, fontCellformat2);
	    					Totallbl = new Label(row, 6, empty, fontCellformat2);
	    				}
	    				else{
	    					Breakslbl = new Label(row, 4, TimeHelper.displayHoursandMinutes(String.valueOf(breakTime)), fontCellformat2);
	    					Totallbl = new Label(row, 6, TimeHelper.displayHoursandMinutes(String.valueOf(totalTime)), fontCellformat2);
	    				}
	    				
	    				
	    			}
	    			else{ //if there's one time log
	    				totalTime = 0;
	    				endTime = timelogs.get(0).getEndTime();
						startTime = timelogs.get(0).getStartTime();

	    				StartTimelbl = new Label(row, 2, startTime, fontCellformat2);
	    				EndTimelbl = new Label(row, 3, endTime, fontCellformat2);
	    				
	    				if(endTime.contentEquals("--")){ //if end time is empty 
	    					mins = 0;
	    				}
	    				else
	    					mins = Integer.parseInt(timelogs.get(0).getMinutes());
	    				
	    				totalTime = totalTime + mins;
	    				breakTime = 0;
	    				//if(breakTime == 0){
	    				//	Breakslbl = new Label(row, 4, empty, fontCellformat2);
	    			//	}
	    				//else
	    				Breakslbl = new Label(row, 4, empty, fontCellformat2); //There's no breaks if one log is displayed so we use the empty string
	    				if(totalTime == 0){
	    					Totallbl = new Label(row, 6, empty, fontCellformat2);
	    				}
	    				else
	    					Totallbl = new Label(row, 6, TimeHelper.displayHoursandMinutes(String.valueOf(totalTime)), fontCellformat2);
	    				
	    			}

	    			
	    			//Breakslbl = new Label(i, 4, String.valueOf(breakTime), fontCellformat2);
    				//Totallbl = new Label(i, 6, String.valueOf(totalTime), fontCellformat2);
	    			
	    		   // c.add(Calendar.DATE, 1);
	    		    
	    		    sheet.addCell(Daylbl);
	    		    sheet.addCell(Datelbl);
	    		    sheet.addCell(StartTimelbl);
	    		    sheet.addCell(EndTimelbl);
	    		    sheet.addCell(Breakslbl);
	    		    sheet.addCell(Totallbl);
	    		    
		            sheet.setColumnView(row, width);
		            
		            //add up total hours
		            totalHours = totalHours + totalTime;
	    		}
	    	    /** predefined Rows and Columns **/
	    		//get calendar set to current date and Time
	    		Calendar cal = Calendar.getInstance();
	    		
	    		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    		// Get calendar set to current date and time
	    		//Calendar cal = Calendar.getInstance();
	   			Label Namelbl = new Label(1, 8, profile.getFirstName() + " " + profile.getLastName(), fontCellformat2);
	   			Label CurrentDatelbl = new Label(1, 9,  df.format(cal.getTime()), fontCellformat2);
	   			Label Companylbl = new Label(1, 10, company.getName(), fontCellformat2);
	   			
	   			sheet.addCell(Namelbl);
	   		    sheet.addCell(CurrentDatelbl);
	   		    sheet.addCell(Companylbl);
	    		/**Total Hours labels**/
	   		    Label lbl = new Label(8, 0, "Total Hours",fontCellformat);
		        sheet.setColumnView(8, width);
		        sheet.addCell(lbl);
		        Label lbl2 = new Label(8, 6, TimeHelper.displayHoursandMinutes(String.valueOf(totalHours)), fontCellformat3);
	    		sheet.addCell(lbl2);
		        //sheet.setColumnView(8, width);
   
	            //for loop 
	            Label label9 = new Label(0, 1, "Date",fontCellformat);
	            Label label10 = new Label(0, 2, "Start Time",fontCellformat);
	            Label label11 = new Label(0, 3, "End Time",fontCellformat);
	            Label label12 = new Label(0, 4, "Breaks",fontCellformat);
	            Label label13 = new Label(0, 5, "",fontCellformat);
	            Label label14 = new Label(0, 6, "Total ",fontCellformat);
	            Label label15 = new Label(0, 8, "Name: ",fontCellformat);
	            Label label16 = new Label(0, 9, "Date: ",fontCellformat);
	            Label label17 = new Label(0, 10, "Company: ",fontCellformat);
	        
	       	 	sheet.setRowView(0, height); //set 1st row height
	            sheet.setRowView(1, height); //set 2st row height
	            sheet.setColumnView(1, width); //set row width
	            sheet.setColumnView(0, width2); //set row width
	            
	            
	            sheet.addCell(label9);
	            sheet.setRowView(1, height);
	            //sheet.setColumnView(0, height);
	            sheet.addCell(label10);
	            sheet.setRowView(2, height);
	           // sheet.setColumnView(0, height);
	            sheet.addCell(label11);
	            sheet.setRowView(3, height);
	           // sheet.setColumnView(0, height);
	            sheet.addCell(label12);
	            sheet.setRowView(4, height);
	          //  sheet.setColumnView(0, height);
	            sheet.addCell(label13);
	            sheet.setRowView(5, height);
	           // sheet.setColumnView(0, height);
	            sheet.addCell(label14);
	            sheet.setRowView(6, height);
	           // sheet.setRowView(6, width2);
	            //sheet.setColumnView(0, height);
	            sheet.addCell(label15);
	            sheet.setRowView(8, height);
	           // sheet.setColumnView(0, height);
	            sheet.addCell(label16);
	            sheet.setRowView(9, height);
	           // sheet.setColumnView(0, height);
	            sheet.addCell(label17);
	            sheet.setRowView(10, height);
	           // sheet.setColumnView(0, height);
	 
	            //Write and close the workbook
	            workBook.write();
	            workBook.close();
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (RowsExceededException e) {
	            e.printStackTrace();
	        } catch (WriteException e) {
	            e.printStackTrace();
	        }
	    }

}
