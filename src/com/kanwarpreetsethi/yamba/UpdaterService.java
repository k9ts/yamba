package com.kanwarpreetsethi.yamba;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {

	static final String TAG = "UpdaterService";
	static final int DELAY = 60000;
	private boolean runflag = false;
	private Updater updater;
	private YambaApplication yamba;
	SQLiteDatabase db;
	
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
		
		public Updater() {
			super("UpdaterService-Updater");
			
		}
		
		@Override
		public void run() {
			UpdaterService updaterService = UpdaterService.this;
			while (updaterService.runflag) {
				try {
					Log.d(TAG, "Updater running");
					
					int newUpdates = updaterService.yamba.fetchStatusUpdates(); 
					if (newUpdates > 0) { 
					Log.d(TAG, "We have a new status");
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
