package com.mcpubba.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mcpubba.game.screens.GameScreen;
import com.mcpubba.game.util.Animation;
import com.mcpubba.game.util.SkinIniParser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Will on 2016-12-08.
 *
 * A class to manage details of the skin.
 * This is legitimately one of the most well-structured classes I've ever written
 * and I feel really proud of it.
 */

public class Skin {
    HashMap<String, Animation> note;
    HashMap<String, Animation> key;
    HashMap<String, Animation> rank;
    Animation lightingN;
    Animation lightingL;
    Animation back;

    Animation stageLight;
    Texture percent;
    Texture dot;
    public Sound missSound;

    FileHandle file;
    Texture[] combo;
    Texture[] score;
    public HitAnim anim;
    SkinIniParser ini;
    int hitLoc;
    final int textScale = 2;
    //the coordinates for items are based on a height of 480.
    //changing screenScale changes where the top of the phone is
    //with respect to those coordinates. With screenScale = 2,
    //the top of the screen according to osu! is twice as high as
    //the height of the phone.
    final float screenScale = 1.5f;

    public Skin(){
        this(Gdx.files.internal("mania"));
    }
    public Skin(FileHandle f){
        file = f;
    }
    public void load(){
        ini = new SkinIniParser(fileIgnoreCase(file, "skin.ini"));

        missSound = loadSound(file.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.startsWith("combobreak");
            }
        })[0].name(), file);
        note = loadAnimatableTextures("mania-note",
                new String[]{"1","2","S","1H","2H","SH","1L","2L","SL","1T","2T","ST"},file, true);

        key = loadAnimatableTextures("mania-key",
                new String[]{"1","2","S","1D","2D","SD"},file, true);

        anim = new HitAnim(loadAnimatableTextures("mania-hit",
                new String[]{"0","50","100","200","300", "300g"},file,true));

        rank = loadAnimatableTextures("ranking-",
                new String[]{"XH", "X", "SH", "S", "A", "B", "C", "D"}, file, true);
        lightingN = loadAnimatableTexture("lightingN", file, false);
        lightingL = loadAnimatableTexture("lightingL", file, true);
        stageLight = loadAnimatableTexture("mania-stage-light", file, true);
        combo = new Texture[10];
        for(int i = 0; i < 10; i++){
            combo[i] = loadTexture(ini.getFonts("ComboPrefix")+"-"+i, file);
        }
        score = new Texture[10];
        for(int i = 0; i < 10; i++){
            score[i] = loadTexture(ini.getFonts("ScorePrefix")+"-"+i, file);
        }
        dot = loadTexture(ini.getFonts("ScorePrefix")+"-dot", file);
        percent = loadTexture(ini.getFonts("ScorePrefix")+"-percent", file);
    }
    public Sound loadSound(String name, FileHandle f){
        if(f.child(name).exists()) {
            return Gdx.audio.newSound(f.child(name));
        }
        return Gdx.audio.newSound(Gdx.files.internal("mania").child(name));
    }
    public Texture loadTexture(String name, FileHandle f){
        if(f.child(name+".png").exists()) {
            return new Texture(f.child(name+".png"));
        }else if(!f.equals(Gdx.files.internal("mania"))){
            return loadTexture(name,Gdx.files.internal("mania"));
        }else{
            Gdx.app.log("infinite loop o no", name);
            return null;
        }
    }
    public HashMap<String, Texture> loadTextures(String name, String[] poss,
                                                 FileHandle f){
        HashMap<String, Texture> h = new HashMap<String, Texture>();
        for(String i : poss) {
            if(f.child(name+i+".png").exists()) {
                h.put(i, new Texture(f.child(name+i+".png")));
            }else if(!f.equals(Gdx.files.internal("mania"))){
                return loadTextures(name, poss, Gdx.files.internal("mania"));
            }else{
                Gdx.app.log("infinite loop o no", name);
            }
        }
        return h;
    }
    public Animation loadAnimatableTexture(String name, FileHandle f, boolean loop, int framerate){
        if(f.child(name+".png").exists()) {
            return new Animation(new Texture(f.child(name+".png")));
        }else if(f.child(name +"-0.png").exists()){
            ArrayList<Texture> arr = new ArrayList<Texture>();
            for (int j = 0; f.child(name +"-"+ j + ".png").exists(); j++) {
                try {
                    arr.add(new Texture(f.child(name + "-" + j + ".png")));
                }catch (Exception e){}
            }
            return new Animation(arr,framerate, loop);
        }else if(!f.equals(Gdx.files.internal("mania"))){
            return loadAnimatableTexture(name, Gdx.files.internal("mania"), loop, framerate);
        }else{
            Gdx.app.log("infinite loop o no", name);
        }
        return null;
    }
    public Animation loadAnimatableTexture(String name, FileHandle f, boolean loop){
        return loadAnimatableTexture(name, f, loop,
                Integer.parseInt(ini.getGeneral("AnimationFramerate")));
    }

    public HashMap<String, Animation> loadAnimatableTextures(String name, String[] poss,
                                                             FileHandle f, boolean loop){
        return loadAnimatableTextures(name, poss, f, loop,
                Integer.parseInt(ini.getGeneral("AnimationFramerate")));
    }
    public HashMap<String, Animation> loadAnimatableTextures(String name, String[] poss,
                                                             FileHandle f, boolean loop,
                                                             int framerate){
        HashMap<String, Animation> h = new HashMap<String, Animation>();
        for(String i : poss) {
            if(f.child(name+i+".png").exists()) {
                try {
                    h.put(i, new Animation(new Texture(f.child(name + i + ".png"))));
                }catch (Exception e){};
            }else if(f.child(name+i +"-0.png").exists()){
                ArrayList<Texture> arr = new ArrayList<Texture>();
                for(int j = 0; f.child(name+i+"-"+j+".png").exists(); j++) {
                    try{
                        arr.add(new Texture(f.child(name+i+"-"+j+".png")));
                    }catch (Exception e){}
                }
                h.put(i, new Animation(arr, framerate, loop));
            }else if(!f.equals(Gdx.files.internal("mania"))){
                return loadAnimatableTextures(name, poss, Gdx.files.internal("mania"), loop);
            }else{
                Gdx.app.log("infinite loop o no", name+i+".png");
            }
        }
        return h;
    }

    public void renderGame(Batch b, GameScreen game){
        int w = Gdx.app.getGraphics().getWidth();
        int h = Gdx.app.getGraphics().getHeight();
        Map map = game.map;
        int keys = map.getKeys();
        int t = game.game.time();
        hitLoc = (int)((h-Integer.parseInt(ini.getMania(keys, "HitPosition"))*h/480)* screenScale);
        for(int i = 0; i < keys; i++){
            for(int j = 0; j<map.getUnHit().get(i).size()
                    &&hitLoc+(map.getUnHit().get(i).get(j).time() - t)*game.speed<h; j++){

                if(map.getUnHit().get(i).get(j).endTime() > 0){
                    if(map.getUnHit().get(i).get(j).isSlbreak()
                            ||map.getUnHit().get(i).get(j).getFirstHit()==0){
                        b.setColor(1,1,1,0.4f);
                    }
                    getNoteType(i, keys, "L").draw(b,
                            i*w/keys,
                            hitLoc+(map.getUnHit().get(i).get(j).time()-t)*game.speed
                                    +82*(w/keys)/256,
                            w/keys,
                            map.getUnHit().get(i).get(j).len()*game.speed);
                    (getNoteType(i, keys, "T")!=null?
                            getNoteType(i, keys, "T"):
                            getNoteType(i, keys, "H")).draw(b,
                            i*w/keys,
                            hitLoc+(map.getUnHit().get(i).get(j).endTime()-t)*game.speed,
                            w/keys,
                            82*(w/keys)/256,
                            false, true);
                    getNoteType(i, keys, "H").draw(b,
                            i * w / keys,
                            hitLoc + (map.getUnHit().get(i).get(j).time() - t) * game.speed,
                            w / keys,
                            82 * (w / keys) / 256);
                    if(map.getUnHit().get(i).get(j).isSlbreak()
                            ||map.getUnHit().get(i).get(j).getFirstHit()==0){
                        b.setColor(Color.WHITE);
                    }

                }else {
                    getNoteType(i, keys, "").draw(b,
                            i * w / keys,
                            hitLoc + (map.getUnHit().get(i).get(j).time() - t) * game.speed,
                            w / keys,
                            getNoteType(i, keys, "").getHeight() *
                                    (w / keys) / getNoteType(i, keys, "").getWidth());
                }
            }
            getKeyType(i, keys, game.getLane(i))
                    .draw(b,
                            i*w/keys, 0,
                            w/keys,
                            getKeyType(i, keys, game.getLane(i)).getHeight()*h* screenScale /768);
        }
        int com = map.getScore().getCombo();
        String cs = com+"";
        int gap = -Integer.parseInt(ini.getFonts("ComboOverlap"));
        for(int i = 0; i < cs.length(); i++){
            if (com==0)break;
            b.draw(combo[cs.charAt(i)-48],
                    (w+(2*i-cs.length())*(gap+combo[0].getWidth()*textScale)+gap)/2,
                    (h-h*Integer.parseInt(ini.getMania(keys, "ComboPosition"))/480)* screenScale,
                    combo[0].getWidth()*textScale, combo[0].getHeight()*textScale);
        }
        String ss = map.getScore().getScore()+"";
        gap = -Integer.parseInt(ini.getFonts("ScoreOverlap"));
        for(int i = 0; i < ss.length(); i++){
            b.draw(score[ss.charAt(i)-48], w-(gap+score[0].getWidth()*textScale)*(ss.length()-i),
                    h- score[0].getHeight()*textScale,
                    score[0].getWidth()*textScale, score[0].getHeight()*textScale);
        }
        String as = String.format("%.2f%%", map.getScore().getAcc()*100);
        if(as.length()<5)as="0"+as;
        int index = 0;
        for(int i = 0; i < as.length(); i++){
            Texture tex;
            switch (as.charAt(i)){
                case '.':tex=dot;
                    break;
                case '%':tex=percent;
                    break;
                default:tex=combo[as.charAt(i)-48];
                    break;
            }
            index+=tex.getWidth()*textScale+gap;
        }
        for(int i = 0; i < as.length(); i++){
            Texture tex;
            switch (as.charAt(i)){
                case '.':tex=dot;
                    break;
                case '%':tex=percent;
                    break;
                default:tex=combo[as.charAt(i)-48];
                    break;
            }
            index-=tex.getWidth()*textScale+gap;
            b.draw(tex, w-tex.getWidth()*textScale-index, h-tex.getHeight()*textScale*2,
                    tex.getWidth()*textScale, tex.getHeight()*textScale);

        }
        anim.draw(b, w/2, (h-h*Integer.parseInt(ini.getMania(keys, "ScorePosition"))/480)* screenScale, 2);
    }
    public static FileHandle fileIgnoreCase(FileHandle parent, final String str){
        return parent.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.equalsIgnoreCase(str);
            }
        })[0];
    }
    public void renderScore(Batch b, Score scor){
        int w = Gdx.app.getGraphics().getWidth();
        int h = Gdx.app.getGraphics().getHeight();


        String r;
        switch((int)(scor.getAcc()*20)){
            case 20:r = "X";
                break;
            case 19:r = "S";
                break;
            case 18:r = "A";
                break;
            case 17:case 16: r = "B";
                break;
            case 15:case 14: r = "C";
                break;
            default: r = "D";
                break;
        }
        rank.get(r).draw(b, (w-2*rank.get(r).getWidth())/2, h-2*rank.get(r).getHeight(),
                2*rank.get(r).getWidth(),
                2*rank.get(r).getHeight());

        String ss = scor.getScore()+"";
        int gap = -Integer.parseInt(ini.getFonts("ScoreOverlap"));
        for(int i = 0; i < ss.length(); i++){
            b.draw(score[ss.charAt(i)-48], (gap+combo[0].getWidth()*textScale)*(ss.length()-i),
                    h-2*rank.get(r).getHeight()-score[0].getHeight()*textScale,
                    score[0].getWidth()*textScale, score[0].getHeight()*textScale);
        }
        String as = String.format("%.2f%%", scor.getAcc()*100);
        if(as.length()<5)as="0"+as;
        int index = 0;
        for(int i = 0; i < as.length(); i++){
            Texture tex;
            switch (as.charAt(i)){
                case '.':tex=dot;
                    break;
                case '%':tex=percent;
                    break;
                default:tex=combo[as.charAt(i)-48];
                    break;
            }
            b.draw(tex, w-tex.getWidth()*textScale-index,
                    h-2*rank.get(r).getHeight()-tex.getHeight()*textScale*2,
                    tex.getWidth()*textScale, tex.getHeight()*textScale);
            index+=tex.getWidth()*textScale+gap;
        }
    }

    public Animation getKeyType(int pos, int keys, boolean down){
        int pos1 = Math.min(pos, keys-pos-1);
        if(down){
            if (keys % 2 == 1 && pos1 * 2 == keys - 1) return key.get("SD");
            if (pos1 % 2 == 0) return key.get("1D");
            return key.get("2D");
        }else {
            if (keys % 2 == 1 && pos1 * 2 == keys - 1) return key.get("S");
            if (pos1 % 2 == 0) return key.get("1");
            return key.get("2");
        }
    }
    public Animation getNoteType(int pos, int keys, String hl){
        int pos1 = Math.min(pos, keys-pos-1);
        if (keys % 2 == 1 && pos1 * 2 == keys - 1) return note.get("S"+hl);
        if (pos1 % 2 == 0) return note.get("1"+hl);
        return note.get("2"+hl);
    }
    public void dispose(){
        //TODO
    }
}
