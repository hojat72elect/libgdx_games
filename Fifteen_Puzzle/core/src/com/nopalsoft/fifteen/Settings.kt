package com.nopalsoft.fifteen

import com.badlogic.gdx.Gdx

object Settings {

    private val pref = Gdx.app.getPreferences("com.tiar.fifteen")
    var bestTime = 0
    var bestMoves = 0
    var didBuyNoAds = false
    var isMusicOn = false
    var isSoundOn = false
    var numeroVecesJugadas = 0

    fun load() {
        bestTime = pref.getInteger("bestTime", Int.Companion.MAX_VALUE)
        bestMoves = pref.getInteger("bestMoves", Int.Companion.MAX_VALUE)
        numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0)

        didBuyNoAds = pref.getBoolean("didBuyNoAds", false)
        isMusicOn = pref.getBoolean("isMusicOn", true)
        isSoundOn = pref.getBoolean("isSoundOn", true)
    }

    fun save() {
        pref.putInteger("bestTime", bestTime)
        pref.putInteger("bestMoves", bestMoves)
        pref.putInteger("numeroVecesJugadas", numeroVecesJugadas)
        pref.putBoolean("didBuyNoAds", didBuyNoAds)
        pref.putBoolean("isMusicOn", isMusicOn)
        pref.putBoolean("isSoundOn", isSoundOn)
        pref.flush()
    }

    fun setBestScores(time: Int, moves: Int) {
        if (time < bestTime) bestTime = time
        if (moves < bestMoves) bestMoves = moves
        save()
    }
}
