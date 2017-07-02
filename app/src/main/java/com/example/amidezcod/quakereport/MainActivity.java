package com.example.amidezcod.quakereport;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuakePojo>> {
    private RecyclerView mRecyclerView;
    private ArrayList<EarthQuakePojo> earthQuakePojos;
    private EarthQuakeClassAdapter earthQuakeClassAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRecyclerview();
        getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this).forceLoad();
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
                earthQuakePojos = queryUtility.fetchRequestUrl("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02");
                return earthQuakePojos;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthQuakePojo>> loader, ArrayList<EarthQuakePojo> earthQuakePojos) {
        earthQuakeClassAdapter = new EarthQuakeClassAdapter(this, earthQuakePojos);
        mRecyclerView.setAdapter(earthQuakeClassAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EarthQuakePojo>> loader) {
        int size = earthQuakePojos.size();
        earthQuakePojos.clear();
        earthQuakeClassAdapter.notifyItemRangeRemoved(0, size);
    }
}
