package com.nopalsoft.zombiedash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nopalsoft.zombiedash.AnimationSprite;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.MainZombieDash;
import com.nopalsoft.zombiedash.Settings;
import com.nopalsoft.zombiedash.game.GameScreen;
import com.nopalsoft.zombiedash.objects.Hero;
import com.nopalsoft.zombiedash.scene2d.AnimatedSpriteActor;

public abstract class Screens extends InputAdapter implements Screen {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 480;

	public static final float WORLD_WIDTH = 8;
	public static final float WORLD_HEIGHT = 4.8f;

	public MainZombieDash game;

	public OrthographicCamera oCam;
	public SpriteBatch batcher;
	public Stage stage;

	protected Music music;

	public Screens(MainZombieDash game) {
		this.stage = game.stage;
		this.stage.clear();
		this.batcher = game.batcher;
		this.game = game;

		oCam = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

		InputMultiplexer input = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(input);

	}

	@Override
	public void render(float delta) {
		if (delta > .1f)
			delta = .1f;

		update(delta);
		stage.act(delta);

		oCam.update();
		batcher.setProjectionMatrix(oCam.combined);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		draw(delta);
		stage.draw();
	}

	public void addEfectoPress(final Actor actor) {
		actor.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				actor.setPosition(actor.getX(), actor.getY() - 5);
				event.stop();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				actor.setPosition(actor.getX(), actor.getY() + 5);
			}
		});
	}

	Image blackFadeOut;

	public void changeScreenWithFadeOut(final Class<?> newScreen, final MainZombieDash game) {
		blackFadeOut = new Image(Assets.pixelNegro);
		blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		blackFadeOut.getColor().a = 0;
		blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run(new Runnable() {
			@Override
			public void run() {

				if (newScreen == GameScreen.class) {
					game.setScreen(new GameScreen(game));
				}
				else if (newScreen == MainMenuScreen.class)
					game.setScreen(new MainMenuScreen(game));
				else if (newScreen == SettingsScreen.class)
					game.setScreen(new SettingsScreen(game));

				// El blackFadeOut se remueve del stage cuando se le da new Screens(game) "Revisar el constructor de la clase Screens" por lo que no hay necesidad de hacer
				// blackFadeout.remove();
			}
		})));

		Label lbl = new Label(game.idiomas.get("loading"), Assets.labelStyleGrande);
		lbl.setPosition(SCREEN_WIDTH / 2f - lbl.getWidth() / 2f, SCREEN_HEIGHT / 2f - lbl.getHeight() / 2f);
		lbl.getColor().a = 0;
		lbl.addAction(Actions.fadeIn(.6f));

		AnimationSprite heroRun = null;
		switch (Settings.skinSeleccionada) {
		case Hero.TIPO_FORCE:
			heroRun = Assets.heroForceRun;
			break;
		case Hero.TIPO_RAMBO:
			heroRun = Assets.heroRamboRun;
			break;
		case Hero.TIPO_SOLDIER:
			heroRun = Assets.heroSoldierRun;
			break;
		case Hero.TIPO_SWAT:
			heroRun = Assets.heroSwatRun;
			break;
		case Hero.TIPO_VADER:
			heroRun = Assets.heroVaderRun;
			break;
		}

		AnimatedSpriteActor corriendo = new AnimatedSpriteActor(heroRun);
		corriendo.setSize(70, 70);
		corriendo.setPosition(SCREEN_WIDTH / 2f - corriendo.getWidth() / 2f, 250);

		stage.addActor(blackFadeOut);
		stage.addActor(corriendo);
		stage.addActor(lbl);

	}

	public abstract void update(float delta);

	public abstract void draw(float delta);

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		if (music != null) {
			music.stop();
			music.dispose();
			music = null;
		}

		Settings.save();
		Settings.save();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batcher.dispose();
	}

}
