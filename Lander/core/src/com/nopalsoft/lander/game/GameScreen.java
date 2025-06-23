package com.nopalsoft.lander.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.lander.Assets;
import com.nopalsoft.lander.MainLander;
import com.nopalsoft.lander.Settings;
import com.nopalsoft.lander.dialogs.VentanaCompleted;
import com.nopalsoft.lander.dialogs.VentanaGameOver;
import com.nopalsoft.lander.dialogs.VentanaPaused;
import com.nopalsoft.lander.game.objetos.LifeBar;
import com.nopalsoft.lander.screens.MainMenuScreen;
import com.nopalsoft.lander.screens.Screens;

public class GameScreen extends Screens {
	public static final int STATE_READY = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_PAUSED = 2;
	public static final int STATE_GAME_OVER = 3;
	public static int state;

	public final int level;
	WorldGame oWorld;
	WorldGameRenderer renderer;

	float sensibilidad = 3;

	LifeBar lifeBar;
	LifeBar gasBar;
	Table marcoStats;

	ImageButton btPause;
	VentanaGameOver dialogGameover;
	VentanaPaused dialogPaused;
	VentanaCompleted dialogNextLevel;

	public GameScreen(MainLander game, int level) {
		super(game);
		this.level = level;
		Assets.cargarMapa(level);
		oWorld = new WorldGame();
		renderer = new WorldGameRenderer(batcher, oWorld);

		dialogGameover = new VentanaGameOver(game, oWorld, level);
		dialogPaused = new VentanaPaused(game, oWorld, level);
		dialogNextLevel = new VentanaCompleted(game, oWorld, level);

		// Marcador Stats
		marcoStats = new Table();
		marcoStats.setSize(172, 98);
		marcoStats.setBackground(Assets.marcoStats);
		marcoStats.setPosition(0, SCREEN_HEIGHT - 99);

		lifeBar = new LifeBar(oWorld.oNave.vida);
		gasBar = new LifeBar(oWorld.oNave.gas);

		marcoStats.add(lifeBar).width(90).height(25).padLeft(35).padBottom(5);
		marcoStats.row();
		marcoStats.add(gasBar).width(90).height(25).padLeft(35).padTop(6);

		// Boton Pause
		btPause = new ImageButton(Assets.styleImageButtonPause);
		btPause.setSize(32, 32);
		btPause.setPosition(SCREEN_WIDTH - btPause.getWidth() - 5, SCREEN_HEIGHT - btPause.getHeight() - 5);
		btPause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setPaused();
			}
		});

		stage.addActor(marcoStats);
		stage.addActor(btPause);

		state = STATE_RUNNING;
	}

	@Override
	public void update(float delta) {
		switch (state) {
			case STATE_READY:
				updateReady(delta);
				break;
			case STATE_RUNNING:
				updateRunning(delta);
				break;
		}
	}

	private void updateReady(float delta) {

	}

	private void updateRunning(float delta) {
		float accelX = 0, accelY = 0;

		if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
			accelX = Gdx.input.getAccelerometerX() / sensibilidad * -1;
		}

		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT))
			accelX = -1;
		else if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT))
			accelX = 1;

		if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.SPACE))
			accelY = 1;

		oWorld.update(delta, accelY, accelX);

		lifeBar.updateActualLife(oWorld.oNave.vida);
		gasBar.updateActualLife(oWorld.oNave.gas);

		if (oWorld.state == WorldGame.STATE_GAME_OVER) {
			setGameover();
		}
		else if (oWorld.state == WorldGame.STATE_NEXT_LEVEL) {
			setNextLevel();
		}

	}

	@Override
	public void draw(float delta) {
		renderer.render(delta);
		oCam.update();
		batcher.setProjectionMatrix(oCam.combined);

		batcher.begin();

		// Assets.font.draw(batcher, "Velocidad " + oWorld.oNave.velocidadResultante, 10, 60);
		// Assets.font.draw(batcher, "Velocidad X " + oWorld.oNave.velocity.x, 10, 40);
		// Assets.font.draw(batcher, "Velocidad Y " + oWorld.oNave.velocity.y, 10, 20);

		// Assets.font.draw(batcher, "Estrellas  " + oWorld.estrellasTomadas, 300, 60);
		// Assets.font.draw(batcher, "Vida  " + oWorld.oNave.vida, 300, 40);
		// Assets.font.draw(batcher, "Gas  " + oWorld.oNave.gas, 300, 20);
		batcher.end();

	}

	private void setPaused() {
		state = STATE_PAUSED;
		dialogPaused.show(stage);
	}

	private void setGameover() {
		state = STATE_GAME_OVER;
		dialogGameover.show(stage);

	}

	private void setNextLevel() {
		state = STATE_GAME_OVER;
		Settings.setStarsFromLevel(level, oWorld.estrellasTomadas);
		dialogNextLevel.show(stage);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			game.setScreen(new MainMenuScreen(game));
			return true;
		}
		return false;
	}

}
