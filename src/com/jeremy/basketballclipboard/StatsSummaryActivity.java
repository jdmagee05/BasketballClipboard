package com.jeremy.basketballclipboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class StatsSummaryActivity extends Activity {

	public StatisticsCruncher statsCruncher = new StatisticsCruncher();
	File sdCard = Environment.getExternalStorageDirectory();
	String directory = sdCard.getAbsolutePath()
			+ "/BasketballAssistant/StatSheets/";

	// the spinners
	private Spinner playerOrTeamSpinner, specificPlayerOrTeamSpinner;

	// the player and team lists
	ArrayList<String> playerList;
	ArrayList<String> teamList;

	ArrayAdapter<String> playerAdp;
	ArrayAdapter<String> teamAdp;

	// variable to check if this is the first time the submit button was clicked
	boolean initalSubmit = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats_summary);
		// Show the Up button in the action bar.
		setupActionBar();

		// populate the lists by call populateLists from StatisticsCruncher
		try {
			statsCruncher.populateLists(directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		playerList = statsCruncher.getPlayerList();
		teamList = statsCruncher.getTeamList();

		// create the array adapters
		createArrayAdapters();

		addListenerOnPlayerOrTeamSpinner();
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
		getMenuInflater().inflate(R.menu.stats_summary, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

	private void createArrayAdapters() {
		playerAdp = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, playerList);
		playerAdp
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		playerAdp.notifyDataSetChanged();

		teamAdp = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, teamList);
		teamAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		teamAdp.notifyDataSetChanged();
	}

	public void addListenerOnPlayerOrTeamSpinner() {
		playerOrTeamSpinner = (Spinner) findViewById(R.id.playerOrTeamSpinner);
		specificPlayerOrTeamSpinner = (Spinner) findViewById(R.id.specificPlayerOrTeamSpinner);
		playerOrTeamSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						String value = arg0.getItemAtPosition(arg2).toString();
						if (value.equals("Team")) {
							specificPlayerOrTeamSpinner.setAdapter(teamAdp);
						} else if (value.equals("Player")) {
							specificPlayerOrTeamSpinner.setAdapter(playerAdp);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	public void displayStats(View view) throws IOException {
		// set up the header for the display
		TableLayout tl = (TableLayout) findViewById(R.id.statSummaryTableLayout);
		if (initalSubmit == false) {
			tl.removeAllViews();
		}
		TextView gamesPlayed = new TextView(this);
		TableRow headingTableRow = new TableRow(this);
		headingTableRow.setWeightSum(4f);
		TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.MATCH_PARENT);
		trParams.setMargins(0, 5, 0, 0); // left, top, right, bottom
		headingTableRow.setLayoutParams(trParams);
		// create text views for the header for points, assists, rebounds and
		// steals
		TextView pointsHeader = new TextView(this);
		pointsHeader.setText("Points (ppg)");
		TextView assistsHeader = new TextView(this);
		assistsHeader.setText("Assists (apg)");
		TextView rebsHeader = new TextView(this);
		rebsHeader.setText("Rebounds (rpg)");
		TextView stealsHeader = new TextView(this);
		stealsHeader.setText("Steals (spg)");
		// set the parameters of the text views
		TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT, 1f);
		textViewParams.setMargins(5, 0, 0, 0);
		pointsHeader.setLayoutParams(textViewParams);
		assistsHeader.setLayoutParams(textViewParams);
		rebsHeader.setLayoutParams(textViewParams);
		stealsHeader.setLayoutParams(textViewParams);

		// add the children to their parents
		headingTableRow.addView(pointsHeader);
		headingTableRow.addView(assistsHeader);
		headingTableRow.addView(rebsHeader);
		headingTableRow.addView(stealsHeader);
		// add the games played header to the TableLayout
		tl.addView(gamesPlayed);
		// add the headingTableRow to the TableLayout
		tl.addView(headingTableRow);

		// get the player or team selected
		String playerOrTeamSelection = playerOrTeamSpinner.getItemAtPosition(
				playerOrTeamSpinner.getSelectedItemPosition()).toString();
		String selection = specificPlayerOrTeamSpinner.getItemAtPosition(
				specificPlayerOrTeamSpinner.getSelectedItemPosition())
				.toString();
		if (playerOrTeamSelection.equals("Player")) {
			// calculate that specific players' averages
			double[] playerAverages = statsCruncher.calculatePlayerAverages(
					selection, directory);
			// set the text for the games played text view
			gamesPlayed.setText("Games Played: " + playerAverages[0]);
			// create a new TableRow that will display the player averages
			TableRow statsTableRow = new TableRow(this);
			statsTableRow.setLayoutParams(trParams);
			// iterate through the player averages array and create a textView
			// for each
			// Start at playerAverages[1] because the games played is at playerAverages[0]
			for (int i = 1; i < playerAverages.length; i++) {
				TextView statField = new TextView(this);
				statField.setText(playerAverages[i] + "");
				statField.setLayoutParams(textViewParams);
				statsTableRow.addView(statField);
			}
			// add the statsTableRow to the TableLayout
			tl.addView(statsTableRow);
		}

		initalSubmit = false;
	}
}
