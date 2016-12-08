package com.mcpubba.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mcpubba.game.HitAnim;
import com.mcpubba.game.ManiaAndroid;
import com.mcpubba.game.game.Map;

import java.util.HashMap;

/**
 * Created by Will on 2016-12-06.
 */

public class GameScreen implements Screen, InputProcessor {
    public final ManiaAndroid game;
    HashMap<String, Texture> note;
    HashMap<String, Texture> key;

    Texture[] combo;
    public HitAnim anim;
    Map map;
    float speed = 3;
    HashMap<Integer, Integer> keysDown;
    int hitLoc;
    public int offset = 75;
    public GameScreen(final ManiaAndroid mania, Map m){
        game = mania;
        keysDown = new HashMap<Integer, Integer>();
        HashMap<Integer, Texture> hit = new HashMap<Integer, Texture>();
        note = new HashMap<String, Texture>();
        key = new HashMap<String, Texture>();

        note.put("1", new Texture("mania/mania-note1.png"));
        note.put("2", new Texture("mania/mania-note2.png"));
        note.put("S", new Texture("mania/mania-noteS.png"));
        note.put("1H", new Texture("mania/mania-note1H.png"));
        note.put("2H", new Texture("mania/mania-note2H.png"));
        note.put("SH", new Texture("mania/mania-noteSH.png"));
        note.put("1L", new Texture("mania/mania-note1L.png"));
        note.put("2L", new Texture("mania/mania-note2L.png"));
        note.put("SL", new Texture("mania/mania-noteSL.png"));
        
        key.put("1", new Texture("mania/mania-key1.png"));
        key.put("2", new Texture("mania/mania-key2.png"));
        key.put("S", new Texture("mania/mania-keyS.png"));
        key.put("1D", new Texture("mania/mania-key1D.png"));
        key.put("2D", new Texture("mania/mania-key2D.png"));
        key.put("SD", new Texture("mania/mania-keySD.png"));
        
        combo = new Texture[10];
        for(int i = 0; i < 10; i++){
            combo[i] = new Texture("mania/combo-"+i+".png");
        }
        hit.put(0, new Texture("mania/mania-hit0.png"));
        hit.put(50, new Texture("mania/mania-hit50.png"));
        hit.put(100, new Texture("mania/mania-hit100.png"));
        hit.put(200, new Texture("mania/mania-hit200.png"));
        hit.put(300, new Texture("mania/mania-hit300.png"));
        hit.put(320, new Texture("mania/mania-hit300g.png"));

        anim = new HitAnim(hit);

        Gdx.input.setInputProcessor(this);
        map = m;
        m.load(this);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        int w = Gdx.app.getGraphics().getWidth();
        int h = Gdx.app.getGraphics().getHeight();
        int t = map.time();
        int keys = map.getKeys();
        if(hitLoc==0)hitLoc=h/6;
        map.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        if(t<0){
            game.getBatch().end();
            return;
        }
        for(int i = 0; i < keys; i++){
            for(int j = 0; j<map.getUnHit().get(i).size()
                    &&(map.getUnHit().get(i).get(j).time()-t)<(h-hitLoc)/speed; j++){

                if(map.getUnHit().get(i).get(j).endTime() > 0){
                    if(map.getUnHit().get(i).get(j).isSlbreak()
                            ||map.getUnHit().get(i).get(j).getFirstHit()==0){
                        game.getBatch().setColor(0,0,0,0.4f);
                    }
                    game.getBatch().draw(getNoteType(i, keys, "L"),
                            i*w/keys,
                            hitLoc+(map.getUnHit().get(i).get(j).time()-t)*speed
                                +82*(w/keys)/256,
                            w/keys,
                            map.getUnHit().get(i).get(j).len()*speed);
                    game.getBatch().draw(getNoteType(i, keys, "H"),
                            i*w/keys,
                            hitLoc+(map.getUnHit().get(i).get(j).endTime()-t)*speed,
                            w/keys,
                            82*(w/keys)/256,
                            0, 0,
                            getNoteType(i, keys, "H").getWidth(),
                            getNoteType(i, keys, "H").getHeight(),
                            false, true);
                    game.getBatch().draw(getNoteType(i, keys, "H"),
                            i * w / keys,
                            hitLoc + (map.getUnHit().get(i).get(j).time() - t) * speed,
                            w / keys,
                            82 * (w / keys) / 256);
                    if(map.getUnHit().get(i).get(j).isSlbreak()
                            ||map.getUnHit().get(i).get(j).getFirstHit()==0){
                        game.getBatch().setColor(0,0,0,0);
                    }

                }else {
                    game.getBatch().draw(getNoteType(i, keys, ""),
                            i * w / keys,
                            hitLoc + (map.getUnHit().get(i).get(j).time() - t) * speed,
                            w / keys,
                            82 * (w / keys) / 256);
                }
            }
            game.getBatch().draw(getKeyType(i, keys, getLane(i)),
                    i*w/keys, 0,
                    w/keys, hitLoc*2);
        }

        int com = map.getScore().getCombo();
        String cs = com+"";
        for(int i = cs.length()-1; i >= 0; i--){
            if (com==0)break;
            game.getBatch().draw(combo[com%10], (2*w-(cs.length()-2*i)*66)/4, h*2/3, 66, 92);
            com /=10;
        }
        anim.draw(game.getBatch(), w/2, h/2, 2);
        
        game.getBatch().end();

    }

    public Texture getNoteType(int pos, int keys, String hl){
        int pos1 = Math.min(pos, keys-pos-1);
        if(hl.equals("")) {
            if (keys % 2 == 1 && pos1 * 2 == keys - 1) return note.get("S");
            if (pos1 % 2 == 0) return note.get("1");
            return note.get("2");
        }
        if(hl.equals("H")){
            if (keys % 2 == 1 && pos1 * 2 == keys - 1) return note.get("SH");
            if (pos1 % 2 == 0) return note.get("1H");
            return note.get("2H");
        }
        if(hl.equals("L")){
            if (keys % 2 == 1 && pos1 * 2 == keys - 1) return note.get("SL");
            if (pos1 % 2 == 0) return note.get("1L");
            return note.get("2L");
        }
        return null;
    }
    public Texture getKeyType(int pos, int keys, boolean down){
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

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
    public void finish(){

    }

    @Override
    public void dispose() {
        for(Texture t : note.values()){
            t.dispose();
        }
        for(Texture t : key.values()){
            t.dispose();
        }
        for(Texture t : anim.getHits().values()){
            t.dispose();
        }
        for(int i = 0; i < 10; i++){
            combo[i].dispose();
        }
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    public void setLane(int lane, Integer pointer){
        keysDown.put(lane, pointer);

        if (pointer==null) map.releaseNote(lane);
        else map.hitNote(lane);
    }
    public boolean getLane(int lane){
        return keysDown.get(lane)!=null;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int w = Gdx.app.getGraphics().getWidth();
        setLane(screenX*map.getKeys()/w, pointer);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int w = Gdx.app.getGraphics().getWidth();
        setLane(screenX*map.getKeys()/w, null);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        int w = Gdx.app.getGraphics().getWidth();
        int prevLane = (screenX-Gdx.input.getDeltaX(pointer))*map.getKeys()/w;
        int currLane = screenX*map.getKeys()/w;
        if(prevLane==currLane)return true;
        setLane(prevLane, null);
        setLane(currLane, pointer);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
