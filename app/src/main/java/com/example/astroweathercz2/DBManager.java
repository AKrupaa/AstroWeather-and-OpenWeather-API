package com.example.astroweathercz2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//  https://developer.android.com/training/data-storage/sqlite

public class DBManager {
    private DatabaseHelper databaseHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    //    Before performing any database operations like insert, update, delete records in a table, first open the database connection
    public DBManager open() {
        databaseHelper = new DatabaseHelper(context);
//        Create and/or open a database that will be used for reading and writing.
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    //    Close any open database object.
    public void close() {
        databaseHelper.close();
    }

    /* Inserting new Record into Android SQLite database table
        Returning the primary key value of the new row
        or it will return -1 if there was an error inserting the data.
        This can happen if you have a conflict with pre-existing data in the database.
     */
    public long insert(String name, /*String dateInsert,*/ int lon, int lat, int temp, int feelsLike,
                       int tempMin, int tempMax, int pressure, int humidity, int speed, String desc, /*int deg,*/
                       int sunrise, int sunset, int timezone, int time) {
//        Content Values creates an empty set of values using the given initial size
//        This class is used to store a set of values

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.NAME, name);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        contentValues.put(DatabaseHelper.DATE_OF_INSERT, date);
        contentValues.put(DatabaseHelper.LON, lon);
        contentValues.put(DatabaseHelper.LAT, lat);
        contentValues.put(DatabaseHelper.TEMPERATURE, temp);
        contentValues.put(DatabaseHelper.FEELS_LIKE, feelsLike);
        contentValues.put(DatabaseHelper.TEMP_MIN, tempMin);
        contentValues.put(DatabaseHelper.TEMP_MAX, tempMax);
        contentValues.put(DatabaseHelper.PRESSURE, pressure);
        contentValues.put(DatabaseHelper.HUMIDITY, humidity);
        contentValues.put(DatabaseHelper.SPEED, speed);
//        contentValues.put(DatabaseHelper.DEG, deg);
        contentValues.put(DatabaseHelper.DESCRIPTION, desc);
        contentValues.put(DatabaseHelper.SUNRISE, sunrise);
        contentValues.put(DatabaseHelper.SUNSET, sunset);
        contentValues.put(DatabaseHelper.TIMEZONE, timezone);
        contentValues.put(DatabaseHelper.TIME, time);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        return newRowId;
    }

    // Insert the new row, returning the primary key value of the new row
    public long insert(ContentValues contentValues) {
        long newRowId = database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        return newRowId;
    }

    //    Read information from a database
    public Cursor fetchAll() {
//     Define a projection that specifies which columns from the database
//     you will actually use after this query.
        String[] projection = {
                DatabaseHelper._ID,
                DatabaseHelper.NAME,
                DatabaseHelper.DATE_OF_INSERT,
                DatabaseHelper.LON,
                DatabaseHelper.LAT,
                DatabaseHelper.TEMPERATURE,
                DatabaseHelper.FEELS_LIKE,
                DatabaseHelper.TEMP_MIN,
                DatabaseHelper.TEMP_MAX,
                DatabaseHelper.PRESSURE,
                DatabaseHelper.HUMIDITY,
                DatabaseHelper.SPEED,
                DatabaseHelper.DESCRIPTION,
//                DatabaseHelper.DEG,
                DatabaseHelper.SUNRISE,
                DatabaseHelper.SUNSET,
                DatabaseHelper.TIMEZONE,
                DatabaseHelper.TIME
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DatabaseHelper.NAME + " ASC"; //DESC

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NAME,      // The table to query
                projection,                     // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,               // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder                    // The sort order
        );

//        Once the query is fetched a call to cursor.moveToFirst() is made.
//        Calling moveToFirst() it moves the cursor to the first result (when the set is not empty)
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchIDNameDate() {
//        Define a projection that specifies which columns from the database
//     you will actually use after this query.
        String[] projection = {
                DatabaseHelper._ID,
                DatabaseHelper.NAME,
                DatabaseHelper.DATE_OF_INSERT,
        };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NAME,      // The table to query
                projection,                     // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,               // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                DatabaseHelper.NAME + " ASC"                    // The sort order
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchWhereID(long id) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NAME,      // The table to query
                null,                     // The array of columns to return (pass null to get all)
                DatabaseHelper._ID + "=?",                   // The columns for the WHERE clause
                new String[] { String.valueOf(id) },               // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
        );

//        Cursor findEntry = db.query("sku_table", columns, "owner=? and price=?", new String[] { owner, price }, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //    Deleting a Record in Android SQLite database table
    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    //    Updating Record in Android SQLite database table
    public int update(long _id, String name, /*String dateInsert,*/ int lon, int lat, int temp, int feelsLike,
                      int tempMin, int tempMax, int pressure, int humidity, int speed, String desc,
            /*int deg,*/ int sunrise, int sunset, int timezone, int time) {
//        Content Values creates an empty set of values using the given initial size
//        This class is used to store a set of values
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper._ID, _id);
        contentValues.put(DatabaseHelper.NAME, name);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        contentValues.put(DatabaseHelper.DATE_OF_INSERT, date);
        contentValues.put(DatabaseHelper.LON, lon);
        contentValues.put(DatabaseHelper.LAT, lat);
        contentValues.put(DatabaseHelper.TEMPERATURE, temp);
        contentValues.put(DatabaseHelper.FEELS_LIKE, feelsLike);
        contentValues.put(DatabaseHelper.TEMP_MIN, tempMin);
        contentValues.put(DatabaseHelper.TEMP_MAX, tempMax);
        contentValues.put(DatabaseHelper.PRESSURE, pressure);
        contentValues.put(DatabaseHelper.HUMIDITY, humidity);
        contentValues.put(DatabaseHelper.SPEED, speed);
        contentValues.put(DatabaseHelper.DESCRIPTION, desc);
//        contentValues.put(DatabaseHelper.DEG, deg);
        contentValues.put(DatabaseHelper.SUNRISE, sunrise);
        contentValues.put(DatabaseHelper.SUNSET, sunset);
        contentValues.put(DatabaseHelper.TIMEZONE, timezone);
        contentValues.put(DatabaseHelper.TIME, time);

        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public int update(long _id, ContentValues contentValues) {
        contentValues.put(DatabaseHelper._ID, _id);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public static ContentValues cursorRowToContentValues(Cursor cursor) {
        ContentValues values = new ContentValues();
        String[] columns = cursor.getColumnNames();
        int length = columns.length;
        for (int i = 0; i < length; i++) {
            switch (cursor.getType(i)) {
                case Cursor.FIELD_TYPE_NULL:
                    values.putNull(columns[i]);
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    values.put(columns[i], cursor.getLong(i));
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    values.put(columns[i], cursor.getDouble(i));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    values.put(columns[i], cursor.getString(i));
                    break;
                case Cursor.FIELD_TYPE_BLOB:
                    values.put(columns[i], cursor.getBlob(i));
                    break;
            }
        }
        return values;
    }

}
