package com.cycleview.app.workers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.cycleview.app.Constants;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class PhotoHandler {
	
	public static void savePhoto(Bitmap bitmap, Context context) {
		if (bitmap == null)
			return;
		
		if (!Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).exists())
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).mkdir();
		if (!Constants.imageRoot.exists())
			Constants.imageRoot.mkdir();

		File imageFile = new File(Constants.imageRoot.getPath(), System.currentTimeMillis() + ".jpg");
		OutputStream fout = null;
		
		try {
		    fout = new FileOutputStream(imageFile);
		    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
		    fout.flush();
		    fout.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}

		Uri contentUri = Uri.fromFile(imageFile);
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
		context.sendBroadcast(mediaScanIntent);
	}
	
	public static void deleteOldPhotos() {
		File[] photos = null;
		if (Constants.imageRoot.exists())
			photos = Constants.imageRoot.listFiles();
		
		if (photos != null) {
			for (int i = 0; i < photos.length; i++)
				deleteSinglePhoto(photos[i].getPath());
		}
	}
	
	public static void deleteSinglePhoto(String path) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
		Date fileDate = null;
		
		try {
			ExifInterface intf = null;
			try {
			    intf = new ExifInterface(path);
			} catch(IOException e) {
			    e.printStackTrace();
			}

			String date = intf.getAttribute(ExifInterface.TAG_DATETIME);
			if (intf != null && date != null)
				fileDate = (Date) df.parse(date);
			else
				fileDate = new Date(new File(path).lastModified());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (fileDate != null) {
			Calendar expirationCal = Calendar.getInstance();
			expirationCal.add(Calendar.DAY_OF_YEAR, Constants.expirationDays);
			Date expirationDate = expirationCal.getTime();
			
			if (expirationDate.before(fileDate) && (new File(path).delete()))
				Log.v("CYCLEVIEW", path + " deleted");
		} else {
			Log.v("CYCLEVIEW", path + " not deleted");
		}
	}

}
