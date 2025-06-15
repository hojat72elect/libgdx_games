package com.nopalsoft.invaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    public static boolean drawDebugLines = false;

    public static boolean soundEnabled = false;
    public static boolean musicEnabled = false;
    public final static int[] highScores = new int[]{0, 0, 0, 0, 0};// only 5 scores are saved
    public static boolean isTiltControl = true;
    public static int accelerometerSensitivity = 10;
    public static int numberOfTimesPlayed = 0;

    private final static String preferencesName = "com.tiarsoft.droid.Settings";
    private final static Preferences preferences = Gdx.app.getPreferences(preferencesName);

    public static void load() {

        isTiltControl = preferences.getBoolean("isTiltControl", true);

        soundEnabled = preferences.getBoolean("sonidoActivado", false);
        musicEnabled = preferences.getBoolean("musicaActivado", false);
        for (int i = 0; i < 5; i++) {// only 5 scores are loaded
            highScores[i] = Integer.parseInt(preferences.getString("puntuacion" + i, "0"));
        }
        accelerometerSensitivity = preferences.getInteger("acelerometerSensitive", 10);
        numberOfTimesPlayed = preferences.getInteger("numeroDeVecesQueSeHaJugado", 0);
    }

    public static void save() {
        preferences.putBoolean("isTiltControl", isTiltControl);

        preferences.putBoolean("sonidoActivado", soundEnabled);
        preferences.putBoolean("musicaActivado", musicEnabled);

        for (int i = 0; i < 5; i++) {// solo 5 puntuaciones se cargan
            preferences.putString("puntuacion" + i, String.valueOf(highScores[i]));
        }
        preferences.putInteger("acelerometerSensitive", accelerometerSensitivity);
        preferences.putInteger("numeroDeVecesQueSeHaJugado", numberOfTimesPlayed);
        preferences.flush();
    }

    public static void addScore(int newScore) {
        for (int i = 0; i < 5; i++) {
            if (highScores[i] < newScore) {
                for (int j = 4; j > i; j--)
                    highScores[j] = highScores[j - 1];
                highScores[i] = newScore;
                break;
            }
        }
    }
}
