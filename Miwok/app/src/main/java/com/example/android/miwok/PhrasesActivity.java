package com.example.android.miwok;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class PhrasesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);

        // Create and populate array of english words
        ArrayList<WordTranslation> wordList = new ArrayList<WordTranslation>(
                Arrays.asList(
                        new WordTranslation("Where are you going?", "minto wuksus"),
                        new WordTranslation("What is your name?", "tinnә oyaase'nә"),
                        new WordTranslation("My name is...", "oyaaset..."),
                        new WordTranslation("How are you feeling?", "michәksәs?"),
                        new WordTranslation("I'm feeling good.", "kuchi achit"),
                        new WordTranslation("Are you coming?", "әәnәs'aa?"),
                        new WordTranslation("Yes, I'm coming.", "hәә’ әәnәm"),
                        new WordTranslation("I'm coming.", "әәnәm"),
                        new WordTranslation("Let's go.", "yoowutis"),
                        new WordTranslation("Come here.", "әnni'nem")
                )
        );

        WordAdapter wordAdapter = new WordAdapter(this, wordList, R.color.category_phrases);
        ListView numbersListView = (ListView)findViewById(R.id.phrasesListView);
        numbersListView.setAdapter(wordAdapter);
    }
}
