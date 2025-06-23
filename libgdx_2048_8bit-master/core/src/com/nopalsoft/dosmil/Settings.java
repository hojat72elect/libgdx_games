package com.nopalsoft.dosmil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public static boolean isMusicOn;
	public static boolean isSoundOn;
	public static boolean didBuyNoAds;
	public static int numeroVecesJugadas;
	public static long bestScore;

	private final static Preferences pref = Gdx.app
			.getPreferences("com.tiar.dosmil");

	public static void load() {

		bestScore = pref.getLong("bestScore", 0);
		numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0);

		didBuyNoAds = pref.getBoolean("didBuyNoAds", false);
		isMusicOn = pref.getBoolean("isMusicOn", true);
		isSoundOn = pref.getBoolean("isSoundOn", true);
	}

	public static void save() {
		pref.putLong("bestScore", bestScore);
		pref.putInteger("numeroVecesJugadas", numeroVecesJugadas);
		pref.putBoolean("didBuyNoAds", didBuyNoAds);
		pref.putBoolean("isMusicOn", isMusicOn);
		pref.putBoolean("isSoundOn", isSoundOn);
		pref.flush();

	}

	public static void setBestScores(long score) {
		if (bestScore < score)
			bestScore = score;
		save();

	}

}
