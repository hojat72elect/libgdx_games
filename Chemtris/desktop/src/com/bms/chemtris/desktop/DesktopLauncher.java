package com.bms.chemtris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bms.chemtris.ChemtrisGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Chemtris";
		//config.width = 1000;
		//config.height = 800;
		new LwjglApplication(new ChemtrisGame(), config);
	}
}
