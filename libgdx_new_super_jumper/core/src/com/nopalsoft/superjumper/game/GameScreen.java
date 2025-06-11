package com.nopalsoft.superjumper.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.Settings;
import com.nopalsoft.superjumper.SuperJumperGame;
import com.nopalsoft.superjumper.scene2d.BaseWindowGameover;
import com.nopalsoft.superjumper.scene2d.BaseWindowPause;
import com.nopalsoft.superjumper.screens.Screens;

public class GameScreen extends Screens {

    static final int STATE_RUNNING = 2;
    static final int STATE_PAUSED = 3;
    static final int STATE_GAME_OVER = 4;
    static int state;

    public WorldGame oWorld;
    WorldGameRender renderer;

    Vector3 touchPositionWorldCoordinates;
    boolean didFire;

    Label labelDistance, labelCoins, labelBullets;

    Button buttonPause;

    BaseWindowPause pauseWindow;

    public GameScreen(SuperJumperGame game) {
        super(game);

        pauseWindow = new BaseWindowPause(this);

        oWorld = new WorldGame();
        renderer = new WorldGameRender(batch, oWorld);
        touchPositionWorldCoordinates = new Vector3();

        state = STATE_RUNNING;
        Settings.numTimesPlayed++;

        Table scoresTable = new Table();
        scoresTable.setSize(SCREEN_WIDTH, 40);
        scoresTable.setY(SCREEN_HEIGHT - scoresTable.getHeight());

        labelCoins = new Label("", Assets.labelStyleLarge);
        labelDistance = new Label("", Assets.labelStyleLarge);
        labelBullets = new Label("", Assets.labelStyleLarge);

        scoresTable.add(new Image(new TextureRegionDrawable(Assets.coin))).left().padLeft(5);
        scoresTable.add(labelCoins).left();

        scoresTable.add(labelDistance).center().expandX();

        scoresTable.add(new Image(new TextureRegionDrawable(Assets.gun))).height(45).width(30).left();
        scoresTable.add(labelBullets).left().padRight(5);

        buttonPause = new Button(Assets.buttonPause);
        buttonPause.setSize(35, 35);
        buttonPause.setPosition(SCREEN_WIDTH - 40, SCREEN_HEIGHT - 80);
        addPressEffect(buttonPause);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused();
            }
        });

        stage.addActor(scoresTable);
        stage.addActor(buttonPause);
    }

    @Override
    public void update(float delta) {
        switch (state) {
            case STATE_RUNNING:
                updateRunning(delta);
                break;
            case STATE_GAME_OVER:
                updateGameOver(delta);
                break;
        }
    }

    private void updateRunning(float delta) {

        float accelerationX;

        accelerationX = -(Gdx.input.getAccelerometerX() / 3f);

        if (Gdx.input.isKeyPressed(Keys.A))
            accelerationX = -1;
        else if (Gdx.input.isKeyPressed(Keys.D))
            accelerationX = 1;

        oWorld.update(delta, accelerationX, didFire, touchPositionWorldCoordinates);

        labelCoins.setText("x" + oWorld.coins);
        labelDistance.setText("Score " + oWorld.maxDistance);
        labelBullets.setText("x" + Settings.numBullets);

        if (oWorld.state == WorldGame.STATE_GAMEOVER) {
            setGameover();
        }

        didFire = false;
    }

    private void updateGameOver(float delta) {
        oWorld.update(delta, 0, false, touchPositionWorldCoordinates);
    }

    @Override
    public void draw(float delta) {

        batch.begin();
        batch.draw(Assets.background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();

        if (state != STATE_PAUSED) {
            renderer.render();
        }
    }

    private void setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            pauseWindow.show(stage);
        }
    }

    public void setRunning() {
        state = STATE_RUNNING;
    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        Settings.setBestScore(oWorld.maxDistance);
        new BaseWindowGameover(this).show(stage);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPositionWorldCoordinates.set(screenX, 0, 0);// Always as if I had touched the top of the screen
        renderer.unprojectToWorldCoordinates(touchPositionWorldCoordinates);

        didFire = true;
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            if (pauseWindow.isVisible())
                pauseWindow.hide();
            else
                setPaused();
            return true;
        }
        return super.keyDown(keycode);
    }
}
