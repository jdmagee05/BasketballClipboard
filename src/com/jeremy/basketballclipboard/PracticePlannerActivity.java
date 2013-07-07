package com.jeremy.basketballclipboard;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class PracticePlannerActivity extends Activity {
	EditText oldStartTime;
	EditText oldDuration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice_planner);
		// Show the Up button in the action bar.
		setupActionBar();
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
		getMenuInflater().inflate(R.menu.practice_planner, menu);
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
			// add new practice option to options menu
		case R.id.action_new:
			// newPractice();
			return true;
			// add save practice to options menu
		case R.id.action_save:
			save();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// adds a table row filled with a start time field, drill field and a
	// duration field
	@SuppressLint("SimpleDateFormat")
	public void addDrill(View view) throws ParseException {
		// gets the table layout
		TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);
		// assign values to margins of table row
		int top = 20;
		int left, right, bottom;
		left = right = bottom = 0; // set top, right and bottom to 0
		// creates a new row to be added to the TableView
		TableRow tr = new TableRow(this);
		// set the table row layout params
		TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.WRAP_CONTENT,
				TableLayout.LayoutParams.WRAP_CONTENT);
		trParams.setMargins(left, top, right, bottom);
		tr.setLayoutParams(trParams);

		/* create a new start time field */
		EditText newStartTime = new EditText(this);
		// set the start time field layout params
		TableRow.LayoutParams startTimeParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT, 1);
		startTimeParams.setMargins(5, 0, 0, 0); // left, top, right, bottom
		newStartTime.setLayoutParams(startTimeParams);
		// set attributes
		newStartTime.setEms(10);
		newStartTime.setTextColor(Color.parseColor("#000000"));
		newStartTime.setHint("Start Time");
		newStartTime.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);

		// set the new start time
		if (oldStartTime != null && oldStartTime.getText().toString().contains(":")) {
			// turn prevStartTime to time
			String prevStartTimeStr = oldStartTime.getText().toString();
			SimpleDateFormat formatter = new SimpleDateFormat("h:mm");
			Date prevStartTime = (Date) formatter.parse(prevStartTimeStr);
			//add duration to the start time
			int prevDuration = Integer.parseInt(oldDuration.getText()
					.toString());
			Calendar cl = new GregorianCalendar();
			cl.setTime(prevStartTime);
			cl.add(Calendar.MINUTE, prevDuration);
			Date clDrillTime = cl.getTime(); 
			String newStartTimeStr = formatter.format(clDrillTime);
			newStartTime.setText(newStartTimeStr);
		}
		oldStartTime = newStartTime;

		/* create a new drill field */
		EditText newDrill = new EditText(this);
		// set the drill field layout params
		TableRow.LayoutParams drillParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT, 1);
		drillParams.setMargins(10, 0, 0, 0); // left, top, right, bottom
		newDrill.setLayoutParams(drillParams);
		// set attributes
		newDrill.setEms(10);
		newDrill.setTextColor(Color.parseColor("#000000"));
		newDrill.setHint("Enter your drill here!");

		/* create a new duration field */
		EditText newDuration = new EditText(this);
		// set the duration field layout params
		TableRow.LayoutParams durationParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT, 1);
		durationParams.setMargins(10, 0, 5, 0); // left, top, right, bottom
		newDuration.setLayoutParams(durationParams);
		// set attributes
		newDuration.setEms(10);
		newDuration.setTextColor(Color.parseColor("#000000"));
		newDuration.setHint("Duration");
		newDuration.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
		oldDuration = newDuration;

		// add the text field to the table row
		tr.addView(newStartTime);
		tr.addView(newDrill);
		tr.addView(newDuration);
		// add the table row to the table layout
		tl.addView(tr);

		// set focus on the newly created start time field
		newStartTime.requestFocus();
	}

	// public void saveAs(){
	//
	// }

	public void save() {
		// EditText mEdit = (EditText)findViewById(R.id.practiceText);
		// File file = new File("newPractice.txt");
		// String fileName = file.getName();
		// String fileText = mEdit.getText().toString();
		// FileOutputStream fos;
		// try{
		// //check if file exists
		// if(!file.exists()){
		// file.createNewFile();
		// }
		//
		// fos = openFileOutput(fileName, Context.MODE_PRIVATE);
		// fos.write(fileText.getBytes());
		// fos.close();
		// } catch(IOException e){
		// e.printStackTrace();
		// }
	}

	// public void openPractice(){
	// //get a random file chooser
	// Intent intent = new Intent();
	// intent.setAction(Intent.ACTION_VIEW);
	// File file = new File("/*");
	// intent.setData(Uri.parse("/*"));
	// startActivity(intent);
	// //check if the file is null
	// if(file != null){
	// }
	//
	// }

}
