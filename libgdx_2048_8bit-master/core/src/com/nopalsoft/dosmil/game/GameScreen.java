package com.nopalsoft.dosmil.game;

import com.badlogic.gdx.Gdx;
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
import com.nopalsoft.dosmil.scene2d.MarcoGameOver;
import com.nopalsoft.dosmil.scene2d.MarcoPaused;
import com.nopalsoft.dosmil.screens.MainMenuScreen;
import com.nopalsoft.dosmil.screens.Screens;

public class GameScreen extends Screens {
    static final int STATE_READY = 0;
    static final int STATE_RUNNING = 1;
    static final int STATE_PAUSED = 2;
    static final int STATE_GAME_OVER = 3;
    public int state;

    Tablero oTablero;

    private Stage stageGame;

    Table tbMarcadores;
    Label lbTime, lbScore, lbBestScore;

    Button btPause;
    MarcoPaused oPaused;

    public GameScreen(MainGame game) {
        super(game);
        stageGame = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        oTablero = new Tablero();
        stageGame.addActor(oTablero);

        initUI();

        Settings.numeroVecesJugadas++;
        game.reqHandler.loadInterstitial();

    }

    private void initUI() {
        tbMarcadores = new Table();
        tbMarcadores.setSize(SCREEN_WIDTH, 100);
        tbMarcadores.setPosition(0, 680);
        // tbMarcadores.debug();

        lbTime = new Label(Assets.idiomas.get("score") + "\n0", Assets.labelStyleChico);
        lbTime.setAlignment(Align.center);
        lbTime.setFontScale(1.15f);

        lbScore = new Label(Assets.idiomas.get("score") + "\n0", Assets.labelStyleChico);
        lbScore.setFontScale(1.15f);
        lbScore.setAlignment(Align.center);

        lbBestScore = new Label(Assets.idiomas.get("best") + "\n" + Settings.bestScore, Assets.labelStyleChico);
        lbBestScore.setAlignment(Align.center);
        lbBestScore.setFontScale(1.15f);

        tbMarcadores.row().expand().uniform().center();
        tbMarcadores.add(lbTime);
        tbMarcadores.add(lbScore);
        tbMarcadores.add(lbBestScore);

        oPaused = new MarcoPaused(this);

        btPause = new Button(Assets.styleButtonPause);
        btPause.setPosition(SCREEN_WIDTH / 2 - btPause.getWidth() / 2, 110);
        btPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused();
            }

        });

        stage.addActor(tbMarcadores);
        stage.addActor(btPause);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stageGame.getViewport().update(width, height, true);
    }

    @Override
    public void update(float delta) {

        switch (state) {
            case STATE_RUNNING:
                updateRunning(delta);
                break;
        }

        lbTime.setText(Assets.idiomas.get("time") + "\n" + ((int) oTablero.tiempo));
        lbScore.setText(Assets.idiomas.get("score") + "\n" + (oTablero.score));

    }

    private void updateRunning(float delta) {
        stageGame.act(delta);

        if (oTablero.state == Tablero.STATE_GAMEOVER) {
            setGameover();
        }
    }

    @Override
    public void draw(float delta) {

        batcher.begin();
        batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batcher.end();

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
        game.reqHandler.hideAdBanner();
        if (Settings.numeroVecesJugadas % 3 == 0)
            game.reqHandler.showInterstitial();
    }

    private void setPaused() {
        if (state == STATE_GAME_OVER || state == STATE_PAUSED)
            return;
        state = STATE_PAUSED;
        stage.addActor(oPaused);
        game.reqHandler.showAdBanner();

    }

    public void setRunning() {
        if (state == STATE_GAME_OVER)
            return;
        state = STATE_RUNNING;
        game.reqHandler.hideAdBanner();
    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        Settings.setBestScores(oTablero.score);
        game.gameServiceHandler.submitScore(oTablero.score);
        MarcoGameOver oGameOver = new MarcoGameOver(this, oTablero.didWin, (int) oTablero.tiempo, oTablero.score);
        stage.addActor(oGameOver);
        game.reqHandler.showAdBanner();

    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (state != STATE_PAUSED)
            setRunning();
        return super.fling(velocityX, velocityY, button);
    }
    /**
     * Es muy imporante recordar que lo que se mueve es la pieza blanca por lo tanto si yo digo moveUp la pieza blanca se movera hacia arriba pero el usuario piensa que si hace un swipe down la pieza
     * de arriba de la blanca es la que baja. Cuando nosotros sabemos que la que sube es la blanca.
     */

    @Override
    public void up() {
        oTablero.moveUp = true;
        super.up();
    }

    @Override
    public void down() {
        oTablero.moveDown = true;
        super.down();
    }

    @Override
    public void right() {
        oTablero.moveRight = true;
        super.right();
    }

    @Override
    public void left() {
        oTablero.moveLeft = true;
        super.left();
    }

    /**
     * Es muy imporante recordar que lo que se mueve es la pieza blanca por lo tanto si yo digo moveUp la pieza blanca se movera hacia arriba pero el usuario piensa que si hace un swipe down la pieza
     * de arriba de la blanca es la que baja. Cuando nosotros sabemos que la que sube es la blanca.
     */

    @Override
    public boolean keyDown(int keycode) {
        if (state != STATE_PAUSED) {
            if (keycode == Keys.LEFT) {
                oTablero.moveLeft = true;
                setRunning();
            } else if (keycode == Keys.RIGHT) {
                oTablero.moveRight = true;
                setRunning();
            } else if (keycode == Keys.UP) {
                oTablero.moveUp = true;
                setRunning();
            } else if (keycode == Keys.DOWN) {
                oTablero.moveDown = true;

                setRunning();
            }
        } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {

            changeScreenWithFadeOut(MainMenuScreen.class, game);
        }

        return true;
    }

}
