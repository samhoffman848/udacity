package com.example.android.quakereport;

import android.app.Activity;
import java.text.DecimalFormat;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class QuakeAdapter extends ArrayAdapter<QuakeItem>{
    public QuakeAdapter(Activity context, ArrayList<QuakeItem> quakeItems){
        super(context, 0, quakeItems);
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

    private String formatDate(Long timeInMilliseconds){
        Date dateObj = new Date(timeInMilliseconds);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.UK);
        return dateFormat.format(dateObj);
    }

    private String formatTime(Long timeInMilliseconds){
        Date dateObj = new Date(timeInMilliseconds);
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);
        return dateFormat.format(dateObj);
    }

    private String[] formatLocation(String locationFull){
        String locationSeparator = " of ";

        if (locationFull.contains(locationSeparator)){
            String[] splitLocation = locationFull.split(locationSeparator);
            splitLocation[0] += locationSeparator;
            return splitLocation;
        } else {
            return new String[] {getContext().getString(R.string.near_the), locationFull};
        }
    }

    private String formatMagnitude(double mag){
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(mag);
    }

    private int getMagnitudeColour(double mag){
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

        return ContextCompat.getColor(getContext(), colourId);
    }
}
