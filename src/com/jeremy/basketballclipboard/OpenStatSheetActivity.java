package com.jeremy.basketballclipboard;

import java.io.File;
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
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class OpenStatSheetActivity extends Activity {

	ListView mListView;
	public String fileName;
	public boolean isFileOpened = false;

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
			List<Map> data = generateStatSheetList();
			ListAdapter adapter = new SimpleAdapter(OpenStatSheetActivity.this,
					(List<? extends Map<String, String>>) data,
					R.layout.stat_sheet_list_item, new String[] { "statSheetDate",
							"listStatSheetName" }, new int[] {
							R.id.statSheetDate, R.id.listStatSheetName });
			mListView.setAdapter(adapter);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// get the name of the file of the practice that was just clicked
				TextView text = (TextView) arg1.findViewById(R.id.listStatSheetName);
				fileName = text.getText().toString() + ".txt";
				isFileOpened = true;
				//start the new practice activity
				Intent intent = new Intent(OpenStatSheetActivity.this,
						StatSheetActivity.class);
				intent.putExtra("isFileOpened", isFileOpened);
				intent.putExtra("fileName", fileName);
				startActivity(intent);
			}

		});
	}

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("rawtypes")
	public List<Map> generateStatSheetList() throws ParseException {
		List<Map> list = new ArrayList<Map>();
		Map<String, String> map;
		// get the directory in which the practice files are located.
		File sdCard = Environment.getExternalStorageDirectory();
		File[] files = new File(sdCard.getAbsolutePath()
				+ "/BasketballAssistant/StatSheets").listFiles();
		for (int i = 0; i < files.length; i++) {
			String fileNameWithExt = files[i].getName();
			int fileNameCutOffIndex = fileNameWithExt.lastIndexOf(".");
			String fileName = fileNameWithExt.substring(0, fileNameCutOffIndex);
			SimpleDateFormat formatter = new SimpleDateFormat("MMM/dd/yyyy");
			// get the date the file was last modified
			Date dfileDate = new Date(files[i].lastModified());
			// turn the date in its desired format to a string
			String sParsedFileDate = formatter.format(dfileDate);
			//create a new HashMap to put the data in
			map = new HashMap<String, String>();
			// put the data in the map
			map.put("statSheetDate", "Date: " + sParsedFileDate);
			map.put("listStatSheetName", fileName);
			list.add(map);
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void newStatSheet(){
		Intent intent = new Intent(this, StatSheetActivity.class);
		startActivity(intent);
	}

}