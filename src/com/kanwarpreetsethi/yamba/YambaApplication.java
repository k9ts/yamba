package com.kanwarpreetsethi.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener{

	private static final String TAG = YambaApplication.class.getSimpleName();
	private SharedPreferences prefs;
	Twitter twitter;
	private boolean serviceRunning = false;
	
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

	
}
