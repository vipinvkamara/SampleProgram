package com.example.facebookupdate;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
	}

	public void myClick(View v){

		System.out.println("DAta is Display");
		Toast.makeText(this, "DATA IS ENTERED", Toast.LENGTH_LONG).show();
	}
	public void myCancel(View v){

		System.out.println("DAta is Display");
		Toast.makeText(this, "DATA IS CANCELED", Toast.LENGTH_LONG).show();

	}
}