package com.example.astroweathercz2;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Information extends Fragment {
    private TextView name;
    private TextView lonlat;
    private TextView time;
    private TextView temperature;
    private TextView feelsLike;
    private TextView tempMin;
    private TextView tempMax;
    private TextView pressure;
    private TextView description;
    private TextView windSpeed;
    private TextView sunRise;
    private TextView sunSet;


    public Information() {
        // Required empty public constructor
    }

    public static Information newInstance() {
        Information fragment = new Information();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_information, container, false);
        initialization(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        ContentValues contentValues = model.getSharedData();

        name.setText(String.format("City: %s", contentValues.getAsString(DatabaseHelper.NAME)));
        lonlat.setText(String.format("Lon: %s Lat: %s", contentValues.getAsString(DatabaseHelper.LON), contentValues.getAsString(DatabaseHelper.LAT)));
        //        time.setText(String.format("Time: %s", contentValues.getAsString(DatabaseHelper.TIMEZONE)));
//        time.setText(String.format("Time: %s", new Date()));
        temperature.setText(String.format("Temperature: %s", contentValues.getAsString(DatabaseHelper.TEMPERATURE)));
        feelsLike.setText(String.format("Feels like: %s", contentValues.getAsString(DatabaseHelper.FEELS_LIKE)));
        tempMax.setText(String.format("Max temperature: %s", contentValues.getAsString(DatabaseHelper.TEMP_MAX)));
        tempMin.setText(String.format("Min temperature: %s", contentValues.getAsString(DatabaseHelper.TEMP_MIN)));
        pressure.setText(String.format("Pressure: %s", contentValues.getAsString(DatabaseHelper.PRESSURE)));
        description.setText(String.format("Description: %s", contentValues.getAsString(DatabaseHelper.DESCRIPTION)));
        windSpeed.setText(String.format("Wind speed: %s", contentValues.getAsString(DatabaseHelper.SPEED)));
        long unixTimestamp = contentValues.getAsLong(DatabaseHelper.SUNRISE);
        long javaTimestamp = unixTimestamp * 1000L;
        Date date = new Date(javaTimestamp);
        String sunrise = new SimpleDateFormat("MM:dd hh:mm").format(date);
        sunRise.setText(String.format("Sun rise: %s UTC", sunrise));

        unixTimestamp = contentValues.getAsLong(DatabaseHelper.SUNSET);
        javaTimestamp = unixTimestamp * 1000L;
        date = new Date(javaTimestamp);
        String sunset = new SimpleDateFormat("MM:dd hh:mm").format(date);
        sunSet.setText(String.format("Sun set: %s UTC", sunset));

        unixTimestamp = contentValues.getAsLong(DatabaseHelper.TIME);
        javaTimestamp = unixTimestamp * 1000L;
        date = new Date(javaTimestamp);
        String fetchedTime = new SimpleDateFormat("MM:dd hh:mm").format(date);
        time.setText(String.format("Time: %s UTC", fetchedTime));
    }

    private void initialization(View v) {
        name = v.findViewById(R.id.name);
        lonlat = v.findViewById(R.id.lonlat);
        time = v.findViewById(R.id.tvTime);
        temperature = v.findViewById(R.id.temperature);
        feelsLike = v.findViewById(R.id.feelslike);
        tempMin = v.findViewById(R.id.tempmin);
        tempMax = v.findViewById(R.id.tempmax);
        pressure = v.findViewById(R.id.pressure);
        description = v.findViewById(R.id.description);
        windSpeed = v.findViewById(R.id.windspeed);
        sunRise = v.findViewById(R.id.sunrise);
        sunSet = v.findViewById(R.id.sunset);
    }
}
