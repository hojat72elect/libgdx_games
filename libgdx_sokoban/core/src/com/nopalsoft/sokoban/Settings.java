package com.nopalsoft.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.sokoban.objetos.Level;

public class Settings {

	public static boolean isTest = true;

	public static boolean animationWalkIsON;

	public static int NUM_MAPS = 62;
	public static Level[] arrLevel;// Cada posicion es un nivel

	private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.sokoban");

	public static void load() {
		arrLevel = new Level[NUM_MAPS];

		animationWalkIsON = pref.getBoolean("animationWalkIsON", false);

		for (int i = 0; i < NUM_MAPS; i++) {
			arrLevel[i] = new Level();
			arrLevel[i].numStars = pref.getInteger("numStars" + i, 0);
			arrLevel[i].bestMoves = pref.getInteger("bestMoves" + i, 0);
			arrLevel[i].bestTime = pref.getInteger("bestTime" + i, 0);
		}

	}

	public static void save() {

		pref.putBoolean("animationWalkIsON", animationWalkIsON);
		pref.flush();

	}

	public static void levelCompeted(int level, int moves, int time) {

		arrLevel[level].numStars = 1;
		arrLevel[level].bestMoves = moves;
		arrLevel[level].bestTime = time;

		pref.putInteger("numStars" + level, arrLevel[level].numStars);
		pref.putInteger("bestMoves" + level, arrLevel[level].bestMoves);
		pref.putInteger("bestTime" + level, arrLevel[level].bestTime);

		pref.flush();
	}

}
