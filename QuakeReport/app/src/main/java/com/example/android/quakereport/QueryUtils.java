package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link QuakeItem} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<QuakeItem> extractEarthquakes(String requestUrl) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<QuakeItem> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            String jsonString = Utils.getEarthquakeData(requestUrl);

            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray featuresArray = jsonObj.getJSONArray("features");

            for(int i=0; i < featuresArray.length(); i++){
                JSONObject earthquakeObj = featuresArray.getJSONObject(i);
                JSONObject propertiesObj = earthquakeObj.getJSONObject("properties");

                double mag = propertiesObj.getDouble("mag");
                String location = propertiesObj.getString("place");
                Long time = propertiesObj.getLong("time");

                String url = propertiesObj.getString("url");

                int tsunami = propertiesObj.getInt("tsunami");

                earthquakes.add(new QuakeItem(mag, location, time, url, tsunami));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}