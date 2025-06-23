package com.nopalsoft.impossibledial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    public static int bestScoreEasy;
    public static int bestScoreHard;
    public static int numVecesJugadas;

    private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.impossibledial");

    public static void save() {
        pref.putInteger("bestScore", bestScoreEasy);
        pref.putInteger("bestScoreHard", bestScoreHard);
        pref.putInteger("numVecesJugadas", numVecesJugadas);
        pref.flush();
    }

    public static void load() {
        bestScoreEasy = pref.getInteger("bestScore", 0);
        bestScoreHard = pref.getInteger("bestScoreHard", 0);
        numVecesJugadas = pref.getInteger("numVecesJugadas", 0);
    }

    public static void setNewScoreEasy(int score) {
        if (score > bestScoreEasy) {
            bestScoreEasy = score;
            save();
        }
    }

    public static void setNewScoreHard(int score) {
        if (score > bestScoreHard) {
            bestScoreHard = score;
            save();
        }
    }

}
