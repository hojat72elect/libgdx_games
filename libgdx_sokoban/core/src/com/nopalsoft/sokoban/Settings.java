package com.nopalsoft.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.sokoban.objetos.Level;

public class Settings {

    public static boolean isTest = true;

    public static boolean animationWalkIsON;

    public static int NUM_MAPS = 62;
    public static Level[] levels;// Each position is a level

    private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.sokoban");

    public static void load() {
        levels = new Level[NUM_MAPS];

        animationWalkIsON = pref.getBoolean("animationWalkIsON", false);

        for (int i = 0; i < NUM_MAPS; i++) {
            levels[i] = new Level();
            levels[i].numStars = pref.getInteger("numStars" + i, 0);
            levels[i].bestMoves = pref.getInteger("bestMoves" + i, 0);
            levels[i].bestTime = pref.getInteger("bestTime" + i, 0);
        }
    }

    public static void save() {

        pref.putBoolean("animationWalkIsON", animationWalkIsON);
        pref.flush();
    }

    public static void levelCompeted(int level, int moves, int time) {

        levels[level].numStars = 1;
        levels[level].bestMoves = moves;
        levels[level].bestTime = time;

        pref.putInteger("numStars" + level, levels[level].numStars);
        pref.putInteger("bestMoves" + level, levels[level].bestMoves);
        pref.putInteger("bestTime" + level, levels[level].bestTime);

        pref.flush();
    }
}
