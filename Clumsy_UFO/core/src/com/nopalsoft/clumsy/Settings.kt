package com.nopalsoft.clumsy

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    private val preferences: Preferences = Gdx.app.getPreferences("com.nopalsoft.clumsyufo")
    var didBuyNoAds: Boolean = false
    var bestScoreArcade: Int = 0
    var bestScoreClassic: Int = 0
    var numberOfTimesPlayed: Int = 0

    fun load() {
        bestScoreArcade = preferences.getInteger("bestScoreArcade", 0)
        bestScoreClassic = preferences.getInteger("bestScoreClassic", 0)
        numberOfTimesPlayed = preferences.getInteger("numVecesJugadas", 0)
        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false)
    }

    fun save() {
        preferences.putInteger("bestScoreArcade", bestScoreArcade)
        preferences.putInteger("bestScoreClassic", bestScoreClassic)
        preferences.putInteger("numVecesJugadas", numberOfTimesPlayed)
        preferences.putBoolean("didBuyNoAds", didBuyNoAds)
        preferences.flush()
    }
}
