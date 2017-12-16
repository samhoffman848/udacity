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
                        new WordTranslation("One", "Lutti", R.drawable.number_one),
                        new WordTranslation("Two", "Otiiko", R.drawable.number_two),
                        new WordTranslation("Three", "Tolookosu", R.drawable.number_three),
                        new WordTranslation("Four", "Oyyisa", R.drawable.number_four),
                        new WordTranslation("Five", "Massokka", R.drawable.number_five),
                        new WordTranslation("Six", "Temmokka", R.drawable.number_six),
                        new WordTranslation("Seven", "Kenekaku", R.drawable.number_seven),
                        new WordTranslation("Eight", "Kawinta", R.drawable.number_eight),
                        new WordTranslation("Nine", "Wo'e", R.drawable.number_nine),
                        new WordTranslation("Ten", "Na'aacha", R.drawable.number_ten)
                )
        );

        WordAdapter wordAdapter = new WordAdapter(this, wordList, R.color.category_numbers);
        ListView numbersListView = (ListView)findViewById(R.id.numbersListView);
        numbersListView.setAdapter(wordAdapter);
    }
}
