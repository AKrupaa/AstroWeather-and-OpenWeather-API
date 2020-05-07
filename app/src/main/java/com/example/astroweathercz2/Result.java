package com.example.astroweathercz2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/* przykladowy plik ze wszystkim, jak to powinno wygladac
 * jak przechowywac parametry
 * jak przesylac je spowrotem - patrz interface
 * jak robic aby sie nie narobić */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Result #newInstance} factory method to
 * create an instance of this fragment.
 */
public class Result extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ASTRONOMY = "ASTRONOMY";
    private static final String ARG_LONGITUDE = "ARG_lONGITUDE";
    private static final String ARG_LATITUDE = "ARG_LATITUDE";

    //    private Astronomy astronomy;
    private Double latitude;
    private Double longtitude;

    public Result() {
        // Required empty public constructor
    }

    public static Result newInstance(Double longitude, Double latitude) {
        Result fragment = new Result();

        Bundle args = new Bundle();
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putDouble(ARG_LATITUDE,latitude);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            latitude = getArguments().getDouble(ARG_LATITUDE);
            longtitude = getArguments().getDouble(ARG_LONGITUDE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //child fragment
        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
//        ChildFragment fragB = new ChildFragment ();
//        Moon fragA  = new Moon();
//        Sun fragB = Sun.newInstance();

        childFragTrans.add(R.id.fragment1_container, Moon.newInstance(longtitude, latitude));
//        childFragTrans.replace(R.id.fragment1_container, fragB);
        childFragTrans.addToBackStack("A");
        childFragTrans.add(R.id.fragment2_container, Sun.newInstance(longtitude, latitude));
        childFragTrans.addToBackStack("B");
        childFragTrans.commit();
    }
}