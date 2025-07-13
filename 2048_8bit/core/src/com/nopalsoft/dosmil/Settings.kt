package com.nopalsoft.dosmil

import com.badlogic.gdx.Gdx

object Settings {
    @JvmField
    var isMusicOn = false

    @JvmField
    var isSoundOn = false
    private var didBuyNoAds = false

    @JvmField
    var numberOfTimesPlayed = 0

    @JvmField
    var bestScore = 0L

    private val preferences = Gdx.app.getPreferences("com.tiar.dosmil")

    @JvmStatic
    fun load() {
        bestScore = preferences.getLong("bestScore", 0)
        numberOfTimesPlayed = preferences.getInteger("numeroVecesJugadas", 0)

        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false)
        isMusicOn = preferences.getBoolean("isMusicOn", true)
        isSoundOn = preferences.getBoolean("isSoundOn", true)
    }

    @JvmStatic
    fun save() {
        preferences.putLong("bestScore", bestScore)
        preferences.putInteger("numeroVecesJugadas", numberOfTimesPlayed)
        preferences.putBoolean("didBuyNoAds", didBuyNoAds)
        preferences.putBoolean("isMusicOn", isMusicOn)
        preferences.putBoolean("isSoundOn", isSoundOn)
        preferences.flush()
    }

    @JvmStatic
    fun setBestScores(score: Long) {
        if (bestScore < score) bestScore = score
        save()
    }
}
