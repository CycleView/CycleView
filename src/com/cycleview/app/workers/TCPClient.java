package com.cycleview.app.workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.cycleview.app.CameraScreen;
import com.cycleview.app.Constants;

import android.util.Log;

public class TCPClient extends Thread {

	Socket socket;
	CameraScreen cameraScreen;
	
	public TCPClient(CameraScreen cameraScreen) {
		this.cameraScreen = cameraScreen;
	}

	public void run() {
		Log.v("CYCLEVIEW", "LOL");
		InetAddress serverAddr = null;

		try {
			serverAddr = InetAddress.getByName(Constants.IP);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				socket = new Socket(serverAddr, Constants.PORT);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));

				while (!Thread.interrupted()) {
					String read = in.readLine();
					if (read != null)
						cameraScreen.showDanger();
					else {
						Log.v("CYCLEVIEW", "Server went down");
						break;
					}
				}
			} catch (IOException e) {
				Log.v("CYCLEVIEW", "Connection refused, trying again");
			} finally {
				try {
					if (socket != null)
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
