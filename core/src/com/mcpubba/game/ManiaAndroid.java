package com.mcpubba.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mcpubba.game.game.Map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class ManiaAndroid extends Game {
	SpriteBatch batch;
    ShapeRenderer shapes;
    BitmapFont font;
    ArrayList<Map> maps;
    HashMap<String, Map> mapNames;
    MusicPlayer music;

    public ManiaAndroid(MusicPlayer music){
        this.music = music;
    }
	@Override
	public void create () {
		batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        font = new BitmapFont();
        mapNames = new HashMap<String, Map>();
        try {
            maps = loadMaps(Gdx.files.external("ManiaAndroid/Songs"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setScreen(new Menu(this));
    }

	@Override
	public void render () {
        super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
        shapes.dispose();
	}

    public ArrayList<Map> loadMaps(FileHandle f) throws FileNotFoundException {
        f.mkdirs();
        ArrayList<Map> maps = new ArrayList<Map>();
        for(FileHandle i : f.list()){
            if(i.isDirectory()){
                for(FileHandle j : i.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        return s.endsWith(".osu");
                    }
                })){
                    maps.add(new Map(j.file(), this));
                }
            }else if(i.name().endsWith(".osz")){
                DecompressFast d = new DecompressFast(
                        i.path(),
                        i.path().substring(0, i.path().length()-4));
                d.unzip();
                for(FileHandle j :
                        new FileHandle(i.path().substring(0, i.path().length()-4))
                                .list(new FilenameFilter() {
                                    @Override
                                    public boolean accept(File file, String s) {
                                        return s.endsWith(".osu");
                                    }
                                })){
                    maps.add(new Map(j.file(), this));
                }
                i.delete();
            }
        }
        return maps;
    }
    public ArrayList<String> getMapNames(){
        ArrayList<String> names = new ArrayList<String>();

        for(Map i : maps){
            names.add(i.getTitle()+ " [" + i.getDiff() + "]");
            mapNames.put(i.getTitle()+ " [" + i.getDiff() + "]", i);
        }
        return names;
    }
    public Map getMapByName(String s){
        return mapNames.get(s);
    }
    public void loadMusic(File f){
        music.loadMusic(f);
    }
    public void play(){
        music.play();
    }
    public int time(){
        return music.getTime();
    }
}
