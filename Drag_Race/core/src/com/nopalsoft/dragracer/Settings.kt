package com.nopalsoft.dragracer

import com.badlogic.gdx.Gdx
import com.nopalsoft.dragracer.shop.PlayerSubMenu

object Settings {
    var drawDebugLines = false

    var numberOfTimesPlayed = 0
    var bestScore = 0
    var coinsTotal = 0
    var didBuyNoAds = false
    var didLikeFacebook = false

    var isMusicOn = true

    var selectedSkin = PlayerSubMenu.SKIN_DEVIL

    private val preferences = Gdx.app.getPreferences("com.tiar.dragrace.shop")

    fun load() {
        numberOfTimesPlayed = preferences.getInteger("numeroVecesJugadas")
        bestScore = preferences.getInteger("bestScore")
        coinsTotal = preferences.getInteger("coinsTotal")
        selectedSkin = preferences.getInteger("skinSeleccionada")

        didBuyNoAds = preferences.getBoolean("didBuyNoAds")
        didLikeFacebook = preferences.getBoolean("didLikeFacebook")
        isMusicOn = preferences.getBoolean("isMusicOn", true)
    }

    fun save() {
        preferences.putInteger("numeroVecesJugadas", numberOfTimesPlayed)
        preferences.putInteger("bestScore", bestScore)
        preferences.putInteger("coinsTotal", coinsTotal)
        preferences.putInteger("skinSeleccionada", selectedSkin)

        preferences.putBoolean("didBuyNoAds", didBuyNoAds)
        preferences.putBoolean("didLikeFacebook", didLikeFacebook)
        preferences.putBoolean("isMusicOn", isMusicOn)
        preferences.flush()
    }

    fun setNewScore(score: Int) {
        if (bestScore < score) bestScore = score
        save()
    }
}
