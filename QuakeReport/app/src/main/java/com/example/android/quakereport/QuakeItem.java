package com.example.android.quakereport;

public class QuakeItem {
    private double mMag;
    private String mLocation;
    private Long mTimeInMilliseconds;
    private String mUrl;
    private int mTsunami;

    public QuakeItem(double mag, String location, Long time, String url, int tsunami){
        mMag = mag;
        mLocation = location;
        mTimeInMilliseconds = time;
        mUrl = url;
        mTsunami = tsunami;
    }

    public double getMagnitude(){
        return mMag;
    }

    public String getLocation(){
        return mLocation;
    }

    public Long getTime(){
        return mTimeInMilliseconds;
    }

    public String getUrl(){
        return mUrl;
    }

    public int getTsunami(){
        return mTsunami;
    }

}
