package com.jeremy.basketballclipboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableRow;
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
		// format the NumberPickers
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
			// add new StatSheet to options menu
		case R.id.action_openStatSheet:
			goToStatSheetOpener();
			return true;
		case R.id.action_saveSheet:
			try {
				saveStatSheet();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	private void formatNumberPickers() {
		// format the points number picker
		for (int i = 1; i <= 12; i++) {
			int pointsId = getResources().getIdentifier("pointsCell" + i, "id",
					getPackageName());
			NumberPicker pointsPicker = (NumberPicker) findViewById(pointsId);
			pointsPicker.setMinValue(MIN_POINTS);
			pointsPicker.setMaxValue(MAX_POINTS);
		}
		// format the assists number picker
		for (int i = 1; i <= 12; i++) {
			int assistsId = getResources().getIdentifier("assistsCell" + i,
					"id", getPackageName());
			NumberPicker assistsPicker = (NumberPicker) findViewById(assistsId);
			assistsPicker.setMinValue(MIN_POINTS);
			assistsPicker.setMaxValue(MAX_POINTS);
		}
		// format the rebounds number picker
		for (int i = 1; i <= 12; i++) {
			int reboundsId = getResources().getIdentifier("reboundsCell" + i,
					"id", getPackageName());
			NumberPicker reboundsPicker = (NumberPicker) findViewById(reboundsId);
			reboundsPicker.setMinValue(MIN_POINTS);
			reboundsPicker.setMaxValue(MAX_POINTS);
		}
		// format the steals number picker
		for (int i = 1; i <= 12; i++) {
			int stealsId = getResources().getIdentifier("stealsCell" + i, "id",
					getPackageName());
			NumberPicker stealsPicker = (NumberPicker) findViewById(stealsId);
			stealsPicker.setMinValue(MIN_POINTS);
			stealsPicker.setMaxValue(MAX_POINTS);
		}
	}

	public void saveStatSheet() throws IOException {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if (mExternalStorageAvailable == true
				&& mExternalStorageWriteable == true) {
			EditText gameNameBox = (EditText) findViewById(R.id.gameName);
			// This will get the SD Card directory and create a folder named
			// BasketballAssistant in it.
			File sdCard = Environment.getExternalStorageDirectory();
			File directory = new File(sdCard.getAbsolutePath()
					+ "/BasketballAssistant/StatSheets");
			directory.mkdirs();
			// Now create the file in the above directory and write the contents
			// into it
			File file = new File(directory, gameNameBox.getText().toString()
					+ ".txt");
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			// String fileName = file.getName();
			if (!file.exists()) {
				file.createNewFile();
			}
			for(int i = 1; i <= 12; i++){
				int rowsId = getResources().getIdentifier("statsRow" + i, "id",
						getPackageName());
				TableRow statsRows = (TableRow) findViewById(rowsId);
				EditText playerCell = (EditText) statsRows.getChildAt(0);
				NumberPicker pointsCell = (NumberPicker) statsRows.getChildAt(1);
				NumberPicker assistsCell = (NumberPicker) statsRows.getChildAt(2);
				NumberPicker rebsCell = (NumberPicker) statsRows.getChildAt(3);
				NumberPicker stealsCell = (NumberPicker) statsRows.getChildAt(4);
				//get the text from each view
				String sPlayer = playerCell.getText().toString();
				String sPoints = String.valueOf(pointsCell.getValue());
				String sAssists = String.valueOf(assistsCell.getValue());
				String sRebs = String.valueOf(rebsCell.getValue());
				String sSteals = String.valueOf(stealsCell.getValue());
				//write the information taken from the StatSheet to the new file created.
				osw.write(sPlayer + "$");
				osw.write(sPoints + "$");
				osw.write(sAssists + "$");
				osw.write(sRebs + "$");
				osw.write(sSteals + "$\n");
			}
			osw.flush();
			osw.close();
		}

	}
	
	public void goToStatSheetOpener(){
		Intent intent = new Intent(this, OpenStatSheetActivity.class);
		startActivity(intent);
	}

}
