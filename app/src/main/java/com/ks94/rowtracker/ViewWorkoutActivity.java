package com.ks94.rowtracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class ViewWorkoutActivity extends Activity
{
    Workout workout;

    public static final String EXTRA_WORKOUT_ID = "EXTRA_WORKOUT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workout);
        allowEdit(false);

        int id = getIntent().getIntExtra(EXTRA_WORKOUT_ID, -1);

        if(id > -1)
        {
            //get workout from db
            setControls();
        }

        workout = null; //pass the id through the intent and then find
    }

    private void allowEdit(boolean allow)
    {
        EditText editText1 = (EditText) findViewById(R.id.date);
        editText1.setFocusable(allow);
        EditText editText2 = (EditText) findViewById(R.id.distance);
        editText1.setFocusable(allow);
        EditText editText3 = (EditText) findViewById(R.id.distance_rank);
        editText1.setFocusable(allow);
        EditText editText4 = (EditText) findViewById(R.id.time_500);
        editText1.setFocusable(allow);
        EditText editText5 = (EditText) findViewById(R.id.time_500_rank);
        editText1.setFocusable(allow);
        EditText editText6 = (EditText) findViewById(R.id.time);
        editText1.setFocusable(allow);
        EditText editText7 = (EditText) findViewById(R.id.time_rank);
        editText1.setFocusable(allow);
    }

    private void setControls()
    {
//        EditText editText1 = (EditText) findViewById(R.id.date);
//        editText1.setText(workout.getDate());
//        EditText editText2 = (EditText) findViewById(R.id.distance);
//        editText1.setText(workout.getDate());
//        EditText editText3 = (EditText) findViewById(R.id.distance_rank);
//        editText1.setText(workout.getDate());
//        EditText editText4 = (EditText) findViewById(R.id.time_500);
//        editText1.setText(workout.getDate());
//        EditText editText5 = (EditText) findViewById(R.id.time_500_rank);
//        editText1.setText(workout.getDate());
//        EditText editText6 = (EditText) findViewById(R.id.time);
//        editText1.setText(workout.getDate());
//        EditText editText7 = (EditText) findViewById(R.id.time_rank);
//        editText1.setText(workout.getDate());
    }
}
