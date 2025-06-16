package com.nopalsoft.invaders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    @JvmField
    var drawDebugLines: Boolean = false

    @JvmField
    var soundEnabled: Boolean = false

    @JvmField
    var musicEnabled: Boolean = false

    @JvmField
    val highScores: IntArray = intArrayOf(0, 0, 0, 0, 0) // only 5 scores are saved

    @JvmField
    var isTiltControl: Boolean = true

    @JvmField
    var accelerometerSensitivity: Int = 10

    @JvmField
    var numberOfTimesPlayed: Int = 0

    private const val preferencesName = "com.tiarsoft.droid.Settings"
    private val preferences: Preferences = Gdx.app.getPreferences(preferencesName)

    @JvmStatic
    fun load() {
        isTiltControl = preferences.getBoolean("isTiltControl", true)

        soundEnabled = preferences.getBoolean("sonidoActivado", false)
        musicEnabled = preferences.getBoolean("musicaActivado", false)
        for (i in 0..4) { // only 5 scores are loaded
            highScores[i] = preferences.getString("puntuacion$i", "0").toInt()
        }
        accelerometerSensitivity = preferences.getInteger("acelerometerSensitive", 10)
        numberOfTimesPlayed = preferences.getInteger("numeroDeVecesQueSeHaJugado", 0)
    }

    @JvmStatic
    fun save() {
        preferences.putBoolean("isTiltControl", isTiltControl)

        preferences.putBoolean("sonidoActivado", soundEnabled)
        preferences.putBoolean("musicaActivado", musicEnabled)

        for (i in 0..4) { // solo 5 puntuaciones se cargan
            preferences.putString("puntuacion$i", highScores[i].toString())
        }
        preferences.putInteger("acelerometerSensitive", accelerometerSensitivity)
        preferences.putInteger("numeroDeVecesQueSeHaJugado", numberOfTimesPlayed)
        preferences.flush()
    }

    @JvmStatic
    fun addScore(newScore: Int) {
        for (i in 0..4) {
            if (highScores[i] < newScore) {
                for (j in 4 downTo i + 1) highScores[j] = highScores[j - 1]
                highScores[i] = newScore
                break
            }
        }
    }
}
