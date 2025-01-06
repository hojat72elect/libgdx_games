package com.bms.chemtris;

import com.badlogic.gdx.Game;
import com.bms.chemtris.game.ScoreManager;
import com.bms.chemtris.screen.Splash;

public class ChemtrisGame extends Game {
	
	@Override
	public void create () {
		ScoreManager.setup();
		setScreen(new Splash());
	}

	@Override
	public void render () {
		//render super class
		super.render();

	}

}
