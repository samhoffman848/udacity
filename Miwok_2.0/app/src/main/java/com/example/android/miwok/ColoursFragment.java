package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ColoursFragment extends Fragment {
    private MediaPlayer mPlayer;
    private AudioManager mAudioManager;

    public ColoursFragment() {
        // Required empty public constructor
    }

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange){
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_GAIN:
                    if (mPlayer != null){
                        mPlayer.start();
                        break;
                    }
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    if (mPlayer != null){
                        mPlayer.pause();
                        mPlayer.seekTo(0);
                        break;
                    }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create and populate array of english words
        final ArrayList<WordTranslation> wordList = new ArrayList<WordTranslation>(
                Arrays.asList(
                        new WordTranslation("Red", "Weṭeṭṭi", R.drawable.color_red, R.raw.color_red),
                        new WordTranslation("Green", "Chokokki", R.drawable.color_green, R.raw.color_green),
                        new WordTranslation("Brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown),
                        new WordTranslation("Gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray),
                        new WordTranslation("Black", "Kululli", R.drawable.color_black, R.raw.color_black),
                        new WordTranslation("White", "Kelelli", R.drawable.color_white, R.raw.color_white),
                        new WordTranslation("Dusty Yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow),
                        new WordTranslation("Mustard Yellow", "Chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow)
                )
        );

        WordAdapter wordAdapter = new WordAdapter(getActivity(), wordList, R.color.category_colors);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WordTranslation wordTranslation = wordList.get(position);

                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mPlayer = MediaPlayer.create(getActivity(), wordTranslation.getAudioResource());
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer(){
        if(mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }
    }
}
