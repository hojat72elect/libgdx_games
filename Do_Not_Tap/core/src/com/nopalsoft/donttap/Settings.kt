package com.nopalsoft.donttap

import com.badlogic.gdx.Gdx

object Settings {
    @JvmField
    var numeroVecesJugadas = 0

    @JvmField
    var bestTimeClassicMode = 100100F // Default in seconds

    @JvmField
    var bestScoreTimeMode = 0

    @JvmField
    var bestScoreEndlessMode = 0

    @JvmField
    var isSoundEnabled = true

    private val preferences = Gdx.app.getPreferences("com.nopalsoft.donttap")

    @JvmStatic
    fun save() {
        preferences.putInteger("numeroVecesJugadas", numeroVecesJugadas)
        preferences.putFloat("bestTimeClassicMode", bestTimeClassicMode)
        preferences.putInteger("bestScoreTimeMode", bestScoreTimeMode)
        preferences.putInteger("bestScoreEndlessMode", bestScoreEndlessMode)
        preferences.putBoolean("isSoundEnabled", isSoundEnabled)
        preferences.flush()
    }

    @JvmStatic
    fun load() {
        numeroVecesJugadas = preferences.getInteger("numeroVecesJugadas", 0)
        bestTimeClassicMode = preferences.getFloat("bestTimeClassicMode", 100100f)
        bestScoreTimeMode = preferences.getInteger("bestScoreTimeMode", 0)
        bestScoreEndlessMode = preferences.getInteger("bestScoreEndlessMode", 0)
        isSoundEnabled = preferences.getBoolean("isSoundEnabled", true)
    }
}
