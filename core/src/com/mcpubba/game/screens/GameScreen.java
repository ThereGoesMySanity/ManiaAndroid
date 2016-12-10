package com.mcpubba.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mcpubba.game.ManiaAndroid;
import com.mcpubba.game.game.Map;

import java.util.HashMap;

/**
 * Created by Will on 2016-12-06.
 */

public class GameScreen implements Screen, InputProcessor {
    public final ManiaAndroid game;

    public Map map;
    public float speed = 3;
    HashMap<Integer, Integer> keysDown;

    public int offset = 75;
    public boolean ready;

    public GameScreen(final ManiaAndroid mania, Map m){
        game = mania;
        keysDown = new HashMap<Integer, Integer>();
        ready = false;


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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(!ready)return;
        map.update();
        game.getBatch().begin();
        game.skin.renderGame(game.getBatch(), this);
        game.getBatch().end();
        if(Gdx.gl.glGetError()!=0)Gdx.app.log("oglError", ""+Gdx.gl.glGetError());
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
        if(!ready)return;
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
