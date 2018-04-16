package com.example.android.quakereport;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.google.android.gms.maps.model.LatLng;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import info.hoang8f.android.segmented.SegmentedGroup;

public class EarthquakeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<QuakeItem>> {
    EarthquakeMapFragment mEarthquakeMapFragment;
    private FragmentListListener fragmentListListener;

    View mOverlay;

    /** URL to query the USGS dataset for earthquake information */
    private static final String USGS_BASE_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    /** Globals for spinner populating */
    final private String[] LIMIT_LIST = new String[]{"10", "25", "50", "100", "250", "500"};
    final private String[] ORDER_BY_LIST = new String[]{"time", "time-asc", "magnitude", "magnitude-asc"};

    /** Globals for url formatting and search */
    private String mOrderBy = "time";
    private String mMinMag = "4";
    private String mMaxMag = "10";
    private String mLimit = "25";
    private int mFromYear;
    private int mFromMonth;
    private int mFromDay;
    private int mToYear;
    private int mToMonth;
    private int mToDay;

    /** USGS API Request Loader ID */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /** Globals for UI elements */
    private QuakeAdapter mAdapter;
    private TextView mEmptyListTextView;
    private ProgressBar mProgressBar;
    private SegmentedGroup mFilterGroup;
    private RangeBar mMagRange;
    private MaterialSpinner mLimitSpinner;
    private MaterialSpinner mOrderBySpinner;
    private ImageButton mFromDateButton;
    private ImageButton mToDateButton;
    private EditText mFromDateText;
    private EditText mToDateText;

    private LocationManager mLocationManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    public EarthquakeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.earthquake_fragment, container, false);

        // Acquire a reference to the system Location Manager
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Get current date
        final Calendar cal = Calendar.getInstance();
        mToYear = cal.get(Calendar.YEAR);
        mToMonth = cal.get(Calendar.MONTH);
        mToDay = cal.get(Calendar.DAY_OF_MONTH);

        // Get last month date
        cal.add(Calendar.MONTH, -1);
        mFromYear = cal.get(Calendar.YEAR);
        mFromMonth = cal.get(Calendar.MONTH);
        mFromDay = cal.get(Calendar.DAY_OF_MONTH);

        mOverlay = (View) getActivity().findViewById(R.id.mapOverlay);

        mFilterGroup = (SegmentedGroup) rootView.findViewById(R.id.filtersLayout);
        mFilterGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                filterList();
            }
        });

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new QuakeAdapter(getActivity(), new ArrayList<QuakeItem>());

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) rootView.findViewById(R.id.list);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        mEmptyListTextView = (TextView) getActivity().findViewById(R.id.emptyListTextView);
        earthquakeListView.setEmptyView(mEmptyListTextView);

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                QuakeItem quakeItem = mAdapter.getItem(pos);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(quakeItem.getUrl()));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected){
            String url = formatUrl();

            Bundle bundle = new Bundle();
            bundle.putString("url", url);

            LoaderManager loaderManager = getLoaderManager();
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, bundle, this);
        } else {
            mEmptyListTextView.setText(R.string.no_connection);
            mProgressBar.setVisibility(View.GONE);
        }

        return rootView;
    }


    public ArrayList<QuakeItem> getQuakeList(){
        return mAdapter.quakeList;
    }


    public interface FragmentListListener{
        public Fragment getFragmentAtPos(int pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentListListener = (FragmentListListener) context;
        } catch (ClassCastException castException) {
            Log.e(LOG_TAG, "Activity failed to implement listener", castException);
        }
    }


    //-------------------------------------------------------------------------------------------
    /* Filter Methods */
    //-------------------------------------------------------------------------------------------
    private void filterList(){
        int radioId = mFilterGroup.getCheckedRadioButtonId();
        RadioButton selectedButton = (RadioButton) getActivity().findViewById(radioId);
        String filterString = selectedButton.getText().toString();

        if (filterString.equals("Both")){
            filterString = "";
        }

        mAdapter.resetData();
        mAdapter.getFilter().filter(filterString);
    }


    //-------------------------------------------------------------------------------------------
    /* Search Dialog Methods */
    //-------------------------------------------------------------------------------------------
    public void createAndDisplaySearchDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Search");

        LayoutInflater inflater = getLayoutInflater();
        View builderLayout = inflater.inflate(R.layout.search_dialog, null);

        builder.setView(builderLayout);

        builder.setPositiveButton("Search",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prepareNewRequest();
                    }
                });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Setup Range Bar
        mMagRange = (RangeBar) builderLayout.findViewById(R.id.magRangeBar);
        mMagRange.setRangePinsByValue(Float.valueOf(mMinMag), Float.valueOf(mMaxMag));

        // Setup Spinners
        mLimitSpinner = (MaterialSpinner) builderLayout.findViewById(R.id.limitSpinnerView);
        mLimitSpinner.setItems(LIMIT_LIST);
        mLimitSpinner.setSelectedIndex(Arrays.asList(LIMIT_LIST).indexOf(mLimit));

        mOrderBySpinner = (MaterialSpinner) builderLayout.findViewById(R.id.orderBySpinnerView);
        mOrderBySpinner.setItems(ORDER_BY_LIST);
        mOrderBySpinner.setSelectedIndex(Arrays.asList(ORDER_BY_LIST).indexOf(mOrderBy));

        // Setup Date Fields
        mFromDateButton = (ImageButton) builderLayout.findViewById(R.id.fromDateButton);
        mToDateButton = (ImageButton) builderLayout.findViewById(R.id.toDateButton);

        mFromDateText = (EditText) builderLayout.findViewById(R.id.fromDateInput);
        mFromDateText.addTextChangedListener(new MaskWatcher("####-##-##"));
        mFromDateText.setText(formatDate(mFromYear, mFromMonth+1, mFromDay));

        mToDateText = (EditText) builderLayout.findViewById(R.id.toDateInput);
        mToDateText.addTextChangedListener(new MaskWatcher("####-##-##"));
        mToDateText.setText(formatDate(mToYear, mToMonth+1, mToDay));

        mFromDateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                createDatePicker(mFromDateText, mFromYear, mFromMonth, mFromDay);
            }
        });

        mToDateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                createDatePicker(mToDateText, mToYear, mToMonth, mToDay);
            }
        });

        builder.create().show();
    }

    private void createDatePicker(final EditText editText, int year, int month, int day){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        int correctedMonth = month + 1;

                        String formattedText = formatDate(year, correctedMonth, day);
                        editText.setText(formattedText);

                        if (editText==mToDateText){
                            mToYear = year;
                            mToMonth = month;
                            mToDay = day;
                        } else {
                            mFromYear = year;
                            mFromMonth = month;
                            mFromDay = day;
                        }
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }


    //-------------------------------------------------------------------------------------------
    /* Request Methods */
    //-------------------------------------------------------------------------------------------
    private void prepareNewRequest(){
        mMinMag = mMagRange.getLeftPinValue();
        mMaxMag = mMagRange.getRightPinValue();

        int limitIndex = mLimitSpinner.getSelectedIndex();
        int orderByIndex = mOrderBySpinner.getSelectedIndex();

        mLimit = LIMIT_LIST[limitIndex];
        mOrderBy = ORDER_BY_LIST[orderByIndex];

        sendNewRequst();
    }

    public void sendNewRequst(){
        String url = formatUrl();

        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, bundle, this);

        mFilterGroup.check(R.id.bothRadioButton);
    }


    //-------------------------------------------------------------------------------------------
    /* Format Methods */
    //-------------------------------------------------------------------------------------------
    /** Format new Url from globals */
    private String formatUrl(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean deviceLoc = sharedPrefs.getBoolean(
                getString(R.string.settings_device_loc_key),
                false);
        String location = sharedPrefs.getString(
                getString(R.string.settings_location_key),
                getString(R.string.settings_location_default));
        String distance = sharedPrefs.getString(
                getString(R.string.settings_distance_key),
                getString(R.string.settings_distance_default));

        String startTime = formatDate(mFromYear, mFromMonth+1, mFromDay);
        String endTime = formatDate(mToYear, mToMonth+1, mToDay);

        Location coordinates = getLocation(deviceLoc, location);

        Uri baseUri = Uri.parse(USGS_BASE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("eventtype", "earthquake");
        uriBuilder.appendQueryParameter("orderby", mOrderBy);
        uriBuilder.appendQueryParameter("minmag", mMinMag);
        uriBuilder.appendQueryParameter("maxmag", mMaxMag);
        uriBuilder.appendQueryParameter("limit", mLimit);
        uriBuilder.appendQueryParameter("starttime", startTime);
        uriBuilder.appendQueryParameter("endtime", endTime);

        if (coordinates != null) {
            uriBuilder.appendQueryParameter("maxradiuskm", distance);
            uriBuilder.appendQueryParameter("latitude", Location.convert(coordinates.getLatitude(), Location.FORMAT_DEGREES));
            uriBuilder.appendQueryParameter("longitude", Location.convert(coordinates.getLongitude(), Location.FORMAT_DEGREES));
        }

        return  uriBuilder.toString();
    }

    /** Format date as string */
    private String formatDate(int year, int month, int day){
        return String.valueOf(year) + "-"
                + String.valueOf(month) + "-"
                + String.format(Locale.UK, "%02d", day);

    }

    /** Get latitude and Longitude from device or address */
    private Location getLocation(Boolean useDevice, String loc) {
        if (useDevice) {
            if (hasPermission()) {
                return getLastKnownLocation();
            }
        } else {
            if (loc != null && !loc.isEmpty()) {
                LatLng point = getLocationFromAddress(getActivity(), loc);
                Location coordinates = new Location("Test");
                coordinates.setLatitude(point.latitude);
                coordinates.setLongitude(point.longitude);
                return coordinates;
            }
        }
        return null;
    }

    private boolean hasPermission() {
        int courseResult = getActivity().checkCallingPermission("android.permission.ACCESS_COARSE_LOCATION");
        int fineResult = getActivity().checkCallingPermission("android.permission.ACCESS_FINE_LOCATION");

        if (courseResult != PackageManager.PERMISSION_GRANTED || fineResult != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return true;
        }

        return true;
    }

    private boolean isGpsLocationProviderEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isNetworkLocationProviderEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private Location getLastKnownLocation() {
        Location netLoc = null;
        Location gpsLoc = null;

        if (isGpsLocationProviderEnabled()) {
            gpsLoc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (isNetworkLocationProviderEnabled()) {
            netLoc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        // If there are both values use the latest one
        if (gpsLoc != null && netLoc != null){
            if (gpsLoc.getTime() > netLoc.getTime()) {
                return gpsLoc;
            } else {
                return netLoc;
            }
        }

        if (gpsLoc != null) {
            return gpsLoc;
        }
        if (netLoc != null) {
            return netLoc;
        }
        return null;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    //-------------------------------------------------------------------------------------------
    /** Loader Overrides */
    //-------------------------------------------------------------------------------------------
    @Override
    public Loader<List<QuakeItem>> onCreateLoader(int i, Bundle args) {
        Fragment page = fragmentListListener.getFragmentAtPos(1);
        mEarthquakeMapFragment = (EarthquakeMapFragment) page;

        mAdapter.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyListTextView.setText("");
        mFilterGroup.setVisibility(View.INVISIBLE);

        mOverlay.setVisibility(View.VISIBLE);

        String url = args.getString("url");

        // Create a new loader for the given URL
        return new EarthquakeLoader(getActivity(), url);
    }

    @Override
    public void onLoadFinished(Loader<List<QuakeItem>> loader, List<QuakeItem> earthquakes) {
        Fragment page = fragmentListListener.getFragmentAtPos(1);
        mEarthquakeMapFragment = (EarthquakeMapFragment) page;

        mAdapter.clear();

        mProgressBar.setVisibility(View.GONE);

        if (earthquakes != null && !earthquakes.isEmpty()){
            mAdapter.addAll(earthquakes);

            ArrayList<QuakeItem> quakeList = new ArrayList<QuakeItem>(earthquakes);
            mEarthquakeMapFragment.updateMapMarkers(quakeList);

            mEmptyListTextView.setText("");
            mOverlay.setVisibility(View.INVISIBLE);
            mFilterGroup.setVisibility(View.VISIBLE);
        } else {
            mEarthquakeMapFragment.clearMapMarkers();

            mEmptyListTextView.setText(R.string.no_earthquake);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<QuakeItem>> loader) {
        mAdapter.clear();
    }
}
