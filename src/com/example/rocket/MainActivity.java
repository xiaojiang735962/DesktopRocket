package com.example.rocket;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void startRocket(View v){
		startService(new Intent(this, RocketService.class));
		finish();
	}
	public void stopRocket(View v){
		stopService(new Intent(this, RocketService.class));
		finish();
	}
}
