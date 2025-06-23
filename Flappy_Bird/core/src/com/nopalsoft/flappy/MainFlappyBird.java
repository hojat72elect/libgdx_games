package com.nopalsoft.flappy;

import com.badlogic.gdx.Game;
import com.nopalsoft.flappy.game.GameScreen;

public class MainFlappyBird extends Game {

	@Override
	public void create() {
		Assets.load();
		setScreen(new GameScreen(this));
	}
}
