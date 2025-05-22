package com.nopalsoft.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.sokoban.objects.Level;

public class Settings {

    public static boolean isTest = true;

    public static boolean animationWalkIsON;

    public static int NUM_MAPS = 62;
    public static Level[] levels;// Each position is a level

    private final static Preferences preferences = Gdx.app.getPreferences("com.nopalsoft.sokoban");

    public static void load() {
        levels = new Level[NUM_MAPS];

        animationWalkIsON = preferences.getBoolean("animationWalkIsON", false);

        for (int i = 0; i < NUM_MAPS; i++) {
            levels[i] = new Level();
            levels[i].numStars = preferences.getInteger("numStars" + i, 0);
            levels[i].bestMoves = preferences.getInteger("bestMoves" + i, 0);
            levels[i].bestTime = preferences.getInteger("bestTime" + i, 0);
        }
    }

    public static void save() {

        preferences.putBoolean("animationWalkIsON", animationWalkIsON);
        preferences.flush();
    }

    public static void levelCompeted(int level, int moves, int time) {

        levels[level].numStars = 1;
        levels[level].bestMoves = moves;
        levels[level].bestTime = time;

        preferences.putInteger("numStars" + level, levels[level].numStars);
        preferences.putInteger("bestMoves" + level, levels[level].bestMoves);
        preferences.putInteger("bestTime" + level, levels[level].bestTime);

        preferences.flush();
    }
}
