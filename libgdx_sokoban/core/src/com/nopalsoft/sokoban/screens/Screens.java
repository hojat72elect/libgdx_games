package com.nopalsoft.sokoban.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.SokobanGame;
import com.nopalsoft.sokoban.Settings;
import com.nopalsoft.sokoban.game.GameScreen;

public abstract class Screens extends InputAdapter implements Screen, GestureListener {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    public SokobanGame game;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public Stage stage;

    public Screens(final SokobanGame game) {
        this.stage = game.stage;
        this.stage.clear();
        this.batch = game.batch;
        this.game = game;

        camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

        GestureDetector gestureDetector = new GestureDetector(20, .5f, 2, .15f, this);

        InputMultiplexer input = new InputMultiplexer(this, gestureDetector, stage);
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        draw(delta);

        stage.act(delta);
        stage.draw();
    }

    Image blackFadeOut;

    public void changeScreenWithFadeOut(final Class<?> newScreen, final int level, final SokobanGame game) {
        blackFadeOut = new Image(Assets.blackPixelDrawable);
        blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        blackFadeOut.getColor().a = 0;
        blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run(() -> {
            if (newScreen == GameScreen.class) {
                Assets.loadTiledMap(level);
                game.setScreen(new GameScreen(game, level));
            } else if (newScreen == MainMenuScreen.class)
                game.setScreen(new MainMenuScreen(game));
        })));
        stage.addActor(blackFadeOut);
    }

    public void changeScreenWithFadeOut(final Class<?> newScreen, final SokobanGame game) {
        changeScreenWithFadeOut(newScreen, -1, game);
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
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > 0) {
                right();
            } else {
                left();
            }
        } else {
            if (velocityY > 0) {
                down();
            } else {
                up();
            }
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    public void up() {
        Gdx.app.log("UP", "");
    }

    public void down() {
        Gdx.app.log("DOWN", "");
    }

    public void left() {
        Gdx.app.log("LEFT", "");
    }

    public void right() {
        Gdx.app.log("RIGHT", "");
    }

    @Override
    public void pinchStop() {
    }
}