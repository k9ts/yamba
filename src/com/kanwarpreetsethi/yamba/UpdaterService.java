package com.kanwarpreetsethi.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {

	static final String TAG = "UpdaterService";
	static final int DELAY = 60000;
	private boolean runflag = false;
	private Updater updater;
	private YambaApplication yamba;
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.updater = new Updater();
		this.yamba = (YambaApplication) getApplication();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		this.runflag = true;
		this.yamba.setServiceRunning(true);
		this.updater.start();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
		this.updater.interrupt();
		this.updater = null;
		this.yamba.setServiceRunning(false);
	}

	

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	private class Updater extends Thread {
		List<Twitter.Status> timeline;
		
		public Updater() {
			super("UpdaterService-Updater");
			
		}
		
		@Override
		public void run() {
			UpdaterService updaterService = UpdaterService.this;
			while (updaterService.runflag) {
				try {
					Log.d(TAG, "Updater running");
					try {
						timeline = yamba.getTwitter().getFriendsTimeline();
					} catch (TwitterException t) {
						System.err.println(t);
					}
					for (Twitter.Status status : timeline) {
						Log.d(TAG, String.format("%s, %s", status.user.name, status.text));
					}
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					updaterService.runflag = false;
					System.err.println(e);
				}
			}
		}
	} //Updater 

}
