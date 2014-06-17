package com.tricellsoftware.timetrackertestapp.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class SendEmailHelper {
	
	public void SendEmailWithExcelFile(Context ctx, String to,String subject, String message, String fileAndLocation){
		try {
		System.out.println(fileAndLocation);
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		emailIntent.setType("application/excel");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{to});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		Uri uri = Uri.parse("file://" + fileAndLocation);
		emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
		ctx.startActivity(Intent
				.createChooser(emailIntent, "Send mail..."));
		//Toast.makeText(ctx, "Excel File was saved in the following location: My Files/All Files/Device storage" + fileAndLocation.toString(), Toast.LENGTH_LONG).show();
		
		
		} catch (Throwable t) {
			Toast.makeText(ctx, "Request failed: " + t.toString(),
			Toast.LENGTH_LONG).show();
		}
			
	}

}
