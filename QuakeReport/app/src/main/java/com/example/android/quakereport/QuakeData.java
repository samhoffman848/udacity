package com.example.android.quakereport;

public class QuakeData{
    private String url;
    private double magnitude;
    private String time;
    private String date;
    private String location;
    private int magnitudeColour;


    public QuakeData(){
        super();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setMag(double mag) {
        this.magnitude = mag;
    }

    public double getMag() {
        return magnitude;
    }

    public void setTimeString(String time) {
        this.time = time;
    }

    public String getTimeString() {
        return time;
    }

    public void setDateString(String date) {
        this.date = date;
    }

    public String getDateString() {
        return date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setMagColour(int magnitudeColour) {
        this.magnitudeColour = magnitudeColour;
    }

    public int getMagColour() {
        return magnitudeColour;
    }
}