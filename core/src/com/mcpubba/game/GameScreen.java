package com.mcpubba.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mcpubba.game.game.Map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Will on 2016-12-06.
 */

public class GameScreen implements Screen, InputProcessor {
    public final ManiaAndroid game;
    Texture mania1, mania2, maniaS;
    Texture[] combo;
    Map map;
    float speed = 5;
    HashMap<Integer, Integer> keys;
    public GameScreen(final ManiaAndroid mania, Map m){
        game = mania;
        mania1 = new Texture("mania-note1.png");
        mania2 = new Texture("mania-note2.png");
        maniaS = new Texture("mania-noteS.png");
        combo = new Texture[10];
        for(int i = 0; i < 10; i++){
            combo[i] = new Texture("combo-"+i+".png");
        }
        keys = new HashMap<Integer, Integer>();
        Gdx.input.setInputProcessor(this);
        map = m;
        m.load();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        int w = Gdx.app.getGraphics().getWidth();
        int h = Gdx.app.getGraphics().getHeight();
        int t = map.time();
        map.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.shapes.begin(ShapeRenderer.ShapeType.Filled);
        int keys = map.getKeys();
        game.shapes.setColor(Color.DARK_GRAY);

        for(int i = 1; i < keys; i++){
            game.shapes.rect(i*w/keys, 0, 5, h);
        }

        if(t<0){
            game.batch.end();
            return;
        }
        for(int i = 0; i < keys; i++){
            for(int j = 0; j<map.getUnHit().get(i).size()
                    &&(map.getUnHit().get(i).get(j).time()-t)<h*5/6/speed; j++){

                if(map.getUnHit().get(i).get(j).endTime() > 0){

                    game.shapes.rect(
                            i*w/keys+w/keys/6,
                            h/6+(map.getUnHit().get(i).get(j).time()-t)*speed,
                            w/keys*2/3,
                            map.getUnHit().get(i).get(j).len()*speed);
                    game.batch.draw(getNoteType(i, keys),
                            i*w/keys,
                            h/6+(map.getUnHit().get(i).get(j).endTime()-t)*speed,
                            w/keys,
                            82*(w/keys)/256);

                }
                game.batch.draw(getNoteType(i, keys),
                        i*w/keys,
                        h/6+(map.getUnHit().get(i).get(j).time()-t)*speed,
                        w/keys,
                        82*(w/keys)/256);
            }
        }
        int com = map.getScore().getCombo();
        String cs = com+"";
        for(int i = cs.length()-1; i >= 0; i--){
            game.batch.draw(combo[com%10], (2*w-(cs.length()-2*i)*66)/4, h*2/3, 66, 92);
        }
        game.shapes.end();
        game.shapes.begin(ShapeRenderer.ShapeType.Filled);
        game.shapes.rect(0, 0, w, h/6);
        if(Gdx.graphics.getFramesPerSecond()<59)
            Gdx.app.log("test", ""+Gdx.graphics.getFramesPerSecond());
        game.batch.end();
        game.shapes.end();
    }

    public Texture getNoteType(int pos, int keys){
        int pos1 = Math.min(pos, keys-pos-1);
        if(keys%2==1&&pos1*2==keys-1)return maniaS;
        if(pos1%2==0)return mania1;
        return mania2;
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

    @Override
    public void dispose() {
        mania1.dispose();
        mania2.dispose();
        maniaS.dispose();
        for(int i = 0; i < 10; i++){

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
        keys.put(lane, pointer);

        if (pointer==null) map.releaseNote(lane);
        else map.hitNote(lane);
    }
    public boolean getLane(int lane){
        return keys.get(lane)!=null;
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
