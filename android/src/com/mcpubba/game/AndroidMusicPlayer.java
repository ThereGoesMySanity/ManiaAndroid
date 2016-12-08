package com.mcpubba.game;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.mcpubba.game.game.Map;

import java.io.File;
import java.io.IOException;

/**
 * Created by Will on 2016-12-06.
 */

public class AndroidMusicPlayer implements com.mcpubba.game.util.MusicPlayer, MediaPlayer.OnCompletionListener {
    MediaPlayer music;
    Context c;
    Map map;
    public AndroidMusicPlayer(Context c){
        this.c=c;
    }
    @Override
    public void loadMusic(File f, Map m){
        map = m;
        music = new MediaPlayer();
        music.setAudioStreamType(AudioManager.STREAM_MUSIC);
        music.setOnCompletionListener(this);
        try {
            music.setDataSource(c, Uri.fromFile(f));
            music.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        music.start();
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

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        map.onFinish();
    }
}
