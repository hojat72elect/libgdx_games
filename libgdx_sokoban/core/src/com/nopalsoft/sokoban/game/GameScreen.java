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
import com.nopalsoft.sokoban.scene2d.CounterTable;
import com.nopalsoft.sokoban.screens.MainMenuScreen;
import com.nopalsoft.sokoban.screens.Screens;

public class GameScreen extends Screens {
    static final int STATE_RUNNING = 0;
    static final int STATE_PAUSED = 1;
    static final int STATE_GAME_OVER = 2;
    public int state;
    public int level;
    BoardRenderer boardRenderer;
    Board board;
    com.nopalsoft.sokoban.scene2d.OnScreenGamePad oControl;
    Button buttonUndo;
    Button buttonPause;
    CounterTable barTime;
    CounterTable barMoves;
    com.nopalsoft.sokoban.scene2d.DialogPause vtPause;
    private final Stage stageGame;

    public GameScreen(final MainSokoban game, int level) {
        super(game);
        this.level = level;

        stageGame = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        board = new Board();

        boardRenderer = new BoardRenderer(batcher);

        oControl = new com.nopalsoft.sokoban.scene2d.OnScreenGamePad(this);

        barTime = new CounterTable(Assets.backgroundTime, 5, 430);
        barMoves = new CounterTable(Assets.backgroundMoves, 5, 380);

        vtPause = new com.nopalsoft.sokoban.scene2d.DialogPause(this);

        Label labelLevel = new Label("Level " + (level + 1), new LabelStyle(Assets.fontRed, Color.WHITE));
        labelLevel.setWidth(barTime.getWidth());
        labelLevel.setPosition(5, 330);
        labelLevel.setAlignment(Align.center);

        buttonUndo = new Button(Assets.btRefresh, Assets.btRefreshPress);
        buttonUndo.setSize(80, 80);
        buttonUndo.setPosition(700, 20);
        buttonUndo.getColor().a = oControl.getColor().a;// That they have the same color of alpha
        buttonUndo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                board.undo = true;
            }
        });

        buttonPause = new Button(Assets.btPausa, Assets.btPausaPress);
        buttonPause.setSize(60, 60);
        buttonPause.setPosition(730, 410);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPause();
            }

        });

        stageGame.addActor(board);
        stageGame.addActor(barTime);
        stageGame.addActor(barMoves);
        stage.addActor(labelLevel);
        stage.addActor(oControl);
        stage.addActor(buttonUndo);
        stage.addActor(buttonPause);

        setRunning();
    }

    @Override
    public void draw(float delta) {
        Assets.background.render(delta);

        //Render the tileMap
        boardRenderer.render();

        // Render the board
        stageGame.draw();

    }

    @Override
    public void update(float delta) {

        if (state != STATE_PAUSED) {
            stageGame.act(delta);
            barMoves.updateActualNum(board.moves);
            barTime.updateActualNum((int) board.time);

            if (state == STATE_RUNNING && board.state == Board.STATE_GAMEOVER) {
                setGameover();
            }
        }

    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        Settings.levelCompleted(level, board.moves, (int) board.time);
        stage.addAction(Actions.sequence(Actions.delay(.35f), Actions.run(() -> {
            level += 1;
            if (level >= com.nopalsoft.sokoban.Settings.NUM_MAPS)
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
            vtPause.show(stage);
        }
    }

    @Override
    public void up() {
        board.moveUp = true;
        super.up();
    }

    @Override
    public void down() {
        board.moveDown = true;
        super.down();
    }

    @Override
    public void right() {
        board.moveRight = true;
        super.right();
    }

    @Override
    public void left() {
        board.moveLeft = true;
        super.left();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == STATE_RUNNING) {
            if (keycode == Keys.LEFT || keycode == Keys.A) {
                board.moveLeft = true;

            } else if (keycode == Keys.RIGHT || keycode == Keys.D) {
                board.moveRight = true;

            } else if (keycode == Keys.UP || keycode == Keys.W) {
                board.moveUp = true;

            } else if (keycode == Keys.DOWN || keycode == Keys.S) {
                board.moveDown = true;

            } else if (keycode == Keys.Z) {
                board.undo = true;

            } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                setPause();
            }
        } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            if (vtPause.isShown())
                vtPause.hide();
        }

        return true;
    }

}
