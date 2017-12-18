package com.example.android.quakereport;

public class QuakeItem {
    private double mMag;
    private String mLocation;
    private String mTime;

    public QuakeItem(double mag, String location, String time){
        mMag = mag;
        mLocation = location;
        mTime = time;
    }

    public double getMagnitude(){
        return mMag;
    }

    public String getLocation(){
        return mLocation;
    }

    public String getTime(){
        return mTime;
    }
}
