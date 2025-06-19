package com.nopalsoft.slamthebird

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    @JvmField
    var currentCoins: Int = 0

    @JvmField
    var bestScore: Int = 0
    var selectedSkin: Int = 0

    @JvmField
    var playCount: Int = 0

    @JvmField
    var BOOST_DURATION: Int = 0
    var BOOST_FREEZE: Int = 0
    var BOOST_COINS: Int = 0
    var BOOST_INVINCIBLE: Int = 0
    var BOOST_SUPER_JUMP: Int = 0

    @JvmField
    var isMusicOn: Boolean = false

    @JvmField
    var isSoundOn: Boolean = false

    var didBuyNoAds: Boolean = false
    var didLikeFacebook: Boolean = false

    @JvmField
    var didRateApp: Boolean = false

    private val preferences: Preferences = Gdx.app
        .getPreferences("com.nopalsoft.slamthebird")

    @JvmStatic
    fun save() {
        preferences.putInteger("monedasActuales", currentCoins)
        preferences.putInteger("bestScore", bestScore)
        preferences.putInteger("skinSeleccionada", selectedSkin)
        preferences.putInteger("numeroVecesJugadas", playCount)
        preferences.putInteger("NIVEL_BOOST_BOOST_TIME", BOOST_DURATION)
        preferences.putInteger("NIVEL_BOOST_ICE", BOOST_FREEZE)
        preferences.putInteger("NIVEL_BOOST_MONEDAS", BOOST_COINS)
        preferences.putInteger("NIVEL_BOOST_INVENCIBLE", BOOST_INVINCIBLE)
        preferences.putInteger("NIVEL_BOOST_SUPER_JUMP", BOOST_SUPER_JUMP)

        preferences.putBoolean("isMusicOn", isMusicOn)
        preferences.putBoolean("isSoundOn", isSoundOn)

        preferences.putBoolean("didBuyNoAds", didBuyNoAds)
        preferences.putBoolean("didLikeFacebook", didLikeFacebook)
        preferences.putBoolean("seCalifico", didRateApp)
        preferences.flush()
    }

    fun load() {
        currentCoins = preferences.getInteger("monedasActuales", 0)
        bestScore = preferences.getInteger("bestScore", 0)
        selectedSkin = preferences.getInteger("skinSeleccionada", 0)
        playCount = preferences.getInteger("numeroVecesJugadas", 0)
        BOOST_DURATION = preferences.getInteger("NIVEL_BOOST_BOOST_TIME", 0)
        BOOST_FREEZE = preferences.getInteger("NIVEL_BOOST_ICE", 0)
        BOOST_COINS = preferences.getInteger("NIVEL_BOOST_MONEDAS", 0)
        BOOST_INVINCIBLE = preferences.getInteger("NIVEL_BOOST_INVENCIBLE", 0)
        BOOST_SUPER_JUMP = preferences.getInteger("NIVEL_BOOST_SUPER_JUMP", 0)

        isMusicOn = preferences.getBoolean("isMusicOn", true)
        isSoundOn = preferences.getBoolean("isSoundOn", true)

        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false)
        didLikeFacebook = preferences.getBoolean("didLikeFacebook", false)
        didRateApp = preferences.getBoolean("seCalifico", false)
    }

    @JvmStatic
    fun setBestScores(score: Int) {
        if (bestScore < score) bestScore = score
        save()
    }
}
