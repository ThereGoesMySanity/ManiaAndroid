package com.mcpubba.game.game;


import com.badlogic.gdx.files.FileHandle;
import com.mcpubba.game.screens.GameScreen;
import com.mcpubba.game.screens.ScoreScreen;

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
    private static final Pattern hitObj = Pattern.compile("(\\d+),\\d+,(\\d+),\\d+,\\d+,(\\d*):?\\d:0:0:0:");
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
    private GameScreen game;
    public Map(File f){
        int note = 0;
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
                                        Integer.parseInt(m.group(2)),
                                        m.group(3).length()==0?0:Integer.parseInt(m.group(3))
                                ));
                        note++;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        score = new Score(note);
    }
    public void load(GameScreen g){
        game = g;
        game.game.loadMusic(mp3.file(), this);
        unHit = new ArrayList<ArrayList<Note>>();
        for(int i = 0; i < notes.size(); i++){
            unHit.add(new ArrayList<Note>());
            unHit.get(i).addAll(notes.get(i));
        }
        game.game.play();
    }
    public void hitNote(int lane){
        if(getNote(lane)==null)return;
        int time = getNote(lane).time-time();
        int absTime = Math.abs(time);
        int hit = -1;
        if(absTime<17){
            hit = (320);
        } else if(absTime<64-3*od){
            hit = (300);
        }else if(absTime<97-3*od){
            hit = (200);
        }else if(absTime<127-3*od){
            hit = (100);
        }else if(absTime<151-3*od) {
            hit = (50);
        }else if(time<188-3*od){
            hit = (0);
        }
        if(hit>-1){
            if(getNote(lane).endTime==0) {
                unHit.get(lane).remove(0);
                registerHit(hit, time);
            }else{
                getNote(lane).hit(hit, time());
                score.addTime(time);
            }
        }
    }
    public void registerHit(int hit, int time){
        score.noteHit(hit, time);
        game.anim.setCurrentHit(hit);
    }
    public void hold(int lane){
        if(getNote(lane)==null)return;
        if(getNote(lane).endTime==0)return;
        if(getNote(lane).endTime-time()<=-127+3*od){

            registerHit(getNote(lane).getFirstHit()==0?50:100, Integer.MIN_VALUE);
            unHit.get(lane).remove(0);
            return;
        }
        if(time()-getNote(lane).lastTick>99){
            score.sliderHeld();
            getNote(lane).tick();
        }
    }
    public void releaseNote(int lane){
        Note note = getNote(lane);
        if(note==null)return;
        if(note.endTime==0)return;
        if(note.time()-time()>0)return;
        int time = note.endTime-time();
        int absTime = Math.abs(time);
        int hit = -1;
        if(absTime<17){
            hit = (320);
        } else if(absTime<64-3*od){
            hit = (300);
        }else if(absTime<97-3*od){
            hit = (200);
        }else if(absTime<127-3*od){
            hit = (100);
        }else{
            note.slBreak();
            score.sliderBreak();
            return;
        }
        if(note.slbreak){
            registerHit(50, time);
            unHit.get(lane).remove(0);
            return;
        }
        switch (hit){
            case 320:
                switch (note.getFirstHit()){
                    case 320:
                        registerHit(320, time);
                        break;
                    case 300:
                        registerHit(300, time);
                        break;
                    case 200:
                    case 100:
                        registerHit(200, time);
                        break;
                    case 50:
                        registerHit(100, time);
                        break;
                    case 0:
                        registerHit(50, time);
                        break;
                }
                break;
            case 300:
                switch (note.getFirstHit()){
                    case 320:
                    case 300:
                    case 200:
                        registerHit(300, time);
                        break;
                    case 100:
                    case 50:
                        registerHit(200, time);
                        break;
                    case 0:
                        registerHit(50, time);
                        break;
                }
                break;
            case 200:
                switch (note.getFirstHit()){
                    case 320:
                    case 300:
                        registerHit(300, time);
                        break;
                    case 200:
                    case 100:
                        registerHit(200, time);
                        break;
                    case 50:
                        registerHit(100, time);
                        break;
                    case 0:
                        registerHit(50, time);
                        break;
                }
                break;
            case 100:
                switch (note.getFirstHit()){
                    case 320:
                    case 300:
                        registerHit(300, time);
                        break;
                    case 200:
                        registerHit(200, time);
                        break;
                    case 100:
                    case 50:
                        registerHit(100, time);
                        break;
                    case 0:
                        registerHit(50, time);
                        break;
                }
                break;
            case 50:
                switch (note.getFirstHit()){
                    case 320:
                    case 300:
                    case 200:
                        registerHit(100, time);
                        break;
                    case 100:
                    case 50:
                    case 0:
                        registerHit(50, time);
                        break;
                }
                break;
            case 0:
                registerHit(0, time);
                break;
        }
        unHit.get(lane).remove(0);
    }
    public Note getNote(int lane){
        if(lane>=keys||lane<0)return null;
        if(unHit.get(lane).size()==0)return null;
        return unHit.get(lane).get(0);
    }
    public void update() {
        if(unHit.size()==0)return;
        for (int lane = 0; lane < keys; lane++){
            if (game.getLane(lane)) hold(lane);
            while (unHit.get(lane).size()>0&&
                    unHit.get(lane).get(0).time - time() < -151 + 3 * od &&
                    unHit.get(lane).get(0).endTime == 0) {
                unHit.get(lane).remove(0);

                registerHit(0, Integer.MIN_VALUE);
            }
            while (unHit.get(lane).size()>0&&
                    unHit.get(lane).get(0).endTime > 0&&
                    unHit.get(lane).get(0).endTime - time() < -151 + 3 * od) {
                unHit.get(lane).remove(0);

                registerHit(0, Integer.MIN_VALUE);
            }
        }
    }
    public void onFinish(){
        game.game.setScreen(new ScoreScreen(game.game, score));
    }
    public void pause(){
        game.game.pause();
    }
    public void play(){
        game.game.play();
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
        return game.game.time()-game.offset;
    }
}
