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


public class PhrasesFragment extends Fragment {
    private MediaPlayer mPlayer;
    private AudioManager mAudioManager;

    public PhrasesFragment() {
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
                        new WordTranslation("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going),
                        new WordTranslation("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name),
                        new WordTranslation("My name is...", "oyaaset...", R.raw.phrase_my_name_is),
                        new WordTranslation("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling),
                        new WordTranslation("I'm feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good),
                        new WordTranslation("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming),
                        new WordTranslation("Yes, I'm coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming),
                        new WordTranslation("I'm coming.", "әәnәm", R.raw.phrase_im_coming),
                        new WordTranslation("Let's go.", "yoowutis", R.raw.phrase_lets_go),
                        new WordTranslation("Come here.", "әnni'nem", R.raw.phrase_come_here)
                )
        );

        WordAdapter wordAdapter = new WordAdapter(getActivity(), wordList, R.color.category_phrases);
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
