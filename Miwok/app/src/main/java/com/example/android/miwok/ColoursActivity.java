package com.example.android.miwok;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class ColoursActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colours);

        // Create and populate array of english words
        ArrayList<WordTranslation> wordList = new ArrayList<WordTranslation>(
                Arrays.asList(
                        new WordTranslation("Red", "Weṭeṭṭi", R.drawable.color_red),
                        new WordTranslation("Green", "Chokokki", R.drawable.color_green),
                        new WordTranslation("Brown", "ṭakaakki", R.drawable.color_brown),
                        new WordTranslation("Gray", "ṭopoppi", R.drawable.color_gray),
                        new WordTranslation("Black", "Kululli", R.drawable.color_black),
                        new WordTranslation("White", "Kelelli", R.drawable.color_white),
                        new WordTranslation("Dusty Yellow", "ṭopiisә", R.drawable.color_dusty_yellow),
                        new WordTranslation("Mustard Yellow", "Chiwiiṭә", R.drawable.color_mustard_yellow)
                )
        );

        WordAdapter wordAdapter = new WordAdapter(this, wordList, R.color.category_colors);
        ListView numbersListView = (ListView)findViewById(R.id.coloursListView);
        numbersListView.setAdapter(wordAdapter);
    }
}
