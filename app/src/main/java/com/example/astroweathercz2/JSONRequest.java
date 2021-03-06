package com.example.astroweathercz2;


//    Endpoint:
//- Please, use the endpoint api.openweathermap.org for your API calls
//- Example of API call:
//api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=89535a3904f21dd32e9cdbf11894c1a0

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

//API call:
//api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}

//By geographic coordinates
//API call:
//api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={your api key}


//java.lang.RuntimeException: Bad URL api.openweathermap.org/data/2.5/weather?q=London&appid=89535a3904f21dd32e9cdbf11894c1a0

public class JSONRequest {
    private String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private String BASE_CORDS_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private String APP_ID = "89535a3904f21dd32e9cdbf11894c1a0";
    private String UNITS = "&units=metric&";
    private RequestQueue mQueue;

    public JSONRequest(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    public void getResponse(String city, final VolleyRequestCallback volleyCallback) {
        String url = BASE_URL + city + UNITS + "&appid=" + APP_ID + "&lang=en";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                volleyCallback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallback.onErrorResponse(error);
            }
        });
        mQueue.add(request);
    }


    public void onResponseLatLon(String lat, String lon, final VolleyRequestCallback volleyCallback) {
//        api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={your api key}
//        https://api.openweathermap.org/data/2.5/weather?lat=19&lon=51&units=metric&appid=89535a3904f21dd32e9cdbf11894c1a0&lang=en
        String url = BASE_CORDS_URL + "lat=" + lat + "&lon=" + lon + UNITS + "appid=" + APP_ID + "&lang=en";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                volleyCallback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallback.onErrorResponse(error);
            }
        });
        mQueue.add(request);
    }

    public void jsonParse(JSONObject response, final VolleyParseCallback parseCallback) {
        try {
            // WARTO!
            ContentValues contentValues = new ContentValues();

            JSONObject coord = response.getJSONObject("coord");

            int lon = coord.getInt("lon");
            int lat = coord.getInt("lat");

            contentValues.put(DatabaseHelper.LON, lon);
            contentValues.put(DatabaseHelper.LAT, lat);

            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
            String description = weather.getString("description");

            contentValues.put(DatabaseHelper.DESCRIPTION, description);

            JSONObject main = response.getJSONObject("main");
            int temp = main.getInt("temp");
            int feelsLike = main.getInt("feels_like");
            int temp_min = main.getInt("temp_min");
            int temp_max = main.getInt("temp_max");
            int pressure = main.getInt("pressure");
            int humidity = main.getInt("humidity");

            contentValues.put(DatabaseHelper.TEMPERATURE, temp);
            contentValues.put(DatabaseHelper.FEELS_LIKE, feelsLike);
            contentValues.put(DatabaseHelper.TEMP_MIN, temp_min);
            contentValues.put(DatabaseHelper.TEMP_MAX, temp_max);
            contentValues.put(DatabaseHelper.PRESSURE, pressure);
            contentValues.put(DatabaseHelper.HUMIDITY, humidity);

            JSONObject wind = response.getJSONObject("wind");
            int speed = wind.getInt("speed");

            // OJ TAK 08-05-2020 - ZMIANA STRUKTURY DANYCH JSONA, OJ TAK ....................
            // [*]
            // "WCZORAJ DZIALALO!"
//                    int deg = wind.getInt("deg");

            contentValues.put(DatabaseHelper.SPEED, speed);
//                    contentValues.put(DatabaseHelper.DEG, deg);

            JSONObject sys = response.getJSONObject("sys");
            int sunrise = sys.getInt("sunrise");
            int sunset = sys.getInt("sunset");

            contentValues.put(DatabaseHelper.SUNRISE, sunrise);
            contentValues.put(DatabaseHelper.SUNSET, sunset);

            int timezone = response.getInt("timezone");

            contentValues.put(DatabaseHelper.TIMEZONE, timezone);

            int time = response.getInt("dt");
            contentValues.put(DatabaseHelper.TIME, time);

            // You are asking for Lodz, yeah?
            // Yeah
            // Here you are: łódź
            // Are you serious?

            String name = response.getString("name");
            name = name.toLowerCase();
            String convertedString = name
                    .replaceAll("ą", "a")
                    .replaceAll("ę", "e")
                    .replaceAll("ó", "o")
                    .replaceAll("ś", "s")
                    .replaceAll("ł", "l")
                    .replaceAll("ż", "z")
                    .replaceAll("ź", "z")
                    .replaceAll("ć", "c")
                    .replaceAll("ń", "n")
                    .replaceAll(" ", "");
            contentValues.put(DatabaseHelper.NAME, convertedString);

            // actual date *new element
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            contentValues.put(DatabaseHelper.DATE_OF_INSERT, date);

            parseCallback.onSuccessResult(contentValues);

        } catch (JSONException e) {
            Log.e("JSONRequest Exception", e.getMessage());
            e.printStackTrace();
        }
    }

    public interface VolleyRequestCallback {
        void onSuccessResponse(JSONObject response);

        void onErrorResponse(VolleyError error);
    }


    public interface VolleyParseCallback {
        void onSuccessResult(ContentValues result);
    }
}
