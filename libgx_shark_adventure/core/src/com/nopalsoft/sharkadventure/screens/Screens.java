package com.nopalsoft.sharkadventure.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.nopalsoft.sharkadventure.MainShark;
import com.nopalsoft.sharkadventure.Settings;

public abstract class Screens extends InputAdapter implements Screen {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 480;

	public static final float WORLD_WIDTH = 8f;
	public static final float WORLD_HEIGHT = 4.8f;

	public MainShark game;
	public OrthographicCamera oCam;
	public SpriteBatch batcher;

	public Stage stage;

	public Screens(MainShark game) {
		this.game = game;
		stage = game.stage;
		stage.clear();
		batcher = game.batcher;

		oCam = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

		InputMultiplexer input = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render(float delta) {

		update(delta);
		stage.act(delta);

		oCam.update();
		batcher.setProjectionMatrix(oCam.combined);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		draw(delta);
		stage.draw();

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
		Settings.save();
	}

	@Override
	public void pause() {
		Settings.save();

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
