package com.jeremy.basketballclipboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.support.v4.app.NavUtils;

public class StatSheetActivity extends Activity {

	private final int MIN_POINTS = 0;
	private final int MAX_POINTS = 100;
	private TableRow statisticsRow;
	List<TableRow> tableRows = new ArrayList<TableRow>();

	// variables needed for opening a stat sheet
	boolean statSheetOpened = false;
	boolean newStatisticsSheet = false;
	String fileName;
	String teamName;
	File sdCard = Environment.getExternalStorageDirectory();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_stat_sheet);
			// Show the Up button in the action bar.
			setupActionBar();

			/******* Generate the statistics sheet opened by the user *******/
			// get the data sent from the OpenStatSheetActivity
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				statSheetOpened = extras.getBoolean("isFileOpened");
				fileName = extras.getString("fileName");
				teamName = extras.getString("teamName");
				newStatisticsSheet = extras.getBoolean("newStatisticsSheet");
			}
			// check that a stat sheet has actually been opened from the
			// OpenStatSheetActivity
			if (statSheetOpened == true) {
				// set the title of the game
				String fileNameWithoutExtension;
				fileNameWithoutExtension = fileName.substring(0,
						fileName.lastIndexOf('.'));
				// set the game name
				EditText statSheetTitle = (EditText) findViewById(R.id.gameName);
				statSheetTitle.setText(fileNameWithoutExtension);
				// set the team name
				EditText teamNameBox = (EditText) findViewById(R.id.teamName);
				teamNameBox.setText(teamName);
				// open a file stream to read from the text file which has the
				// contents of the practice
				File directory = new File(sdCard.getAbsolutePath()
						+ "/BasketballAssistant/StatSheets/" + fileName);

				BufferedReader br = new BufferedReader(
						new FileReader(directory));

				String strLine = null;
				// make sure a team name has been entered
				if (teamNameBox.getText().toString() != null) {
					strLine = br.readLine();
				}
				for (int i = 1; i <= 12; i++) {
					strLine = br.readLine();
					String[] statisticsArray = parseStatisticsFile(strLine);
					initializeOneStatisticsRow();
					setPlayerStats(R.id.statsSheetTableLayout, i, statisticsArray);
				}
				br.close();
			}
			else{
				initializeAllStatisticsRows();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Generate all statistics row. Used when a new practice is opened.
	private void initializeAllStatisticsRows() {
		TableLayout tl = (TableLayout) findViewById(R.id.statsSheetTableLayout);
		for (int i = 0; i < 12; i++) {
			TableRow statisticsRow = new TableRow(this);
			//set the layout parameters for the table row
			TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.MATCH_PARENT);
			trParams.setMargins(0, 0, 0, 10); //left, top, right, bottom
			statisticsRow.setWeightSum(6);
			statisticsRow.setBackgroundColor(Color.parseColor("#FFFFFF"));
			statisticsRow.setLayoutParams(trParams);
			
			//Create and set the layout parameters for the Player Name, Points, Assists,
			//Rebounds, and Steals
			EditText txtPlayerCell = new EditText(this);
			NumberPicker npPointsCell = new NumberPicker(this);
			NumberPicker npAssistsCell = new NumberPicker(this);
			NumberPicker npReboundsCell = new NumberPicker(this);
			NumberPicker npStealsCell = new NumberPicker(this);
			
			//parameters for Player Cell
			TableRow.LayoutParams txtPlayerCellParams = new TableRow.LayoutParams(
					TableLayout.LayoutParams.WRAP_CONTENT,
					TableLayout.LayoutParams.WRAP_CONTENT, 2f);
			txtPlayerCellParams.setMargins(0, 0, 5, 0); //left, top, right, bottom
			txtPlayerCell.setHint("Player Name");
			txtPlayerCell.setTextColor(Color.parseColor("#000000"));
			txtPlayerCell.setLayoutParams(txtPlayerCellParams);
			
			//parameters for all Number Pickers
			TableRow.LayoutParams numberPickerParams = new TableRow.LayoutParams(
					TableLayout.LayoutParams.WRAP_CONTENT,
					TableLayout.LayoutParams.WRAP_CONTENT, 1f);
			numberPickerParams.setMargins(0, 0, 5, 0); //left, top, right, bottom
			
			formatNumberPickers(npPointsCell, npAssistsCell, npReboundsCell, npStealsCell);
			
			//apply the number picker parameters to each number picker
			npPointsCell.setLayoutParams(numberPickerParams);
			npAssistsCell.setLayoutParams(numberPickerParams);
			npReboundsCell.setLayoutParams(numberPickerParams);
			npStealsCell.setLayoutParams(numberPickerParams);
			
			//add the statistics fields to the statistics row
			statisticsRow.addView(txtPlayerCell);
			statisticsRow.addView(npPointsCell);
			statisticsRow.addView(npAssistsCell);
			statisticsRow.addView(npReboundsCell);
			statisticsRow.addView(npStealsCell);
			
			//add the statistics row to the table view
			tl.addView(statisticsRow);
			tableRows.add(statisticsRow);
		}
	}
	
	//create one statistics row. Used when a saved practice is opened.
	private void initializeOneStatisticsRow(){
		TableLayout tl = (TableLayout) findViewById(R.id.statsSheetTableLayout);
		statisticsRow = new TableRow(this);
		//set the layout parameters for the table row
		TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.MATCH_PARENT);
		trParams.setMargins(0, 0, 0, 10); //left, top, right, bottom
		statisticsRow.setWeightSum(6);
		statisticsRow.setBackgroundColor(Color.parseColor("#FFFFFF"));
		statisticsRow.setLayoutParams(trParams);
		
		//Create and set the layout parameters for the Player Name, Points, Assists,
		//Rebounds, and Steals
		EditText txtPlayerCell = new EditText(this);
		NumberPicker npPointsCell = new NumberPicker(this);
		NumberPicker npAssistsCell = new NumberPicker(this);
		NumberPicker npReboundsCell = new NumberPicker(this);
		NumberPicker npStealsCell = new NumberPicker(this);
		
		//parameters for Player Cell
		TableRow.LayoutParams txtPlayerCellParams = new TableRow.LayoutParams(
				TableLayout.LayoutParams.WRAP_CONTENT,
				TableLayout.LayoutParams.WRAP_CONTENT, 2f);
		txtPlayerCellParams.setMargins(0, 0, 5, 0); //left, top, right, bottom
		txtPlayerCell.setHint("Player Name");
		txtPlayerCell.setTextColor(Color.parseColor("#000000"));
		txtPlayerCell.setLayoutParams(txtPlayerCellParams);
		
		//parameters for all Number Pickers
		TableRow.LayoutParams numberPickerParams = new TableRow.LayoutParams(
				TableLayout.LayoutParams.WRAP_CONTENT,
				TableLayout.LayoutParams.WRAP_CONTENT, 1f);
		numberPickerParams.setMargins(0, 0, 5, 0); //left, top, right, bottom
		//set the min and max values
		formatNumberPickers(npPointsCell, npAssistsCell, npReboundsCell, npStealsCell);
		
		//apply the number picker parameters to each number picker
		npPointsCell.setLayoutParams(numberPickerParams);
		npAssistsCell.setLayoutParams(numberPickerParams);
		npReboundsCell.setLayoutParams(numberPickerParams);
		npStealsCell.setLayoutParams(numberPickerParams);
		
		//add the statistics fields to the statistics row
		statisticsRow.addView(txtPlayerCell);
		statisticsRow.addView(npPointsCell);
		statisticsRow.addView(npAssistsCell);
		statisticsRow.addView(npReboundsCell);
		statisticsRow.addView(npStealsCell);
		
		//add the statistics row to the table view
		tl.addView(statisticsRow);
		tableRows.add(statisticsRow);
	}
	
	//fills out the player name and their corresponding statistics
	private void setPlayerStats(int statSheetTableLayout, int index, String[] statisticsArray){
		EditText playerCell = (EditText) statisticsRow.getChildAt(0);
		NumberPicker pointsCell = (NumberPicker) statisticsRow
				.getChildAt(1);
		NumberPicker assistsCell = (NumberPicker) statisticsRow
				.getChildAt(2);
		NumberPicker rebsCell = (NumberPicker) statisticsRow
				.getChildAt(3);
		NumberPicker stealsCell = (NumberPicker) statisticsRow
				.getChildAt(4);
		// set the values
		playerCell.setText(statisticsArray[0]);
		pointsCell.setValue(Integer.parseInt(statisticsArray[1]));
		assistsCell.setValue(Integer.parseInt(statisticsArray[2]));
		rebsCell.setValue(Integer.parseInt(statisticsArray[3]));
		stealsCell.setValue(Integer.parseInt(statisticsArray[4]));
	}
	
	//parses through the statistics file and stores the values of a line in an array
	private String[] parseStatisticsFile(String strLine){
		String[] statisticsArray;
		int index = strLine.indexOf("$");
		String playerName = strLine.substring(0, index);
		strLine = strLine.substring(index + 1);
		index = strLine.indexOf("$");
		String sPoints = strLine.substring(0, index);
		strLine = strLine.substring(index + 1);
		index = strLine.indexOf("$");
		String sAssists = strLine.substring(0, index);
		strLine = strLine.substring(index + 1);
		index = strLine.indexOf("$");
		String sRebs = strLine.substring(0, index);
		strLine = strLine.substring(index + 1);
		index = strLine.indexOf("$");
		String sSteals = strLine.substring(0, index);
		statisticsArray = new String[] {playerName, sPoints, sAssists, sRebs, sSteals};
		
		return statisticsArray;
	}
	
	public String[] parseStatisticsRowsFromSheet(TableRow tableRow, int index){
		tableRow = tableRows.get(index);
		String[] playerStatsFromFile;
		EditText playerCell = (EditText) tableRow.getChildAt(0);
		NumberPicker pointsCell = (NumberPicker) tableRow
				.getChildAt(1);
		NumberPicker assistsCell = (NumberPicker) tableRow
				.getChildAt(2);
		NumberPicker rebsCell = (NumberPicker) tableRow
				.getChildAt(3);
		NumberPicker stealsCell = (NumberPicker) tableRow
				.getChildAt(4);
		
		playerStatsFromFile = new String[] {
				playerCell.getText().toString(),
				String.valueOf(pointsCell.getValue()), 
				String.valueOf(assistsCell.getValue()),
				String.valueOf(rebsCell.getValue()),
				String.valueOf(stealsCell.getValue())
				};
		
		return playerStatsFromFile;
		
	}
	
	private void formatNumberPickers(NumberPicker pointsCell, 
			NumberPicker assistsCell, 
			NumberPicker rebsCell, 
			NumberPicker stealsCell){
		pointsCell.setMinValue(MIN_POINTS);
		pointsCell.setMaxValue(MAX_POINTS);
		assistsCell.setMinValue(MIN_POINTS);
		assistsCell.setMaxValue(MAX_POINTS);
		rebsCell.setMinValue(MIN_POINTS);
		rebsCell.setMaxValue(MAX_POINTS);
		stealsCell.setMinValue(MIN_POINTS);
		stealsCell.setMaxValue(MAX_POINTS);
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
		case R.id.action_deleteStatSheet:
			deleteStatSheet();
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

	//TODO - Fix save method
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
			EditText teamNameBox = (EditText) findViewById(R.id.teamName);
			String teamName = teamNameBox.getText().toString();
			osw.write(teamName + "\n");
			for (int i = 0; i < tableRows.size(); i++) {
				TableRow tableRow = tableRows.get(i);
				String[] statisticsArray = parseStatisticsRowsFromSheet(tableRow, i);
				// write the information taken from the StatSheet to the new
				// file created.
				osw.write(statisticsArray[0] + "$");
				osw.write(statisticsArray[1] + "$");
				osw.write(statisticsArray[2] + "$");
				osw.write(statisticsArray[3] + "$");
				osw.write(statisticsArray[4] + "$\n");
			}
			osw.flush();
			osw.close();
		}
	}

	private void deleteStatSheet() {
		EditText statSheetTitle = (EditText) findViewById(R.id.gameName);
		String fileName = statSheetTitle.getText().toString();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete " + fileName + "?")
				.setTitle("Warning!");
		builder.setPositiveButton("Yes", dialogDeleteClickListener)
				.setNegativeButton("No", dialogDeleteClickListener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	DialogInterface.OnClickListener dialogDeleteClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				File file = new File(sdCard.getAbsolutePath()
						+ "/BasketballAssistant/StatSheets/" + fileName);
				file.delete();
				Intent intent = new Intent(StatSheetActivity.this,
						OpenStatSheetActivity.class);
				startActivity(intent);
			case DialogInterface.BUTTON_NEGATIVE:
				// do nothing, just go back the statistics sheet
			}
		}
	};
}
