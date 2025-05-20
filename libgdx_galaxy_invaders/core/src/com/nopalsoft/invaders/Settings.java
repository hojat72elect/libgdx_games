/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.nopalsoft.invaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    public static boolean drawDebugLines = false;

    public static boolean soundEnabled = false;
    public static boolean musicEnabled = false;
    public final static int[] highScores = new int[]{0, 0, 0, 0, 0};// solo 5 puntuaciones se guardan
    public static boolean isTiltControl = true;
    public static int aceletometerSensitive = 10;
    public static int numeroDeVecesQueSeHaJugado = 0;

    private final static String prefName = "com.tiarsoft.droid.Settings";
    private final static Preferences pref = Gdx.app.getPreferences(prefName);

    public static void load() {

        isTiltControl = pref.getBoolean("isTiltControl", true);

        soundEnabled = pref.getBoolean("sonidoActivado", false);
        musicEnabled = pref.getBoolean("musicaActivado", false);
        for (int i = 0; i < 5; i++) {// solo 5 puntuaciones se cargan
            highScores[i] = Integer.parseInt(pref.getString("puntuacion" + i, "0"));
        }
        aceletometerSensitive = pref.getInteger("acelerometerSensitive", 10);
        numeroDeVecesQueSeHaJugado = pref.getInteger("numeroDeVecesQueSeHaJugado", 0);
    }

    public static void save() {
        pref.putBoolean("isTiltControl", isTiltControl);

        pref.putBoolean("sonidoActivado", soundEnabled);
        pref.putBoolean("musicaActivado", musicEnabled);

        for (int i = 0; i < 5; i++) {// solo 5 puntuaciones se cargan
            pref.putString("puntuacion" + i, String.valueOf(highScores[i]));
        }
        pref.putInteger("acelerometerSensitive", aceletometerSensitive);
        pref.putInteger("numeroDeVecesQueSeHaJugado", numeroDeVecesQueSeHaJugado);
        pref.flush();
    }

    public static void borrarDatosGuardados() {
        pref.clear();
        load();
    }

    public static void agregarPuntuacion(int puntuacion) {
        for (int i = 0; i < 5; i++) {
            if (highScores[i] < puntuacion) {
                for (int j = 4; j > i; j--)
                    highScores[j] = highScores[j - 1];
                highScores[i] = puntuacion;
                break;
            }
        }
    }

}
