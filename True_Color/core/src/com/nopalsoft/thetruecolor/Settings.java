package com.nopalsoft.thetruecolor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.thetruecolor.scene2d.DialogHelpSettings.Languages;

public class Settings {

    public static Languages selectedLanguage = Languages.DEFAULT;

    public static int bestScore;
    public static int numberOfTimesPlayed;

    private final static Preferences preferences = Gdx.app.getPreferences("com.nopalsoft.thetruecolor");

    public static void save() {
        preferences.putInteger("bestScore", bestScore);
        preferences.putInteger("numVecesJugadas", numberOfTimesPlayed);
        preferences.putString("selectedLanguage", selectedLanguage.toString());
        preferences.flush();
    }

    public static void load() {
        bestScore = preferences.getInteger("bestScore", 0);
        numberOfTimesPlayed = preferences.getInteger("numVecesJugadas", 0);
        selectedLanguage = Languages.valueOf(preferences.getString("selectedLanguage", Languages.DEFAULT.toString()));
    }

    public static void setNewScore(int score) {
        if (score > bestScore) {
            bestScore = score;
            save();
        }
    }
}
