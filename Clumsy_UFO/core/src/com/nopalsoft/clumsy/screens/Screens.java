package com.nopalsoft.clumsy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nopalsoft.clumsy.Assets;
import com.nopalsoft.clumsy.ClumsyUfoGame;
import com.nopalsoft.clumsy.Settings;
import com.nopalsoft.clumsy.game.arcade.GameScreenArcade;
import com.nopalsoft.clumsy.game.classic.ClassicGameScreen;

import java.util.Random;

public abstract class Screens extends InputAdapter implements Screen {
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 800;

    public static final int WORLD_SCREEN_WIDTH = 4;
    public static final int WORLD_SCREEN_HEIGHT = 8;

    public ClumsyUfoGame game;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public Stage stage;

    Random random;
    Image blackFadeOut;

    public Screens(ClumsyUfoGame game) {
        this.stage = game.stage;
        this.stage.clear();
        this.batch = game.batch;
        this.game = game;

        camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

        InputMultiplexer input = new InputMultiplexer(this, stage);
        Gdx.input.setInputProcessor(input);

        random = new Random();
        int ale = random.nextInt(3);

        if (ale == 0)
            Assets.background0 = Assets.background1;
        else if (ale == 1)
            Assets.background0 = Assets.background2;
        else {
            Assets.background0 = Assets.background3;
        }
    }

    @Override
    public void render(float delta) {
        if (delta > .1f)
            delta = .1f;

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw(delta);

        stage.act(delta);
        stage.draw();
    }

    public void changeScreenWithFadeOut(final Class<?> newScreen,
                                        final ClumsyUfoGame game) {
        blackFadeOut = new Image(Assets.blackDrawable);
        blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        blackFadeOut.getColor().a = 0;
        blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (newScreen == ClassicGameScreen.class)
                            game.setScreen(new ClassicGameScreen(game));
                        else if (newScreen == MainMenuScreen.class)
                            game.setScreen(new MainMenuScreen(game));
                        else if (newScreen == GameScreenArcade.class)
                            game.setScreen(new GameScreenArcade(game));
                    }
                })));
        stage.addActor(blackFadeOut);
    }

    public void drawScore(float x, float y, int scoreNumericalValue) {
        String score = String.valueOf(scoreNumericalValue);

        int len = score.length();
        float charWidth = 42;
        float textWidth = len * charWidth;
        for (int i = 0; i < len; i++) {
            AtlasRegion keyFrame;

            char character = score.charAt(i);

            if (character == '0') {
                keyFrame = Assets.num0Large;
            } else if (character == '1') {
                keyFrame = Assets.num1Large;
            } else if (character == '2') {
                keyFrame = Assets.num2Large;
            } else if (character == '3') {
                keyFrame = Assets.num3Large;
            } else if (character == '4') {
                keyFrame = Assets.num4Large;
            } else if (character == '5') {
                keyFrame = Assets.num5Large;
            } else if (character == '6') {
                keyFrame = Assets.num6Large;
            } else if (character == '7') {
                keyFrame = Assets.num7Large;
            } else if (character == '8') {
                keyFrame = Assets.num8Large;
            } else {// 9
                keyFrame = Assets.num9Large;
            }

            batch.draw(keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f,
                    y, charWidth, 64);
        }
    }

    public void drawScoreCentered(float x, float y, int scoreNumericalValue) {
        String score = String.valueOf(scoreNumericalValue);

        int len = score.length();
        float charWidth = 42;
        float textWidth = 0;
        for (int i = 0; i < len; i++) {
            AtlasRegion keyFrame;

            char character = score.charAt(i);

            if (character == '0') {
                keyFrame = Assets.num0Large;
            } else if (character == '1') {
                keyFrame = Assets.num1Large;
            } else if (character == '2') {
                keyFrame = Assets.num2Large;
            } else if (character == '3') {
                keyFrame = Assets.num3Large;
            } else if (character == '4') {
                keyFrame = Assets.num4Large;
            } else if (character == '5') {
                keyFrame = Assets.num5Large;
            } else if (character == '6') {
                keyFrame = Assets.num6Large;
            } else if (character == '7') {
                keyFrame = Assets.num7Large;
            } else if (character == '8') {
                keyFrame = Assets.num8Large;
            } else {
                keyFrame = Assets.num9Large;
            }

            batch.draw(keyFrame, x + textWidth, y, charWidth, 64);
            textWidth += charWidth;
        }
    }

    public void drawSmallScoreRightAligned(float x, float y,
                                           int scoreNumericalValue) {
        String score = String.valueOf(scoreNumericalValue);

        int len = score.length();
        float charWidth;
        float textWidth = 0;
        for (int i = len - 1; i >= 0; i--) {
            AtlasRegion keyFrame;

            charWidth = 22;
            char character = score.charAt(i);

            if (character == '0') {
                keyFrame = Assets.num0Small;
            } else if (character == '1') {
                keyFrame = Assets.num1Small;
                charWidth = 11f;
            } else if (character == '2') {
                keyFrame = Assets.num2Small;
            } else if (character == '3') {
                keyFrame = Assets.num3Small;
            } else if (character == '4') {
                keyFrame = Assets.num4Small;
            } else if (character == '5') {
                keyFrame = Assets.num5Small;
            } else if (character == '6') {
                keyFrame = Assets.num6Small;
            } else if (character == '7') {
                keyFrame = Assets.num7Small;
            } else if (character == '8') {
                keyFrame = Assets.num8Small;
            } else {
                keyFrame = Assets.num9Small;
            }
            textWidth += charWidth;
            batch.draw(keyFrame, x - textWidth, y, charWidth, 32);
        }
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
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }
}
