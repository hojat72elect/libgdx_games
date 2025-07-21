package com.nopalsoft.clumsy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    private final static Preferences preferences = Gdx.app.getPreferences("com.nopalsoft.clumsyufo");
    public static boolean didBuyNoAds = false;
    public static int bestScoreArcade = 0;
    public static int bestScoreClassic = 0;
    public static int numberOfTimesPlayed = 0;

    public static void load() {
        bestScoreArcade = preferences.getInteger("bestScoreArcade", 0);
        bestScoreClassic = preferences.getInteger("bestScoreClassic", 0);
        numberOfTimesPlayed = preferences.getInteger("numVecesJugadas", 0);
        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false);
    }

    public static void save() {
        preferences.putInteger("bestScoreArcade", bestScoreArcade);
        preferences.putInteger("bestScoreClassic", bestScoreClassic);
        preferences.putInteger("numVecesJugadas", numberOfTimesPlayed);
        preferences.putBoolean("didBuyNoAds", didBuyNoAds);
        preferences.flush();
    }
}
