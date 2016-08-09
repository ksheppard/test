package com.ks94.rowtracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity
{

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set on click listener for the menu
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> listView, View v, int position, long id){
                if(position == 0){
                    Intent intent = new Intent(MainActivity.this, AddWorkoutActivity.class);
                    startActivity(intent);
                }
                else if(position == 1){
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                }
                else if(position == 2){
                    //Intent intent = new Intent(MainActivity.this, AddWorkoutActivity.class);
                    //startActivity(intent);
                }
            }
        };

        ListView lv = (ListView) findViewById(R.id.menu_options);
        lv.setOnItemClickListener(itemClickListener);

        //need to get the data of last workout and display in textview
        getMostRecentWorkout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_add_new:
                Intent intent = new Intent(MainActivity.this, AddWorkoutActivity.class);
                startActivity(intent);
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

    private void getMostRecentWorkout()
    {
        try
        {
            SQLiteOpenHelper dbHelper = new RowingDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("WORKOUT", new String[]{"MAX(DATE_MILLIS) as recent"}, null, null, null, null, null);

            if(cursor.moveToFirst())
            {
                long recentDate = cursor.getLong(0);

                TextView textview = (TextView)findViewById(R.id.recent_textview);

                Date date = new Date(recentDate);

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //needs to be region specific
                String dateString = dateFormat.format(date);

                textview.setText(getResources().getString(R.string.last_workout) + " " + dateString);
            }

            cursor.close();
            db.close();

        }catch(SQLiteException e)
        {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }


}
