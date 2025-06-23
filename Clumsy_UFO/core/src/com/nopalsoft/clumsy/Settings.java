package com.nopalsoft.clumsy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	/**
	 * Se modifica esta variable para modificar cada cuantos juegos se muestra el interstitial
	 */
	public static int MOSTRAR_ADD_CADA_N_INTENTOS = 10;

	public static boolean didBuyNoAds = false;

	public static int bestScoreArcade = 0;
	public static int bestScoreClassic = 0;
	public static int numVecesJugadas = 0;

	private final static String prefName = "com.nopalsoft.clumsyufo";
	private final static Preferences pref = Gdx.app.getPreferences(prefName);

	public static void load() {
		bestScoreArcade = pref.getInteger("bestScoreArcade", 0);
		bestScoreClassic = pref.getInteger("bestScoreClassic", 0);
		numVecesJugadas = pref.getInteger("numVecesJugadas", 0);
		didBuyNoAds = pref.getBoolean("didBuyNoAds", false);
	}

	public static void guardar() {
		pref.putInteger("bestScoreArcade", bestScoreArcade);
		pref.putInteger("bestScoreClassic", bestScoreClassic);
		pref.putInteger("numVecesJugadas", numVecesJugadas);
		pref.putBoolean("didBuyNoAds", didBuyNoAds);
		pref.flush();
	}

}
