package com.tricellsoftware.timetrackertestapp.helper;

import java.util.List;

import com.tricellsoftware.timetrackertestapp.R;
import com.tricellsoftware.timetrackertestapp.DTOs.Status_Enum;
import com.tricellsoftware.timetrackertestapp.DTOs.TimeLogDTO;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<TimeLogDTO>{
	
   Context myContext;
	
    private final List<TimeLogDTO> itemsArrayList;
	public CustomArrayAdapter(Context context, int TexViewResourceID, List<TimeLogDTO> timelogs){
		super(context, TexViewResourceID, timelogs);
		// TODO Auto-generated constructor stub
		myContext = context;

		this.itemsArrayList = timelogs;
	}
	@Override
	public int getCount() {
	    return itemsArrayList.size();
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	 // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) myContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 2. Get rowView from inflater
        View view = inflater.inflate(R.layout.timelog_customlist_fragment, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) view.findViewById(R.id.txtDatelog);
        TextView valueView = (TextView) view.findViewById(R.id.txtStatus);

        TextView StartTimeView = (TextView) view.findViewById(R.id.txtStartTime);
        TextView EndTimeView = (TextView) view.findViewById(R.id.txt);
        TextView HoursView = (TextView) view.findViewById(R.id.txtHours);
        
        TimeLogDTO item = itemsArrayList.get(position);
        
        //split the start time to just display the hours also avaliable item.getStartTime().substring()
        String[] startTimes = item.getStartTime().split(" ");
        String strTime = null;
        if(startTimes.length > 2){
        	strTime = startTimes[1] +" "+ startTimes[2];
        }
        else
        	strTime = startTimes[0] +" "+startTimes[1];
        String ndTime = null;
        if(!item.getEndTime().equals("--")){     
	        //split the end time to just display the hours 
	        String[] endTimes = item.getEndTime().split(" ");
	       
	        if(endTimes.length > 2){
	        	ndTime = endTimes[1] +" "+endTimes[2];
	        }
	        else
	        	ndTime = endTimes[0] +" "+endTimes[1];
	        
	        EndTimeView.setText(ndTime);
        }
        else
        	EndTimeView.setText(item.getEndTime());
        // 4. Set the text for textView
        labelView.setText(item.getDate());
        StartTimeView.setText(strTime);
        
        HoursView.setText(TimeHelper.displayHoursandMinutes(item.getMinutes()));
        if(item.getStatusID() == Status_Enum.In.getValue()){
        	 valueView.setText("Last Status: "+ Status_Enum.In.toString());
        }
        else if(item.getStatusID() == Status_Enum.Out.getValue()){
        	 valueView.setText("Last Status: " + Status_Enum.Out.toString());
        }

        // 5. retrn rowView
        return view;
    }

}
