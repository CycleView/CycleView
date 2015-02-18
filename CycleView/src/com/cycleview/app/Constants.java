package com.cycleview.app;

import java.io.File;

import android.os.Environment;

public class Constants {

	final static String SSID = "CycleView";
	final static String IP = "192.168.42.1";
	final static String path = "";
	final static String appDirectoryName = "CycleView";
	final static File imageRoot = new File(
			Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
			appDirectoryName);
	final static int expirationDate = 1;
}
