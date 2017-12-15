package com.example.android.miwok;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class FamilyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        // Create and populate array of english words
        ArrayList<WordTranslation> wordList = new ArrayList<WordTranslation>(
                Arrays.asList(
                        new WordTranslation("Father", "әpә"),
                        new WordTranslation("Mother", "әṭa"),
                        new WordTranslation("Son", "Angsi"),
                        new WordTranslation("Daughter", "Tune"),
                        new WordTranslation("Older Brother", "Taachi"),
                        new WordTranslation("Younger Brother", "Chalitti"),
                        new WordTranslation("Older Sister", "Teṭe"),
                        new WordTranslation("Younger Sister", "Kolliti"),
                        new WordTranslation("Grandfather", "Paapa"),
                        new WordTranslation("Grandmother", "Ama")
                )
        );

        WordAdapter wordAdapter = new WordAdapter(this, wordList);
        ListView numbersListView = (ListView)findViewById(R.id.familyListView);
        numbersListView.setAdapter(wordAdapter);
    }
}
