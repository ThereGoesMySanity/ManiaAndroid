package com.mcpubba.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

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
    public boolean soundPlayed;
    public String getSound(boolean ound) {
        soundPlayed = ound||soundPlayed;
        return sound;
    }

    String sound;
    public Note(int time, int endTime, String s){
        this.time = time;
        this.endTime = endTime;
        this.lastTick = time;
        sound = s;
        slbreak = false;
        firstHit = -1;
        soundPlayed = false;
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
        if(endTime>0) {
            firstHit = i;
            lastTick = time;
        }
    }

}
