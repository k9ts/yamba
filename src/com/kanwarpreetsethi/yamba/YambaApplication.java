package com.kanwarpreetsethi.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener{

	private SharedPreferences prefs;
	Twitter twitter;
	private boolean serviceRunning = false;
	private StatusData statusData;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		twitter = null;
		
	}
	
	public synchronized Twitter getTwitter() {
    	if (twitter == null) {
    		String username, password, apiRoot;
    		username = prefs.getString("username", "");
    		password = prefs.getString("password", "");
    		apiRoot = prefs.getString("apiRoot", "");
    		
    		twitter = new Twitter(username, password);
    		twitter.setAPIRootUrl(apiRoot);
    	}
    	
    	return twitter;
    }
	
	public boolean isServiceRunning() {
		return serviceRunning;
	}
	
	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}

	public StatusData getStatusData() {
		
		if (statusData == null)
			statusData = new StatusData(this);
		
		return statusData;
	}
	
	public synchronized int fetchStatusUpdates() {
		Twitter twitter = this.getTwitter();
		try {
			List<Twitter.Status> timeline = twitter.getFriendsTimeline();
			long latestStatusCreatedAtTime = this.getStatusData()
					.getLatestStatusCreatedAtTime();
			int count = 0;
			ContentValues values = new ContentValues();
			for (Twitter.Status status : timeline) {
				values.clear();
				values.put(StatusData.C_ID, status.id);
				values.put(StatusData.C_CREATED_AT, status.createdAt.getTime());
				values.put(StatusData.C_TEXT, status.text);
				values.put(StatusData.C_USER, status.user.name);
				long createdAt = status.getCreatedAt().getTime();
				
				this.getStatusData().insertOrIgnore(values);
				if (latestStatusCreatedAtTime < createdAt) {
					count++; 
				}
			}
			return count;
			
		} catch (RuntimeException t) {
			System.err.println(t);
			return 0;
		}
	}
	
}
