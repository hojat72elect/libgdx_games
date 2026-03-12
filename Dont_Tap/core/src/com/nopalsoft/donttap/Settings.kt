package com.nopalsoft.donttap

import com.badlogic.gdx.Gdx

object Settings {
    var numberOfTimesPlayed = 0

    var bestTimeClassicMode = 100100F // Default in seconds

    var bestScoreTimeMode = 0

    var bestScoreEndlessMode = 0

    var isSoundEnabled = true

    private val preferences = Gdx.app.getPreferences("com.nopalsoft.donttap")

    fun save() {
        preferences.putInteger("numeroVecesJugadas", numberOfTimesPlayed)
        preferences.putFloat("bestTimeClassicMode", bestTimeClassicMode)
        preferences.putInteger("bestScoreTimeMode", bestScoreTimeMode)
        preferences.putInteger("bestScoreEndlessMode", bestScoreEndlessMode)
        preferences.putBoolean("isSoundEnabled", isSoundEnabled)
        preferences.flush()
    }

    fun load() {
        numberOfTimesPlayed = preferences.getInteger("numeroVecesJugadas", 0)
        bestTimeClassicMode = preferences.getFloat("bestTimeClassicMode", 100100f)
        bestScoreTimeMode = preferences.getInteger("bestScoreTimeMode", 0)
        bestScoreEndlessMode = preferences.getInteger("bestScoreEndlessMode", 0)
        isSoundEnabled = preferences.getBoolean("isSoundEnabled", true)
    }
}
