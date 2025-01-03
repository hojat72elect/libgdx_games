package com.nopalsoft.sharkadventure

import com.badlogic.gdx.Gdx

object Settings {

    private val pref = Gdx.app.getPreferences("com.nopalsoft.sharkadventure")
    var numberOfTimesPlayed = 0L
    var bestScore = 0L
    var isMusicOn = true
    var isSoundOn = true
    private var didBuyNoAds = false
    private var didRate = false

    fun save() {
        pref.putBoolean("isMusicOn", isMusicOn)
        pref.putBoolean("isSoundOn", isSoundOn)
        pref.putBoolean("didBuyNoAds", didBuyNoAds)
        pref.putBoolean("didRate", didRate)
        pref.putLong("numVecesJugadas", numberOfTimesPlayed)
        pref.putLong("bestScore", bestScore)
        pref.flush()
    }

    fun load() {
        isMusicOn = pref.getBoolean("isMusicOn", true)
        isSoundOn = pref.getBoolean("isSoundOn", true)
        didBuyNoAds = pref.getBoolean("didBuyNoAds", false)
        didRate = pref.getBoolean("didRate", false)
        numberOfTimesPlayed = pref.getLong("numVecesJugadas", 0)
        bestScore = pref.getLong("bestScore", 0)
    }

    fun changeBestScore(score: Long) {
        if (score > bestScore) {
            bestScore = score
            save()
        }
    }
}