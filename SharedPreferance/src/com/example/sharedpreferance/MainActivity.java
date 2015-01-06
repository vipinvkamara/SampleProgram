package com.example.sharedpreferance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public  void OnLogin(View v) {
		String usernsme_login=((EditText)findViewById(R.id.editText1)).getText().toString();
		String password_login=((EditText)findViewById(R.id.editText2)).getText().toString();

		SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String usernsme_saved=sp.getString("name", "");
		String password_saved=sp.getString("password", "");

		if(usernsme_login.equalsIgnoreCase(usernsme_saved)&&(password_login.equalsIgnoreCase(password_saved))){
			Toast.makeText(this, "login fa11111111ils", Toast.LENGTH_LONG).show();
			sp.edit().putBoolean("status_is_true", true);
			startActivity(new Intent(this,ThridActivity.class));
			finish();			
		}
		else if(usernsme_saved.equalsIgnoreCase("")&&password_saved.equalsIgnoreCase("")){
			Toast.makeText(this, "login2222222222222 fails", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this,SecondActivity.class));
			finish();
		}
		else{
			Toast.makeText(this, "login fails", Toast.LENGTH_LONG).show();
		}
	}
	public void name() {
		
	}
}
