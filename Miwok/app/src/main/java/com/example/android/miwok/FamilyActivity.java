package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class FamilyActivity extends AppCompatActivity {
    private MediaPlayer mPlayer;
    private AudioManager mAudioManager;

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
                    mPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    mPlayer.pause();
                    mPlayer.seekTo(0);
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    mPlayer.pause();
                    mPlayer.seekTo(0);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

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

        WordAdapter wordAdapter = new WordAdapter(this, wordList, R.color.category_family);
        ListView listView = (ListView)findViewById(R.id.familyListView);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WordTranslation wordTranslation = wordList.get(position);

                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mPlayer = MediaPlayer.create(FamilyActivity.this, wordTranslation.getAudioResource());
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    @Override
    protected void onStop() {
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
