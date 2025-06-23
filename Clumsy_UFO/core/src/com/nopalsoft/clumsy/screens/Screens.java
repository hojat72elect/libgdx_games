package com.nopalsoft.clumsy.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nopalsoft.clumsy.Assets;
import com.nopalsoft.clumsy.MainClumsy;
import com.nopalsoft.clumsy.Settings;
import com.nopalsoft.clumsy.game.arcade.GameScreenArcade;
import com.nopalsoft.clumsy.game.classic.GameScreenClassic;

public abstract class Screens extends InputAdapter implements Screen {
	public static final int SCREEN_WIDTH = 480;
	public static final int SCREEN_HEIGHT = 800;

	public static final int WORLD_SCREEN_WIDTH = 4;
	public static final int WORLD_SCREEN_HEIGHT = 8;

	public MainClumsy game;

	public OrthographicCamera oCam;
	public SpriteBatch batcher;
	public Stage stage;

	Random oRan;

	public Screens(MainClumsy game) {
		this.stage = game.stage;
		this.stage.clear();
		this.batcher = game.batcher;
		this.game = game;

		oCam = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

		InputMultiplexer input = new InputMultiplexer(this, stage);
		Gdx.input.setInputProcessor(input);

		oRan = new Random();
		int ale = oRan.nextInt(3);

		if (ale == 0)
			Assets.fondo = Assets.fondo1;
		else if (ale == 1)
			Assets.fondo = Assets.fondo2;
		else if (ale == 2)
			Assets.fondo = Assets.fondo3;

	}

	@Override
	public void render(float delta) {
		if (delta > .1f)
			delta = .1f;

		update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		draw(delta);

		stage.act(delta);
		stage.draw();
	}

	Image blackFadeOut;

	public void changeScreenWithFadeOut(final Class<?> newScreen,
			final MainClumsy game) {
		blackFadeOut = new Image(Assets.negro);
		blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		blackFadeOut.getColor().a = 0;
		blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						if (newScreen == GameScreenClassic.class)
							game.setScreen(new GameScreenClassic(game));
						else if (newScreen == MainMenuScreen.class)
							game.setScreen(new MainMenuScreen(game));
						else if (newScreen == GameScreenArcade.class)
							game.setScreen(new GameScreenArcade(game));

						// El blackFadeOut se remueve del stage cuando se le da
						// new Screens(game)
						// "Revisar el constructor de la clase Screens" por lo
						// que no hay necesidad de hacer blackFadeout.remove();
					}
				})));
		stage.addActor(blackFadeOut);
	}

	public void drawPuntuacionGrande(float x, float y, int puntuacion) {
		String score = String.valueOf(puntuacion);

		int len = score.length();
		float charWidth = 42;
		float textWidth = len * charWidth;
		for (int i = 0; i < len; i++) {
			AtlasRegion keyFrame;

			char character = score.charAt(i);

			if (character == '0') {
				keyFrame = Assets.num0Grande;

			}
			else if (character == '1') {
				keyFrame = Assets.num1Grande;
			}
			else if (character == '2') {
				keyFrame = Assets.num2Grande;
			}
			else if (character == '3') {
				keyFrame = Assets.num3Grande;
			}
			else if (character == '4') {
				keyFrame = Assets.num4Grande;
			}
			else if (character == '5') {
				keyFrame = Assets.num5Grande;
			}
			else if (character == '6') {
				keyFrame = Assets.num6Grande;
			}
			else if (character == '7') {
				keyFrame = Assets.num7Grande;
			}
			else if (character == '8') {
				keyFrame = Assets.num8Grande;
			}
			else {// 9
				keyFrame = Assets.num9Grande;
			}

			batcher.draw(keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f,
					y, charWidth, 64);

		}

	}

	public void drawPuntuacionGrandeSinCentrar(float x, float y, int puntuacion) {
		String score = String.valueOf(puntuacion);

		int len = score.length();
		float charWidth = 42;
		float textWidth = 0;
		for (int i = 0; i < len; i++) {
			AtlasRegion keyFrame;

			char character = score.charAt(i);

			if (character == '0') {
				keyFrame = Assets.num0Grande;

			}
			else if (character == '1') {
				keyFrame = Assets.num1Grande;
			}
			else if (character == '2') {
				keyFrame = Assets.num2Grande;
			}
			else if (character == '3') {
				keyFrame = Assets.num3Grande;
			}
			else if (character == '4') {
				keyFrame = Assets.num4Grande;
			}
			else if (character == '5') {
				keyFrame = Assets.num5Grande;
			}
			else if (character == '6') {
				keyFrame = Assets.num6Grande;
			}
			else if (character == '7') {
				keyFrame = Assets.num7Grande;
			}
			else if (character == '8') {
				keyFrame = Assets.num8Grande;
			}
			else {// 9
				keyFrame = Assets.num9Grande;
			}

			batcher.draw(keyFrame, x + textWidth, y, charWidth, 64);
			textWidth += charWidth;

		}

	}

	public void drawPuntuacionChicoOrigenDerecha(float x, float y,
			int puntuacion) {
		String score = String.valueOf(puntuacion);

		int len = score.length();
		float charWidth = 22;
		float textWidth = 0;
		for (int i = len - 1; i >= 0; i--) {
			AtlasRegion keyFrame;

			charWidth = 22;
			char character = score.charAt(i);

			if (character == '0') {
				keyFrame = Assets.num0Chico;
			}
			else if (character == '1') {
				keyFrame = Assets.num1Chico;
				charWidth = 11f;
			}
			else if (character == '2') {
				keyFrame = Assets.num2Chico;
			}
			else if (character == '3') {
				keyFrame = Assets.num3Chico;
			}
			else if (character == '4') {
				keyFrame = Assets.num4Chico;
			}
			else if (character == '5') {
				keyFrame = Assets.num5Chico;
			}
			else if (character == '6') {
				keyFrame = Assets.num6Chico;
			}
			else if (character == '7') {
				keyFrame = Assets.num7Chico;
			}
			else if (character == '8') {
				keyFrame = Assets.num8Chico;
			}
			else {// 9
				keyFrame = Assets.num9Chico;
			}
			textWidth += charWidth;
			batcher.draw(keyFrame, x - textWidth, y, charWidth, 32);

		}

	}

	public abstract void draw(float delta);

	public abstract void update(float delta);

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		Settings.guardar();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
		batcher.dispose();
	}

}
