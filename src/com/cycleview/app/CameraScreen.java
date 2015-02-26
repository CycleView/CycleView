package com.cycleview.app;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cycleview.app.R;
import com.cycleview.app.workers.Beeper;
import com.cycleview.app.workers.PhotoHandler;
import com.cycleview.app.workers.TCPClient;
import com.gstreamer.*;

public class CameraScreen extends Activity implements SurfaceHolder.Callback,
		MediaScannerConnectionClient {

	private TCPClient tcpThread;
	private Beeper beeper;
	private SurfaceView sv;
	private Button botaoVerImagens;

	private MediaScannerConnection conn;

	public String[] allFiles;
	private String SCAN_PATH;
	private static final String FILE_TYPE = "image/*";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize GStreamer and warn if it fails
		try {
			GStreamer.init(this);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		setContentView(R.layout.camerascreen);

		/*
		 * TextView tx = (TextView)findViewById(R.id.tv_menu); Typeface
		 * custom_font = Typeface.createFromAsset(getAssets(),
		 * "fonts/BLANCH_CAPS-webfont.ttf"); tx.setTypeface(custom_font);
		 */

		// Comunication with Base
		tcpThread = new TCPClient(this);
		tcpThread.start();

		// Beeper
		beeper = new Beeper(this.getApplicationContext());

		final View settingsLayout = (View) this
				.findViewById(R.id.layout_settings);
		((Button) this.findViewById(R.id.button_settings))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (settingsLayout.getVisibility() == View.GONE) {
							settingsLayout.setVisibility(View.VISIBLE);
							((Button) v).setText(R.string.hide_settings);
						} else {
							settingsLayout.setVisibility(View.GONE);
							((Button) v).setText(R.string.show_settings);
						}
					}
				});

		sv = (SurfaceView) this.findViewById(R.id.surface_video);
		sv.getHolder().addCallback(this);

		nativeInit();
		nativePlay();

		botaoVerImagens = (Button) findViewById(R.id.bt_ver_imagens_galeria);

		botaoVerImagens.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				/*
				 * File mediaStorageDir = new
				 * File(Environment.getExternalStoragePublicDirectory
				 * (Environment.DIRECTORY_PICTURES),
				 * Constants.appDirectoryName); // This location works best if
				 * you want the created images to be shared // between
				 * applications and persist after your app has been uninstalled.
				 * 
				 * // Create the storage directory if it does not exist
				 * Constants.appDirectoryName =
				 * Environment.getExternalStorageDirectory().toString(); if (!
				 * mediaStorageDir.exists()){ if (! mediaStorageDir.mkdirs()){
				 * Log.d("MyCameraApp", "failed to create directory"); } }
				 */

				/*
				 * CameraScreen cs = new CameraScreen(); cs.buttonGallery1(v);
				 */
				final String path = android.os.Environment.DIRECTORY_PICTURES;

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("content://media/internal/images/media/CYCLEVIEW/"));
				startActivityForResult(intent, 0);
			}
		});

	}

	public void buttonGallery(View v) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivity(intent);
	}

	public void buttonGallery1(View v) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.setType("/CYCLEVIEW/*");
		startActivity(intent);
	}

	public void showDanger() {
		Log.v("CYCLEVIEW", "Danger!!");

		beeper.beep();

		Bitmap bitmap = Bitmap.createBitmap(sv.getMeasuredWidth(),
				sv.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		sv.layout(0, 0, sv.getMeasuredWidth(), sv.getMeasuredHeight());
		sv.draw(canvas);
		PhotoHandler.savePhoto(bitmap, this.getApplicationContext());
	}

	// Initialize native code, build pipeline, etc
	private native void nativeInit();

	// Destroy pipeline and shutdown native code
	private native void nativeFinalize();

	// Set pipeline to PLAYING
	private native void nativePlay();

	// Set pipeline to PAUSED
	private native void nativePause();

	// Initialize native class: cache Method IDs for callbacks
	private static native boolean nativeClassInit();

	private native void nativeSurfaceInit(Object surface);

	private native void nativeSurfaceFinalize();

	// Native code will use this to keep private data
	private long native_custom_data;

	protected void onDestroy() {
		nativeFinalize();
		super.onDestroy();
	}

	// Called from native code. This sets the content of the TextView from the
	// UI thread.
	private void setMessage(final String message) {
		final TextView tv = (TextView) this.findViewById(R.id.textview_status);
		runOnUiThread(new Runnable() {
			public void run() {
				tv.setText(message);
			}
		});
	}

	// Called from native code. Native code calls this once it has created its
	// pipeline and the main loop is running, so it is ready to accept commands.
	private void onGStreamerInitialized() {
		Log.i("GStreamer", "Gst initialized. Playing");
		nativePlay();
	}

	static {
		System.loadLibrary("gstreamer_android");
		System.loadLibrary("cycleview");
		nativeClassInit();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("GStreamer", "Surface changed to format " + format + " width "
				+ width + " height " + height);
		nativeSurfaceInit(holder.getSurface());
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("GStreamer", "Surface created: " + holder.getSurface());
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("GStreamer", "Surface destroyed");
		nativeSurfaceFinalize();
	}

	@Override
	public void onMediaScannerConnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		// TODO Auto-generated method stub

	}

}
