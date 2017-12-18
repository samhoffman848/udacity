package com.example.android.mediaplayer;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer player;
    private int currVolume = 9;
    private int maxVolume = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player = MediaPlayer.create(this, R.raw.ff_prelude);
    }

    public void playSong(View view){
        player.start();
    }

    public void pauseSong(View view){
        if(player.isPlaying()) {
            player.pause();
        }
    }

    public void stopSong(View view){
        player.stop();
        player.reset();

        resetTrack();
    }

    public void decreaseVol(View view){
        currVolume = currVolume + 1;

        if(currVolume>=maxVolume){
            currVolume = maxVolume-1;
        }

        setVolume();
    }

    public void increaseVol(View view){
        currVolume = currVolume - 1;

        if(currVolume<=1){
            currVolume = 1;
        }

        setVolume();
    }

    private void setVolume(){
        float volume = (float) (Math.log(maxVolume-currVolume)/Math.log(maxVolume));
        player.setVolume(volume, volume);
    }

    private void resetTrack(){
        AssetFileDescriptor afd = this.getResources().openRawResourceFd(R.raw.ff_prelude);
        try {
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
        } catch (IOException ex){
            Log.d("MediaPlayer", "Create failed: ", ex);
        }
    }
}
