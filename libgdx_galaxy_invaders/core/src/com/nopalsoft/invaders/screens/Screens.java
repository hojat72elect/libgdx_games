package com.nopalsoft.invaders.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.MainInvaders;
import com.nopalsoft.invaders.Settings;
import com.nopalsoft.invaders.game.GameScreen;

public abstract class Screens extends InputAdapter implements Screen {
    public static final int SCREEN_WIDTH = 320;
    public static final int SCREEN_HEIGHT = 480;

    public static final int WORLD_SCREEN_WIDTH = 32;
    public static final int WORLD_SCREEN_HEIGHT = 48;

    public MainInvaders game;

    public OrthographicCamera oCam;
    public SpriteBatch batcher;
    public Stage stage;
    public Assets oAssets;

    public Screens(MainInvaders game) {
        stage = game.stage;
        stage.clear();
        this.game = game;
        oAssets = game.oAssets;

        oCam = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);
        batcher = game.spriteBatch;

        InputMultiplexer input = new InputMultiplexer(this, stage);
        Gdx.input.setInputProcessor(input);

        Assets.font10.getData().setScale(.65f);
        Assets.font15.getData().setScale(1f);
        Assets.font45.getData().setScale(.85f);
        Assets.font60.getData().setScale(1.2f);
        if (this instanceof MainMenuScreen) {
            Assets.font10.getData().setScale(.65f);
            Assets.font15.getData().setScale(1f);
            Assets.font45.getData().setScale(.85f);
            Assets.font60.getData().setScale(1.2f);
        } else if (this instanceof GameScreen) {
            Assets.font15.getData().setScale(1);
            Assets.font45.getData().setScale(.7f);
            Assets.font10.getData().setScale(.65f);
        } else if (this instanceof SettingsScreen) {
            Assets.font10.getData().setScale(1);
            Assets.font15.getData().setScale(1f);
            Assets.font45.getData().setScale(.65f);
            Assets.font60.getData().setScale(1);
        }

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw(delta);

        stage.act(delta);
        stage.draw();
    }

    public abstract void draw(float delta);

    public abstract void update(float delta);

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        Settings.save();
    }

    @Override
    public void pause() {
        Assets.music.pause();
    }

    @Override
    public void resume() {
        if (Settings.musicEnabled && !Assets.music.isPlaying())
            Assets.music.play();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batcher.dispose();
    }

}
