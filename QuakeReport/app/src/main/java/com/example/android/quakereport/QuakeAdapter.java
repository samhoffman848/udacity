package com.example.android.quakereport;

import android.app.Activity;
import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class QuakeAdapter extends ArrayAdapter<QuakeItem> implements Filterable {
    public ArrayList<QuakeItem> origQuakeList;
    public ArrayList<QuakeItem> quakeList;
    private Filter quakeFilter;
    private static Context mContext;

    public static final String LOG_TAG = QuakeAdapter.class.getName();

    public QuakeAdapter(Activity context, ArrayList<QuakeItem> quakeItems){
        super(context, 0, quakeItems);
        origQuakeList = quakeItems;
        quakeList = quakeItems;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }

        QuakeItem currentItem = getItem(position);

        double mag = currentItem.getMagnitude();

        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitudeView);
        magnitudeTextView.setText(formatMagnitude(mag));

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
        int magnitudeColour = getMagnitudeColour(mag);
        magnitudeCircle.setColor(magnitudeColour);

        int isTsunami = currentItem.getTsunami();
        int strokeColor = magnitudeColour;
        if (isTsunami==1){
            strokeColor = Color.BLACK;
        }
        magnitudeCircle.setStroke(6, strokeColor);

        String[] formattedString = formatLocation(currentItem.getLocation());

        TextView locationOffsetTextView = (TextView) listItemView.findViewById(R.id.locationOffsetView);
        locationOffsetTextView.setText(formattedString[0]);

        TextView locationTextView = (TextView) listItemView.findViewById(R.id.locationView);
        locationTextView.setText(formattedString[1]);

        Long timeInMilliseconds = currentItem.getTime();

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.timeView);
        timeTextView.setText(formatTime(timeInMilliseconds));

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.dateView);
        dateTextView.setText(formatDate(timeInMilliseconds));

        return listItemView;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static String formatDate(Long timeInMilliseconds){
        Date dateObj = new Date(timeInMilliseconds);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.UK);
        return dateFormat.format(dateObj);
    }

    public static String formatTime(Long timeInMilliseconds){
        Date dateObj = new Date(timeInMilliseconds);
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);
        return dateFormat.format(dateObj);
    }

    public static String[] formatLocation(String locationFull){
        String locationSeparator = " of ";

        if (locationFull.contains(locationSeparator)){
            String[] splitLocation = locationFull.split(locationSeparator);
            splitLocation[0] += locationSeparator;
            return splitLocation;
        } else {
            Context context = getAppContext();
            return new String[] {context.getString(R.string.near_the), locationFull};
        }
    }

    private String formatMagnitude(double mag){
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(mag);
    }

    public static int getMagnitudeColour(double mag){
        int colourId;
        int magFloor = (int) Math.floor(mag);

        switch (magFloor){
            case 0:
            case 1:
                colourId = R.color.magnitude1;
                break;
            case 2:
                colourId = R.color.magnitude2;
                break;
            case 3:
                colourId = R.color.magnitude3;
                break;
            case 4:
                colourId = R.color.magnitude4;
                break;
            case 5:
                colourId = R.color.magnitude5;
                break;
            case 6:
                colourId = R.color.magnitude6;
                break;
            case 7:
                colourId = R.color.magnitude7;
                break;
            case 8:
                colourId = R.color.magnitude8;
                break;
            case 9:
                colourId = R.color.magnitude9;
                break;
            default:
                colourId = R.color.magnitude10plus;
                break;
        }

        Context context = getAppContext();
        return ContextCompat.getColor(context, colourId);
    }


    public void resetData() {
        quakeList = origQuakeList;
    }

    public int getCount() {
        return quakeList.size();
    }

    public QuakeItem getItem(int position) {
        return quakeList.get(position);
    }

    public long getItemId(int position) {
        return quakeList.get(position).hashCode();
    }

    @Override
    public Filter getFilter() {
        if (quakeFilter == null)
            quakeFilter = new QuakeFilter();

        return quakeFilter;
    }

    private class QuakeFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0){
                // No filter found, return original list
                results.values = origQuakeList;
                results.count = origQuakeList.size();
            } else {
                // Perform filter
                ArrayList<QuakeItem> nQuakeList = new ArrayList<QuakeItem>();

                switch (constraint.toString()){
                    case "Tsunami":
                        for (QuakeItem q : quakeList){
                            if (q.getTsunami() == 1){
                                nQuakeList.add(q);
                            }
                        }
                        break;
                    case "Earthquake":
                        for (QuakeItem q : quakeList){
                            if (q.getTsunami() == 0){
                                nQuakeList.add(q);
                            }
                        }
                        break;
                    default:
                        nQuakeList.addAll(quakeList);
                }

                results.values = nQuakeList;
                results.count = nQuakeList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            if (filterResults.count == 0){
                notifyDataSetInvalidated();
            } else {
                quakeList = (ArrayList<QuakeItem>) filterResults.values;
                Log.d(LOG_TAG, "FILTER " + quakeList);
                notifyDataSetChanged();
            }
        }
    }


}
