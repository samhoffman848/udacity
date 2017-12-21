package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class EarthquakeMapFragment extends Fragment implements OnMapReadyCallback {
    EarthquakeFragment mEarthquakeFragment;
    private FragmentListener fragmentListener;

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

    public interface FragmentListener{
        public Fragment getFragmentAtPos(int pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentListener = (FragmentListener) context;
        } catch (ClassCastException castException) {
            Log.e(LOG_TAG, "Activity failed to implement listener", castException);
        }
    }

    //-------------------------------------------------------------------------------------------
    /** Map Overrides */
    //-------------------------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Fragment page = fragmentListener.getFragmentAtPos(0);
        mEarthquakeFragment = (EarthquakeFragment) page;

        ArrayList<QuakeItem> quakeList = mEarthquakeFragment.getQuakeList();

        for(QuakeItem q : quakeList){
            String[] formattedString = QuakeAdapter.formatLocation(q.getLocation());
            LatLng pos = new LatLng(q.getLat(), q.getLng());
            Marker marker = googleMap.addMarker(new MarkerOptions().position(pos));
            marker.setTitle(formattedString[1]);
            marker.setSnippet(q.getMagnitude() + " earthquake " + q.getLocation());

            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    Context context = getActivity(); //or getActivity(), YourActivity.this, etc.

                    View infoWindow = LayoutInflater.from(getActivity()).inflate(R.layout.info_window, null);
                    TextView titleView = infoWindow.findViewById(R.id.titleText);
                    titleView.setText(marker.getTitle());

                    TextView snippetView = infoWindow.findViewById(R.id.snippetText);
                    snippetView.setText(marker.getSnippet());

                    return infoWindow;
                }
            });
        }



        LatLng zero = new LatLng(0, 0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(zero));

    }
}
