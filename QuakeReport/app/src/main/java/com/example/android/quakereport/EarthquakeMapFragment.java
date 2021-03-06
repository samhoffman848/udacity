package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class EarthquakeMapFragment extends Fragment implements OnMapReadyCallback {
    EarthquakeFragment mEarthquakeFragment;
    private FragmentMapListener fragmentMapListener;

    GoogleMap mGoogleMap;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    public EarthquakeMapFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.earthquake_map_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    public interface FragmentMapListener{
        public Fragment getFragmentAtPos(int pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentMapListener = (FragmentMapListener) context;
        } catch (ClassCastException castException) {
            Log.e(LOG_TAG, "Activity failed to implement listener", castException);
        }
    }


    public float[] getHsvFromColourId(int colour){
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(colour), Color.green(colour), Color.blue(colour), hsv);

        return hsv;
    }

    public void updateMapMarkers(ArrayList<QuakeItem> quakeList){
        // Make sure google map object exists
        if (mGoogleMap==null){
            return;
        }

        clearMapMarkers();

        for(QuakeItem q : quakeList){
            String[] formattedString = QuakeAdapter.formatLocation(q.getLocation());
            LatLng pos = new LatLng(q.getLat(), q.getLng());
            Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(pos));
            marker.setTitle(formattedString[1]);

            int magColour = QuakeAdapter.getMagnitudeColour(q.getMagnitude());
            float[] hsv = getHsvFromColourId(magColour);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(hsv[0]));

            QuakeData markerData=new QuakeData();
            markerData.setLocation(q.getLocation());
            markerData.setDateString(QuakeAdapter.formatDate(q.getTime()));
            markerData.setTimeString(QuakeAdapter.formatTime(q.getTime()));
            markerData.setUrl(q.getUrl());
            markerData.setMag(q.getMagnitude());
            markerData.setMagColour(magColour);

            marker.setTag(markerData);

            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    Context context = getActivity();

                    QuakeData qData = (QuakeData) marker.getTag();

                    View infoWindow = LayoutInflater.from(getActivity()).inflate(R.layout.info_window, null);
                    TextView titleView = infoWindow.findViewById(R.id.titleText);
                    titleView.setText(marker.getTitle());

                    TextView magnitudeView = infoWindow.findViewById(R.id.magnitudeText);
                    magnitudeView.setText(String.valueOf(qData.getMag()));
                    magnitudeView.setTextColor(qData.getMagColour());

                    TextView locationView = infoWindow.findViewById(R.id.locationText);
                    locationView.setText(qData.getLocation());

                    TextView dateView = infoWindow.findViewById(R.id.dateText);
                    dateView.setText(qData.getDateString() + " at " + qData.getTimeString());

                    return infoWindow;
                }
            });
        }
    }

    public void clearMapMarkers(){
        // Make sure google map object exists
        if (mGoogleMap==null){
            return;
        }

        mGoogleMap.clear();
    }

    //-------------------------------------------------------------------------------------------
    /** Map Overrides */
    //-------------------------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        Fragment page = fragmentMapListener.getFragmentAtPos(0);
        mEarthquakeFragment = (EarthquakeFragment) page;

        ArrayList<QuakeItem> quakeList = mEarthquakeFragment.getQuakeList();

        updateMapMarkers(quakeList);

        LatLng zero = new LatLng(0, 0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(zero));

    }
}
