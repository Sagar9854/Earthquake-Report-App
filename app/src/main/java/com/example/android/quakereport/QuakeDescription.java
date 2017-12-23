package com.example.android.quakereport;

/**
 * Created by sagar on 21/12/17.
 */

public class QuakeDescription {
    private double magnitude;
    private String quakePlace;
    private long mTimeInMilliSeconds;
    private String mUrl;

    public QuakeDescription(double magnitude, String quakePlace, long mTimeInMilliSeconds, String mUrl) {
        this.mTimeInMilliSeconds = mTimeInMilliSeconds;
        this.magnitude = magnitude;
        this.quakePlace = quakePlace;
        this.mUrl = mUrl;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public long getmTimeInMilliSeconds() {
        return mTimeInMilliSeconds;
    }

    public String getQuakePlace() {
        return quakePlace;
    }

    public String getmUrl() {
        return mUrl;
    }
}
