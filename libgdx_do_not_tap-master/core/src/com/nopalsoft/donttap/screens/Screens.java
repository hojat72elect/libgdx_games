package com.nopalsoft.donttap.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.MainDoNot;
import com.nopalsoft.donttap.Settings;
import com.nopalsoft.donttap.game.GameScreen;

public abstract class Screens extends InputAdapter implements Screen {
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 800;

    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 800 - 80;// El menos 80 es la barra de marcadores

    public MainDoNot game;

    public OrthographicCamera oCam;
    public SpriteBatch batcher;
    public Stage stage;

    Random oRan;

    public Screens(final MainDoNot game) {
        this.stage = game.stage;
        this.stage.clear();
        this.batcher = game.batcher;
        this.game = game;

        oCam = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

        InputMultiplexer input = new InputMultiplexer(this, stage);
        Gdx.input.setInputProcessor(input);

    }

    @Override
    public void render(float delta) {
        if (delta > .1f)
            delta = .1f;

        update(delta);

        // Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);
        draw(delta);

        stage.act(delta);
        stage.draw();

//		stage.setDebugAll(true);
    }

    Image blackFadeOut;

    public void changeScreenWithFadeOut(final Class<?> newScreen,
                                        final MainDoNot game) {
        changeScreenWithFadeOut(newScreen, game, -1);
    }

    public void changeScreenWithFadeOut(final Class<?> newScreen,
                                        final MainDoNot game, final int mode) {
        blackFadeOut = new Image(Assets.pixelNegro);
        blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        blackFadeOut.getColor().a = 0;
        blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (newScreen == SelectScreenScreen.class)
                            game.setScreen(new SelectScreenScreen(game));
                        else if (newScreen == GameScreen.class)
                            game.setScreen(new GameScreen(game, mode));
                        else if (newScreen == MainMenuScreen.class)
                            game.setScreen(new MainMenuScreen(game));
                        // El blackFadeOut se remueve del stage cuando se le da new Screens(game) "Revisar el constructor de la clase Screens" por lo que no hay necesidad de hacer
                        // blackFadeout.remove();
                    }
                })));
        stage.addActor(blackFadeOut);
    }

    public void addEfectoPress(final Actor actor) {
        actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() - 3);
                event.stop();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() + 3);
            }
        });
    }

    protected void addBackGround() {
        Table tabBackground = new Table();
        tabBackground.setFillParent(true);
        tabBackground.defaults().expand().uniform().fill();

        for (int col = 0; col < 4; col++) {

            int tileColorPos = MathUtils.random(3);
            for (int ren = 0; ren < 4; ren++) {
                Image img = new Image(Assets.tileBlanco);

                if (tileColorPos == ren) {

                    int colorTile = MathUtils.random(4);
                    switch (colorTile) {
                        case 0:
                            img = new Image(Assets.tileAmarillo);
                            break;
                        case 1:
                            img = new Image(Assets.tileAzul);
                            break;
                        case 2:
                            img = new Image(Assets.tileRojo);
                            break;
                        case 3:
                            img = new Image(Assets.tileMorado);
                            break;
                        case 4:
                            img = new Image(Assets.tileNaranja);
                            break;
                    }
                }
                tabBackground.add(img);
            }
            tabBackground.row();
        }
        stage.addActor(tabBackground);

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
        // Assets.music.pause();

    }

    @Override
    public void resume() {
        // if (Settings.isMusicOn)
        // Assets.music.play();

    }

    @Override
    public void dispose() {
        stage.dispose();
        batcher.dispose();
    }

}
