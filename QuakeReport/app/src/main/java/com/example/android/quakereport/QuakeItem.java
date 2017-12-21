package com.example.android.quakereport;

public class QuakeItem {
    private double mMag;
    private String mLocation;
    private Long mTimeInMilliseconds;
    private String mUrl;
    private int mTsunami;
    private int mLat;
    private int mLng;
    private int mDepth;

    public QuakeItem(double mag, String location, Long time, String url, int tsunami, int lng, int lat, int depth){
        mMag = mag;
        mLocation = location;
        mTimeInMilliseconds = time;
        mUrl = url;
        mTsunami = tsunami;
        mLat = lat;
        mLng = lng;
        mDepth = depth;
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

    public int getLat(){
        return mLat;
    }

    public int getLng(){
        return mLng;
    }

    public int getDepth(){
        return mDepth;
    }

}
