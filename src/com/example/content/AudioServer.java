package com.example.content;

import com.bairuitech.anychat.AnyChatCoreSDK;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AudioServer extends Service {
	private AnyChatCoreSDK anychat;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
}
