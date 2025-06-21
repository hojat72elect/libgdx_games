package com.nopalsoft.sharkadventure

import com.badlogic.gdx.Gdx

object Settings {
    @JvmField
    var numberOfTimesPlayed = 0L

    @JvmField
    var bestScore = 0L

    @JvmField
    var isMusicOn = true

    @JvmField
    var isSoundOn = true

    internal var didRate = false
    var didBuyNoAds = false

    private val preferences = Gdx.app.getPreferences("com.nopalsoft.sharkadventure")

    @JvmStatic
    fun save() {
        preferences.putBoolean("isMusicOn", isMusicOn)
        preferences.putBoolean("isSoundOn", isSoundOn)

        preferences.putBoolean("didBuyNoAds", didBuyNoAds)
        preferences.putBoolean("didRate", didRate)

        preferences.putLong("numVecesJugadas", numberOfTimesPlayed)
        preferences.putLong("bestScore", bestScore)
        preferences.flush()
    }

    fun load() {
        isMusicOn = preferences.getBoolean("isMusicOn", true)
        isSoundOn = preferences.getBoolean("isSoundOn", true)

        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false)
        didRate = preferences.getBoolean("didRate", false)

        numberOfTimesPlayed = preferences.getLong("numVecesJugadas", 0)

        bestScore = preferences.getLong("bestScore", 0)
    }

    @JvmStatic
    fun setBestScore(score: Long) {
        if (score > bestScore) {
            bestScore = score
            save()
        }
    }
}
