package com.nopalsoft.lander.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.nopalsoft.lander.MainLander;
import com.nopalsoft.lander.Settings;

public abstract class Screens extends InputAdapter implements Screen {
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 800;

    public static final float WORLD_SCREEN_WIDTH = 4.8f;
    public static final float WORLD_SCREEN_HEIGHT = 8;

    public MainLander game;
    // public IdiomasManager idiomas;

    public OrthographicCamera oCam;
    public SpriteBatch batcher;
    public Stage stage;
    // public IdiomasManager idiomas;

    // protected SkeletonRenderer skelrender;

    protected float ScreenlastStatetime;
    protected float ScreenStateTime;

    public Screens(MainLander game) {
        this.stage = game.stage;
        this.stage.clear();
        // this.idiomas = game.idiomas;
        this.batcher = game.batcher;
        this.game = game;

        oCam = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

        // skelrender = new SkeletonRenderer();

        InputMultiplexer input = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(input);

        ScreenlastStatetime = ScreenStateTime = 0;

        // if (this instanceof MainMenuScreen) {
        // Assets.fontMediano.setScale(1.2f);
        // Assets.fontChico.setScale(.95f);
        // }
        // else if (this instanceof SettingScreen) {
        // Assets.fontGrande.setScale(.65f);
        //
        // }
        // else if (this instanceof ShopScreen) {
        // Assets.fontGrande.setScale(.65f);
        // Assets.fontChico.setScale(.85f);
        //
        // }
        // if (this instanceof GameScreen) {
        // Assets.fontNums.setScale(.8f);
        // }
    }

    @Override
    public void render(float delta) {
        if (delta > .1f)
            delta = .1f;

        ScreenlastStatetime = ScreenStateTime;
        ScreenStateTime += delta;
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw(delta);

        stage.act(delta);
        stage.draw();

//        stage.setDebugAll(true);
    }

    public abstract void draw(float delta);

    public abstract void update(float delta);

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(SCREEN_WIDTH, SCREEN_HEIGHT, true);
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
        // Assets.music.pause();
    }

    @Override
    public void resume() {
        //
        // if (Settings.musicEnabled)
        // Assets.music.play();

    }

    @Override
    public void dispose() {

        batcher.dispose();
    }

}
