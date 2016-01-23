package com.soboapps.ohfark;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

public class LocationFound  extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_found);

		// get action bar
		ActionBar actionBar = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			actionBar = getActionBar();
		}

		// Enabling Up / Back navigation
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
}
