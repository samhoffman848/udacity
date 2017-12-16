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
                        new WordTranslation("Father", "әpә", R.drawable.family_father),
                        new WordTranslation("Mother", "әṭa", R.drawable.family_mother),
                        new WordTranslation("Son", "Angsi", R.drawable.family_son),
                        new WordTranslation("Daughter", "Tune", R.drawable.family_daughter),
                        new WordTranslation("Older Brother", "Taachi", R.drawable.family_older_brother),
                        new WordTranslation("Younger Brother", "Chalitti", R.drawable.family_younger_brother),
                        new WordTranslation("Older Sister", "Teṭe", R.drawable.family_older_sister),
                        new WordTranslation("Younger Sister", "Kolliti", R.drawable.family_younger_sister),
                        new WordTranslation("Grandfather", "Paapa", R.drawable.family_grandfather),
                        new WordTranslation("Grandmother", "Ama", R.drawable.family_grandmother)
                )
        );

        WordAdapter wordAdapter = new WordAdapter(this, wordList, R.color.category_family);
        ListView numbersListView = (ListView)findViewById(R.id.familyListView);
        numbersListView.setAdapter(wordAdapter);
    }
}
