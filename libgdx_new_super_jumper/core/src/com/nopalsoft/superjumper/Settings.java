package com.nopalsoft.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public final static int NUM_GEMS_SHARE_FACEBOOK = 250;
	public final static int NUM_GEMS_INVITE_FACEBOOK = 50;

	public static boolean isMusicOn;
	public static boolean isSoundOn;

	public static boolean didBuyNoAds;
	public static boolean didLikeFacebook;
	public static boolean didRate;

	public static int numeroVecesJugadas;

	public static int coinsTotal;
	public static int numBullets;

	public static int bestScore;

	public static int LEVEL_LIFE;
	public static int LEVEL_SHIELD;
	public static int LEVEL_SECOND_JUMP;
	public static int LEVEL_WEAPON;

	private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.superjumper");

	public static void save() {

		pref.putBoolean("isMusicOn", isMusicOn);
		pref.putBoolean("isSoundOn", isSoundOn);

		pref.putBoolean("didBuyNoAds", didBuyNoAds);
		pref.putBoolean("didLikeFacebook", didLikeFacebook);
		pref.putBoolean("didRate", didRate);

		pref.putInteger("numeroVecesJugadas", numeroVecesJugadas);
		pref.putInteger("coinsTotal", coinsTotal);
		pref.putInteger("numBullets", numBullets);
		pref.putInteger("bestScore", bestScore);

		pref.putInteger("LEVEL_WEAPON", LEVEL_WEAPON);
		pref.putInteger("LEVEL_SECOND_JUMP", LEVEL_SECOND_JUMP);
		pref.putInteger("LEVEL_LIFE", LEVEL_LIFE);
		pref.putInteger("LEVEL_SHIELD", LEVEL_SHIELD);

		pref.flush();

	}

	public static void load() {

		isMusicOn = pref.getBoolean("isMusicOn", true);
		isSoundOn = pref.getBoolean("isSoundOn", true);

		didBuyNoAds = pref.getBoolean("didBuyNoAds", false);
		didLikeFacebook = pref.getBoolean("didLikeFacebook", false);
		didRate = pref.getBoolean("didRate", false);

		numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0);

		coinsTotal = pref.getInteger("coinsTotal", 0);
		numBullets = pref.getInteger("numBullets", 30);
		bestScore = pref.getInteger("bestScore", 0);

		LEVEL_WEAPON = pref.getInteger("LEVEL_WEAPON", 0);
		LEVEL_SECOND_JUMP = pref.getInteger("LEVEL_SECOND_JUMP", 0);
		LEVEL_LIFE = pref.getInteger("LEVEL_LIFE", 0);
		LEVEL_SHIELD = pref.getInteger("LEVEL_SHIELD", 0);

	}

	public static void setBestScore(int distance) {
		if (bestScore < distance) {
			bestScore = distance;
			save();
		}

	}

}
