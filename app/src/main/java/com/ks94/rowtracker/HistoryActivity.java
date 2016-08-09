package com.ks94.rowtracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
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

public class HistoryActivity extends Activity
{
    private Cursor historyCursor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> listView, View v, int position, long id){
                //open the workout in a new window where can view/edit
                int workoutId = (int)id;
                Intent intent = new Intent(HistoryActivity.this, ViewWorkoutActivity.class);
                intent.putExtra(ViewWorkoutActivity.EXTRA_WORKOUT_ID, workoutId);
                startActivity(intent);
            }
        };

        AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                //show popup menu with option to delete
                return false;
            }
        };

        ListView lv = (ListView) findViewById(R.id.list_history);
        lv.setOnItemClickListener(itemClickListener);
        lv.setOnItemLongClickListener(itemLongClickListener);


        try
        {
            SQLiteOpenHelper dbHelper = new RowingDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();
            historyCursor = db.query("WORKOUT", new String[]{"_id", "DATE_MILLIS", "TIME_MILLIS", "DISTANCE"}, null, null, null, null, "DATE_MILLIS DESC, _id");

            SimpleCursorAdapter historyAdapter = new SimpleCursorAdapter(this,
                    R.layout.row,
                    historyCursor,
                    new String[]{"DATE_MILLIS", "DISTANCE", "TIME_MILLIS"},
                    new int[]{R.id.textDate, R.id.textDist, R.id.textTime}, 0);

            historyAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                    //used to manipulate date and time values from sql in order to convert from millis to string

                    if(columnIndex == 1) {

                        long longDate = cursor.getLong(columnIndex);
                        Date date = new Date(longDate);

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //needs to be region specific
                        String dateString = dateFormat.format(date);

                        TextView tv = (TextView) view.findViewById(R.id.textDate);
                        tv.setText(dateString);

                        return true;
                    }
                    else if(columnIndex == 2){

                        long longTime = cursor.getLong(columnIndex);
                        Time time = new Time(longTime);

                        TextView tv = (TextView) view.findViewById(R.id.textTime);
                        tv.setText(time.toString());

                        return true;
                    }

                    return false;
                }
            });
            lv.setAdapter(historyAdapter);
        }catch(SQLiteException e)
        {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
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
                Intent intent = new Intent(HistoryActivity.this, AddWorkoutActivity.class);
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
}
