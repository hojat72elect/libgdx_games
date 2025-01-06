package com.tetris.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.tetris.game.helpers.AssetLoader;
import com.tetris.game.helpers.ButtonLoader;
import com.tetris.screens.Menu;

public class Tetris extends Game {
	AssetLoader assets;
	ButtonLoader buttonLoader;
	Button music, sound;
	
	@Override
	public void create () {
		assets = new AssetLoader();
		buttonLoader = new ButtonLoader(assets);
		music = buttonLoader.getMusic();
		sound = buttonLoader.getSound();
		setScreen(new Menu(this,assets,buttonLoader,music,sound));
	}

	@Override
	public void render () {
		super.render();
	}
}
