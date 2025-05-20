package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public static int monedasActuales = 0;
	public static int bestScore;
	public static int skinSeleccionada = 0;
	public static int numeroVecesJugadas = 0;

	public static int NIVEL_BOOST_BOOST_TIME = 0;
	public static int NIVEL_BOOST_ICE = 0;
	public static int NIVEL_BOOST_MONEDAS = 0;
	public static int NIVEL_BOOST_INVENCIBLE = 0;
	public static int NIVEL_BOOST_SUPER_JUMP = 0;

	public static boolean isMusicOn;
	public static boolean isSoundOn;

	public static boolean didBuyNoAds;
	public static boolean didLikeFacebook;
	public static boolean seCalifico;

	private final static Preferences pref = Gdx.app
			.getPreferences("com.nopalsoft.slamthebird");

	public static void save() {
		pref.putInteger("monedasActuales", monedasActuales);
		pref.putInteger("bestScore", bestScore);
		pref.putInteger("skinSeleccionada", skinSeleccionada);
		pref.putInteger("numeroVecesJugadas", numeroVecesJugadas);
		pref.putInteger("NIVEL_BOOST_BOOST_TIME", NIVEL_BOOST_BOOST_TIME);
		pref.putInteger("NIVEL_BOOST_ICE", NIVEL_BOOST_ICE);
		pref.putInteger("NIVEL_BOOST_MONEDAS", NIVEL_BOOST_MONEDAS);
		pref.putInteger("NIVEL_BOOST_INVENCIBLE", NIVEL_BOOST_INVENCIBLE);
		pref.putInteger("NIVEL_BOOST_SUPER_JUMP", NIVEL_BOOST_SUPER_JUMP);

		pref.putBoolean("isMusicOn", isMusicOn);
		pref.putBoolean("isSoundOn", isSoundOn);

		pref.putBoolean("didBuyNoAds", didBuyNoAds);
		pref.putBoolean("didLikeFacebook", didLikeFacebook);
		pref.putBoolean("seCalifico", seCalifico);
		pref.flush();

	}

	public static void load() {
		monedasActuales = pref.getInteger("monedasActuales", 0);
		bestScore = pref.getInteger("bestScore", 0);
		skinSeleccionada = pref.getInteger("skinSeleccionada", 0);
		numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0);
		NIVEL_BOOST_BOOST_TIME = pref.getInteger("NIVEL_BOOST_BOOST_TIME", 0);
		NIVEL_BOOST_ICE = pref.getInteger("NIVEL_BOOST_ICE", 0);
		NIVEL_BOOST_MONEDAS = pref.getInteger("NIVEL_BOOST_MONEDAS", 0);
		NIVEL_BOOST_INVENCIBLE = pref.getInteger("NIVEL_BOOST_INVENCIBLE", 0);
		NIVEL_BOOST_SUPER_JUMP = pref.getInteger("NIVEL_BOOST_SUPER_JUMP", 0);

		isMusicOn = pref.getBoolean("isMusicOn", true);
		isSoundOn = pref.getBoolean("isSoundOn", true);

		didBuyNoAds = pref.getBoolean("didBuyNoAds", false);
		didLikeFacebook = pref.getBoolean("didLikeFacebook", false);
		seCalifico = pref.getBoolean("seCalifico", false);

	}

	public static void setBestScores(int score) {
		if (bestScore < score)
			bestScore = score;
		save();

	}

}
