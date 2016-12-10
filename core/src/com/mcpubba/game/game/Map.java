package com.mcpubba.game.game;


import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.mcpubba.game.screens.GameScreen;
import com.mcpubba.game.screens.ScoreScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Will on 2016-12-04.
 */

public class Map {
    private final boolean useHitSounds = false;
    private static final Pattern hitObj = Pattern.compile("(\\d+),\\d+,(\\d+),\\d+,\\d+,(\\d*):?\\d:\\d+:\\d+:\\d+:(.*)");
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
    private ArrayList<FileHandle> sounds;
    private HashMap<String, Sound> loadedSounds;
    private Score score;
    private GameScreen game;
    public Map(File f){
        int note = 0;
        sounds = new ArrayList<FileHandle>();
        loadedSounds = new HashMap<String, Sound>();
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
                        if(useHitSounds&&
                                m.group(4).length()>0&&
                                !sounds.contains(Gdx.files.external(
                                "ManiaAndroid/Songs/"+
                                        f.getParentFile().getName()+
                                        "/"+m.group(4)))) {
                            sounds.add(Gdx.files.external(
                                    "ManiaAndroid/Songs/"+
                                            f.getParentFile().getName()+
                                            "/"+m.group(4)));
                        }
                        notes.get(Integer.parseInt(m.group(1)) * keys / 512)
                                .add(new Note(
                                        Integer.parseInt(m.group(2)),
                                        m.group(3).length() == 0 ? 0 : Integer.parseInt(m.group(3)),
                                        m.group(4)));
                        note++;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        score = new Score(note, this);
    }
    public void load(GameScreen g){
        game = g;
        game.game.loadMusic(mp3.file(), this);
        if(useHitSounds)loadSounds();
        unHit = new ArrayList<ArrayList<Note>>();
        for(int i = 0; i < notes.size(); i++){
            unHit.add(new ArrayList<Note>());
            unHit.get(i).addAll(notes.get(i));
        }
        game.game.play();
        game.ready = true;
    }
    public void loadSounds(){
        for(FileHandle f : sounds){
            loadedSounds.put(f.name(), Gdx.audio.newSound(f));
        }
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
                registerHit(hit, time, getNote(lane));
                unHit.get(lane).remove(0);
            }else{
                getNote(lane).hit(hit, time());
                if(useHitSounds&&
                        !getNote(lane).getSound(false).equals("")
                        &&!getNote(lane).soundPlayed
                        &&hit!=0)
                    loadedSounds.get(getNote(lane).getSound(true)).play();
                score.addTime(time);
            }
        }
    }
    private void registerHit(int hit, int time, Note note){
        if(useHitSounds&&
                !note.getSound(false).equals("")
                &&!note.soundPlayed
                &&hit!=0) {
            loadedSounds.get(note.getSound(true)).play();
        }
        if(hit==0&&score.getCombo()>19){
            game.game.skin.missSound.play();
        }

        score.noteHit(hit, time);
        game.game.skin.anim.setCurrentHit(hit);
    }
    private void hold(int lane){
        if(getNote(lane)==null)return;
        if(getNote(lane).endTime==0)return;
        if(getNote(lane).endTime-time()<=-127+3*od){
            registerHit(getNote(lane).getFirstHit()==0?50:100, Integer.MIN_VALUE, getNote(lane));
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
        int hit;
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
            if(score.getCombo()>19){
                game.game.skin.missSound.play();
            }
            return;
        }
        if(note.slbreak){
            registerHit(50, time, note);
            unHit.get(lane).remove(0);
            return;
        }
        switch (hit){
            case 320:
                switch (note.getFirstHit()){
                    case 320:
                        registerHit(320, time, note);
                        break;
                    case 300:
                        registerHit(300, time, note);
                        break;
                    case 200:
                    case 100:
                        registerHit(200, time, note);
                        break;
                    case 50:
                        registerHit(100, time, note);
                        break;
                    case 0:
                        registerHit(50, time, note);
                        break;
                }
                break;
            case 300:
                switch (note.getFirstHit()){
                    case 320:
                    case 300:
                    case 200:
                        registerHit(300, time, note);
                        break;
                    case 100:
                    case 50:
                        registerHit(200, time, note);
                        break;
                    case 0:
                        registerHit(50, time, note);
                        break;
                }
                break;
            case 200:
                switch (note.getFirstHit()){
                    case 320:
                    case 300:
                        registerHit(300, time, note);
                        break;
                    case 200:
                    case 100:
                        registerHit(200, time, note);
                        break;
                    case 50:
                        registerHit(100, time, note);
                        break;
                    case 0:
                        registerHit(50, time, note);
                        break;
                }
                break;
            case 100:
                switch (note.getFirstHit()){
                    case 320:
                    case 300:
                        registerHit(300, time, note);
                        break;
                    case 200:
                        registerHit(200, time, note);
                        break;
                    case 100:
                    case 50:
                        registerHit(100, time, note);
                        break;
                    case 0:
                        registerHit(50, time, note);
                        break;
                }
                break;
            case 50:
                switch (note.getFirstHit()){
                    case 320:
                    case 300:
                    case 200:
                        registerHit(100, time, note);
                        break;
                    case 100:
                    case 50:
                    case 0:
                        registerHit(50, time, note);
                        break;
                }
                break;
            case 0:
                registerHit(0, time, note);
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
                    getNote(lane).time - time() < -151 + 3 * od &&
                    getNote(lane).endTime == 0) {

                registerHit(0, Integer.MIN_VALUE, getNote(lane));
                unHit.get(lane).remove(0);
            }
            while (unHit.get(lane).size()>0&&
                    getNote(lane).endTime > 0&&
                    getNote(lane).endTime - time() < -151 + 3 * od) {
                unHit.get(lane).remove(0);

                registerHit(0, Integer.MIN_VALUE, getNote(lane));
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
