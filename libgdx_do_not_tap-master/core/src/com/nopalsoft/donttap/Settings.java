package com.nopalsoft.donttap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public static int numeroVecesJugadas = 0;
	public static float bestTimeClassicMode = 100100;// Default en segundos
	public static int bestScoreTimeMode = 0;
	public static int bestScoreEndlessMode = 0;
	public static boolean isSoundEnabled = true;

	private final static Preferences pref = Gdx.app
			.getPreferences("com.nopalsoft.donttap");

	public static void save() {
		pref.putInteger("numeroVecesJugadas", numeroVecesJugadas);
		pref.putFloat("bestTimeClassicMode", bestTimeClassicMode);
		pref.putInteger("bestScoreTimeMode", bestScoreTimeMode);
		pref.putInteger("bestScoreEndlessMode", bestScoreEndlessMode);
		pref.putBoolean("isSoundEnabled", isSoundEnabled);
		pref.flush();

	}

	public static void load() {
		numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0);
		bestTimeClassicMode = pref.getFloat("bestTimeClassicMode", 100100);
		bestScoreTimeMode = pref.getInteger("bestScoreTimeMode", 0);
		bestScoreEndlessMode = pref.getInteger("bestScoreEndlessMode", 0);
		isSoundEnabled = pref.getBoolean("isSoundEnabled", true);

	}

}
