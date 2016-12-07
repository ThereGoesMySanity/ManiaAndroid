package com.mcpubba.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mcpubba.game.game.Map;

/**
 * Created by Will on 2016-12-06.
 */

public class Menu implements Screen{
    final ManiaAndroid game;
    private Stage stage;
    private List<String> list;
    private Table container;
    public Menu(final ManiaAndroid game){
        this.game = game;
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        Gdx.input.setInputProcessor(stage);
        container = new Table();
        list = new List<String>(skin);
        stage.addActor(container);
        container.setFillParent(true);
        list.setFillParent(true);
        list.setItems(game.getMapNames().toArray(new String[game.getMapNames().size()]));
        final ScrollPane scrollPane = new ScrollPane(list, skin);
        scrollPane.setFlickScroll(true);
        skin.getFont("default-font").getData().setScale(4f);
        container.add(list).expand().fill();
        list.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent inputEvent, float x, float y){
                game.setScreen(new GameScreen(game, game.getMapByName(list.getSelected())));
            }
        });
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f,0.1f,0.1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
