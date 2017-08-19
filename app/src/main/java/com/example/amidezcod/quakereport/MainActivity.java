package com.example.amidezcod.quakereport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.amidezcod.quakereport.Model.Example;
import com.example.amidezcod.quakereport.Model.Feature;
import com.example.amidezcod.quakereport.Model.Properties;
import com.example.amidezcod.quakereport.Prefrences.SettingsActivity;
import com.example.amidezcod.quakereport.adapter.EarthQuakeClassAdapter;
import com.example.amidezcod.quakereport.rest.APiClient;
import com.example.amidezcod.quakereport.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {


    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    public ApiInterface apiService = APiClient.getApiClient().create(ApiInterface.class);
    private TextView mEmptyStateTextView;
    private RecyclerView mRecyclerView;
    private EarthQuakeClassAdapter earthQuakeClassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRecyclerview();
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);
        earthQuakeClassAdapter = new EarthQuakeClassAdapter(MainActivity.this, new ArrayList<Properties>());

        response();
    }

    public void response() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String minmag = sharedPreferences.getString(getString(R.string.min_mag_key), "0");
        String orderby = sharedPreferences.getString(getString(R.string.list_key_orderby),
                getString(R.string.settings_orderby_mag_value));

        final Call<Example> earthQuakePojoCall = apiService.getEarthquake("geojson",
                minmag,
                orderby);
        Log.v("AMAN", "URL" + earthQuakePojoCall.request().url().toString());
        earthQuakePojoCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(@NonNull Call<Example> call, @NonNull Response<Example> response) {
                Log.v("AMAN", "URL" + response.raw().request().url().toString() + "Response code" + response.code());
                ArrayList<Feature> features = response.body().getFeatures();
                if (features.size() == 0)
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setText(R.string.no_earthquakes);
                ArrayList<Properties> propertiesArrayList = new ArrayList<>();
                for (int i = 0; i < features.size(); i++) {
                    Properties properties = features.get(i).getProperties();
                    propertiesArrayList.add(properties);

                }
                features.clear();
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
                // Set empty state text to display "No earthquakes found."
                if (!propertiesArrayList.isEmpty()) {
                    earthQuakeClassAdapter.swapData(propertiesArrayList);
                    mRecyclerView.setAdapter(earthQuakeClassAdapter);
                } else {
                    if (earthQuakeClassAdapter != null) {
                        earthQuakeClassAdapter.clear();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Example> call, @NonNull Throwable t) {
                Log.v("AMAN", t.toString());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupRecyclerview() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.asdff, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.refresh:
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.min_mag_key)) || s.equals(getString(R.string.list_key_orderby))) {
            mEmptyStateTextView.setVisibility(View.GONE);
            if (earthQuakeClassAdapter != null)
                earthQuakeClassAdapter.clear();
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
            response();
        }
    }

    private void clearView() {
        View progressIndicator = findViewById(R.id.loading_indicator);
        progressIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_internet_message);
        View image = findViewById(R.id.imageview);
        image.setVisibility(View.VISIBLE);
    }
}
