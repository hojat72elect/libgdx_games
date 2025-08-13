package com.nopalsoft.zombiewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.nopalsoft.zombiewars.Assets;
import com.nopalsoft.zombiewars.MainZombieWars;
import com.nopalsoft.zombiewars.Settings;
import com.nopalsoft.zombiewars.game.GameScreen;
import com.nopalsoft.zombiewars.scene2d.AnimatedSpriteActor;

public abstract class Screens extends InputAdapter implements Screen {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    public static final float WORLD_WIDTH = 8;
    public static final float WORLD_HEIGHT = 4.8f;

    public MainZombieWars game;

    public OrthographicCamera oCam;
    public SpriteBatch batcher;
    public Stage stage;

    protected Music music;
    Image blackFadeOut;

    public Screens(MainZombieWars game) {
        this.stage = game.stage;
        this.stage.clear();
        this.batcher = game.batcher;
        this.game = game;

        oCam = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

        InputMultiplexer input = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {
        if (delta > .1f)
            delta = .1f;

        update(delta);
        stage.act(delta);

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        draw(delta);
        stage.draw();
    }

    public void changeScreenWithFadeOut(final Class<?> newScreen, final MainZombieWars game) {
        blackFadeOut = new Image(Assets.pixelNegro);
        blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        blackFadeOut.getColor().a = 0;
        blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run(() -> {
            if (newScreen == GameScreen.class) {
                Assets.loadTiledMap();
                game.setScreen(new GameScreen(game));
            }
        })));

        Label lbl = new Label("Loading..", Assets.labelStyleGrande);
        lbl.setPosition(SCREEN_WIDTH / 2f - lbl.getWidth() / 2f, SCREEN_HEIGHT / 2f - lbl.getHeight() / 2f);
        lbl.getColor().a = 0;
        lbl.addAction(Actions.fadeIn(.6f));

        AnimatedSpriteActor corriendo = new AnimatedSpriteActor(Assets.zombieKidWalk);
        corriendo.setSize(70, 70);
        corriendo.setPosition(SCREEN_WIDTH / 2f - corriendo.getWidth() / 2f, 250);

        stage.addActor(blackFadeOut);
        stage.addActor(corriendo);
        stage.addActor(lbl);
    }

    public abstract void update(float delta);

    public abstract void draw(float delta);

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
        if (music != null) {
            music.stop();
            music.dispose();
            music = null;
        }

        Settings.save();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batcher.dispose();
    }
}
