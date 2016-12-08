package com.mcpubba.game;

import com.mcpubba.game.game.Map;

import java.io.File;

/**
 * Created by Will on 2016-12-06.
 */

public interface MusicPlayer {
    void loadMusic(File f, Map m);
    void play();
    void pause();
    void stop();
    int getTime();
}
