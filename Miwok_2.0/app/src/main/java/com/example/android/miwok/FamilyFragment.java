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


public class FamilyFragment extends Fragment {
    private MediaPlayer mPlayer;
    private AudioManager mAudioManager;

    public FamilyFragment() {
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
                        new WordTranslation("Father", "әpә", R.drawable.family_father, R.raw.family_father),
                        new WordTranslation("Mother", "әṭa", R.drawable.family_mother, R.raw.family_mother),
                        new WordTranslation("Son", "Angsi", R.drawable.family_son, R.raw.family_son),
                        new WordTranslation("Daughter", "Tune", R.drawable.family_daughter, R.raw.family_daughter),
                        new WordTranslation("Older Brother", "Taachi", R.drawable.family_older_brother, R.raw.family_older_brother),
                        new WordTranslation("Younger Brother", "Chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother),
                        new WordTranslation("Older Sister", "Teṭe", R.drawable.family_older_sister, R.raw.family_older_sister),
                        new WordTranslation("Younger Sister", "Kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister),
                        new WordTranslation("Grandfather", "Paapa", R.drawable.family_grandfather, R.raw.family_grandfather),
                        new WordTranslation("Grandmother", "Ama", R.drawable.family_grandmother, R.raw.family_grandmother)
                )
        );

        WordAdapter wordAdapter = new WordAdapter(getActivity(), wordList, R.color.category_family);
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
