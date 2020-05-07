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
    private String APP_ID = "89535a3904f21dd32e9cdbf11894c1a0";
    private String UNITS = "&units=metric&";
    private RequestQueue mQueue;

    public JSONRequest(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    public interface VolleyRequestCallback {
        void onSuccessResponse(ContentValues contentValuesResult);
    }

    public void jsonParse(String city, final VolleyRequestCallback volleyRequestCallback) {
        String url = BASE_URL + city + UNITS + "&appid=" + APP_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ContentValues contentValues = new ContentValues();

                    JSONObject coord = response.getJSONObject("coord");

                    int lon = coord.getInt("lon");
                    int lat = coord.getInt("lat");

                    contentValues.put(DatabaseHelper.LON, lon);
                    contentValues.put(DatabaseHelper.LAT, lat);

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
                    int deg = wind.getInt("deg");

                    contentValues.put(DatabaseHelper.SPEED, speed);
                    contentValues.put(DatabaseHelper.DEG, deg);

                    JSONObject sys = response.getJSONObject("sys");
                    int sunrise = sys.getInt("sunrise");
                    int sunset = sys.getInt("sunset");

                    contentValues.put(DatabaseHelper.SUNRISE, sunrise);
                    contentValues.put(DatabaseHelper.SUNSET, sunset);

                    int timezone = response.getInt("timezone");

                    contentValues.put(DatabaseHelper.TIMEZONE, timezone);

                    String name = response.getString("name");

                    contentValues.put(DatabaseHelper.NAME, name);

                    // actual date *new element
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sdf.format(new Date());
                    contentValues.put(DatabaseHelper.DATE_OF_INSERT, date);

                    volleyRequestCallback.onSuccessResponse(contentValues);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.getMessage());
            }
        });
        mQueue.add(request);
    }
}
