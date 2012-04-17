package com.kanwarpreetsethi.yamba;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class TimelineActivity extends BaseActivity {

	Cursor cursor;
	TextView textTimeline;
	StatusData statusData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline_basic);
		
		textTimeline = (TextView) findViewById(R.id.textTimeline);
		this.statusData = yamba.getStatusData();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		cursor = this.statusData.getStatusUpdates();
		startManagingCursor(cursor);
		
		String user, text, output;
		while (cursor.moveToNext()) { //
			user = cursor.getString(cursor.getColumnIndex(StatusData.C_USER)); //
			text = cursor.getString(cursor.getColumnIndex(StatusData.C_TEXT));
			output = String.format("%s: %s\n", user, text); //
			textTimeline.append(output);
		}
	}
}
