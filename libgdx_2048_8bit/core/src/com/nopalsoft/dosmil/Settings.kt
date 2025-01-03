package com.nopalsoft.dosmil

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    private val pref: Preferences = Gdx.app
        .getPreferences("com.tiar.dosmil")
    var isMusicOn: Boolean = false
    var isSoundOn: Boolean = false
    var didBuyNoAds: Boolean = false
    var numberTimesPlayed: Int = 0
    var bestScore: Long = 0


    fun load() {
        bestScore = pref.getLong("bestScore", 0)
        numberTimesPlayed = pref.getInteger("numeroVecesJugadas", 0)

        didBuyNoAds = pref.getBoolean("didBuyNoAds", false)
        isMusicOn = pref.getBoolean("isMusicOn", true)
        isSoundOn = pref.getBoolean("isSoundOn", true)
    }

    fun save() {
        pref.putLong("bestScore", bestScore)
        pref.putInteger("numeroVecesJugadas", numberTimesPlayed)
        pref.putBoolean("didBuyNoAds", didBuyNoAds)
        pref.putBoolean("isMusicOn", isMusicOn)
        pref.putBoolean("isSoundOn", isSoundOn)
        pref.flush()
    }

    fun setBestScores(score: Long) {
        if (bestScore < score) bestScore = score
        save()
    }
}
