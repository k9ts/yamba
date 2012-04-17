package com.kanwarpreetsethi.yamba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	YambaApplication yamba;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		yamba = (YambaApplication) getApplication();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemPrefs:
			this.startActivity(new Intent(this, PrefsActivity.class));
			break;
		case R.id.itemServiceStart:
			this.startService(new Intent(this, UpdaterService.class));
			break;
		case R.id.itemServiceStop:
			this.stopService(new Intent(this, UpdaterService.class));
			break;
		case R.id.itemTimeline:
			startActivity(new Intent(this, TimelineActivity.class)); 
			break;
		case R.id.itemStatus:
			startActivity(new Intent(this, StatusActivity.class)); 
			break;
		}
		
		return true;
	}

}
