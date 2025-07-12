package com.nopalsoft.dosmil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    public static boolean isMusicOn;
    public static boolean isSoundOn;
    public static boolean didBuyNoAds;
    public static int numberOfTimesPlayed;
    public static long bestScore;

    private final static Preferences preferences = Gdx.app.getPreferences("com.tiar.dosmil");

    public static void load() {

        bestScore = preferences.getLong("bestScore", 0);
        numberOfTimesPlayed = preferences.getInteger("numeroVecesJugadas", 0);

        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false);
        isMusicOn = preferences.getBoolean("isMusicOn", true);
        isSoundOn = preferences.getBoolean("isSoundOn", true);
    }

    public static void save() {
        preferences.putLong("bestScore", bestScore);
        preferences.putInteger("numeroVecesJugadas", numberOfTimesPlayed);
        preferences.putBoolean("didBuyNoAds", didBuyNoAds);
        preferences.putBoolean("isMusicOn", isMusicOn);
        preferences.putBoolean("isSoundOn", isSoundOn);
        preferences.flush();
    }

    public static void setBestScores(long score) {
        if (bestScore < score)
            bestScore = score;
        save();
    }
}
