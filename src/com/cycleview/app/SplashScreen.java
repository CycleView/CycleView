package com.cycleview.app;

import com.cycleview.app.workers.PhotoHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity{

	private static int timeout = 2000;

	protected void onCreate(Bundle savedInstanceState) {
		long initial = System.currentTimeMillis(); 
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		
		// Delete photos that have expired
		PhotoHandler.deleteOldPhotos();

		long remainingWaitTime = timeout - (System.currentTimeMillis() - initial);
		if (remainingWaitTime < 0)
			remainingWaitTime = 0;
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashScreen.this, WifiScreen.class));
				finish();
			}
		}, remainingWaitTime);
	}
}
