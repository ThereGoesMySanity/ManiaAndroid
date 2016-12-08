package com.mcpubba.game.game;

/**
 * Created by Will on 2016-12-04.
 */

public class Note {
    int time;
    int endTime;

    public boolean isSlbreak() {
        return slbreak;
    }

    boolean slbreak;
    int firstHit;
    int lastTick;
    public Note(int time, int endTime){
        this.time = time;
        this.endTime = endTime;
        this.lastTick = time;
        slbreak = false;
        firstHit = -1;
    }
    public int len(){return endTime-time;}
    public int time(){return time;}
    public int endTime(){return endTime;}
    public void slBreak(){slbreak = true;}
    public void tick(){
        lastTick += 100;
    }
    public int getFirstHit(){
        return firstHit;
    }
    public void hit(int i, int time){
        firstHit = i;
        lastTick = time;
    }
}
