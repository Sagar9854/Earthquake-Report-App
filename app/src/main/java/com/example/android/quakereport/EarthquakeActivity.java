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
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<QuakeDescription>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private QuakeAdapter mAdapter;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    /**
     * adding the swipe refresh functionality
     */
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoaderManager loaderManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.earthquake_activity );

        progressBar = (ProgressBar) findViewById( R.id.progress_bar );
        progressBar.setVisibility( View.VISIBLE );

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById( R.id.swipe_refresh );

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new QuakeAdapter(
                this, new ArrayList<QuakeDescription>() );

        loaderManager = getLoaderManager();
        mEmptyStateTextView = (TextView) findViewById( R.id.empty_view );

        if (isNetworkAvailable() == false) {
            progressBar.setVisibility( View.GONE );
            setNoInternet();
        } else {
            // Find a reference to the {@link ListView} in the layout
            final ListView earthquakeListView = (ListView) findViewById( R.id.list );

            earthquakeListView.setEmptyView( mEmptyStateTextView );

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter( mAdapter );

            // Log.e( LOG_TAG, "onCreate: " + "Layout is created here\n");

            earthquakeListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // Find the current earthquake that was clicked on
                    QuakeDescription currentEarthquake = mAdapter.getItem( i );
                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri webpage = Uri.parse( currentEarthquake.getmUrl() );
                    // Create a new intent to view the earthquake URI
                    Intent intent = new Intent( Intent.ACTION_VIEW, webpage );
                    if (intent.resolveActivity( getPackageManager() ) != null) {
                        startActivity( intent );
                    }
                }
            } );

            // loaderManager = getLoaderManager();
            loaderManager.initLoader( EARTHQUAKE_LOADER_ID, null, this );
//        new EarthquakeAsyncTask().execute( USGS_REQUEST_URL );
        }

        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO : update the data
                //fetchMovies();
                swipeRefreshLayout.setRefreshing( true );

                Log.i( LOG_TAG, "onRefresh: " + "the function is here\n" );
                if (isNetworkAvailable() == true) {
                    findViewById( R.id.no_internet_connection ).setVisibility( View.GONE );
                    loaderManager.initLoader( EARTHQUAKE_LOADER_ID, null, EarthquakeActivity.this );

                } else {
                    setNoInternet();
                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing( false );
                }
            }
        } );
    }

    private void setNoInternet() {
        Log.i( LOG_TAG, "setNoInternet: " + "IT IS PRINTING\n" );
        TextView noInternet = (TextView) findViewById( R.id.no_internet_connection );
        noInternet.setText( R.string.no_internet );
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public Loader<List<QuakeDescription>> onCreateLoader(int i, Bundle bundle) {
        //Log.e( LOG_TAG, "mLoader: " + "LOADER is loaded here\n");
        return new EarthquakeLoader( this, USGS_REQUEST_URL );
    }

    @Override
    public void onLoadFinished(Loader<List<QuakeDescription>> loader,
                               List<QuakeDescription> data) {
        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing( false );
        findViewById( R.id.no_internet_connection ).setVisibility( View.GONE );
        //progress bar while fetching data from the internet
        progressBar = (ProgressBar) findViewById( R.id.progress_bar );
        progressBar.setVisibility( View.GONE );

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText( R.string.empty );
        Log.i( LOG_TAG, "onLoadFinished: " + "THE OPERATION HAS FINISHED\n" );
        // Clear the adapter of previous earthquake data
        if (mAdapter != null)
            mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll( data );
        }
    }

    @Override
    public void onLoaderReset(Loader<List<QuakeDescription>> loader) {
        // Clear the adapter of previous earthquake data
        if (mAdapter != null)
            mAdapter.clear();
        // Log.e( LOG_TAG, "LOADER_RESET: " + "Loader is destroyed/reset here\n");
    }


//    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<QuakeDescription>>{
//
//        @Override
//        protected List<QuakeDescription> doInBackground(String... url) {
//            if (url[0] == null || url.length < 1){
//                return null;
//            }
//            return QueryUtils.fetchEarthquakeData( url[0] );
//        }
//
//        @Override
//        protected void onPostExecute(List<QuakeDescription> data) {
//            // Clear the adapter of previous earthquake data
//            mAdapter.clear();
//
//            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
//            // data set. This will trigger the ListView to update.
//            if (data != null && !data.isEmpty()) {
//                mAdapter.addAll(data);
//            }
//        }
//    }
}
