package com.mcpubba.game;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Will on 2016-12-06.
 */

public class AndroidMusicPlayer implements MusicPlayer {
    MediaPlayer music;
    Context c;
    public AndroidMusicPlayer(Context c){
        this.c=c;
    }
    @Override
    public void loadMusic(File f){

        music = new MediaPlayer();
        music.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            music.setDataSource(c, Uri.fromFile(f));
            music.prepare();
            Log.d("test", "why");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        music.start();
        Log.d("test", "eh");
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void stop() {
        music.stop();
    }

    @Override
    public int getTime() {
        if(music==null||!music.isPlaying())return -1;
        return music.getCurrentPosition();
    }

}
