package com.example.astroweathercz2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements Options.IOptionsListener {

    private static final String ARG_IS_TABLET = "ARG_IS_TABLET";
    private static final String ARG_CONFIRM_OPTION_CLICKED = "ARG_CONFIRM_OPTION_CLICKED";
    private static final String ARG_ASTRONOMY = "ARG_ASTRONOMY";
    private static final String ARG_DELAY_IN_MS = "ARG_DELAY_IN_MS";
    private static final String ARG_FRAGMENT_OPTIONS = "ARG_FRAGMENT_OPTIONS";
    private static final String ARG_FRAGMENT_MOON = "ARG_FRAGMENT_MOON";
    private static final String ARG_FRAGMENT_SUN = "ARG_FRAGMENT_SUN";
    private static final String ARG_FRAGMENT_RESULT = "ARG_FRAGMENT_RESULT";
    private static final String ARG_LONGTITUDE = "ARG_LONGTITUDE";
    private static final String ARG_LATITUDE = "ARG_LATITUDE";

    private static final String[] TAB = {"Options", "Moon", "Sun", "Result"};

    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private ViewPager2 viewPager2;
    private TextView tvActualTime;
    private TextView tvActualLocalization;

    private Double longtitude;
    private Double latitude;
    private static long delayInMS;
    private boolean confirmOptionClicked = false;
    private boolean STOP_THREAD = false;
//    ContentValues contentValues;


    private Thread threadTime = new Thread() {
        @Override
        public void run() {
            try {
                while (!threadTime.isInterrupted() || !STOP_THREAD) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setActualTime();
                        }
                    });
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Log.e("Problem z watkiem", "Problem z watkiem od czasu/daty");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTextViews();
        threadTime.start();

        viewPager2 = findViewById(R.id.view_pager);
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerFragmentAdapter.addFragment(Options.newInstance());

        // i basta.
        if (savedInstanceState != null) {
            confirmOptionClicked = savedInstanceState.getBoolean(ARG_CONFIRM_OPTION_CLICKED);
            delayInMS = savedInstanceState.getLong(ARG_DELAY_IN_MS);

            if(confirmOptionClicked) {
                latitude = savedInstanceState.getDouble(ARG_LATITUDE);
                longtitude = savedInstanceState.getDouble(ARG_LONGTITUDE);
                // ustaw szerokosc i dlugosc geo.
                tvActualLocalization.setText(String.format("Latitude %s Longtitude %s", latitude, longtitude));
            }

//            astronomy = (Astronomy) savedInstanceState.getSerializable(ARG_ASTRONOMY);
            if (isTablet(getApplicationContext()) && confirmOptionClicked) {
                viewPagerFragmentAdapter.addFragment(Result.newInstance(longtitude, latitude));
            }
            if (!isTablet(getApplicationContext()) && confirmOptionClicked) {
                viewPagerFragmentAdapter.addFragment(Moon.newInstance());
                viewPagerFragmentAdapter.addFragment(Sun.newInstance());
            }
        }

        viewPager2.setAdapter(viewPagerFragmentAdapter);

        JSONRequest jsonRequest = new JSONRequest(this);

        jsonRequest.jsonParse("London", new JSONRequest.VolleyRequestCallback() {
            @Override
            public void onSuccessResponse(ContentValues contentValuesResult) {
                Log.e("blad?" , contentValuesResult.getAsString(DatabaseHelper.NAME));
            }
        });

//        contentValues = jsonRequest.getContentValues();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(ARG_CONFIRM_OPTION_CLICKED, confirmOptionClicked);
        outState.putLong(ARG_DELAY_IN_MS, delayInMS);

        try {
            outState.putDouble(ARG_LATITUDE, latitude);
            outState.putDouble(ARG_LONGTITUDE, longtitude);
        } catch (Exception ex) {
            Log.i("Oszustwo", "Mnie nie oszukasz, i tyle w temacie");
        }
    }

    @Override
    public void onConfirmOptions(String sLongitude, String sLatitude, String delayTime, String nameOfCity) {

        delayInMS = Long.parseLong(delayTime.replaceAll(" ", "")) * 1000 * 60;

        if(nameOfCity!=null)
            tvActualLocalization.setText(nameOfCity);

        if(sLongitude != null && sLatitude != null) {
            longtitude = Double.valueOf(sLongitude);
            latitude = Double.valueOf(sLatitude);
            // ustaw długość i szerokośc geograficzną
            tvActualLocalization.setText(String.format("Latitude %s Longtitude %s", latitude, longtitude));
        }

        //TODO: tutaj weryfikacja danych - są w bazie? nie ma w bazie?
        // jak nie ma -> pobierz z neta
        // jak są ale stare -> pobierz z neta
        // jak są aktualne -> pobierz z BAZY!


//

//
//        if(!confirmOptionClicked) {
//            if (isTablet(getApplicationContext())) {
//                // dodaj tylko fragment result
//                viewPagerFragmentAdapter.addFragment(Result.newInstance(longtitude, latitude));
//                viewPagerFragmentAdapter.notifyDataSetChanged();
//            } else {
//                // dodaj fragment moon i sun
//                viewPagerFragmentAdapter.addFragment(Moon.newInstance(longtitude, latitude));
//                viewPagerFragmentAdapter.addFragment(Sun.newInstance(longtitude, latitude));
//                viewPagerFragmentAdapter.notifyDataSetChanged();
//            }
//        } else {
//            if (isTablet(getApplicationContext())) {
//                // podmien result fragment
//                viewPagerFragmentAdapter.replaceFragment(Result.newInstance(longtitude, latitude), 1);
//                viewPagerFragmentAdapter.notifyDataSetChanged();
//            } else {
//                // podmien moon i sun fragment
//                viewPagerFragmentAdapter.replaceFragment(Moon.newInstance(longtitude, latitude), 1);
//                viewPagerFragmentAdapter.replaceFragment(Sun.newInstance(longtitude, latitude), 2);
//                viewPagerFragmentAdapter.notifyDataSetChanged();
//            }
        }

//        confirmOptionClicked = true;
//    }

    private void setActualTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss"); // Format time
        String time = df.format(Calendar.getInstance().getTime());
        tvActualTime.setText(String.format("%s ", time));
    }

    void setTextViews() {
        tvActualTime = findViewById(R.id.textViewActualTime);
        tvActualLocalization = findViewById(R.id.textViewActualLocalization);
    }

    private boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        STOP_THREAD = true;
    }

    // getters / setters

    static public long getDelayInMS() {
        return delayInMS;
    }
}
