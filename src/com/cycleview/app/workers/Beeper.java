package com.cycleview.app.workers;

import android.content.Context;
import android.media.MediaPlayer;

import com.cycleview.app.R;

public class Beeper {
	
	private MediaPlayer mp;
	
	public Beeper(Context context) {
		mp = MediaPlayer.create(context, R.raw.beep);
	}

	public void beep() {
		mp.start();
	}
}
