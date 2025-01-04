package com.nopalsoft.impossibledial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nopalsoft.impossibledial.Assets;
import com.nopalsoft.impossibledial.MainGame;
import com.nopalsoft.impossibledial.Settings;
import com.nopalsoft.impossibledial.game.GameScreen;

import java.util.Random;

public abstract class Screens extends InputAdapter implements Screen {
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 800;

    public MainGame game;

    public OrthographicCamera oCam;
    public SpriteBatch batcher;
    public Stage stage;

    Random oRan;

    public Screens(final MainGame game) {
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
        update(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);
        draw(delta);

        stage.act(delta);
        stage.draw();

    }

    Image blackFadeOut;

    public void changeScreenWithFadeOut(final Class<?> newScreen, final MainGame game, final int level) {
        blackFadeOut = new Image(Assets.pixelNegro);
        blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        blackFadeOut.getColor().a = 0;
        blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run(new Runnable() {
            @Override
            public void run() {
                if (newScreen == GameScreen.class) {
                    game.setScreen(new GameScreen(game, level));
                } else if (newScreen == MainMenuScreen.class) {
                    game.setScreen(new MainMenuScreen(game));
                }
                // else if (newScreen == HelpScreen.class)
                // game.setScreen(new HelpScreen(game));

                // El blackFadeOut se remueve del stage cuando se le da new Screens(game) "Revisar el constructor de la clase Screens" por lo que no hay necesidad de hacer
                // blackFadeout.remove();
            }
        })));
        stage.addActor(blackFadeOut);
    }

    public void changeScreenWithFadeOut(final Class<?> newScreen, final MainGame game) {
        changeScreenWithFadeOut(newScreen, game, -1);
    }

    public void addEfectoPress(final Actor actor) {
        actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() - 5);
                event.stop();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() + 5);
            }
        });
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
        // Assets.pauseMusic();
    }

    @Override
    public void resume() {
        // Assets.playMusic();
    }

    @Override
    public void dispose() {
        // stage.dispose();
        // batcher.dispose();
    }

}