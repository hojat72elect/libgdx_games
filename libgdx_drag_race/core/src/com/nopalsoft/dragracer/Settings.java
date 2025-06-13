package com.nopalsoft.dragracer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.dragracer.shop.PlayerSubMenu;

public class Settings {

    public static boolean drawDebugLines = false;

    public static int numberOfTimesPlayed = 0;
    public static int bestScore = 0;
    public static int coinsTotal = 0;
    public static boolean didBuyNoAds;
    public static boolean didLikeFacebook;
    public static boolean isMusicOn = true;

    public static int selectedSkin = PlayerSubMenu.SKIN_DEVIL;

    private final static Preferences preferences = Gdx.app
            .getPreferences("com.tiar.dragrace.shop");

    public static void load() {
        numberOfTimesPlayed = preferences.getInteger("numeroVecesJugadas");
        bestScore = preferences.getInteger("bestScore");
        coinsTotal = preferences.getInteger("coinsTotal");
        selectedSkin = preferences.getInteger("skinSeleccionada");

        didBuyNoAds = preferences.getBoolean("didBuyNoAds");
        didLikeFacebook = preferences.getBoolean("didLikeFacebook");
        isMusicOn = preferences.getBoolean("isMusicOn", true);
    }

    public static void save() {
        preferences.putInteger("numeroVecesJugadas", numberOfTimesPlayed);
        preferences.putInteger("bestScore", bestScore);
        preferences.putInteger("coinsTotal", coinsTotal);
        preferences.putInteger("skinSeleccionada", selectedSkin);

        preferences.putBoolean("didBuyNoAds", didBuyNoAds);
        preferences.putBoolean("didLikeFacebook", didLikeFacebook);
        preferences.putBoolean("isMusicOn", isMusicOn);
        preferences.flush();
    }

    public static void setNewScore(int score) {
        if (bestScore < score)
            bestScore = score;
        save();
    }
}
