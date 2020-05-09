package com.example.astroweathercz2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

//Dla Słońca:
//        • Wchód (czas i azymut).
//        • Zachód (czas i azymut).
//        • Zmierz i świt cywilny (czas).


public class Sun extends Fragment {

    private static final String ARG_TV_SUN_RISE = "ARG_TV_SUN_RISE";
    private static final String ARG_TV_SUN_RISE_AZIMUTH = "ARG_TV_SUN_RISE_AZIMUTH";
    private static final String ARG_TV_SUN_SET = "ARG_TV_SUN_SET";
    private static final String ARG_TV_SUN_SET_AZIMUTH = "ARG_TV_SUN_SET_AZIMUTH";
    private static final String ARG_TV_SUN_TWILIGHT = "ARG_TV_SUN_TWILIGHT";
    private static final String ARG_TV_SUN_CIVIL_DAWN = "ARG_TV_SUN_CIVIL_DAWN";
    private static final String ARG_DELAY = "ARG_DELAY";
    private static final String ARG_ASTRONOMY = "ARG_ASTRONOMY";
    private static final String ARG_LONGITUDE = "ARG_lONGITUDE";
    private static final String ARG_LATITUDE = "ARG_LATITUDE";

    private TextView tvSunRise, tvSunRiseAzimuth, tvSunSet, tvSunSetAzimuth, tvSunTwilight, tvSunCivilDawn;

    private static long delay;
    private boolean STOP_THREAD = false;

    public Sun() {
        // Required empty public constructor
    }

    public static Sun newInstance() {
        Sun fragment = new Sun();
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
        View v = inflater.inflate(R.layout.fragment_sun, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

//    SETTERS

    public void setSunRiseText(String text) { tvSunRise.setText(text); }

    public void setSunRiseAzimuthText(String text) {
        tvSunRiseAzimuth.setText(text);
    }

    public void setSunSetText(String text) {
        tvSunSet.setText(text);
    }

    public void setSunSetAzimuthText(String text) {
        tvSunSetAzimuth.setText(text);
    }

    public void setSunTwilightText(String text) {
        tvSunTwilight.setText(text);
    }

    public void setSunCivilDawnText(String text) {
        tvSunCivilDawn.setText(text);
    }
}