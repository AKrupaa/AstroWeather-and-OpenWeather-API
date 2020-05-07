package com.example.astroweathercz2;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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
    private DBManager dbManager;

    public AstroWeatherCompare(Context context) {
        dbManager = new DBManager(context);
    }

    boolean doINeedToFetchFromInternet(String name) {
        Cursor cursor;
        cursor = dbManager.fetchIDNameDate();

        for (; !cursor.isAfterLast(); cursor.moveToNext()) {
            // jezeli masz to miasto ...
            // jezeli miasto w bazie danych == miasto ktore szuka uzytkownik
            if(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)).equals(name)) {

                // to zgarnij date wpisu dla tego wlasnie miasta
                String fetchedDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_OF_INSERT));

                // porownaj aktualny czas z tym w bazie danych
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dateFromDB = sdf.parse(fetchedDate);

                    String time = sdf.format(Calendar.getInstance().getTime());
                    Date now = sdf.parse(time);

                    long timeFromDB = dateFromDB.getTime();
                    long timeNow = now.getTime();

                    long timeDifferenceMilliseconds = Math.abs(timeFromDB - timeNow);

                    // jezeli dane nie są stare tj. minelo mniej niz 30 minut od ostatniego sprawdzenia to...
                    if(timeDifferenceMilliseconds < 1000* 60 * 30)
                        dbManager.close();
                        // GET FROM DATABASE
                        return false;

                } catch (ParseException e) {
                    Log.e("PARSE-COMPARE-ERROR", e.getMessage());
                }
            }
        }

        dbManager.close();
        // FETCH FROM INTERNET
        return true;
    }


}
