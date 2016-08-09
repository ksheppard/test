package com.ks94.rowtracker;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Kyran on 14/06/2016.
 */
public class Workout
{
    private int id;
    private Date date;
    private long sqlDate;
    private Time time;
    private long sqlTime;
    private double distance;
    private String notes;
    private Time time500;
    private long sqlTime500;

    public Workout(String notes, double distance, Time time, Date date)
    {
        this.notes = notes;
        this.distance = distance;
        this.time = time;
        this.date = date;

        sqlDate = date.getTime();
        sqlTime = time.getTime();
        set500mTime();
    }

    public Workout(int id, String notes, double distance, long sqlTime, long sqlDate)
    {
        this.id = id;
        this.notes = notes;
        this.distance = distance;
        this.sqlDate = date.getTime();
        this.sqlTime = time.getTime();

        this.time = new Time(sqlTime);
        this.date = new Date(sqlDate);
        set500mTime();
    }

    private void set500mTime()
    {
        sqlTime500 = convertTo500(sqlTime, distance);
        time500 = new Time(sqlTime500);
    }

    public static long convertTo500(long time, double distance)
    {
        return time / ((long)(distance / 500));
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    public Time getTime()
    {
        return time;
    }

    public void setTime(Time time)
    {
        this.time = time;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public long getSqlTime()
    {
        return sqlTime;
    }

    public void setSqlTime(long sqlTime)
    {
        this.sqlTime = sqlTime;
    }

    public long getSqlDate()
    {
        return sqlDate;
    }

    public void setSqlDate(long sqlDate)
    {
        this.sqlDate = sqlDate;
    }
}
