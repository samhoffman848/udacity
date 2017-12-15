package com.example.android.miwok;

public class WordTranslation {
    private String mTranslatedWord;
    private String mDefaultWord;

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
}
