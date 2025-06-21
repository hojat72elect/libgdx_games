package com.nopalsoft.superjumper

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    private var isMusicOn: Boolean = false
    private var isSoundOn: Boolean = false

    private var didBuyNoAds: Boolean = false
    private var didLikeFacebook: Boolean = false
    private var didRate: Boolean = false

    private val preferences: Preferences = Gdx.app.getPreferences("com.nopalsoft.superjumper")

    private var coinsTotal: Int = 0
    var numBullets: Int = 0

    var bestScore: Int = 0

    private var LEVEL_LIFE: Int = 0
    private var LEVEL_SHIELD: Int = 0
    private var LEVEL_SECOND_JUMP: Int = 0
    private var LEVEL_WEAPON: Int = 0
    var numTimesPlayed: Int = 0

    fun save() {
        preferences.putBoolean("isMusicOn", isMusicOn)
        preferences.putBoolean("isSoundOn", isSoundOn)

        preferences.putBoolean("didBuyNoAds", didBuyNoAds)
        preferences.putBoolean("didLikeFacebook", didLikeFacebook)
        preferences.putBoolean("didRate", didRate)

        preferences.putInteger("numeroVecesJugadas", numTimesPlayed)
        preferences.putInteger("coinsTotal", coinsTotal)
        preferences.putInteger("numBullets", numBullets)
        preferences.putInteger("bestScore", bestScore)

        preferences.putInteger("LEVEL_WEAPON", LEVEL_WEAPON)
        preferences.putInteger("LEVEL_SECOND_JUMP", LEVEL_SECOND_JUMP)
        preferences.putInteger("LEVEL_LIFE", LEVEL_LIFE)
        preferences.putInteger("LEVEL_SHIELD", LEVEL_SHIELD)

        preferences.flush()
    }

    fun load() {
        isMusicOn = preferences.getBoolean("isMusicOn", true)
        isSoundOn = preferences.getBoolean("isSoundOn", true)

        didBuyNoAds = preferences.getBoolean("didBuyNoAds", false)
        didLikeFacebook = preferences.getBoolean("didLikeFacebook", false)
        didRate = preferences.getBoolean("didRate", false)

        numTimesPlayed = preferences.getInteger("numeroVecesJugadas", 0)

        coinsTotal = preferences.getInteger("coinsTotal", 0)
        numBullets = preferences.getInteger("numBullets", 30)
        bestScore = preferences.getInteger("bestScore", 0)

        LEVEL_WEAPON = preferences.getInteger("LEVEL_WEAPON", 0)
        LEVEL_SECOND_JUMP = preferences.getInteger("LEVEL_SECOND_JUMP", 0)
        LEVEL_LIFE = preferences.getInteger("LEVEL_LIFE", 0)
        LEVEL_SHIELD = preferences.getInteger("LEVEL_SHIELD", 0)
    }

    fun updateBestScore(distance: Int) {
        if (bestScore < distance) {
            bestScore = distance
            save()
        }
    }
}
