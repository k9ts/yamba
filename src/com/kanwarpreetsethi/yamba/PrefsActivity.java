package com.kanwarpreetsethi.yamba;

import android.os.Bundle;
import android.preference.PreferenceActivity;

	
public class PrefsActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.prefs);
		
		//this.getFragmentManager().beginTransaction().add(0, new PrefsFragment(), "prefs");
	}

	
	/*
	public static class PrefsFragment extends PreferenceFragment {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.addPreferencesFromResource(R.xml.prefs);
		}
		
	}
	*/
}

	