package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class WordAdapter extends ArrayAdapter<WordTranslation> {

    public WordAdapter(Activity context, ArrayList<WordTranslation> wordTranslations){
        super(context, 0, wordTranslations);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }

        WordTranslation currentTranslation = getItem(position);

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.defaultTextView);
        defaultTextView.setText(currentTranslation.getDefaultWord());

        TextView translatedTextView = (TextView) listItemView.findViewById(R.id.translatedTextView);
        translatedTextView.setText(currentTranslation.getTranslatedWord());

        return listItemView;
    }
}
