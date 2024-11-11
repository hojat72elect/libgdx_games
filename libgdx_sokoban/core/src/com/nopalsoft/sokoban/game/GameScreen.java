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
import com.nopalsoft.sokoban.scene2d.ContadorBar;
import com.nopalsoft.sokoban.scene2d.ControlesNoPad;
import com.nopalsoft.sokoban.screens.MainMenuScreen;
import com.nopalsoft.sokoban.screens.Screens;

public class GameScreen extends Screens {
    static final int STATE_RUNNING = 0;
    static final int STATE_PAUSED = 1;
    static final int STATE_GAME_OVER = 2;
    public int state;
    public int level;
    TableroRenderer renderer;
    Board oBoard;
    ControlesNoPad oControl;
    Button btUndo;
    Button btPausa;
    ContadorBar barTime;
    ContadorBar barMoves;
    com.nopalsoft.sokoban.scene2d.DialogPause vtPause;
    private final Stage stageGame;

    public GameScreen(final MainSokoban game, int level) {
        super(game);
        this.level = level;

        stageGame = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        oBoard = new Board();

        renderer = new TableroRenderer(batcher);

        oControl = new ControlesNoPad(this);

        barTime = new ContadorBar(Assets.backgroundTime, 5, 430);
        barMoves = new ContadorBar(Assets.backgroundMoves, 5, 380);

        vtPause = new com.nopalsoft.sokoban.scene2d.DialogPause(this);

        Label lbNivel = new Label("Level " + (level + 1), new LabelStyle(Assets.fontRed, Color.WHITE));
        lbNivel.setWidth(barTime.getWidth());
        lbNivel.setPosition(5, 330);
        lbNivel.setAlignment(Align.center);

        btUndo = new Button(Assets.btRefresh, Assets.btRefreshPress);
        btUndo.setSize(80, 80);
        btUndo.setPosition(700, 20);
        btUndo.getColor().a = oControl.getColor().a;// Que tengan el mismo color de alpha
        btUndo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                oBoard.undo = true;
            }
        });

        btPausa = new Button(Assets.btPausa, Assets.btPausaPress);
        btPausa.setSize(60, 60);
        btPausa.setPosition(730, 410);
        btPausa.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPause();
            }

        });

        stageGame.addActor(oBoard);
        stageGame.addActor(barTime);
        stageGame.addActor(barMoves);
        stage.addActor(lbNivel);
        stage.addActor(oControl);
        stage.addActor(btUndo);
        stage.addActor(btPausa);

        setRunning();
    }

    @Override
    public void draw(float delta) {
        Assets.background.render(delta);

        // Render el tileMap
        renderer.render(delta);

        // Render el tablero
        stageGame.draw();

    }

    @Override
    public void update(float delta) {

        if (state != STATE_PAUSED) {
            stageGame.act(delta);
            barMoves.updateActualNum(oBoard.moves);
            barTime.updateActualNum((int) oBoard.time);

            if (state == STATE_RUNNING && oBoard.state == Board.STATE_GAMEOVER) {
                setGameover();
            }
        }

    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        Settings.levelCompeted(level, oBoard.moves, (int) oBoard.time);
        stage.addAction(Actions.sequence(Actions.delay(.35f), Actions.run(new Runnable() {
            @Override
            public void run() {
                level += 1;
                if (level >= Settings.NUM_MAPS)
                    changeScreenWithFadeOut(MainMenuScreen.class, game);
                else
                    changeScreenWithFadeOut(GameScreen.class, level, game);

            }
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
        oBoard.moveUp = true;
        super.up();
    }

    @Override
    public void down() {
        oBoard.moveDown = true;
        super.down();
    }

    @Override
    public void right() {
        oBoard.moveRight = true;
        super.right();
    }

    @Override
    public void left() {
        oBoard.moveLeft = true;
        super.left();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == STATE_RUNNING) {
            if (keycode == Keys.LEFT || keycode == Keys.A) {
                oBoard.moveLeft = true;

            } else if (keycode == Keys.RIGHT || keycode == Keys.D) {
                oBoard.moveRight = true;

            } else if (keycode == Keys.UP || keycode == Keys.W) {
                oBoard.moveUp = true;

            } else if (keycode == Keys.DOWN || keycode == Keys.S) {
                oBoard.moveDown = true;

            } else if (keycode == Keys.Z) {
                oBoard.undo = true;

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
