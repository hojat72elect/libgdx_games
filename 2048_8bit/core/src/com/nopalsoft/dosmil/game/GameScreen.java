package com.nopalsoft.dosmil.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.MainGame;
import com.nopalsoft.dosmil.Settings;
import com.nopalsoft.dosmil.scene2d.GameOverDialog;
import com.nopalsoft.dosmil.scene2d.GamePausedDialog;
import com.nopalsoft.dosmil.screens.MainMenuScreen;
import com.nopalsoft.dosmil.screens.Screens;

public class GameScreen extends Screens {

    static final int STATE_RUNNING = 1;
    static final int STATE_PAUSED = 2;
    static final int STATE_GAME_OVER = 3;
    public int state;

    GameBoard oGameBoard;

    private final Stage stageGame;

    Table tableBookmarks;
    Label labelTime, labelScore, labelBestScore;

    Button buttonPause;
    GamePausedDialog pausedDialog;

    public GameScreen(MainGame game) {
        super(game);
        stageGame = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        oGameBoard = new GameBoard();
        stageGame.addActor(oGameBoard);

        initUI();

        Settings.numberOfTimesPlayed++;
    }

    private void initUI() {
        tableBookmarks = new Table();
        tableBookmarks.setSize(SCREEN_WIDTH, 100);
        tableBookmarks.setPosition(0, 680);


        labelTime = new Label(Assets.languagesBundle.get("score") + "\n0", Assets.labelStyleSmall);
        labelTime.setAlignment(Align.center);
        labelTime.setFontScale(1.15f);

        labelScore = new Label(Assets.languagesBundle.get("score") + "\n0", Assets.labelStyleSmall);
        labelScore.setFontScale(1.15f);
        labelScore.setAlignment(Align.center);

        labelBestScore = new Label(Assets.languagesBundle.get("best") + "\n" + Settings.bestScore, Assets.labelStyleSmall);
        labelBestScore.setAlignment(Align.center);
        labelBestScore.setFontScale(1.15f);

        tableBookmarks.row().expand().uniform().center();
        tableBookmarks.add(labelTime);
        tableBookmarks.add(labelScore);
        tableBookmarks.add(labelBestScore);

        pausedDialog = new GamePausedDialog(this);

        buttonPause = new Button(Assets.buttonStylePause);
        buttonPause.setPosition(SCREEN_WIDTH / 2F - buttonPause.getWidth() / 2, 110);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused();
            }
        });

        stage.addActor(tableBookmarks);
        stage.addActor(buttonPause);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stageGame.getViewport().update(width, height, true);
    }

    @Override
    public void update(float delta) {

        if (state == STATE_RUNNING) {
            updateRunning(delta);
        }

        labelTime.setText(Assets.languagesBundle.get("time") + "\n" + ((int) oGameBoard.elapsedTime));
        labelScore.setText(Assets.languagesBundle.get("score") + "\n" + (oGameBoard.score));
    }

    private void updateRunning(float delta) {
        stageGame.act(delta);

        if (oGameBoard.state == GameBoard.STATE_GAMEOVER) {
            setGameover();
        }
    }

    @Override
    public void draw(float delta) {

        batch.begin();
        batch.draw(Assets.backgroundAtlasRegion, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();

        stageGame.draw();
    }

    @Override
    public void pause() {
        setPaused();
        super.pause();
    }

    @Override
    public void hide() {
        super.hide();
        stageGame.dispose();
    }

    private void setPaused() {
        if (state == STATE_GAME_OVER || state == STATE_PAUSED)
            return;
        state = STATE_PAUSED;
        stage.addActor(pausedDialog);
    }

    public void setRunning() {
        if (state == STATE_GAME_OVER)
            return;
        state = STATE_RUNNING;
    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        Settings.setBestScores(oGameBoard.score);
        GameOverDialog gameOverDialog = new GameOverDialog(this, oGameBoard.didWin, (int) oGameBoard.elapsedTime, oGameBoard.score);
        stage.addActor(gameOverDialog);
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (state != STATE_PAUSED)
            setRunning();
        return super.fling(velocityX, velocityY, button);
    }

    @Override
    public void up() {
        oGameBoard.moveUp = true;
        super.up();
    }

    @Override
    public void down() {
        oGameBoard.moveDown = true;
        super.down();
    }

    @Override
    public void right() {
        oGameBoard.moveRight = true;
        super.right();
    }

    @Override
    public void left() {
        oGameBoard.moveLeft = true;
        super.left();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state != STATE_PAUSED) {
            if (keycode == Keys.LEFT) {
                oGameBoard.moveLeft = true;
                setRunning();
            } else if (keycode == Keys.RIGHT) {
                oGameBoard.moveRight = true;
                setRunning();
            } else if (keycode == Keys.UP) {
                oGameBoard.moveUp = true;
                setRunning();
            } else if (keycode == Keys.DOWN) {
                oGameBoard.moveDown = true;

                setRunning();
            }
        } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {

            changeScreenWithFadeOut(MainMenuScreen.class, game);
        }

        return true;
    }
}
