package com.nopalsoft.sharkadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public static long numVecesJugadas = 0;
	public static long bestScore;

	public static boolean isMusicOn = true;
	public static boolean isSoundOn = true;

	protected static boolean didRate = false;
	public static boolean didBuyNoAds = false;

	private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.sharkadventure");

	public static void save() {
		pref.putBoolean("isMusicOn", isMusicOn);
		pref.putBoolean("isSoundOn", isSoundOn);

		pref.putBoolean("didBuyNoAds", didBuyNoAds);
		pref.putBoolean("didRate", didRate);

		pref.putLong("numVecesJugadas", numVecesJugadas);
		pref.putLong("bestScore", bestScore);
		pref.flush();

	}

	public static void load() {
		isMusicOn = pref.getBoolean("isMusicOn", true);
		isSoundOn = pref.getBoolean("isSoundOn", true);

		didBuyNoAds = pref.getBoolean("didBuyNoAds", false);
		didRate = pref.getBoolean("didRate", false);

		numVecesJugadas = pref.getLong("numVecesJugadas", 0);

		bestScore = pref.getLong("bestScore", 0);

	}

	public static void setBestScore(long score) {
		if (score > bestScore) {
			bestScore = score;
			save();
		}
	}

}
