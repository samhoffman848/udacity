package com.example.android.miwok;

public class WordTranslation {
    private String mTranslatedWord;
    private String mDefaultWord;
    private int mImageResource = NO_IMAGE_PROVIDED;
    private int mAudioResource;

    private static final int NO_IMAGE_PROVIDED = -1;

    public WordTranslation(String defaultWord, String translatedWord, int imageResource, int audioResource){
        mTranslatedWord = translatedWord;
        mDefaultWord = defaultWord;
        mImageResource = imageResource;
        mAudioResource = audioResource;
    }

    public WordTranslation(String defaultWord, String translatedWord, int audioResource){
        mTranslatedWord = translatedWord;
        mDefaultWord = defaultWord;
        mAudioResource = audioResource;
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

    public int getAudioResource(){
        return mAudioResource;
    }

    public boolean hasImage(){
        return mImageResource != NO_IMAGE_PROVIDED;
    }
}
