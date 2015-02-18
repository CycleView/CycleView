package com.cycleview.app;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.media.ExifInterface;

public class DeletingPictures extends ExifInterface{
	
	private String path;
	
	public DeletingPictures(String filename) throws IOException{
		super(filename);

	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public void deletingPhoto(){
		Calendar c = Calendar.getInstance(), cFile ;
		SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy hh:mm");
		//String formatada = df.format(c.getTime());
		Date dateFile = null, currentDate = null; long diff;
		
		try {
			dateFile = (Date) df.parse(super.getAttribute(TAG_DATETIME));
			currentDate = (Date) c.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (dateFile != null){
			
			cFile = Calendar.getInstance();
			cFile.setTime(dateFile);
			c.setTime(currentDate);
			diff = (c.getTimeInMillis() - cFile.getTimeInMillis())/(24 * 60 * 60 * 1000);
			
			if ((int)diff >= Constants.expirationDate){
				File f = new File(path);
				f.delete();
			}
		}
	}

}
