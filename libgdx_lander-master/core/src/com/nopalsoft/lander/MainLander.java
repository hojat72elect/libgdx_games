package com.nopalsoft.lander;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.nopalsoft.lander.screens.MainMenuScreen;

public class MainLander extends Game {

	public Stage stage;
	public SpriteBatch batcher;

	@Override
	public void create() {
		Assets.cargar();
		stage = new Stage();
		batcher = new SpriteBatch();

		// setScreen(new GameScreen(this, 1));
		setScreen(new MainMenuScreen(this));
	}

}