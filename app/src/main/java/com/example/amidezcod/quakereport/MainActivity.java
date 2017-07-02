package com.example.amidezcod.quakereport;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuakePojo>> {
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query";
    TextView mEmptyStateTextView;
    private RecyclerView mRecyclerView;
    private EarthQuakeClassAdapter earthQuakeClassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        setupRecyclerview();

        earthQuakeClassAdapter = new EarthQuakeClassAdapter(this, new ArrayList<EarthQuakePojo>());

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, MainActivity.this).forceLoad();
        } else {
            View progressIndicator = findViewById(R.id.loading_indicator);
            progressIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_message);
            View image = findViewById(R.id.imageview);
            image.setVisibility(View.VISIBLE);
        }
    }


    private void setupRecyclerview() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public Loader<ArrayList<EarthQuakePojo>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<EarthQuakePojo>>(this) {
            @Override
            public ArrayList<EarthQuakePojo> loadInBackground() {
                QueryUtility queryUtility = new QueryUtility();


                return queryUtility.fetchRequestUrl("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmagnitude=5");

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthQuakePojo>> loader, ArrayList<EarthQuakePojo> earthQuakePojos) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No earthquakes found."
        if (earthQuakePojos != null && !earthQuakePojos.isEmpty()) {
            earthQuakeClassAdapter.swapData(earthQuakePojos);
            earthQuakeClassAdapter.notifyDataSetChanged();
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
}
