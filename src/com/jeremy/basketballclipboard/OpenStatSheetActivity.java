package com.jeremy.basketballclipboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class OpenStatSheetActivity extends Activity {

	ListView mListView;
	public String fileName;
	public String teamName;
	public boolean isFileOpened = false;
	public boolean newStatisticsSheet = false;

	File sdCard = Environment.getExternalStorageDirectory();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_open_stat_sheet);
			// Show the Up button in the action bar.
			setupActionBar();
			mListView = (ListView) findViewById(R.id.statSheetList);
			// create a list adapter that will show the individual practices in
			// the form of a list
			String allStatSheets = "All";
			List<Map> data = generateStatSheetList(allStatSheets);
			ListAdapter adapter = new SimpleAdapter(OpenStatSheetActivity.this,
					(List<? extends Map<String, String>>) data,
					R.layout.stat_sheet_list_item, new String[] {
							"statSheetDate", "listStatSheetName",
							"listStatSheetTeamName" }, new int[] {
							R.id.statSheetDate, R.id.listStatSheetName,
							R.id.listStatSheetTeamName });
			mListView.setAdapter(adapter);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// get the name of the file of the practice that was just
				// clicked
				TextView gameNameText = (TextView) arg1
						.findViewById(R.id.listStatSheetName);
				fileName = gameNameText.getText().toString() + ".txt";
				TextView teamNameText = (TextView) arg1
						.findViewById(R.id.listStatSheetTeamName);
				teamName = teamNameText.getText().toString();
				isFileOpened = true;
				// start the new practice activity
				Intent intent = new Intent(OpenStatSheetActivity.this,
						StatSheetActivity.class);
				intent.putExtra("isFileOpened", isFileOpened);
				intent.putExtra("fileName", fileName);
				intent.putExtra("teamName", teamName);
				intent.putExtra("newStatisticsSheet", newStatisticsSheet);
				startActivity(intent);
			}

		});
	}

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("rawtypes")
	public List<Map> generateStatSheetList(String filterParameters)
			throws ParseException, IOException {
		List<Map> list = new ArrayList<Map>();
		Map<String, String> map;
		// get the directory in which the practice files are located.
		File sdCard = Environment.getExternalStorageDirectory();
		File[] files = new File(sdCard.getAbsolutePath()
				+ "/BasketballAssistant/StatSheets").listFiles();
		String allTeamsFilter = "All";
		for (int i = 0; i < files.length; i++) {
			String fileNameWithExt = files[i].getName();
			int fileNameCutOffIndex = fileNameWithExt.lastIndexOf(".");
			String fileName = fileNameWithExt.substring(0, fileNameCutOffIndex);
			SimpleDateFormat formatter = new SimpleDateFormat("MMM/dd/yyyy");
			// get the date the file was last modified
			Date dfileDate = new Date(files[i].lastModified());
			// turn the date in its desired format to a string
			String sParsedFileDate = formatter.format(dfileDate);
			// get the teamName
			String teamName = getStatSheetName(files[i].getName());
			// create a new HashMap to put the data in
			map = new HashMap<String, String>();
			// put the data in the map
			if (allTeamsFilter.equals(filterParameters)) {
				map.put("statSheetDate", "Date: " + sParsedFileDate);
				map.put("listStatSheetName", fileName);
				map.put("listStatSheetTeamName", teamName);
				list.add(map);
			}
			else if(teamName.equals(filterParameters)){
				map.put("statSheetDate", "Date: " + sParsedFileDate);
				map.put("listStatSheetName", fileName);
				map.put("listStatSheetTeamName", teamName);
				list.add(map);
			}
		}
		return list;
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
		getMenuInflater().inflate(R.menu.open_stat_sheet, menu);
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
		case R.id.action_new_stat_sheet:
			newStatSheet();
			return true;
		case R.id.action_stats_cruncher:
			goToStatsCruncher();
			return true;
		case R.id.action_filter_stat_sheets:
			try {
				openFilterDialog();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void openFilterDialog() throws IOException {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Teams");
		ArrayList<String> data = getTeamFilterList();
		final ListAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, data);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			
			//set the new filtered list to display the desired data
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String filterParameters = adapter.getItem(which).toString();
				try {
					List<Map> filteredData = generateStatSheetList(filterParameters);
					ListAdapter adapter = new SimpleAdapter(OpenStatSheetActivity.this,
							(List<? extends Map<String, String>>) filteredData,
							R.layout.stat_sheet_list_item, new String[] {
									"statSheetDate", "listStatSheetName",
									"listStatSheetTeamName" }, new int[] {
									R.id.statSheetDate, R.id.listStatSheetName,
									R.id.listStatSheetTeamName });
					mListView.setAdapter(adapter);
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		builder.create();
		builder.show();
	}
	
	private ArrayList<String> getTeamFilterList() throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map;
		//add "All" to the list in order to retrieve all statistics sheets
		list.add("All");
		// get the directory in which the practice files are located.
		File sdCard = Environment.getExternalStorageDirectory();
		File[] files = new File(sdCard.getAbsolutePath()
				+ "/BasketballAssistant/StatSheets").listFiles();
		for (int i = 0; i < files.length; i++) {
			String fileNameWithExt = files[i].getName();
			int fileNameCutOffIndex = fileNameWithExt.lastIndexOf(".");
			String fileName = fileNameWithExt.substring(0, fileNameCutOffIndex);
			String teamName = getStatSheetName(files[i].getName());
			if(!(list.contains(teamName))){
				list.add(teamName);
			}
		}
		return list;
	}

	public void newStatSheet() {
		newStatisticsSheet = true;
		Intent intent = new Intent(this, StatSheetActivity.class);
		intent.putExtra("newStatisticsSheet", newStatisticsSheet);
		startActivity(intent);
	}

	public void goToStatsCruncher() {
		Intent intent = new Intent(OpenStatSheetActivity.this,
				StatsSummaryActivity.class);
		startActivity(intent);
	}

	private String getStatSheetName(String files) throws IOException {
		File directory = new File(sdCard.getAbsolutePath()
				+ "/BasketballAssistant/StatSheets/" + files);
		BufferedReader br = new BufferedReader(new FileReader(directory));
		String statSheetTeamName = null;
		String strLine = null;
		strLine = br.readLine();
		statSheetTeamName = strLine;
		br.close();
		return statSheetTeamName;
	}

}