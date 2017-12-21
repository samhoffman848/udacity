package com.example.android.quakereport;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class EarthquakeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<QuakeItem>> {
    /** URL to query the USGS dataset for earthquake information */
    private static final String USGS_BASE_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake";

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

    public EarthquakeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.earthquake_fragment, container, false);

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

        mEmptyListTextView = (TextView) rootView.findViewById(R.id.emptyListTextView);
        earthquakeListView.setEmptyView(mEmptyListTextView);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

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

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

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

        Bundle bundle = new Bundle();
        bundle.putString("url", null);
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
    }


    //-------------------------------------------------------------------------------------------
    /* Format Methods */
    //-------------------------------------------------------------------------------------------
    /** Format new Url from globals */
    private String formatUrl(){

        String newUrl = USGS_BASE_REQUEST_URL;

        String startTime = formatDate(mFromYear, mFromMonth+1, mFromDay);
        String endTime = formatDate(mToYear, mToMonth+1, mToDay);

        newUrl += "&orderby=" + mOrderBy;
        newUrl += "&minmag=" + mMinMag;
        newUrl += "&maxmag=" +mMaxMag;
        newUrl += "&limit=" + mLimit;
        newUrl += "&starttime=" + startTime;
        newUrl += "&endtime=" + endTime;

        return newUrl;
    }

    /** Format date as string */
    private String formatDate(int year, int month, int day){
        return String.valueOf(year) + "-"
                + String.valueOf(month) + "-"
                + String.valueOf(day);

    }


    //-------------------------------------------------------------------------------------------
    /** Loader Overrides */
    //-------------------------------------------------------------------------------------------
    @Override
    public Loader<List<QuakeItem>> onCreateLoader(int i, Bundle args) {
        mAdapter.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyListTextView.setText("");

        String url = args.getString("url");

        // Create a new loader for the given URL
        return new EarthquakeLoader(getActivity(), url);
    }

    @Override
    public void onLoadFinished(Loader<List<QuakeItem>> loader, List<QuakeItem> earthquakes) {
        mAdapter.clear();

        mProgressBar.setVisibility(View.GONE);

        if (earthquakes != null && !earthquakes.isEmpty()){
            mAdapter.addAll(earthquakes);
            mEmptyListTextView.setText("");
        } else {
            mEmptyListTextView.setText(R.string.no_earthquake);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<QuakeItem>> loader) {
        mAdapter.clear();
    }
}
