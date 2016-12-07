package com.mcpubba.game.game;

/**
 * Created by Will on 2016-12-04.
 */

public class Note {
    long time;
    long endTime;
    public Note(long time, long endTime){
        this.time = time;
        this.endTime = endTime;
    }
    public long len(){return endTime-time;}
    public long time(){return time;}
    public long endTime(){return endTime;}
}
