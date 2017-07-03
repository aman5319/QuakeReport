package com.example.amidezcod.quakereport;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuakePojo>>,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    TextView mEmptyStateTextView;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private EarthQuakeClassAdapter earthQuakeClassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        setupRecyclerview();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container_recycler);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAction();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.holo_blue_bright),
                ContextCompat.getColor(this, android.R.color.holo_red_light),
                ContextCompat.getColor(this, android.R.color.holo_orange_light));
        earthQuakeClassAdapter = new EarthQuakeClassAdapter(this, new ArrayList<EarthQuakePojo>());
        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, MainActivity.this);
        } else {
            View progressIndicator = findViewById(R.id.loading_indicator);
            progressIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_message);
            View image = findViewById(R.id.imageview);
            image.setVisibility(View.VISIBLE);
        }
    }

    private void refreshAction() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                earthQuakeClassAdapter.clear();
                getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, null, MainActivity.this);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }


    private void setupRecyclerview() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public Loader<ArrayList<EarthQuakePojo>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String minmag = sharedPreferences.getString(getString(R.string.min_mag_key), "0");
        String orderby = sharedPreferences.getString(getString(R.string.list_key_orderby),
                getString(R.string.settings_orderby_mag_value));
        Uri uri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter("format", "geojson");
        builder.appendQueryParameter("minmagnitude", minmag);
        builder.appendQueryParameter("orderby", orderby);
        Log.v("AMAN", builder.toString());
        return new EarthquakeLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthQuakePojo>> loader, ArrayList<EarthQuakePojo> earthQuakePojos) {
        Log.v("AMAN", "InsideLoadfinished");

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No earthquakes found."
        if (earthQuakePojos != null && !earthQuakePojos.isEmpty()) {
            earthQuakeClassAdapter.swapData(earthQuakePojos);
            mRecyclerView.setAdapter(earthQuakeClassAdapter);

        } else {
            earthQuakeClassAdapter.clear();
            mEmptyStateTextView.setText(R.string.no_earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EarthQuakePojo>> loader) {
        earthQuakeClassAdapter.clear();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.min_mag_key)) || s.equals(getString(R.string.list_key_orderby))) {
            mEmptyStateTextView.setVisibility(View.GONE);
            earthQuakeClassAdapter.clear();
            // Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
            // Restart the loader to requery the USGS as the query settings have been updated
            getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, null, MainActivity.this);
        }
    }
}
