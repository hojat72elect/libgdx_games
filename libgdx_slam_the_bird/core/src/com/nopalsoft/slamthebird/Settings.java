package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    public static int currentCoins = 0;
    public static int bestScore;
    public static int selectedSkin = 0;
    public static int playCount = 0;

    public static int BOOST_DURATION = 0;
    public static int BOOST_FREEZE = 0;
    public static int BOOST_COINS = 0;
    public static int BOOST_INVINCIBLE = 0;
    public static int BOOST_SUPER_JUMP = 0;

    public static boolean isMusicOn;
    public static boolean isSoundOn;

    public static boolean didBuyNoAds;
    public static boolean didLikeFacebook;
    public static boolean didRateApp;

    private final static Preferences preferences = Gdx.app
            .getPreferences("com.nopalsoft.slamthebird");

    public static void save() {
        preferences.putInteger("monedasActuales", currentCoins);
        preferences.putInteger("bestScore", bestScore);
        preferences.putInteger("skinSeleccionada", selectedSkin);
        preferences.putInteger("numeroVecesJugadas", playCount);
        preferences.putInteger("NIVEL_BOOST_BOOST_TIME", BOOST_DURATION);
        preferences.putInteger("NIVEL_BOOST_ICE", BOOST_FREEZE);
        preferences.putInteger("NIVEL_BOOST_MONEDAS", BOOST_COINS);
        preferences.putInteger("NIVEL_BOOST_INVENCIBLE", BOOST_INVINCIBLE);
        preferences.putInteger("NIVEL_BOOST_SUPER_JUMP", BOOST_SUPER_JUMP);

        preferences.putBoolean("isMusicOn", isMusicOn);
        preferences.putBoolean("isSoundOn", isSoundOn);

        preferences.putBoolean("didBuyNoAds", didBuyNoAds);
        preferences.putBoolean("didLikeFacebook", didLikeFacebook);
        preferences.putBoolean("seCalifico", didRateApp);
        preferences.flush();
    }

    public static void load() {
        currentCoins = preferences.getInteger("monedasActuales", 0);
        bestScore = preferences.getInteger("bestScore", 0);
        selectedSkin = preferences.getInteger("skinSeleccionada", 0);
        playCount = preferences.getInteger("numeroVecesJugadas", 0);
        BOOST_DURATION = preferences.getInteger("NIVEL_BOOST_BOOST_TIME", 0);
        BOOST_FREEZE = preferences.getInteger("NIVEL_BOOST_ICE", 0);
        BOOST_COINS = preferences.getInteger("NIVEL_BOOST_MONEDAS", 0);
        BOOST_INVINCIBLE = preferences.getInteger("NIVEL_BOOST_INVENCIBLE", 0);
        BOOST_SUPER_JUMP = preferences.getInteger("NIVEL_BOOST_SUPER_JUMP", 0);

        isMusicOn = preferences.getBoolean("isMusicOn", true);
        isSoundOn = preferences.getBoolean("isSoundOn", true);

        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false);
        didLikeFacebook = preferences.getBoolean("didLikeFacebook", false);
        didRateApp = preferences.getBoolean("seCalifico", false);
    }

    public static void setBestScores(int score) {
        if (bestScore < score)
            bestScore = score;
        save();
    }
}
