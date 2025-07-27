package com.nopalsoft.ponyrace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
    public static final int MONEDAS_REGALO_FACEBOOK = 3500;
    public static final int MONEDAS_REGALO_SHARE_FACEBOOK = 1000;

    /**
     * I didn't use enums because with the way implemented to change the difficulty it was easier to use integers.
     */
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_NORMAL = 1;
    public static final int DIFFICULTY_HARD = 2;
    public static final int DIFFICULTY_VERY_HARD = 3;

    private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.ponyRace.settings");
    public static boolean isEnabledSecretWorld = false; // This variable is always false at startup and is not saved.
    public static boolean isSoundOn = false;
    public static boolean isMusicOn = false;
    public static int numberOfGameLevelsUnlocked;
    public static boolean wasAppLiked = false;
    public static boolean wasAppRated = false;

    public static String selectedSkin;
    public static int numeroBombas = 0;// Bombs to use in game
    public static int numeroWoods = 0;// Logs available for use in game
    public static int numeroMonedasActual = 0;// Coins available to spend
    public static int totalCoinsCollected;// Total coin statistics.
    public static int statTimesPlayed;
    public static int difficultyLevel = DIFFICULTY_NORMAL;
    public static int chocolateLevel = 0;
    public static int balloonLevel = 0;
    public static int chiliLevel = 0;
    public static int bombLevel = 0;
    public static int woodLevel = 0;
    public static int coinLevel = 0;
    public static int timeLevel = 0;
    public static boolean isBackGroundEnabled = true;

    public static void cargar() {
        isSoundOn = pref.getBoolean("isSonidoON", true);
        isMusicOn = pref.getBoolean("isMusicaON", true);

        wasAppLiked = pref.getBoolean("seDioLike", false);
        wasAppRated = pref.getBoolean("seCalificoApp", false);

        selectedSkin = pref.getString("skinSeleccionada", "Cloud");
        numberOfGameLevelsUnlocked = pref.getInteger("mundosDesbloqueados", 1);

        // Usable items in the game Bomb coins
        numeroBombas = pref.getInteger("numeroBombas", 25);
        numeroWoods = pref.getInteger("numeroWoods", 25);
        numeroMonedasActual = pref.getInteger("numeroMonedasActual", 500);

        // Coin Statistics
        totalCoinsCollected = pref.getInteger("statMonedasTotal", 0);
        statTimesPlayed = pref.getInteger("statTimesPlayed", 0);

        // Levels of things
        chocolateLevel = pref.getInteger("nivelChocolate", 0);
        balloonLevel = pref.getInteger("nivelBallon", 0);
        chiliLevel = pref.getInteger("nivelChili", 0);
        bombLevel = pref.getInteger("nivelBomb", 0);
        woodLevel = pref.getInteger("nivelWood", 0);
        coinLevel = pref.getInteger("nivelCoin", 0);
        timeLevel = pref.getInteger("nivelTime", 0);

        difficultyLevel = pref.getInteger("dificultadActual",
                DIFFICULTY_NORMAL);
    }

    public static void guardar() {
        pref.putBoolean("isSonidoON", isSoundOn);
        pref.putBoolean("isMusicaON", isMusicOn);

        pref.putBoolean("seDioLike", wasAppLiked);
        pref.putBoolean("seCalificoApp", wasAppRated);

        pref.putString("skinSeleccionada", selectedSkin);
        pref.putInteger("mundosDesbloqueados", numberOfGameLevelsUnlocked);

        pref.putInteger("numeroBombas", numeroBombas);
        pref.putInteger("numeroWoods", numeroWoods);
        pref.putInteger("numeroMonedasActual", numeroMonedasActual);

        pref.putInteger("statMonedasTotal", totalCoinsCollected);
        pref.putInteger("statTimesPlayed", statTimesPlayed);

        pref.putInteger("nivelChocolate", chocolateLevel);
        pref.putInteger("nivelBallon", balloonLevel);
        pref.putInteger("nivelChili", chiliLevel);
        pref.putInteger("nivelBomb", bombLevel);
        pref.putInteger("nivelWood", woodLevel);
        pref.putInteger("nivelCoin", coinLevel);
        pref.putInteger("nivelTime", timeLevel);

        pref.putInteger("dificultadActual", difficultyLevel);
        pref.flush();
    }

    /**
     * Adds numberOfCoins to the total coins to spend and to the statistics for coins collected for achievements.
     *
     * @param numberOfCoins Number of coins to increase.
     */
    public static void sumarMonedas(int numberOfCoins) {
        numeroMonedasActual += numberOfCoins;
        // These increase by 1 regardless of whether a coin worth x2 or x3 has been taken. To make it easier to implement Google Game Services.
        totalCoinsCollected += numberOfCoins;
    }
}
