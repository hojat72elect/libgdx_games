package com.nopalsoft.donttap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.MainDoNot;
import com.nopalsoft.donttap.Settings;
import com.nopalsoft.donttap.dialogs.VentanaGameOver;
import com.nopalsoft.donttap.dialogs.VentanaGamePaused;
import com.nopalsoft.donttap.screens.Screens;

public class GameScreen extends Screens {
    static final int STATE_RUNNING = 2;
    static final int STATE_PAUSED = 3;
    static final int STATE_GAME_OVER = 4;
    public int state;

    static public final int MODE_CLASSIC = 0;// Reccore 50 tiles en el menor tiempo
    static public final int MODE_TIME = 1;// Recorre por 1 minuto
    static public final int MODE_ENDLESS = 2;// No dejes que ningun tile se pase
    public int mode;

    final private Stage stageGame;
    public WorldGame oWorld;

    Label lbTime, lbTilesScore, lbBestScore;
    Table tbMarcadores;

    public GameScreen(MainDoNot game, int mode) {
        super(game);
        this.mode = mode;
        stageGame = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));

        oWorld = new WorldGame(mode);
        stageGame.addActor(oWorld);

        initMarcadores();

        // Tengo que agregar a los eventos el stageGame
        InputMultiplexer input = new InputMultiplexer(this, stage, stageGame);
        Gdx.input.setInputProcessor(input);

        state = STATE_RUNNING;
        Settings.numeroVecesJugadas++;
    }

    private void initMarcadores() {
        tbMarcadores = new Table();
        tbMarcadores.setSize(SCREEN_WIDTH, 80);
        tbMarcadores.setPosition(0, SCREEN_HEIGHT - tbMarcadores.getHeight());

        Image background = new Image(Assets.tileBlanco);
        background.setSize(tbMarcadores.getWidth(), tbMarcadores.getHeight());
        background.setPosition(0, 0);
        tbMarcadores.addActor(background);

        lbTime = new Label("Time\n0.0", Assets.labelStyleBlack);
        lbTime.setAlignment(Align.center);
        lbTime.setFontScale(.9f);

        lbTilesScore = new Label("Tiles\n0", Assets.labelStyleBlack);
        lbTilesScore.setAlignment(Align.center);
        lbTilesScore.setFontScale(.85f);

        lbBestScore = new Label("Best\n0", Assets.labelStyleBlack);
        lbBestScore.setAlignment(Align.center);
        lbBestScore.setFontScale(.8f);

        switch (mode) {
            case MODE_CLASSIC:
                String text = game.formatter.format("%.1f", Settings.bestTimeClassicMode);
                if (Settings.bestTimeClassicMode >= 100100)
                    text = "X";
                lbBestScore.setText("Best\n" + text);
                break;
            case MODE_TIME:
                lbBestScore.setText("Best\n" + Settings.bestScoreTimeMode);
                break;

            case MODE_ENDLESS:
                lbBestScore.setText("Best\n" + Settings.bestScoreEndlessMode);
                break;
        }

        tbMarcadores.row().expand().uniform().center();
        tbMarcadores.add(lbTime);
        tbMarcadores.add(lbTilesScore);
        tbMarcadores.add(lbBestScore);

        stage.addActor(tbMarcadores);
    }

    @Override
    public void update(float delta) {

        if (state == STATE_RUNNING) {
            stageGame.act(delta);

            if (oWorld.state == WorldGame.STATE_GAME_OVER_2) {
                if (mode == MODE_ENDLESS)
                    setWin();
                else
                    setGameover();
            } else if (oWorld.state == WorldGame.STATE_GAME_WIN) {
                setWin();
            }
        }

        lbTime.setText("Time\n" + game.formatter.format("%.1f", oWorld.time));
        lbTilesScore.setText("Tiles\n" + oWorld.score);
    }

    // La diferencia si se gana es que se suben las puntuaciones;
    private void setWin() {
        switch (mode) {
            case MODE_CLASSIC:
                if (Settings.bestTimeClassicMode > oWorld.time)
                    Settings.bestTimeClassicMode = oWorld.time;
                break;
            case MODE_TIME:
                if (Settings.bestScoreTimeMode < oWorld.score)
                    Settings.bestScoreTimeMode = oWorld.score;
                break;

            case MODE_ENDLESS:
                if (Settings.bestScoreEndlessMode < oWorld.score)
                    Settings.bestScoreEndlessMode = oWorld.score;
                break;
        }

        setGameover();
    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        VentanaGameOver ventana = new VentanaGameOver(this);
        ventana.show(stage);
    }

    public void setRunning() {
        state = STATE_RUNNING;
    }

    public void setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            VentanaGamePaused oPaused = new VentanaGamePaused(this);
            oPaused.show(stage);
        }
    }

    @Override
    public void draw(float delta) {
        stageGame.draw();
//		stageGame.setDebugAll(true);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stageGame.getViewport().update(width, height, true);
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

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.S)
            oWorld.moveRowsDown();
        else if (keycode == Keys.W)
            oWorld.moveRowsUp();
        else if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
            setPaused();
        return super.keyDown(keycode);
    }
}
