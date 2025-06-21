package com.nopalsoft.dragracer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.nopalsoft.dragracer.shop.PlayerSubMenu

object Settings {
    var drawDebugLines: Boolean = false

    var numberOfTimesPlayed: Int = 0
    var bestScore: Int = 0
    var coinsTotal: Int = 0
    var didBuyNoAds: Boolean = false
    var didLikeFacebook: Boolean = false

    @JvmField
    var isMusicOn: Boolean = true

    var selectedSkin: Int = PlayerSubMenu.SKIN_DEVIL

    private val preferences: Preferences = Gdx.app
        .getPreferences("com.tiar.dragrace.shop")

    @JvmStatic
    fun load() {
        numberOfTimesPlayed = preferences.getInteger("numeroVecesJugadas")
        bestScore = preferences.getInteger("bestScore")
        coinsTotal = preferences.getInteger("coinsTotal")
        selectedSkin = preferences.getInteger("skinSeleccionada")

        didBuyNoAds = preferences.getBoolean("didBuyNoAds")
        didLikeFacebook = preferences.getBoolean("didLikeFacebook")
        isMusicOn = preferences.getBoolean("isMusicOn", true)
    }

    @JvmStatic
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
