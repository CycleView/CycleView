package com.cycleview.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

public class WifiScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifiscreen);

		final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		
		if (wifiInfo != null) {
			String name = wifiInfo.getSSID();
			
			if (name != null && name.equalsIgnoreCase(Constants.SSID))
				wifiReady();	
		}
		
		// register for wifi change detection
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent)) {
					WifiInfo newWifiInfo = wifiManager.getConnectionInfo();
					if (newWifiInfo != null && newWifiInfo.getSSID().equalsIgnoreCase(Constants.SSID))
						wifiReady();
				}
			}
		}, intentFilter);
	}
	
	protected void wifiReady() {
		startActivity(new Intent(WifiScreen.this, CameraScreen.class));
	}
}