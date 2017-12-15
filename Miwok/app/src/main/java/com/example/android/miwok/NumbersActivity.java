package com.example.android.miwok;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class NumbersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        // Create and populate array of english words
        ArrayList<WordTranslation> wordList = new ArrayList<WordTranslation>(
                Arrays.asList(
                        new WordTranslation("One", "Lutti"),
                        new WordTranslation("Two", "Otiiko"),
                        new WordTranslation("Three", "Tolookosu"),
                        new WordTranslation("Four", "Oyyisa"),
                        new WordTranslation("Five", "Massokka"),
                        new WordTranslation("Six", "Temmokka"),
                        new WordTranslation("Seven", "Kenekaku"),
                        new WordTranslation("Eight", "Kawinta"),
                        new WordTranslation("Nine", "Wo'e"),
                        new WordTranslation("Ten", "Na'aacha")
                )
        );

        WordAdapter wordAdapter = new WordAdapter(this, wordList);
        ListView numbersListView = (ListView)findViewById(R.id.numbersListView);
        numbersListView.setAdapter(wordAdapter);
    }
}
