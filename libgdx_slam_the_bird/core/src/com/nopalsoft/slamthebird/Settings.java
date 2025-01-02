package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public static int currentCoins = 0;
	public static int bestScore;
	public static int selectedSkin = 0;
	public static int numTimesPlayed = 0;

	public static int BOOST_TIME = 0;
	public static int BOOST_ICE = 0;
	public static int BOOST_COINS = 0;
	public static int BOOST_INVINCIBLE = 0;
	public static int BOOST_SUPER_JUMP = 0;

	public static boolean isMusicOn;
	public static boolean isSoundOn;
	public static boolean seCalifico;

	private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.slamthebird");

	public static void save() {
		pref.putInteger("monedasActuales", currentCoins);
		pref.putInteger("bestScore", bestScore);
		pref.putInteger("skinSeleccionada", selectedSkin);
		pref.putInteger("numeroVecesJugadas", numTimesPlayed);
		pref.putInteger("NIVEL_BOOST_BOOST_TIME", BOOST_TIME);
		pref.putInteger("NIVEL_BOOST_ICE", BOOST_ICE);
		pref.putInteger("NIVEL_BOOST_MONEDAS", BOOST_COINS);
		pref.putInteger("NIVEL_BOOST_INVENCIBLE", BOOST_INVINCIBLE);
		pref.putInteger("NIVEL_BOOST_SUPER_JUMP", BOOST_SUPER_JUMP);

		pref.putBoolean("isMusicOn", isMusicOn);
		pref.putBoolean("isSoundOn", isSoundOn);

		pref.putBoolean("seCalifico", seCalifico);
		pref.flush();

	}

	public static void load() {
		currentCoins = pref.getInteger("monedasActuales", 0);
		bestScore = pref.getInteger("bestScore", 0);
		selectedSkin = pref.getInteger("skinSeleccionada", 0);
		numTimesPlayed = pref.getInteger("numeroVecesJugadas", 0);
		BOOST_TIME = pref.getInteger("NIVEL_BOOST_BOOST_TIME", 0);
		BOOST_ICE = pref.getInteger("NIVEL_BOOST_ICE", 0);
		BOOST_COINS = pref.getInteger("NIVEL_BOOST_MONEDAS", 0);
		BOOST_INVINCIBLE = pref.getInteger("NIVEL_BOOST_INVENCIBLE", 0);
		BOOST_SUPER_JUMP = pref.getInteger("NIVEL_BOOST_SUPER_JUMP", 0);

		isMusicOn = pref.getBoolean("isMusicOn", true);
		isSoundOn = pref.getBoolean("isSoundOn", true);

		seCalifico = pref.getBoolean("seCalifico", false);

	}

	public static void setBestScores(int score) {
		if (bestScore < score)
			bestScore = score;
		save();

	}

}
