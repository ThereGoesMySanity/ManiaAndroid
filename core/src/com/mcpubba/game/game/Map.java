package com.mcpubba.game.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.mcpubba.game.GameScreen;
import com.mcpubba.game.ManiaAndroid;
import com.mcpubba.game.MusicPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Will on 2016-12-04.
 */

public class Map {
    private static final Pattern hitObj = Pattern.compile("(\\d+),\\d+,(\\d+),\\d+,\\d+,(\\d+):0:0:0:0?:?");
    private FileHandle mp3;
    private int keys;
    private int leadIn;
    private float od;
    private String title;
    private String diff;
    private String artist;
    private String creator;
    private ArrayList<ArrayList<Note>> notes;
    private ArrayList<ArrayList<Note>> unHit;
    private Score score;
    private long startTime;
    private ManiaAndroid m;
    public Map(File f, ManiaAndroid man){
        score = new Score();
        this.m=man;
        if(f.getName().endsWith("osu")){
            Scanner s = null;
            try {
                s = new Scanner(f);
                while(s.hasNextLine()) {
                    String i = s.nextLine();
                    if (i.startsWith("AudioFilename:")) {
                        mp3 = new FileHandle(new File(f.getParent(), i.substring(15)));
                    }
                    if (i.startsWith("AudioLeadIn:")) {
                        leadIn = Integer.parseInt(i.substring(13));
                    }
                    if (i.startsWith("Title:")) {
                        title = i.substring(6);
                    }
                    if (i.startsWith("Artist:")) {
                        artist = i.substring(7);
                    }
                    if (i.startsWith("Creator:")) {
                        creator = i.substring(8);
                    }
                    if (i.startsWith("Version:")) {
                        diff = i.substring(8);
                    }
                    if (i.startsWith("CircleSize:")) {
                        keys = Integer.parseInt(i.substring(11));
                    }
                    if (i.startsWith("OverallDifficulty:")) {
                        od = Float.parseFloat(i.substring(18));
                    }
                    if (i.equals("[HitObjects]")) {
                        break;
                    }
                }

                notes = new ArrayList<ArrayList<Note>>();
                unHit = new ArrayList<ArrayList<Note>>();
                for(int i = 0; i < keys; i++){
                    notes.add(new ArrayList<Note>());
                }

                while(s.hasNextLine()) {
                    String i = s.nextLine();
                    Matcher m = hitObj.matcher(i);
                    if(m.matches()) {
                        notes.get(Integer.parseInt(m.group(1))*keys/512)
                                .add(new Note(
                                        Long.parseLong(m.group(2)),
                                        Long.parseLong(m.group(3))
                                ));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public void load(){
        m.loadMusic(mp3.file());
        unHit = new ArrayList<ArrayList<Note>>();
        for(int i = 0; i < notes.size(); i++){
            unHit.add(new ArrayList<Note>());
            unHit.get(i).addAll(notes.get(i));
        }
        m.play();
    }
    public void hitNote(int lane){
        if(getNote(lane)==null)return;
        long time = getNote(lane).time-time();
        long absTime = Math.abs(time);
        boolean remove = true;
        if(absTime<17){
            score.noteHit(320);
        } else if(absTime<64-3*od){
            score.noteHit(300);
        }else if(absTime<97-3*od){
            score.noteHit(200);
        }else if(absTime<127-3*od){
            score.noteHit(100);
        }else if(absTime<151-3*od) {
            score.noteHit(50);
        }else if(getNote(lane).endTime>0){
            score.noteHit(50);
            score.sliderBreak();
        }else if(time<188-3*od){
            score.noteHit(0);
        }else{
            remove = false;
        }
        if(remove)unHit.get(lane).remove(0);
    }
    public void hold(int lane){
        if(getNote(lane)==null)return;
        if(getNote(lane).endTime>0&&getNote(lane).time<0){
            score.sliderHeld();
        }
    }
    public void releaseNote(int lane){
        if(getNote(lane)==null)return;
        if(getNote(lane).endTime==0)return;
        long time = getNote(lane).endTime;
        if(Math.abs(time)<151-3*od){
            score.noteHit(300);
        }else if(time<188-3*od){
            score.noteHit(0);
        }
    }
    public Note getNote(int lane){
        if(unHit.get(lane).size()==0)return null;
        return unHit.get(lane).get(0);
    }
    public void update() {
        if(unHit.size()==0)return;
        for (int lane = 0; lane < keys; lane++){
            if (unHit.get(lane).get(0).time - time() < -151 + 3 * od &&
                    unHit.get(lane).get(0).endTime == 0) {
                unHit.get(lane).remove(0);
                score.noteHit(0);
            }
        }
    }
    public void pause(){
        m.pause();
    }
    public void play(){
        m.play();
    }
    public Score getScore(){return score;}
    public int getKeys() {
        return keys;
    }

    public float getOD() {
        return od;
    }

    public String getTitle() {
        return title;
    }

    public String getDiff() {
        return diff;
    }

    public String getArtist() {
        return artist;
    }

    public String getCreator() {
        return creator;
    }

    public ArrayList<ArrayList<Note>> getUnHit() {
        return unHit;
    }

    public int time(){
        return m.time();
    }
}
