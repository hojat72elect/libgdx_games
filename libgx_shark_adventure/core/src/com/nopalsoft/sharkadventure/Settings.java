package com.nopalsoft.sharkadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    public static long numberOfTimesPlayed = 0;
    public static long bestScore;

    public static boolean isMusicOn = true;
    public static boolean isSoundOn = true;

    protected static boolean didRate = false;
    public static boolean didBuyNoAds = false;

    private final static Preferences preferences = Gdx.app.getPreferences("com.nopalsoft.sharkadventure");

    public static void save() {
        preferences.putBoolean("isMusicOn", isMusicOn);
        preferences.putBoolean("isSoundOn", isSoundOn);

        preferences.putBoolean("didBuyNoAds", didBuyNoAds);
        preferences.putBoolean("didRate", didRate);

        preferences.putLong("numVecesJugadas", numberOfTimesPlayed);
        preferences.putLong("bestScore", bestScore);
        preferences.flush();
    }

    public static void load() {
        isMusicOn = preferences.getBoolean("isMusicOn", true);
        isSoundOn = preferences.getBoolean("isSoundOn", true);

        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false);
        didRate = preferences.getBoolean("didRate", false);

        numberOfTimesPlayed = preferences.getLong("numVecesJugadas", 0);

        bestScore = preferences.getLong("bestScore", 0);
    }

    public static void setBestScore(long score) {
        if (score > bestScore) {
            bestScore = score;
            save();
        }
    }
}
