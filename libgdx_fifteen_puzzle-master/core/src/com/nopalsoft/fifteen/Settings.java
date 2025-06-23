package com.nopalsoft.fifteen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public static int bestTime;
	public static int bestMoves;
	public static boolean didBuyNoAds;
	public static boolean isMusicOn;
	public static boolean isSoundOn;
	public static int numeroVecesJugadas;

	private final static Preferences pref = Gdx.app
			.getPreferences("com.tiar.fifteen");

	public static void load() {

		bestTime = pref.getInteger("bestTime", Integer.MAX_VALUE);
		bestMoves = pref.getInteger("bestMoves", Integer.MAX_VALUE);
		numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0);

		didBuyNoAds = pref.getBoolean("didBuyNoAds", false);
		isMusicOn = pref.getBoolean("isMusicOn", true);
		isSoundOn = pref.getBoolean("isSoundOn", true);
	}

	public static void save() {
		pref.putInteger("bestTime", bestTime);
		pref.putInteger("bestMoves", bestMoves);
		pref.putInteger("numeroVecesJugadas", numeroVecesJugadas);
		pref.putBoolean("didBuyNoAds", didBuyNoAds);
		pref.putBoolean("isMusicOn", isMusicOn);
		pref.putBoolean("isSoundOn", isSoundOn);
		pref.flush();

	}

	public static void setBestScores(int time, int moves) {
		if (time < bestTime)
			bestTime = time;
		if (moves < bestMoves)
			bestMoves = moves;
		save();
	}
}
