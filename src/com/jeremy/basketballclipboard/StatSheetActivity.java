package com.jeremy.basketballclipboard;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.support.v4.app.NavUtils;

public class StatSheetActivity extends Activity {

	private final int MIN_POINTS = 0;
	private final int MAX_POINTS = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stat_sheet);
		// Show the Up button in the action bar.
		setupActionBar();
		//format the NumberPickers
		formatNumberPickers();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stat_sheet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
			//add new StatSheet to options menu
		case R.id.action_newSheet:
			//newStatSheet();
			return true;
			//add save StatSheet to options menu
		case R.id.action_saveSheet:
			//save();
			return true;
		
		}
		return super.onOptionsItemSelected(item);
	}

	private void formatNumberPickers() {
		// points
		for (int i = 1; i <= 12; i++) {
			int pointsId = getResources().getIdentifier("pointsCell" + i, "id",
					getPackageName());
			NumberPicker pointsPicker = (NumberPicker) findViewById(pointsId);
			pointsPicker.setMinValue(MIN_POINTS);
			pointsPicker.setMaxValue(MAX_POINTS);
		}
		// assists
		for (int i = 1; i <= 12; i++) {
			int assistsId = getResources().getIdentifier("assistsCell" + i,
					"id", getPackageName());
			NumberPicker assistsPicker = (NumberPicker) findViewById(assistsId);
			assistsPicker.setMinValue(MIN_POINTS);
			assistsPicker.setMaxValue(MAX_POINTS);
		}
		// rebounds
		for (int i = 1; i <= 12; i++) {
			int reboundsId = getResources().getIdentifier("reboundsCell" + i,
					"id", getPackageName());
			NumberPicker reboundsPicker = (NumberPicker) findViewById(reboundsId);
			reboundsPicker.setMinValue(MIN_POINTS);
			reboundsPicker.setMaxValue(MAX_POINTS);
		}
		// steals
		for (int i = 1; i <= 12; i++) {
			int stealsId = getResources().getIdentifier("stealsCell" + i, "id",
					getPackageName());
			NumberPicker stealsPicker = (NumberPicker) findViewById(stealsId);
			stealsPicker.setMinValue(MIN_POINTS);
			stealsPicker.setMaxValue(MAX_POINTS);
		}
	}
}
