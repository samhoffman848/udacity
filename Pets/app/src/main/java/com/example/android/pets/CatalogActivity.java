package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        Cursor cursor = getContentResolver().query(
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        Integer idInt = cursor.getColumnIndex(PetEntry._ID);
        Integer nameInt = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
        Integer breedInt = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
        Integer genderInt = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
        Integer weightInt = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText(
                    "Number of rows in pets database table: " + cursor.getCount() +
                    "\n\nid - name - breed - gender - weight\n"
            );

            while (cursor.moveToNext()){
                displayView.append(
                        "\n" + cursor.getInt(idInt) + " "
                        + cursor.getString(nameInt) + " "
                        + cursor.getString(breedInt) + " "
                        + cursor.getInt(genderInt) + " "
                        + cursor.getInt(weightInt)
                );
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void getPetList(){

        String[] projection = {
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED
        };

        Cursor cursor = getContentResolver().query(
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}