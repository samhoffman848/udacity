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
                        new WordTranslation("Red", "Weṭeṭṭi"),
                        new WordTranslation("Green", "Chokokki"),
                        new WordTranslation("Brown", "ṭakaakki"),
                        new WordTranslation("Gray", "ṭopoppi"),
                        new WordTranslation("Black", "Kululli"),
                        new WordTranslation("White", "Kelelli"),
                        new WordTranslation("Dusty Yellow", "ṭopiisә"),
                        new WordTranslation("Mustard Yellow", "Chiwiiṭә")
                )
        );

        WordAdapter wordAdapter = new WordAdapter(this, wordList);
        ListView numbersListView = (ListView)findViewById(R.id.coloursListView);
        numbersListView.setAdapter(wordAdapter);
    }
}
