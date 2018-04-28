package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pets.data.PetContract.PetEntry;

public class PetDbHelper extends SQLiteOpenHelper {
    // Database file name
    private static final String DATABASE_NAME = "shelter.db";

    // Database version. If you change the schema you must increment that db version.
    private static final int DATABASE_VERSION = 1;

    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetEntry.TABLE_NAME + " (" +
                PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, " +
                PetEntry.COLUMN_PET_BREED + " TEXT DEFAULT 'Unknown', " +
                PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, " +
                PetEntry.COLUMN_PET_WEIGHT + " INTEGER DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // To implement later
    }
}
