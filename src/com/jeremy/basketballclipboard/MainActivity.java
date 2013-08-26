package com.jeremy.basketballclipboard;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	private String practicesDir = "/BasketballAssistant/Practices";
	private String statSheetsDir = "/BasketballAssistant/StatSheets";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void goToClipboard(View view) {
		Intent intent = new Intent(this, ClipboardActivity.class);
		startActivity(intent);
	}

	public void goToPractice(View view) {
		if (checkIfDirExists(practicesDir)) {
			Intent intent = new Intent(this, PracticeHubActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, PracticePlannerActivity.class);
			startActivity(intent);
		}
	}

	public void goToStatSheet(View view) {
		if (checkIfDirExists(statSheetsDir)) {
			Intent intent = new Intent(this, OpenStatSheetActivity.class);
			startActivity(intent);
		} else{
			Intent intent = new Intent(this, StatSheetActivity.class);
			startActivity(intent);
		}
	}

	public boolean checkIfDirExists(String desiredDir) {
		File dir = new File(Environment.getExternalStorageDirectory()
				+ desiredDir);
		if (dir.isDirectory()) {
			return true;
		}
		return false;
	}
}
