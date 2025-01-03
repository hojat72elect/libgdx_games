package com.nopalsoft.dragracer

import com.badlogic.gdx.Gdx
import com.nopalsoft.dragracer.shop.CharactersSubMenu

object Settings {

    const val TIMES_TO_SHOW_AD = 5

    private val pref = Gdx.app.getPreferences("com.tiar.dragrace.shop")
    const val DRAW_DEBUG_LINES = false
    var numberOfTimesPlayed = 0
    var bestScore = 0
    var coinsTotal = 0
    var didBuyNoAds = false
    var didLikeFacebook = false

    @JvmField
    var isMusicOn = true
    var selectedSkin = CharactersSubMenu.SKIN_CAR_DEVIL


    fun load() {
        numberOfTimesPlayed = pref.getInteger("numeroVecesJugadas")
        bestScore = pref.getInteger("bestScore")
        coinsTotal = pref.getInteger("coinsTotal")
        selectedSkin = pref.getInteger("skinSeleccionada")

        didBuyNoAds = pref.getBoolean("didBuyNoAds")
        didLikeFacebook = pref.getBoolean("didLikeFacebook")
        isMusicOn = pref.getBoolean("isMusicOn", true)
    }

    @JvmStatic
    fun save() {
        pref.putInteger("numeroVecesJugadas", numberOfTimesPlayed)
        pref.putInteger("bestScore", bestScore)
        pref.putInteger("coinsTotal", coinsTotal)
        pref.putInteger("skinSeleccionada", selectedSkin)

        pref.putBoolean("didBuyNoAds", didBuyNoAds)
        pref.putBoolean("didLikeFacebook", didLikeFacebook)
        pref.putBoolean("isMusicOn", isMusicOn)
        pref.flush()
    }


    fun setNewScore(score: Int) {
        if (bestScore < score) {
            bestScore = score
        }
        save()
    }

}