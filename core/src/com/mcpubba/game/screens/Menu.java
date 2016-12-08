package com.mcpubba.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.mcpubba.game.ManiaAndroid;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The main menu. I'm probably going to overhaul this because it's really bad lol
 */

public class Menu implements Screen, InputProcessor{
    final ManiaAndroid game;
    private ArrayList<String> items;
    private int v;
    private int x;

    public Menu(final ManiaAndroid game){
        this.game = game;
        Gdx.input.setInputProcessor(this);
        items = game.getMapNames();

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        x+=v--;
        if(v<0)v=0;
        Gdx.gl.glClearColor(0.1f,0.1f,0.1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        game.font.getData().setScale(2);
        for(int i = 0; i < items.size(); i++){
            game.font.draw(game.getBatch(),
                    items.get(i),
                    0, Gdx.graphics.getHeight()+x-i*30
            );
        }
        game.getBatch().end();
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (Gdx.input.getDeltaY(pointer)<2&&
                (x+screenY)/30>0&&
                (x+screenY)/30<items.size()){
            Gdx.app.log("test", screenY+"");
            game.setScreen(new GameScreen(game,
                    game.getMapByName(items.get((Gdx.graphics.getHeight()+x-screenY)/30))));
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        v = -(int)Math.signum(Gdx.input.getDeltaY(pointer))
                *Math.max(Math.abs(Gdx.input.getDeltaY(pointer)), Math.abs(v));

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
