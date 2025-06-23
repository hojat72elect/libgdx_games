package com.nopalsoft.zombiedash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.zombiedash.objects.Hero;
import com.nopalsoft.zombiedash.screens.SettingsScreen;

public class Settings {

	public final static int NUM_GEMS_SHARE_FACEBOOK = 250;
	public final static int NUM_GEMS_INVITE_FACEBOOK = 50;

	public final static boolean isTest = false;
	public static boolean isMusicOn;
	public static boolean isSoundOn;
	public static boolean showHelp;

	public static boolean didBuyNoAds;
	public static boolean didLikeFacebook;
	public static boolean didRate;

	public static float buttonSize;
	public static float buttonFirePositionX;
	public static float buttonFirePositionY;
	public static float buttonJumpPositionX;
	public static float buttonJumpPositionY;

	public static int numeroVecesJugadas;
	public static int skinSeleccionada;
	public static int gemsTotal;
	public static int numBullets;

	public static int bestScore;

	public static int LEVEL_LIFE;
	public static int LEVEL_SHIELD;
	public static int LEVEL_SECOND_JUMP;
	public static int LEVEL_WEAPON;

	private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.zombiedash");

	public static void save() {

		pref.putBoolean("isMusicOn", isMusicOn);
		pref.putBoolean("isSoundOn", isSoundOn);

		pref.putBoolean("didBuyNoAds", didBuyNoAds);
		pref.putBoolean("didLikeFacebook", didLikeFacebook);
		pref.putBoolean("didRate", didRate);
		pref.putBoolean("showHelp", showHelp);

		pref.putFloat("buttonSize", buttonSize);
		pref.putFloat("buttonFirePositionX", buttonFirePositionX);
		pref.putFloat("buttonFirePositionY", buttonFirePositionY);
		pref.putFloat("buttonJumpPositionX", buttonJumpPositionX);
		pref.putFloat("buttonJumpPositionY", buttonJumpPositionY);

		pref.putInteger("numeroVecesJugadas", numeroVecesJugadas);
		pref.putInteger("skinSeleccionada", skinSeleccionada);
		pref.putInteger("gemsTotal", gemsTotal);
		pref.putInteger("numBullets", numBullets);
		pref.putInteger("bestScore", bestScore);

		pref.putInteger("LEVEL_WEAPON", LEVEL_WEAPON);
		pref.putInteger("LEVEL_SECOND_JUMP", LEVEL_SECOND_JUMP);
		pref.putInteger("LEVEL_LIFE", LEVEL_LIFE);
		pref.putInteger("LEVEL_SHIELD", LEVEL_SHIELD);

		pref.flush();

	}

	public static void load() {
		if (isTest) {
			pref.clear();
			pref.flush();
		}

		isMusicOn = pref.getBoolean("isMusicOn", true);
		isSoundOn = pref.getBoolean("isSoundOn", true);

		didBuyNoAds = pref.getBoolean("didBuyNoAds", false);
		didLikeFacebook = pref.getBoolean("didLikeFacebook", false);
		didRate = pref.getBoolean("didRate", false);
		showHelp = pref.getBoolean("showHelp", true);

		buttonSize = pref.getFloat("buttonSize", SettingsScreen.DEFAULT_SIZE_BUTTONS);
		buttonFirePositionX = pref.getFloat("buttonFirePositionX", SettingsScreen.DEFAULT_POSITION_BUTTON_FIRE.x);
		buttonFirePositionY = pref.getFloat("buttonFirePositionY", SettingsScreen.DEFAULT_POSITION_BUTTON_FIRE.y);
		buttonJumpPositionX = pref.getFloat("buttonJumpPositionX", SettingsScreen.DEFAULT_POSITION_BUTTON_JUMP.x);
		buttonJumpPositionY = pref.getFloat("buttonJumpPositionY", SettingsScreen.DEFAULT_POSITION_BUTTON_JUMP.y);

		numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0);
		skinSeleccionada = pref.getInteger("skinSeleccionada", Hero.TIPO_SWAT);
		gemsTotal = pref.getInteger("gemsTotal", 0);
		numBullets = pref.getInteger("numBullets", 120);
		bestScore = pref.getInteger("bestScore", 0);

		LEVEL_WEAPON = pref.getInteger("LEVEL_WEAPON", 0);
		LEVEL_SECOND_JUMP = pref.getInteger("LEVEL_SECOND_JUMP", 0);
		LEVEL_LIFE = pref.getInteger("LEVEL_LIFE", 0);
		LEVEL_SHIELD = pref.getInteger("LEVEL_SHIELD", 0);

		if (isTest) {
			gemsTotal += 500000;
			numBullets += 50;
		}
	}

	public static void saveNewButtonFireSettings(float x, float y, float size) {
		buttonSize = size;
		buttonFirePositionX = x;
		buttonFirePositionY = y;
		save();
	}

	public static void saveNewButtonJumpSettings(float x, float y, float size) {
		buttonSize = size;
		buttonJumpPositionX = x;
		buttonJumpPositionY = y;
		save();
	}

	public static void setBestScore(int distance) {
		if (bestScore < distance) {
			bestScore = distance;
			save();
		}

	}

}
