package com.example.android.miwok;

import android.graphics.drawable.Icon;

public class WordTranslation {
    private String mTranslatedWord;
    private String mDefaultWord;
    private int mImageResource = NO_IMAGE_PROVIDED;

    private static final int NO_IMAGE_PROVIDED = -1;

    public WordTranslation(String defaultWord, String translatedWord, int imageResource){
        mTranslatedWord = translatedWord;
        mDefaultWord = defaultWord;
        mImageResource = imageResource;
    }

    public WordTranslation(String defaultWord, String translatedWord){
        mTranslatedWord = translatedWord;
        mDefaultWord = defaultWord;
    }

    public String getDefaultWord(){
        return mDefaultWord;
    }

    public String getTranslatedWord(){
        return mTranslatedWord;
    }

    public int getImageResource(){
        return mImageResource;
    }

    public boolean hasImage(){
        return mImageResource != NO_IMAGE_PROVIDED;
    }
}
