package com.nopalsoft.sharkadventure.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.sharkadventure.Achievements;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.MainShark;
import com.nopalsoft.sharkadventure.Settings;
import com.nopalsoft.sharkadventure.scene2d.GameUI;
import com.nopalsoft.sharkadventure.scene2d.MenuUI;
import com.nopalsoft.sharkadventure.scene2d.VentanaPause;
import com.nopalsoft.sharkadventure.screens.Screens;

public class GameScreen extends Screens {
	final int STATE_MENU = 0;// Menu principal
	final int STATE_RUNNING = 1;// Empieza el juego
	final int STATE_PAUSED = 2;// Pause
	final int STATE_GAME_OVER = 3;// Igual que el menu principal pero sin el titulo.
	int state;

	WorldGame oWorld;
	WorldRenderer renderer;

	GameUI gameUI;
	MenuUI menuUI;
	VentanaPause vtPause;

	public long puntuacion;

	/**
	 * 
	 * @param game
	 * @param showMainMenu
	 *            Muestra el menu principal de lo contrario inicia el juego inmediatamente
	 */
	public GameScreen(MainShark game, boolean showMainMenu) {
		super(game);
		oWorld = new WorldGame();
		renderer = new WorldRenderer(batcher, oWorld);
		gameUI = new GameUI(this, oWorld);
		menuUI = new MenuUI(this, oWorld);
		vtPause = new VentanaPause(this);

		Assets.reloadFondo();

		if (showMainMenu) {
			state = STATE_MENU;
			menuUI.show(stage, showMainMenu);
		}
		else {
			setRunning(false);
		}

		Achievements.tryAgainAchievements();

	}

	@Override
	public void update(float delta) {
		switch (state) {
		case STATE_RUNNING:
			updateRunning(delta);
			break;
		case STATE_MENU:
			updateStateMenu(delta);
			break;

		}

	}

	private void updateRunning(float delta) {
		if (Gdx.input.isKeyPressed(Keys.A))
			gameUI.accelX = -1;

		else if (Gdx.input.isKeyPressed(Keys.D))
			gameUI.accelX = 1;

		if (Gdx.input.isKeyJustPressed(Keys.W) || Gdx.input.isKeyJustPressed(Keys.SPACE))
			gameUI.didSwimUp = true;

		if (Gdx.input.isKeyJustPressed(Keys.CONTROL_RIGHT) || Gdx.input.isKeyJustPressed(Keys.CONTROL_RIGHT) || Gdx.input.isKeyJustPressed(Keys.F))
			gameUI.didFire = true;

		oWorld.update(delta, gameUI.accelX, gameUI.didSwimUp, gameUI.didFire);

		puntuacion = (long) oWorld.puntuacion;

		gameUI.lifeBar.updateActualNum(oWorld.oTiburon.life);
		gameUI.energyBar.updateActualNum(oWorld.oTiburon.energy);

		gameUI.didSwimUp = false;
		gameUI.didFire = false;

		if (oWorld.state == WorldGame.STATE_GAMEOVER) {
			setGameOver();
		}

	}

	private void updateStateMenu(float delta) {
		oWorld.oTiburon.updateStateTime(delta);

	}

	public void setRunning(boolean removeMenu) {
		Runnable runAfterHideMenu = new Runnable() {
			@Override
			public void run() {
				Runnable run = new Runnable() {
					@Override
					public void run() {
						state = STATE_RUNNING;
					}
				};
				gameUI.addAction(Actions.sequence(Actions.delay(GameUI.ANIMATION_TIME), Actions.run(run)));
				gameUI.show(stage);
			}
		};

		if (removeMenu) {
			menuUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideMenu)));
			menuUI.removeWithAnimations();
		}
		else {
			stage.addAction(Actions.run(runAfterHideMenu));
		}

	}

	private void setGameOver() {
		if (state != STATE_GAME_OVER) {
			state = STATE_GAME_OVER;
			Runnable runAfterHideGameUI = new Runnable() {
				@Override
				public void run() {
					menuUI.show(stage, false);
				}
			};

			Settings.setBestScore(puntuacion);

			gameUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideGameUI)));
			gameUI.removeWithAnimations();
			game.gameServiceHandler.submitScore((long) oWorld.puntuacion);

			Settings.numVecesJugadas++;
			if (Settings.numVecesJugadas % 4f == 0) {
				game.reqHandler.showInterstitial();
			}
			game.reqHandler.showAdBanner();
		}
	}

	public void setPaused() {
		if (state == STATE_RUNNING) {
			state = STATE_PAUSED;
			gameUI.removeWithAnimations();
			vtPause.show(stage);
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.A || keycode == Keys.D)
			gameUI.accelX = 0;
		return super.keyUp(keycode);
	}

	@Override
	public void draw(float delta) {

		if (state == STATE_PAUSED || state == STATE_GAME_OVER)
			delta = 0;

		renderer.render(delta);

		oCam.update();
		batcher.setProjectionMatrix(oCam.combined);
		batcher.enableBlending();
		batcher.begin();

		batcher.end();

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK | keycode == Keys.ESCAPE) {
			if (state == STATE_RUNNING) {
				setPaused();
			}
			else if (state == STATE_PAUSED) {
				vtPause.hide();
				setRunning(false);
			}
			else if (state == STATE_MENU) {
				Gdx.app.exit();
			}
			return true;
		}
		if (keycode == Keys.L) {
			game.facebookHandler.facebookSignIn();
			return true;
		}

		if (keycode == Keys.P) {
			game.setScreen(new GameScreen(game, false));
			return true;
		}
		return super.keyDown(keycode);
	}
}
