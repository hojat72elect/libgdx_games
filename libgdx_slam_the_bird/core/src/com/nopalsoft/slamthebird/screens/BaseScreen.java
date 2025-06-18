package com.nopalsoft.slamthebird.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.SlamTheBirdGame;
import com.nopalsoft.slamthebird.game.GameScreen;
import com.nopalsoft.slamthebird.shop.ShopScreen;

public abstract class BaseScreen extends InputAdapter implements Screen {
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 800;

    public static final float WORLD_SCREEN_WIDTH = 4.8f;
    public static final float WORLD_SCREEN_HEIGHT = 8;

    public SlamTheBirdGame game;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public Stage stage;

    public BaseScreen(SlamTheBirdGame game) {
        this.stage = game.stage;
        this.stage.clear();
        this.batch = game.batch;
        this.game = game;

        camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

        InputMultiplexer input = new InputMultiplexer(this, stage);
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {
        if (delta > .1f)
            delta = .1f;

        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw(delta);

        stage.act(delta);
        stage.draw();
    }

    public void drawLargeNumberCenteredX(float x, float y, int score) {
        String scoreText = String.valueOf(score);

        int length = scoreText.length();
        float charWidth = 42;
        float textWidth = length * charWidth;
        for (int i = 0; i < length; i++) {
            AtlasRegion keyFrame;

            char character = scoreText.charAt(i);

            if (character == '0') {
                keyFrame = Assets.largeNum0;
            } else if (character == '1') {
                keyFrame = Assets.largeNum1;
            } else if (character == '2') {
                keyFrame = Assets.largeNum2;
            } else if (character == '3') {
                keyFrame = Assets.largeNum3;
            } else if (character == '4') {
                keyFrame = Assets.largeNum4;
            } else if (character == '5') {
                keyFrame = Assets.largeNum5;
            } else if (character == '6') {
                keyFrame = Assets.largeNum6;
            } else if (character == '7') {
                keyFrame = Assets.largeNum7;
            } else if (character == '8') {
                keyFrame = Assets.largeNum8;
            } else {
                keyFrame = Assets.largeNum9;
            }

            batch.draw(keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f,
                    y, charWidth, 64);
        }
    }

    public void drawSmallScoreRightAligned(float x, float y,
                                           int score) {
        String scoreText = String.valueOf(score);

        int len = scoreText.length();
        float charWidth;
        float textWidth = 0;
        for (int i = len - 1; i >= 0; i--) {
            AtlasRegion keyFrame;

            charWidth = 22;
            char character = scoreText.charAt(i);

            if (character == '0') {
                keyFrame = Assets.smallNum0;
            } else if (character == '1') {
                keyFrame = Assets.smallNum1;
                charWidth = 11f;
            } else if (character == '2') {
                keyFrame = Assets.smallNum2;
            } else if (character == '3') {
                keyFrame = Assets.smallNum3;
            } else if (character == '4') {
                keyFrame = Assets.smallNum4;
            } else if (character == '5') {
                keyFrame = Assets.smallNum5;
            } else if (character == '6') {
                keyFrame = Assets.smallNum6;
            } else if (character == '7') {
                keyFrame = Assets.smallNum7;
            } else if (character == '8') {
                keyFrame = Assets.smallNum8;
            } else {
                keyFrame = Assets.smallNum9;
            }
            textWidth += charWidth;
            batch.draw(keyFrame, x - textWidth, y, charWidth, 32);
        }
    }

    public void drawNumChicoCentradoX(float x, float y, int puntuacion) {
        String score = String.valueOf(puntuacion);

        int len = score.length();
        float charWidth = 22;
        float textWidth = len * charWidth;
        for (int i = 0; i < len; i++) {
            AtlasRegion keyFrame;

            char character = score.charAt(i);

            if (character == '0') {
                keyFrame = Assets.smallNum0;
            } else if (character == '1') {
                keyFrame = Assets.smallNum1;
            } else if (character == '2') {
                keyFrame = Assets.smallNum2;
            } else if (character == '3') {
                keyFrame = Assets.smallNum3;
            } else if (character == '4') {
                keyFrame = Assets.smallNum4;
            } else if (character == '5') {
                keyFrame = Assets.smallNum5;
            } else if (character == '6') {
                keyFrame = Assets.smallNum6;
            } else if (character == '7') {
                keyFrame = Assets.smallNum7;
            } else if (character == '8') {
                keyFrame = Assets.smallNum8;
            } else {
                keyFrame = Assets.smallNum9;
            }

            batch.draw(keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f,
                    y, charWidth, 32);
        }
    }

    Image blackFadeOut;

    public void changeScreenWithFadeOut(final Class<?> newScreen,
                                        final SlamTheBirdGame game) {
        blackFadeOut = new Image(Assets.blackPixel);
        blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        blackFadeOut.getColor().a = 0;
        blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f),
                Actions.run(() -> {
                    if (newScreen == GameScreen.class)
                        game.setScreen(new GameScreen(game));
                    else if (newScreen == ShopScreen.class)
                        game.setScreen(new ShopScreen(game));
                })));
        stage.addActor(blackFadeOut);
    }

    public void addPressEffect(final Actor actor) {
        actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() - 5);
                event.stop();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
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

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        Settings.save();
    }
}
