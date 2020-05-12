package com.example.astroweathercz2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//  https://developer.android.com/training/data-storage/sqlite

//  Once a database is created successfully its located in data/data//databases/ accessible from Android Device Monitor.

public class DatabaseHelper extends SQLiteOpenHelper {

    //     Table Name
    public static final String TABLE_NAME = "OpenWeather";

    //     Table columns
    public static final String _ID = "_id";
    public static final String NAME = "NAME";
    public static final String DATE_OF_INSERT = "DATE";
    public static final String LON = "LON";
    public static final String LAT = "LAT";
    public static final String TEMPERATURE = "TEMPERATURE";
    public static final String FEELS_LIKE = "FEELS_LIKE";
    public static final String TEMP_MIN = "TEMP_MIN";
    public static final String TEMP_MAX = "TEMP_MAX";
    public static final String PRESSURE = "PRESSURE";
    public static final String HUMIDITY = "HUMIDITY";
    public static final String SPEED = "SPEED";
    public static final String DEG = "DEG";
    public static final String SUNRISE = "SUNRISE";
    public static final String SUNSET = "SUNSET";
    public static final String TIMEZONE = "TIMEZONE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String TIME = "TIME";

    //     Database Information
    private static final String DB_NAME = "WEATHER.DB";

    //     database version
    private static final int DB_VERSION = 1;

    //     Creating table query
    private static final String CREATE_TABLE =
            "create table" + " " + TABLE_NAME + "("
                    + _ID + " " + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + NAME + " " + "TEXT,"
                    + DATE_OF_INSERT + " " + "date,"
                    + LON + " " + "INTEGER,"
                    + LAT + " " + "INTEGER,"
                    + TEMPERATURE + " " + "INTEGER,"
                    + FEELS_LIKE + " " + "INTEGER,"
                    + TEMP_MIN + " " + "INTEGER,"
                    + TEMP_MAX + " " + "INTEGER,"
                    + PRESSURE + " " + "INTEGER,"
                    + HUMIDITY + " " + "INTEGER,"
                    + SPEED + " " + "INTEGER,"
                    + DEG + " " + "INTEGER,"
                    + SUNRISE + " " + "INTEGER,"
                    + SUNSET + " " + "INTEGER,"
                    + DESCRIPTION + " " + "INTEGER,"
                    + TIMEZONE + " " + "INTEGER,"
                    + TIME + " " + "INTEGER" + ");";

    //    This takes the Context (e.g., an Activity)
    public DatabaseHelper(@Nullable Context context) {
//        When the application runs the first time – At this point, we do not yet have a database.
//        So we will have to create the tables, indexes, starter data, and so on.
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
