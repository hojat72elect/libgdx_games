package com.nopalsoft.dosmil

import com.badlogic.gdx.Gdx

object Settings {
    var isMusicOn = false

    var isSoundOn = false
    private var didBuyNoAds = false

    var numberOfTimesPlayed = 0

    var bestScore = 0L

    private val preferences = Gdx.app.getPreferences("com.tiar.dosmil")

    fun load() {
        bestScore = preferences.getLong("bestScore", 0)
        numberOfTimesPlayed = preferences.getInteger("numeroVecesJugadas", 0)

        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false)
        isMusicOn = preferences.getBoolean("isMusicOn", true)
        isSoundOn = preferences.getBoolean("isSoundOn", true)
    }

    fun save() {
        preferences.putLong("bestScore", bestScore)
        preferences.putInteger("numeroVecesJugadas", numberOfTimesPlayed)
        preferences.putBoolean("didBuyNoAds", didBuyNoAds)
        preferences.putBoolean("isMusicOn", isMusicOn)
        preferences.putBoolean("isSoundOn", isSoundOn)
        preferences.flush()
    }

    fun setBestScores(score: Long) {
        if (bestScore < score) bestScore = score
        save()
    }
}
