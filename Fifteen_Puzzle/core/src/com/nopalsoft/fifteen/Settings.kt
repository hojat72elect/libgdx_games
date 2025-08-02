package com.nopalsoft.fifteen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    private val pref: Preferences = Gdx.app
        .getPreferences("com.tiar.fifteen")

    @JvmField
    var bestTime: Int = 0

    @JvmField
    var bestMoves: Int = 0
    var didBuyNoAds: Boolean = false

    @JvmField
    var isMusicOn: Boolean = false

    @JvmField
    var isSoundOn: Boolean = false

    @JvmField
    var numeroVecesJugadas: Int = 0

    @JvmStatic
    fun load() {
        bestTime = pref.getInteger("bestTime", Int.Companion.MAX_VALUE)
        bestMoves = pref.getInteger("bestMoves", Int.Companion.MAX_VALUE)
        numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0)

        didBuyNoAds = pref.getBoolean("didBuyNoAds", false)
        isMusicOn = pref.getBoolean("isMusicOn", true)
        isSoundOn = pref.getBoolean("isSoundOn", true)
    }

    @JvmStatic
    fun save() {
        pref.putInteger("bestTime", bestTime)
        pref.putInteger("bestMoves", bestMoves)
        pref.putInteger("numeroVecesJugadas", numeroVecesJugadas)
        pref.putBoolean("didBuyNoAds", didBuyNoAds)
        pref.putBoolean("isMusicOn", isMusicOn)
        pref.putBoolean("isSoundOn", isSoundOn)
        pref.flush()
    }

    @JvmStatic
    fun setBestScores(time: Int, moves: Int) {
        if (time < bestTime) bestTime = time
        if (moves < bestMoves) bestMoves = moves
        save()
    }
}
