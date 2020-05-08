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
import androidx.lifecycle.ViewModelProvider;

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

    private boolean STOP_THREAD = false;

    public Moon() {
        // Required empty public constructor
    }

    public static Moon newInstance() {
        Moon fragment = new Moon();

        Bundle args = new Bundle();
//        args.putDouble(ARG_LONGITUDE, longitude);
//        args.putDouble(ARG_LATITUDE, latitude);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
//        setFullMoonText(String.valueOf(model.getMyInteger()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        STOP_THREAD = true;
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
