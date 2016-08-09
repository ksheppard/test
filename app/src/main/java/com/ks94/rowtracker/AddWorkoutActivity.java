package com.ks94.rowtracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

public class AddWorkoutActivity extends Activity implements View.OnClickListener
{

    private SimpleDateFormat dateFormatter;
    EditText dateTxt;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        //set up popup date chooser
        dateTxt = (EditText) findViewById(R.id.date);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar newDate = Calendar.getInstance();
        dateTxt.setText(dateFormatter.format(newDate.getTime()));
        dateTxt.clearFocus();

        setDateTimeField();
    }

    @Override
    public void onClick(View view) {
        if(view == dateTxt) {
            datePickerDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setDateTimeField() {
        dateTxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateTxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                addWorkout();
                return true;
            case R.id.action_settings:
                //open settings screen
                //Intent intent = new Intent(MainActivity.this, AddWorkoutActivity.class);
                //startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addWorkout()
    {
        //check is valid - all data been added
        //make workout object
        //add to db
        //return user to their previous screen

        //future - display popup with stats regarding the workout - can then click continue when ready to move on.

        try
        {
            String dateStr = dateTxt.getText().toString();
            DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = (Date) dateFormatter.parse(dateStr);

            EditText timeTxt = (EditText) findViewById(R.id.time);
            String timeStr = timeTxt.getText().toString();

            java.sql.Time timeValue;
            DateFormat timeFormatter;
            try{
                timeFormatter = new SimpleDateFormat("mm:ss");
                timeValue = new java.sql.Time(timeFormatter.parse(timeStr).getTime());
            }
            catch(ParseException e)
            {
                timeFormatter = new SimpleDateFormat("HH:mm:ss");
                timeValue = new java.sql.Time(timeFormatter.parse(timeStr).getTime());
            }

            EditText notesTxt = (EditText) findViewById(R.id.notes);
            String notesStr = notesTxt.getText().toString();

            EditText distanceTxt = (EditText) findViewById(R.id.distance);
            double distance = Double.parseDouble(distanceTxt.getText().toString());

            Workout workout = new Workout(notesStr, distance, timeValue, date);

            new AddWorkoutTask().execute(workout);


        }
        catch(ParseException e)
        {
            Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
        }
    }

    private class AddWorkoutTask extends AsyncTask<Workout, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Workout... workouts){

            Workout workout = workouts[0];

            SQLiteOpenHelper dbHelper = new RowingDatabaseHelper(AddWorkoutActivity.this);

            try
            {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues drinkVals = new ContentValues();
                drinkVals.put("DATE_MILLIS", workout.getSqlDate());
                drinkVals.put("TIME_MILLIS", workout.getSqlTime());
                drinkVals.put("DISTANCE", workout.getDistance());
                drinkVals.put("NOTES", workout.getNotes());
                db.insert("WORKOUT", null, drinkVals);
                db.close();

                return true;
            }
            catch(SQLiteException e)
            {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success)
        {
            if(success)
            {
                //give pop up with stats of workout and a button asking to retrun to main menu

                Intent intent = new Intent(AddWorkoutActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(AddWorkoutActivity.this, "Workout saved successfully!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(AddWorkoutActivity.this, "Database unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
