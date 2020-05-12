package com.example.astroweathercz2;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Options.IOptionsListener {
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private ViewPager2 viewPager2;
    private TextView tvActualTime;
    private TextView tvActualLocalization;
    private ProgressBar progressBar;
//    private boolean confirmOptionClicked = false;
//    private boolean STOP_THREAD = false;
    private DBManager dbManager;

    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTextViews();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        viewPager2 = findViewById(R.id.view_pager);
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());


//        if (isTablet(getApplicationContext()) && confirmOptionClicked) {
//
//        }
//
//        if (!isTablet(getApplicationContext()) && confirmOptionClicked) {
//
//        }

        viewPager2.setAdapter(viewPagerFragmentAdapter);
        viewPagerFragmentAdapter.addFragment(Options.newInstance());
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
    }

    @Override
    public void onConfirmOptions(String sLongitude, String sLatitude, /*String delayTime,*/ final String nameOfCity, boolean force) {
        if (nameOfCity != null && !force)
            nameActivation(nameOfCity);
        else if (sLongitude != null && sLatitude != null && !force) {
            lonLatActivation(sLongitude, sLatitude);
        } else if(nameOfCity != null && force) {
            forceNameActivation(nameOfCity);
        } else if(sLongitude != null && sLatitude != null && force) {
            lonLatActivation(sLatitude, sLongitude);
        }
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

    // getters / setters

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        STOP_THREAD = true;
        if (dbManager != null)
            dbManager.close();
        Log.e("ON DESTROY", "Zamknieto baze danych, wylaczono program");
    }

    private void nameActivation(final String nameOfCity) {
        Log.e("nameActivation", "nameActivation");
        // blokuje ekran progress barem
        viewPager2.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // otworz baze danych
//        final DBManager dbManager = new DBManager(this);
        if (dbManager == null) {
            dbManager = new DBManager(this);
            dbManager.open();
        }

        // sprawdz czy istnieje name w bazie danych
        final AstroWeatherCompare astroWeatherCompare = new AstroWeatherCompare();
        // pobierz z bazy danych potrzebne rekordy!
        final Cursor cursor = dbManager.fetchIDNameDate();
//        Sprawdz czy istnieje "name"
//        Jezeli istnieje, porownaj czas ostatniej akt.
//        Jezeli >30 min: zwroc true
//        if false == get from database
//        if true == fetch from Internet
        Log.e("CHECKING", "Czy trzeba pobierac dane z Internetu?");
        boolean fetchFromInternet = astroWeatherCompare.doINeedToFetchFromInternet(nameOfCity, cursor);
        Log.e("CHECKING", "Pobieranie z Internetu?: " + fetchFromInternet);

        if (fetchFromInternet) {
            Log.e("INTERNET", "Pobieranie danych z Internetu...");
            // pobieraj z Internetu!
            final JSONRequest jsonRequest = new JSONRequest(this);
            jsonRequest.getResponse(nameOfCity, new JSONRequest.VolleyRequestCallback() {
                @Override
                public void onSuccessResponse(JSONObject response) {
                    Log.e("INTERNET", "Zebrano dane z Internetu");
                    // wez to teraz przetlumacz na dane, na których będziesz pracować
                    jsonRequest.jsonParse(response, new JSONRequest.VolleyParseCallback() {
                        @Override
                        public void onSuccessResult(ContentValues result) {
                            Log.e("INTERNET", "Parsowanie JSONA powiodlo sie!");
                            // znalazłeś miasto, masz dane i w ogóle jesteś szczęśliwy :-)
                            // data was found in WEB, you are happy now -)

                            // sprawdz czy w bazie istnieje takie cudo
                            try {
                                // istnieje -> akutalizacja
                                Log.e("INTERNET", "Sprawdzam czy istnieje miasto w bazie danych...");
                                long ID = astroWeatherCompare.IDOfCityName(nameOfCity, cursor);
                                Log.e("INTERNET", "Miasto istnieje w bazie danych");
                                int affected = dbManager.update(ID, result);
                                Log.e("INTERNET", "Zaaktualizowano " + affected + " wierszy w bazie danych!");
                                // wrzuc do SharedViewModel (Container)
                                sharedViewModel.setSharedData(result);
                            } catch (Exception e) {
                                // nie istnieje
                                Log.e("INTERNET", "Miasto nie istnieje w bazie danych!");
                                Log.e("FOUND NO row ", e.getMessage());
                                long rowID = dbManager.insert(result);
                                Log.e("INTERNET", "Wprowadzono nowe miasto " + nameOfCity + "; jego ID " + rowID + "; do bazy danych!");
                                // wrzuc do SharedViewModel (Container)
                                sharedViewModel.setSharedData(result);
                            }

                            Log.e("INTERNET", "Tworze fragmenty!");


                            viewPagerFragmentAdapter.addFragment(Information.newInstance());
//                            viewPagerFragmentAdapter.notifyItemChanged(1);
//                            viewPagerFragmentAdapter.notifyDataSetChanged();


                            Log.e("INTERNET", "Fragmenty utworzone!");
                            // zwolniam ekran wczytywania
                            Log.e("INTERNET", "Zmiana ekranu...");
                            viewPager2.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Log.e("INTERNET", "Ekran widoczny");
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Miasto odnalezione, sukces!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    // coś gościu źle wprowadził
                    // wypisz mu blad
                    Log.e("Volley", "Pobieranie danych nie powiodło się!");
                    Log.e("Volley", "Error 404 - NOT FOUND");
                    Log.e("Volley", "Dane nie prawidlowe!");
//                    Log.e("Volley", error.getStackTrace().toString());

                    Log.e("Volley", "Zmiana ekranu...");
                    // zwolniam ekran wczytywania
                    viewPager2.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Log.e("INTERNET", "Ekran widoczny");
                    Toast.makeText(getApplicationContext(), "ERROR 404", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // pobierz dane z bazy danych
            Log.e("NO INTERNET", "Pobieranie danych z bazy danych");
            Cursor c = dbManager.fetchAll();
            Log.e("NO INTERNET", "Pobrano dane z bazy danych!");
            try {
                Log.e("NO INTERNET", "Szukanie ID dla miasta " + nameOfCity);
                long searchID = astroWeatherCompare.IDOfCityName(nameOfCity, c);
                Log.e("NO INTERNET", "Znaleziono ID " + searchID + " dla miasta " + nameOfCity);
                Log.e("NO INTERNET", "Pobieranie wszystkich kolumn dla ID " + searchID);
                Cursor cursor1 = dbManager.fetchWhereID(searchID);
                ContentValues contentValues = dbManager.cursorRowToContentValues(cursor1);
                Log.e("NO INTERNET", "Zamiana danych na typ ContentValues");
//                sharedViewModel.clearSharedData();
                sharedViewModel.setSharedData(contentValues);
                Log.e("NO INTERNET", "Proba utworzenia fragmentow");


                viewPagerFragmentAdapter.addFragment(Information.newInstance());
//                viewPagerFragmentAdapter.notifyItemChanged(1);
//                viewPagerFragmentAdapter.notifyDataSetChanged();


                Log.e("NO INTERNET", "Fragmenty utworzone");
                // zwolniam ekran wczytywania
                viewPager2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.e("NO INTERNET", "Ekran widoczny!");
                Toast.makeText(getApplicationContext(), "Sukces", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("FetchDB not found ID", e.getMessage());
                Log.e("NO INTERNET EX", "Nie znaleziono ID <padla logika>");
            }
        }
    }

    private void lonLatActivation(String lat, String lon) {
        // blokuje ekran progress barem
        Log.e("lonLatActivation", "lonLatActivation");
        Log.e("BLOCKING VIEW", "Blokowanie widoku");
        viewPager2.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Log.e("CHECKING", "Pobieranie z Internetu?: true");

        Log.e("INTERNET", "Pobieranie danych z Internetu...");
        // pobieraj z Internetu!
        final JSONRequest jsonRequest = new JSONRequest(this);
        jsonRequest.onResponseLatLon(lat, lon, new JSONRequest.VolleyRequestCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.e("INTERNET", "Zebrano dane z Internetu");
                // wez to teraz przetlumacz na dane, na których będziesz pracować
                jsonRequest.jsonParse(response, new JSONRequest.VolleyParseCallback() {
                    @Override
                    public void onSuccessResult(ContentValues result) {
                        Log.e("INTERNET", "Parsowanie JSONA powiodlo sie!");
                        // wrzuc do SharedViewModel (Container)
                        sharedViewModel.setSharedData(result);

                        Log.e("INTERNET", "Tworze fragmenty!");


                        viewPagerFragmentAdapter.addFragment(Information.newInstance());
//                        viewPagerFragmentAdapter.notifyItemChanged(1);
//                        viewPagerFragmentAdapter.notifyDataSetChanged();


                        Log.e("INTERNET", "Fragmenty utworzone!");
                        // zwolniam ekran wczytywania
                        Log.e("INTERNET", "Zmiana ekranu...");
                        viewPager2.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Log.e("INTERNET", "Ekran widoczny");
                        Toast.makeText(getApplicationContext(), "Sukces!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                // coś gościu źle wprowadził
                // wypisz mu blad
                Log.e("Volley", "Pobieranie danych nie powiodło się!");
                Log.e("Volley", "Error 404 - NOT FOUND");
                Log.e("Volley", "Dane nie prawidlowe!");
//                    Log.e("Volley", error.getStackTrace().toString());

                Log.e("Volley", "Zmiana ekranu...");
                // zwolniam ekran wczytywania
                viewPager2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.e("INTERNET", "Ekran widoczny");

                Toast.makeText(getApplicationContext(), "ERROR 404", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void forceNameActivation(final String nameOfCity) {
        Log.e("forceNameActivation", "forceNameActivation");
        // blokuje ekran progress barem
        viewPager2.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // otworz baze danych
//        final DBManager dbManager = new DBManager(this);
        if (dbManager == null) {
            dbManager = new DBManager(this);
            dbManager.open();
        }
        Log.e("INTERNET", "Pobieranie danych z Internetu...");
        // pobieraj z Internetu!
        final JSONRequest jsonRequest = new JSONRequest(this);
        jsonRequest.getResponse(nameOfCity, new JSONRequest.VolleyRequestCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.e("INTERNET", "Zebrano dane z Internetu");
                jsonRequest.jsonParse(response, new JSONRequest.VolleyParseCallback() {
                    @Override
                    public void onSuccessResult(ContentValues result) {
                        // sprawdz czy w bazie istnieje takie cudo
                        try {
                            // istnieje -> akutalizacja
                            Log.e("INTERNET", "Sprawdzam czy istnieje miasto w bazie danych...");
                            final Cursor cursor = dbManager.fetchIDNameDate();
                            long ID = new AstroWeatherCompare().IDOfCityName(nameOfCity, cursor);
                            Log.e("INTERNET", "Miasto istnieje w bazie danych");
                            int affected = dbManager.update(ID, result);
                            Log.e("INTERNET", "Zaaktualizowano " + affected + " wierszy w bazie danych!");
                            // wrzuc do SharedViewModel (Container)
                            sharedViewModel.setSharedData(result);
                        } catch (Exception e) {
                            // nie istnieje
                            Log.e("INTERNET", "Miasto nie istnieje w bazie danych!");
                            Log.e("FOUND NO row ", e.getMessage());
                            long rowID = dbManager.insert(result);
                            Log.e("INTERNET", "Wprowadzono nowe miasto " + nameOfCity + "; jego ID " + rowID + "; do bazy danych!");
                            // wrzuc do SharedViewModel (Container)
                            sharedViewModel.setSharedData(result);
                        }

                        Log.e("INTERNET", "Tworze fragmenty!");

                        viewPagerFragmentAdapter.addFragment(Information.newInstance());

                        Log.e("INTERNET", "Fragmenty utworzone!");
                        // zwolniam ekran wczytywania
                        Log.e("INTERNET", "Zmiana ekranu...");
                        viewPager2.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Log.e("INTERNET", "Ekran widoczny");
                    }
                });
                Toast.makeText(getApplicationContext(), "FORCE: Miasto odnalezione, sukces!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                // coś gościu źle wprowadził
                // wypisz mu blad
                Log.e("Volley", "Pobieranie danych nie powiodło się!");
                Log.e("Volley", "Error 404 - NOT FOUND");
                Log.e("Volley", "Dane nie prawidlowe!");
//                    Log.e("Volley", error.getStackTrace().toString());

                Log.e("Volley", "Zmiana ekranu...");
                // zwolniam ekran wczytywania
                viewPager2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.e("INTERNET", "Ekran widoczny");
                Toast.makeText(getApplicationContext(), "ERROR 404", Toast.LENGTH_LONG).show();
            }
        });
    }
}
