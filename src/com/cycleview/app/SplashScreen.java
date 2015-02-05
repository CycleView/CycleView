package com.cycleview.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity{

	private static int timeout = 2000;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashScreen.this, WifiScreen.class));
				finish();
			}
		}, timeout);
	}
}
