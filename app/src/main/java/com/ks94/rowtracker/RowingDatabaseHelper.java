package com.ks94.rowtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kyran on 21/06/2016.
 */
public class RowingDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "rowingTracker";
    private static final int DB_VERSION = 2;

    public RowingDatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE WORKOUT ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DATE_MILLIS INT, "
                + "TIME_MILLIS INT, "//store in seconds so is sortable
                + "DISTANCE FLOAT, "
                + "NOTES TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion == 1)
        {
            //add column for time per 500 meters
            db.execSQL("ALTER TABLE WORKOUT ADD COLUMN TIME_500M_MILLIS INT");
            //go through existing rows in table and calculate this value
            Cursor cursor = db.query(
                    "WORKOUT",
                    new String[]{"_id", "DISTANCE", "TIME_MILLIS"},
                    null, null, null, null, null);

            int id;
            double distance;
            long time, time500;
            ContentValues values;

            if(cursor.moveToFirst())
            {
                id = cursor.getInt(0);
                distance = cursor.getDouble(1);
                time = cursor.getLong(2);

                time500 = Workout.convertTo500(time, distance);

                values = new ContentValues();
                values.put("TIME_500M_MILLIS", time500);
                db.update("WORKOUT", values, "_id = ?", new String[]{Integer.toString(id)});
            }

            cursor.close();
        }

    }
}
