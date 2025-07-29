package com.nopalsoft.thetruecolor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.thetruecolor.scene2d.BaseDialogHelpSettings.Languages;

public class Settings {

    public static Languages selectedLanguage = Languages.DEFAULT;

    public static int bestScore;
    public static int numVecesJugadas;

    private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.thetruecolor");

    public static void save() {
        pref.putInteger("bestScore", bestScore);
        pref.putInteger("numVecesJugadas", numVecesJugadas);
        pref.putString("selectedLanguage", selectedLanguage.toString());
        pref.flush();
    }

    public static void load() {
        bestScore = pref.getInteger("bestScore", 0);
        numVecesJugadas = pref.getInteger("numVecesJugadas", 0);
        selectedLanguage = Languages.valueOf(pref.getString("selectedLanguage", Languages.DEFAULT.toString()));
    }

    public static void setNewScore(int score) {
        if (score > bestScore) {
            bestScore = score;
            save();
        }
    }
}
