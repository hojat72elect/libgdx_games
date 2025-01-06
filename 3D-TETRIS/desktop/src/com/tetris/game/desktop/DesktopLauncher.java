package com.tetris.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tetris.game.Tetris;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "3D TETRIS";
		config.addIcon("Pictures/icon.png", Files.FileType.Internal);
		config.resizable = false;
		new LwjglApplication(new Tetris(), config);
	}
}
