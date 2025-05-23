package com.nopalsoft.sokoban.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.MainSokoban;
import com.nopalsoft.sokoban.Settings;
import com.nopalsoft.sokoban.scene2d.TouchPadControlsTable;
import com.nopalsoft.sokoban.scene2d.CounterTable;
import com.nopalsoft.sokoban.scene2d.WindowGroupPause;
import com.nopalsoft.sokoban.screens.MainMenuScreen;
import com.nopalsoft.sokoban.screens.Screens;

public class GameScreen extends Screens {
    static final int STATE_RUNNING = 0;
    static final int STATE_PAUSED = 1;
    static final int STATE_GAME_OVER = 2;
    public int state;

    BoardRenderer renderer;
    GameBoard gameBoard;

    TouchPadControlsTable touchpadControls;
    Button buttonUndo;
    Button buttonPause;

    CounterTable counterTableTime;
    CounterTable counterTableMoves;

    private final Stage gameStage;

    WindowGroupPause windowPause;

    public int level;

    public GameScreen(final MainSokoban game, int level) {
        super(game);
        this.level = level;

        gameStage = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        gameBoard = new GameBoard();

        renderer = new BoardRenderer(batch);

        touchpadControls = new TouchPadControlsTable(this);

        counterTableTime = new CounterTable(Assets.backgroundTime, 5, 430);
        counterTableMoves = new CounterTable(Assets.backgroundMoves, 5, 380);

        windowPause = new WindowGroupPause(this);

        Label lbNivel = new Label("Level " + (level + 1), new LabelStyle(Assets.fontRed, Color.WHITE));
        lbNivel.setWidth(counterTableTime.getWidth());
        lbNivel.setPosition(5, 330);
        lbNivel.setAlignment(Align.center);

        buttonUndo = new Button(Assets.buttonRefreshDrawable, Assets.buttonRefreshPressedDrawable);
        buttonUndo.setSize(80, 80);
        buttonUndo.setPosition(700, 20);
        buttonUndo.getColor().a = touchpadControls.getColor().a;// which means that they will have the same alpha color.
        buttonUndo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameBoard.undo = true;
            }
        });

        buttonPause = new Button(Assets.buttonPauseDrawable, Assets.buttonPausePressedDrawable);
        buttonPause.setSize(60, 60);
        buttonPause.setPosition(730, 410);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPause();
            }
        });

        gameStage.addActor(gameBoard);
        gameStage.addActor(counterTableTime);
        gameStage.addActor(counterTableMoves);
        stage.addActor(lbNivel);
        stage.addActor(touchpadControls);
        stage.addActor(buttonUndo);
        stage.addActor(buttonPause);

        setRunning();
    }

    @Override
    public void draw(float delta) {
        Assets.background.render(delta);

        // Render the tileMap
        renderer.render();

        // Render the board
        gameStage.draw();
    }

    @Override
    public void update(float delta) {

        if (state != STATE_PAUSED) {
            gameStage.act(delta);
            counterTableMoves.updateDisplayedNumber(gameBoard.moves);
            counterTableTime.updateDisplayedNumber((int) gameBoard.time);

            if (state == STATE_RUNNING && gameBoard.state == GameBoard.STATE_GAME_OVER) {
                setGameOver();
            }
        }
    }

    private void setGameOver() {
        state = STATE_GAME_OVER;
        Settings.levelCompeted(level, gameBoard.moves, (int) gameBoard.time);
        stage.addAction(Actions.sequence(Actions.delay(.35f), Actions.run(() -> {
            level += 1;
            if (level >= Settings.NUM_MAPS)
                changeScreenWithFadeOut(MainMenuScreen.class, game);
            else
                changeScreenWithFadeOut(GameScreen.class, level, game);
        })));
    }

    public void setRunning() {
        if (state != STATE_GAME_OVER) {
            state = STATE_RUNNING;
        }
    }

    private void setPause() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            windowPause.show(stage);
        }
    }

    @Override
    public void up() {
        gameBoard.moveUp = true;
        super.up();
    }

    @Override
    public void down() {
        gameBoard.moveDown = true;
        super.down();
    }

    @Override
    public void right() {
        gameBoard.moveRight = true;
        super.right();
    }

    @Override
    public void left() {
        gameBoard.moveLeft = true;
        super.left();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == STATE_RUNNING) {
            if (keycode == Keys.LEFT || keycode == Keys.A) {
                gameBoard.moveLeft = true;
            } else if (keycode == Keys.RIGHT || keycode == Keys.D) {
                gameBoard.moveRight = true;
            } else if (keycode == Keys.UP || keycode == Keys.W) {
                gameBoard.moveUp = true;
            } else if (keycode == Keys.DOWN || keycode == Keys.S) {
                gameBoard.moveDown = true;
            } else if (keycode == Keys.Z) {
                gameBoard.undo = true;
            } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                setPause();
            }
        } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            if (windowPause.isShown())
                windowPause.hide();
        }

        return true;
    }
}
