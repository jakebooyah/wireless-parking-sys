package com.example.userinterface;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	View v; //View used as a parameter to actionbar method being called
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_grid);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_actionbar, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//Handle presses on the action bar items
		switch(item.getItemId()){
		case R.id.action_search:
			//openSearch();
			return true;
		case R.id.action_settings:
			signalSettings(v);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void signalMap(View view){
		Intent intent = new Intent(this, MapActivity.class);
	    startActivity(intent);  
	}
	
	public void signalSettings(View view){
		Intent intent = new Intent(this, SettingsActivity.class);
	    startActivity(intent);  
	}

}
