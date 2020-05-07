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

//Dla Księżyca:
//        • Wchód i zachód (czas).
//        • Najbliższy nów i pełnia (data).
//        • Faza księżyca (w procentach).
//        • Dzień miesiąca synodycznego.

public class Moon extends Fragment {

    private static final String ARG_TV_MOON_RISE = "ARG_TV_MOON_RISE";
    private static final String ARG_TV_MOON_SET = "ARG_TV_MOON_SET";
    private static final String ARG_TV_NEW_MOON = "ARG_TV_NEW_MOON";
    private static final String ARG_TV_FULL_MOON = "ARG_TV_FULL_MOON";
    private static final String ARG_TV_PHASE_MOON = "ARG_TV_PHASE_MOON";
    private static final String ARG_TV_SYNODIC_MONTH_DAY = "ARG_TV_SYNODIC_MONTH_DAY";
    private static final String ARG_DELAY = "ARG_DELAY";
    private static final String ARG_ASTRONOMY = "ARG_ASTRONOMY";
    private static final String ARG_LONGITUDE = "ARG_lONGITUDE";
    private static final String ARG_LATITUDE = "ARG_LATITUDE";

    private TextView tvMoonRise, tvMoonSet, tvNewMoon, tvFullMoon, tvPhaseOfTheMoon, tvSynodicMonthDay;

    private Astronomy astronomy;
    private boolean STOP_THREAD = false;
    static int i = 0;

    public Moon() {
        // Required empty public constructor
    }

    public static Moon newInstance(Double longitude, Double latitude) {
        Moon fragment = new Moon();

        Bundle args = new Bundle();
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putDouble(ARG_LATITUDE, latitude);
        fragment.setArguments(args);

        return fragment;
    }

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(!STOP_THREAD) {
//                Log.e("TROLL1", "I'm inside runnable!");
                updateTextViewsInMoon();
                try {
//                    Log.e("The time", String.valueOf(MainActivity.getDelayInMS()));
                    Thread.sleep(MainActivity.getDelayInMS());
//                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e("MOON THREAD TROLL", e.getMessage());
                }
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            astronomy = (Astronomy) getArguments().getSerializable(ARG_ASTRONOMY);
            astronomy = new Astronomy();
            Double latitude = getArguments().getDouble(ARG_LATITUDE);
            Double longtitude = getArguments().getDouble(ARG_LONGITUDE);

            // policz z miejsca głupoty
            astronomy.setAstroCalculator(latitude, longtitude);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_moon, container, false);
        setTextViews(v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // jak już jest STWORZONE
        // ale przed tym jak użytkownik końcowy widzi to na własne oczy.

        // wyliczone głupoty w onCreate();
        // wyświetl głupoty
        updateTextViewsInMoon();
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        STOP_THREAD = true;
    }

    private void updateTextViewsInMoon() {
        ArrayList<String> moonStringsToTextViews;
        moonStringsToTextViews = astronomy.getMoonInfo();

        // zapisane w Array wartosci TextView (gotowe)
        setMoonRiseText(moonStringsToTextViews.remove(0));
        setMoonSetText(moonStringsToTextViews.remove(0));
        setNewMoonText(moonStringsToTextViews.remove(0));
        setFullMoonText(moonStringsToTextViews.remove(0));
        setPhaseOfTheMoonText(moonStringsToTextViews.remove(0));
        setSynodicMonthDayText(moonStringsToTextViews.remove(0));
    }

//    SETTERS

    private void setTextViews(View v) {
        tvMoonRise = v.findViewById(R.id.moonRise);
        tvMoonSet = v.findViewById(R.id.moonSet);
        tvNewMoon = v.findViewById(R.id.nearestNewMoon);
        tvFullMoon = v.findViewById(R.id.nearestFullMoon);
        tvPhaseOfTheMoon = v.findViewById(R.id.phaseOfTheMoon);
        tvSynodicMonthDay = v.findViewById(R.id.synodicMonthDay);
    }

    private void setMoonRiseText(String text) {
        tvMoonRise.setText(text);
    }

    private void setMoonSetText(String text) {
        tvMoonSet.setText(text);
    }

    private void setNewMoonText(String text) {
        tvNewMoon.setText(text);
    }

    private void setFullMoonText(String text) {
        tvFullMoon.setText(text);
    }

    private void setPhaseOfTheMoonText(String text) {
        tvPhaseOfTheMoon.setText(text);
    }

    private void setSynodicMonthDayText(String text) {
        tvSynodicMonthDay.setText(text);
    }
}
