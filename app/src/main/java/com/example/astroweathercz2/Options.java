package com.example.astroweathercz2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Options extends Fragment {
    //TODO: auto complete suggestions and database
    private IOptionsListener listener;
    private EditText inputedLongitude;
    private EditText inputedLatitude;
    private EditText inputedCity;
//    private Spinner spinner;

    public Options() {
        // Required empty public constructor
    }

    public static Options newInstance() {
        // konstruktor, wszystko spoko
        Options fragment = new Options();
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
        View v = inflater.inflate(R.layout.fragment_options, container, false);

        Button confirmButton = v.findViewById(R.id.buttonConfirm);
        Button forceButton = v.findViewById(R.id.buttonForce);
        inputedLongitude = v.findViewById(R.id.inputedLongitude);
        inputedLatitude = v.findViewById(R.id.inputedLatitude);
//        spinner = v.findViewById(R.id.spinnerTime);
        inputedCity = v.findViewById(R.id.nameOfCity);
//        addItemsToTimeSpinner();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String readLong = inputedLongitude.getText().toString();
                String readLati = inputedLatitude.getText().toString();
//                String readTime = String.valueOf(spinner.getSelectedItem()).substring(0, 2);
                String nameOfCity = inputedCity.getText().toString();

                if (nameOfCity.length() > 1) {
                    nameOfCity = nameOfCity.toLowerCase().replaceAll(" ", "");
                    listener.onConfirmOptions(null, null, /*readTime,*/ nameOfCity,false);
                } else if (readLong.length() > 0 && readLati.length() > 0)
                    listener.onConfirmOptions(readLong, readLati, /*readTime,*/ null,false);
                else
                    Toast.makeText(getContext(), "Wypełnij dane albo nic z tego!", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        forceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String readLong = inputedLongitude.getText().toString();
                String readLati = inputedLatitude.getText().toString();
//                String readTime = String.valueOf(spinner.getSelectedItem()).substring(0, 2);
                String nameOfCity = inputedCity.getText().toString();

                if (nameOfCity.length() > 1) {
                    nameOfCity = nameOfCity.toLowerCase().replaceAll(" ", "");
                    listener.onConfirmOptions(null, null, /*readTime,*/ nameOfCity, true);
                } else if (readLong.length() > 0 && readLati.length() > 0)
                    listener.onConfirmOptions(readLong, readLati, /*readTime,*/ null,true);
                else
                    Toast.makeText(getContext(), "Wypełnij dane albo nic z tego!", Toast.LENGTH_SHORT).show();
//                }
            }
        });


        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        jeżeli nasza aktywność implementuje IFragmentAListener
        if (context instanceof IOptionsListener) {
            listener = (IOptionsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " musisz zaimplementowac IFragment1Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

//    private void addItemsToTimeSpinner() {
//        List<String> timeDelayList = new ArrayList<String>();
//        timeDelayList.add("1 minuta");
//        timeDelayList.add("2 minuty");
//        timeDelayList.add("5 minut");
//        timeDelayList.add("10 minut");
//        timeDelayList.add("30 minut");
////        Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, timeDelayList);
////        Specify the layout to use when the list of choices appears
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        Apply the adapter to the spinner
//        spinner.setAdapter(dataAdapter);
//    }

    public interface IOptionsListener {
        void onConfirmOptions(String sLongitude, String sLatitude, /*String delayTime,*/ String nameOfCity, boolean force);
    }

}
