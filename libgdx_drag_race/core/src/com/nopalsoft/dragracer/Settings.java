package com.nopalsoft.dragracer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.dragracer.shop.PersonajesSubMenu;

public class Settings {

    public static boolean drawDebugLines = false;

    public static int numeroVecesJugadas = 0;
    public static int bestScore = 0;
    public static int coinsTotal = 0;
    public static boolean didBuyNoAds;
    public static boolean didLikeFacebook;
    public static boolean isMusicOn = true;

    public static int skinSeleccionada = PersonajesSubMenu.SKIN_CARRO_DIABLO;

    private final static Preferences pref = Gdx.app
            .getPreferences("com.tiar.dragrace.shop");

    public static void load() {
        numeroVecesJugadas = pref.getInteger("numeroVecesJugadas");
        bestScore = pref.getInteger("bestScore");
        coinsTotal = pref.getInteger("coinsTotal");
        skinSeleccionada = pref.getInteger("skinSeleccionada");

        didBuyNoAds = pref.getBoolean("didBuyNoAds");
        didLikeFacebook = pref.getBoolean("didLikeFacebook");
        isMusicOn = pref.getBoolean("isMusicOn", true);
    }

    public static void save() {
        pref.putInteger("numeroVecesJugadas", numeroVecesJugadas);
        pref.putInteger("bestScore", bestScore);
        pref.putInteger("coinsTotal", coinsTotal);
        pref.putInteger("skinSeleccionada", skinSeleccionada);

        pref.putBoolean("didBuyNoAds", didBuyNoAds);
        pref.putBoolean("didLikeFacebook", didLikeFacebook);
        pref.putBoolean("isMusicOn", isMusicOn);
        pref.flush();
    }

    public static void setNewScore(int score) {
        if (bestScore < score)
            bestScore = score;
        save();
    }
}
