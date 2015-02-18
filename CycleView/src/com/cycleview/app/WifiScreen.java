package com.cycleview.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.SupplicantState;
import android.os.Bundle;
import android.os.Debug;
import android.widget.Toast;

public class WifiScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifiscreen);

		final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		if (wifiInfo != null) {
			String name = wifiInfo.getSSID().replace("\"", "");

			if (name != null && name.equalsIgnoreCase(Constants.SSID))
				wifiReady();
		}

		// register for wifi change detection
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent
						.getAction())) {
					NetworkInfo nwInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
					// This implies the WiFi connection is through
					if (NetworkInfo.State.CONNECTED.equals(nwInfo.getState())) {
						WifiInfo newWifiInfo = wifiManager.getConnectionInfo();
						if (newWifiInfo != null
								&& newWifiInfo.getSSID().replace("\"", "").equalsIgnoreCase(
										Constants.SSID))
							wifiReady();
					}
				}
			}
		}, intentFilter);
	}

	protected void wifiReady() {
		startActivity(new Intent(WifiScreen.this, CameraScreen.class));
	}
}
