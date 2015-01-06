package com.example.sharedpreferance;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class ThridActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statuse);
		((TextView)findViewById(R.id.textview)).setText("Welcome "+PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("name", "Not Logged in"));
	}


}
