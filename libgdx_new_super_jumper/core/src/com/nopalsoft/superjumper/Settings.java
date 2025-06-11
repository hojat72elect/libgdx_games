package com.nopalsoft.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    public static boolean isMusicOn;
    public static boolean isSoundOn;

    public static boolean didBuyNoAds;
    public static boolean didLikeFacebook;
    public static boolean didRate;

    private final static Preferences preferences = Gdx.app.getPreferences("com.nopalsoft.superjumper");

    public static int coinsTotal;
    public static int numBullets;

    public static int bestScore;

    public static int LEVEL_LIFE;
    public static int LEVEL_SHIELD;
    public static int LEVEL_SECOND_JUMP;
    public static int LEVEL_WEAPON;
    public static int numTimesPlayed;

    public static void save() {

        preferences.putBoolean("isMusicOn", isMusicOn);
        preferences.putBoolean("isSoundOn", isSoundOn);

        preferences.putBoolean("didBuyNoAds", didBuyNoAds);
        preferences.putBoolean("didLikeFacebook", didLikeFacebook);
        preferences.putBoolean("didRate", didRate);

        preferences.putInteger("numeroVecesJugadas", numTimesPlayed);
        preferences.putInteger("coinsTotal", coinsTotal);
        preferences.putInteger("numBullets", numBullets);
        preferences.putInteger("bestScore", bestScore);

        preferences.putInteger("LEVEL_WEAPON", LEVEL_WEAPON);
        preferences.putInteger("LEVEL_SECOND_JUMP", LEVEL_SECOND_JUMP);
        preferences.putInteger("LEVEL_LIFE", LEVEL_LIFE);
        preferences.putInteger("LEVEL_SHIELD", LEVEL_SHIELD);

        preferences.flush();
    }

    public static void load() {

        isMusicOn = preferences.getBoolean("isMusicOn", true);
        isSoundOn = preferences.getBoolean("isSoundOn", true);

        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false);
        didLikeFacebook = preferences.getBoolean("didLikeFacebook", false);
        didRate = preferences.getBoolean("didRate", false);

        numTimesPlayed = preferences.getInteger("numeroVecesJugadas", 0);

        coinsTotal = preferences.getInteger("coinsTotal", 0);
        numBullets = preferences.getInteger("numBullets", 30);
        bestScore = preferences.getInteger("bestScore", 0);

        LEVEL_WEAPON = preferences.getInteger("LEVEL_WEAPON", 0);
        LEVEL_SECOND_JUMP = preferences.getInteger("LEVEL_SECOND_JUMP", 0);
        LEVEL_LIFE = preferences.getInteger("LEVEL_LIFE", 0);
        LEVEL_SHIELD = preferences.getInteger("LEVEL_SHIELD", 0);
    }

    public static void setBestScore(int distance) {
        if (bestScore < distance) {
            bestScore = distance;
            save();
        }
    }
}
