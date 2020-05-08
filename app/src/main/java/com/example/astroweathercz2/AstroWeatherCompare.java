package com.example.astroweathercz2;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//1. pobierz z bazy danych
//2. sprawdz date z bazy danych z aktualnym czasem
//3. jezeli rozni sie o godzine
//4. zgarnij z neta
//5. podmiec baze danych

public class AstroWeatherCompare {

    public AstroWeatherCompare() {
    }

    boolean doINeedToFetchFromInternet(String name, Cursor cur) {
        Cursor cursor = cur;
        cursor.moveToFirst();

        for (; !cursor.isAfterLast(); cursor.moveToNext()) {
            // jezeli masz to miasto ...
            // jezeli miasto w bazie danych == miasto ktore szuka uzytkownik
            String value = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME));
            if (value.equals(name)) {

                // to zgarnij date wpisu dla tego wlasnie miasta
                String fetchedDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_OF_INSERT));

                // porownaj aktualny czas z tym w bazie danych
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date dateFromDB = null;
                try {
                    dateFromDB = sdf.parse(fetchedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String time = sdf.format(Calendar.getInstance().getTime());
                Date now = null;

                try {
                    now = sdf.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long timeFromDB = dateFromDB.getTime();
                long timeNow = now.getTime();

                long timeDifferenceMilliseconds = Math.abs(timeFromDB - timeNow);

                // jezeli dane nie są stare tj. minelo mniej niz 30 minut od ostatniego sprawdzenia to...
                // GET FROM DATABASE
                if (timeDifferenceMilliseconds < (1000 * 60 * 30))
                    return false;

            }
        }

        // FETCH FROM INTERNET
        return true;
    }


    public long IDOfCityName(String name, Cursor cursor) throws Exception {
        cursor.moveToFirst();
        for (; !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)).equals(name)) {
                // ID
                long ID = cursor.getLong(cursor.getColumnIndex(DatabaseHelper._ID));
//                Toast.makeText(null, "ID of City Name " + ID, Toast.LENGTH_LONG).show();
                return ID;
            }
        }
        throw new Exception("There is not any city " + name + " in database");
    }
}
