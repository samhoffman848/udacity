package com.example.android.quakereport;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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

        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitudeView);
        magnitudeTextView.setText(String.valueOf(currentItem.getMagnitude()));

        TextView locationTextView = (TextView) listItemView.findViewById(R.id.locationView);
        locationTextView.setText(currentItem.getLocation());

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.timeView);
        timeTextView.setText(currentItem.getTime());

        return listItemView;
    }
}
