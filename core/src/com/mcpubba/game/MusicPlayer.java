package com.mcpubba.game;

import java.io.File;

/**
 * Created by Will on 2016-12-06.
 */

public interface MusicPlayer {
    void loadMusic(File f);
    void play();
    void pause();
    void stop();
    int getTime();
}
