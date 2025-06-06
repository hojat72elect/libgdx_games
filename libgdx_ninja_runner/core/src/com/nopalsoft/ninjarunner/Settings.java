package com.nopalsoft.ninjarunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.ninjarunner.game_objects.Mascot;
import com.nopalsoft.ninjarunner.game_objects.Player;

public class Settings {

    public static boolean isSoundEnabled = false;
    public static boolean isMusicEnabled = false;

    public static int selectedSkin = Player.TYPE_NINJA;
    public static int totalCoins = 1500000;
    public static Mascot.MascotType selectedMascot = Mascot.MascotType.PINK_BIRD;

    public static int MASCOT_LEVEL_BIRD;
    public static int MASCOT_LEVEL_BOMB;

    public static int LEVEL_MAGNET;
    public static int LEVEL_LIFE;
    public static int LEVEL_ENERGY;
    public static int LEVEL_COINS;
    public static int LEVEL_TREASURE_CHEST;

    private final static Preferences preferences = Gdx.app.getPreferences("com.nopalsoft.ninjarunner.settings");

    public static long bestScore;

    public static void load() {
        LEVEL_MAGNET = preferences.getInteger("LEVEL_MAGNET", 0);
        LEVEL_LIFE = preferences.getInteger("LEVEL_LIFE", 0);
        LEVEL_ENERGY = preferences.getInteger("LEVEL_ENERGY", 0);
        LEVEL_COINS = preferences.getInteger("LEVEL_COINS", 0);
        LEVEL_TREASURE_CHEST = preferences.getInteger("LEVEL_TREASURE_CHEST", 0);
    }

    public static void setNewScore(long score) {
        if (score > bestScore) {
            bestScore = score;
        }
    }
}
