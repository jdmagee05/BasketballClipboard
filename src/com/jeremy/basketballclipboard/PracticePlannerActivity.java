package com.jeremy.basketballclipboard;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
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
	String strOldDuration;
	int properFormEntry = 0; // 0 if initial or no drill duration
							 // 1 if drill line filled completely
							 // 2 if drill duration entered incorrectly
	boolean correctDurationEntry = true;


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
		case R.id.action_newPractice:
			// newPractice();
			return true;
			// add save practice to options menu
		case R.id.action_savePractice:
			save();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void setNewStartTime(String prevStartTime, EditText startTime)
			throws ParseException {
		// set up the Date formatter
		SimpleDateFormat formatter = new SimpleDateFormat("h:mm");
		// change prevStartTime from String to Date
		Date prevStartTimeDate = (Date) formatter.parse(prevStartTime);
		int prevDurationInt = Integer
				.parseInt(oldDuration.getText().toString());
		// instantiate Calendar
		Calendar cl = new GregorianCalendar();
		cl.setTime(prevStartTimeDate);
		// add the duration onto the drill start time
		cl.add(Calendar.MINUTE, prevDurationInt);
		Date clDrillTime = cl.getTime();
		// make the new time a String
		String newStartTimeStr = formatter.format(clDrillTime);
		startTime.setText(newStartTimeStr);
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
		startTimeParams.setMargins(5, 5, 0, 0); // left, top, right, bottom
		tr.setBackgroundColor(Color.parseColor("#FFFFFF"));
		newStartTime.setLayoutParams(startTimeParams);
		// set attributes
		newStartTime.setEms(10);
		newStartTime.setTextColor(Color.parseColor("#000000"));
		newStartTime.setHint("Start Time");
		newStartTime.setRawInputType(InputType.TYPE_CLASS_DATETIME);
		newStartTime.setTypeface(null, Typeface.BOLD);

		if (oldStartTime != null) {
			String strOldStartTime = oldStartTime.getText().toString();
			strOldDuration = oldDuration.getText().toString();
			String threeNumRegex = "\\d{3}";
			String fourNumRegex = "\\d{4}";
			if(strOldDuration.contains(":") || strOldDuration.contains("/")
					|| strOldDuration.contains(" ") || strOldDuration.contains(".")
					|| strOldDuration.contains("-")){
				//create an alert dialog box
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Enter only numbers in the Drill Duration field." +
						" Thanks!")
			       .setTitle("Warning!");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
				oldDuration.setText("");
				oldDuration.requestFocus();
				correctDurationEntry = false;
				properFormEntry = 2;
			}
			if (!(strOldDuration.length() == 0) && correctDurationEntry == true) {
				if (strOldStartTime.contains(":")) {
					String prevStartTimeStr = oldStartTime.getText().toString();
					setNewStartTime(prevStartTimeStr, newStartTime);
					// replace the "." with a ":"
				} else if (strOldStartTime.contains(".")) {
					strOldStartTime = strOldStartTime.replace(".", ":");
					setNewStartTime(strOldStartTime, newStartTime);
					oldStartTime.setText(strOldStartTime);
				}
				// replace the "/" with a ":"
				else if (strOldStartTime.contains("/")) {
					strOldStartTime = strOldStartTime.replace("/", ":");
					setNewStartTime(strOldStartTime, newStartTime);
					oldStartTime.setText(strOldStartTime);
				}
				// replace the " " with a ":"
				else if (strOldStartTime.contains(" ")) {
					strOldStartTime = strOldStartTime.replace(" ", ":");
					setNewStartTime(strOldStartTime, newStartTime);
					oldStartTime.setText(strOldStartTime);
				}
				// replace the "-" with a ":"
				else if (strOldStartTime.contains("-")) {
					strOldStartTime = strOldStartTime.replace("-", ":");
					setNewStartTime(strOldStartTime, newStartTime);
					oldStartTime.setText(strOldStartTime);
				}
				// accounts for if there is no colon and 3 digits
				else if (strOldStartTime.matches(threeNumRegex)) {
					strOldStartTime = strOldStartTime.substring(0, 1)
							+ ":"
							+ strOldStartTime.substring(1,
									strOldStartTime.length());
					setNewStartTime(strOldStartTime, newStartTime);
					oldStartTime.setText(strOldStartTime);
					// accounts for if there is no colon and 4 digits
				} else if (strOldStartTime.matches(fourNumRegex)) {
					strOldStartTime = strOldStartTime.substring(0, 2)
							+ ":"
							+ strOldStartTime.substring(2,
									strOldStartTime.length());
					setNewStartTime(strOldStartTime, newStartTime);
					oldStartTime.setText(strOldStartTime);
				}
				properFormEntry = 1;
			}
		}
		oldStartTime = newStartTime;

		/* create a new drill field */
		EditText newDrill = new EditText(this);
		// set the drill field layout params
		TableRow.LayoutParams drillParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT, 1);
		drillParams.setMargins(10, 5, 0, 0); // left, top, right, bottom
		newDrill.setLayoutParams(drillParams);
		// set attributes
		newDrill.setEms(10);
		newDrill.setTextColor(Color.parseColor("#000000"));
		newDrill.setHint("Enter your drill here!");
		newDrill.setTypeface(null, Typeface.BOLD);

		/* create a new duration field */
		EditText newDuration = new EditText(this);
		// set the duration field layout params
		TableRow.LayoutParams durationParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT, 1);
		durationParams.setMargins(10, 5, 5, 0); // left, top, right, bottom
		newDuration.setLayoutParams(durationParams);
		// set attributes
		newDuration.setEms(10);
		newDuration.setTextColor(Color.parseColor("#000000"));
		newDuration.setHint("Duration");
		newDuration.setInputType(InputType.TYPE_CLASS_DATETIME);
		newDuration.setTypeface(null, Typeface.BOLD);
//		oldDuration = newDuration;

		// add the text field to the table row
		tr.addView(newStartTime);
		tr.addView(newDrill);
		tr.addView(newDuration);
		// add the table row to the table layout
		tl.addView(tr);

		// set focus on the newly created start time field
		if (properFormEntry == 0 || strOldDuration.length() == 0) {
			newStartTime.requestFocus();
			properFormEntry++;
		} else if(properFormEntry == 1) {
			newDrill.requestFocus();
		} else if(properFormEntry == 2){
			oldDuration.requestFocus();
		}
		oldDuration = newDuration;
		correctDurationEntry = true;
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
}
