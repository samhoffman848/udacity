package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class NumbersActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_numbers);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Create and populate array of english words
        final ArrayList<WordTranslation> wordList = new ArrayList<WordTranslation>(
                Arrays.asList(
                        new WordTranslation("One", "Lutti", R.drawable.number_one, R.raw.number_one),
                        new WordTranslation("Two", "Otiiko", R.drawable.number_two, R.raw.number_two),
                        new WordTranslation("Three", "Tolookosu", R.drawable.number_three, R.raw.number_three),
                        new WordTranslation("Four", "Oyyisa", R.drawable.number_four, R.raw.number_four),
                        new WordTranslation("Five", "Massokka", R.drawable.number_five, R.raw.number_five),
                        new WordTranslation("Six", "Temmokka", R.drawable.number_six, R.raw.number_six),
                        new WordTranslation("Seven", "Kenekaku", R.drawable.number_seven, R.raw.number_seven),
                        new WordTranslation("Eight", "Kawinta", R.drawable.number_eight, R.raw.number_eight),
                        new WordTranslation("Nine", "Wo'e", R.drawable.number_nine, R.raw.number_nine),
                        new WordTranslation("Ten", "Na'aacha", R.drawable.number_ten, R.raw.number_ten)
                )
        );

        WordAdapter wordAdapter = new WordAdapter(this, wordList, R.color.category_numbers);
        ListView listView = (ListView)findViewById(R.id.numbersListView);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WordTranslation wordTranslation = wordList.get(position);

                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mPlayer = MediaPlayer.create(NumbersActivity.this, wordTranslation.getAudioResource());
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
