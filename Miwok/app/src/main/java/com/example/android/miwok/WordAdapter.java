package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class WordAdapter extends ArrayAdapter<WordTranslation> {
    private int mColourResource;

    public WordAdapter(Activity context, ArrayList<WordTranslation> wordTranslations, int colourResource){
        super(context, 0, wordTranslations);

        mColourResource = colourResource;
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

        RelativeLayout layout = (RelativeLayout) listItemView.findViewById(R.id.baseLayout);
        int colour = ContextCompat.getColor(getContext(), mColourResource);
        layout.setBackgroundColor(colour);

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.defaultTextView);
        defaultTextView.setText(currentTranslation.getDefaultWord());

        TextView translatedTextView = (TextView) listItemView.findViewById(R.id.translatedTextView);
        translatedTextView.setText(currentTranslation.getTranslatedWord());

        int imgId = currentTranslation.getImageResource();
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.imageView);

        if(currentTranslation.hasImage()){
            imageView.setImageResource(imgId);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
