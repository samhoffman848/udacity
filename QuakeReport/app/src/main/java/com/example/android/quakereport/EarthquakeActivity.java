/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<QuakeItem>>{
    /** URL to query the USGS dataset for earthquake information */
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=3&limit=25";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private QuakeAdapter mAdapter;
    private TextView mEmptyListTextView;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new QuakeAdapter(this, new ArrayList<QuakeItem>());

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        mEmptyListTextView = (TextView) findViewById(R.id.emptyListTextView);
        earthquakeListView.setEmptyView(mEmptyListTextView);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                QuakeItem quakeItem = mAdapter.getItem(pos);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(quakeItem.getUrl()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected){
            Bundle bundle = new Bundle();
            bundle.putString("url", USGS_REQUEST_URL);

            LoaderManager loaderManager = getLoaderManager();
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, bundle, this);
        } else {
            mEmptyListTextView.setText(R.string.no_connection);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void sendNewRequest(View view){
        String testUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=2&limit=25";

        Bundle bundle = new Bundle();
        bundle.putString("url", testUrl);

        getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, bundle, this);
    }

    @Override
    public Loader<List<QuakeItem>> onCreateLoader(int i, Bundle args) {
        String url = args.getString("url");

        // Create a new loader for the given URL
        return new EarthquakeLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<QuakeItem>> loader, List<QuakeItem> earthquakes) {
        mAdapter.clear();

        mProgressBar.setVisibility(View.GONE);

        if (earthquakes != null && !earthquakes.isEmpty()){
            mAdapter.addAll(earthquakes);
            mEmptyListTextView.setText("");
        } else {
            mEmptyListTextView.setText(R.string.no_earthquake);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<QuakeItem>> loader) {
        mAdapter.clear();
    }
}
