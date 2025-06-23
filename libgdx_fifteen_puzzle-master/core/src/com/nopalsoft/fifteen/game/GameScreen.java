package com.nopalsoft.fifteen.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.fifteen.Assets;
import com.nopalsoft.fifteen.MainFifteen;
import com.nopalsoft.fifteen.Settings;
import com.nopalsoft.fifteen.handlers.AmazonGameServicesHandler;
import com.nopalsoft.fifteen.scene2d.MarcoGameOver;
import com.nopalsoft.fifteen.scene2d.MarcoPaused;
import com.nopalsoft.fifteen.screens.MainMenuScreen;
import com.nopalsoft.fifteen.screens.Screens;

public class GameScreen extends Screens {
	static final int STATE_READY = 1;
	static final int STATE_RUNNING = 2;
	static final int STATE_PAUSED = 3;
	static final int STATE_GAME_OVER = 4;
	public int state;

	Tablero oTablero;

	private Stage stageGame;

	Table tbMarcadores;
	Label lbTime, lbMoves;

	Button btPause;

	MarcoPaused oMarcoPaused;

	public GameScreen(MainFifteen game) {
		super(game);
		stageGame = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
		oTablero = new Tablero();
		stageGame.addActor(oTablero);

		initUI();

		setReady();

		Settings.numeroVecesJugadas++;

	}

	private void initUI() {
		tbMarcadores = new Table();
		tbMarcadores.setSize(SCREEN_WIDTH, 100);
		tbMarcadores.setPosition(0, 680);
		// tbMarcadores.debug();

		lbTime = new Label("Time\n0", Assets.labelStyleChico);
		lbTime.setAlignment(Align.center);
		lbTime.setFontScale(1.15f);
		lbMoves = new Label("Moves\n0", Assets.labelStyleChico);
		lbMoves.setFontScale(1.15f);
		lbMoves.setAlignment(Align.center);

		tbMarcadores.row().expand().uniform().center();
		tbMarcadores.add(lbTime);
		tbMarcadores.add(lbMoves);

		btPause = new Button(Assets.styleButtonPause);
		btPause.setPosition(SCREEN_WIDTH / 2 - btPause.getWidth() / 2, 110);
		btPause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setPaused();
			}

		});

		oMarcoPaused = new MarcoPaused(this);

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

		lbTime.setText("Time\n" + ((int) oTablero.tiempo));
		lbMoves.setText("Moves\n" + (oTablero.moves));

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

	private void setReady() {
		state = STATE_READY;

	}

	private void setPaused() {
		if (state == STATE_GAME_OVER || state == STATE_PAUSED)
			return;
		state = STATE_PAUSED;
		stage.addActor(oMarcoPaused);

	}

	public void setRunning() {
		if (state == STATE_GAME_OVER)
			return;
		state = STATE_RUNNING;
	}

	private void setGameover() {
		state = STATE_GAME_OVER;
		Settings.setBestScores((int) oTablero.tiempo, oTablero.moves);

		String idBestTime = "CgkIzqr6xOwWEAIQAA";
		String idBestMoves = "CgkIzqr6xOwWEAIQAQ";

		if (game.gameServiceHandler instanceof AmazonGameServicesHandler) {
			idBestTime = "BestTime";
			idBestMoves = "BestMoves";
		}

		game.gameServiceHandler.submitScore((int) oTablero.tiempo, idBestTime);
		game.gameServiceHandler.submitScore(oTablero.moves, idBestMoves);
		MarcoGameOver oMarcoGameover = new MarcoGameOver(this,
				(int) oTablero.tiempo, oTablero.moves);
		stage.addActor(oMarcoGameover);
		game.reqHandler.showAdBanner();

	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		setRunning();
		return super.fling(velocityX, velocityY, button);
	}

	/**
	 * Es muy imporante recordar que lo que se mueve es la pieza blanca por lo tanto si yo digo moveUp la pieza blanca se movera hacia arriba pero el usuario piensa que si hace un swipe down la pieza
	 * de arriba de la blanca es la que baja. Cuando nosotros sabemos que la que sube es la blanca.
	 */

	@Override
	public void up() {
		oTablero.moveDown = true;
		super.up();
	}

	@Override
	public void down() {
		oTablero.moveUp = true;
		super.down();
	}

	@Override
	public void right() {
		oTablero.moveLeft = true;
		super.right();
	}

	@Override
	public void left() {
		oTablero.moveRight = true;
		super.left();
	}

	/**
	 * Es muy imporante recordar que lo que se mueve es la pieza blanca por lo tanto si yo digo moveUp la pieza blanca se movera hacia arriba pero el usuario piensa que si hace un swipe down la pieza
	 * de arriba de la blanca es la que baja. Cuando nosotros sabemos que la que sube es la blanca.
	 */

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Keys.LEFT) {
			oTablero.moveRight = true;
			setRunning();
		}
		else if (keycode == Keys.RIGHT) {
			oTablero.moveLeft = true;
			setRunning();
		}
		else if (keycode == Keys.UP) {
			oTablero.moveDown = true;
			setRunning();
		}
		else if (keycode == Keys.DOWN) {
			oTablero.moveUp = true;
			setRunning();
		}
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {

			changeScreenWithFadeOut(MainMenuScreen.class, game);
		}

		return true;
	}

}
