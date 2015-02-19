package com.cycleview.app;

import java.io.File;
import android.os.Environment;

public class Constants {

	public final static String SSID = "CycleView";
	public final static String IP = "192.168.42.1";
	public final static int PORT = 5001;
	
	public final static String appDirectoryName = "CycleView";
	public final static File imageRoot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appDirectoryName);
	public final static int expirationDays = 1;
}
