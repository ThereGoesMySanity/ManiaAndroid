package com.mcpubba.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mcpubba.game.ManiaAndroid;
import com.mcpubba.game.game.Score;

/**
 * Created by Will on 2016-12-07.
 */

public class ScoreScreen implements Screen {
    Score score;
    Stage stage;
    final ManiaAndroid mania;
    public ScoreScreen(final ManiaAndroid m, Score s){
        mania = m;
        score = s;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        mania.getBatch().begin();
        mania.skin.renderScore(mania.getBatch(), score);
        mania.getBatch().end();
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
}
