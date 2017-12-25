package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by sagar on 24/12/17.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<QuakeDescription>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super( context );
        mUrl = url;
    }

    @Override
    public List<QuakeDescription> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchEarthquakeData( mUrl );
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

