package com.nopalsoft.zombiedash

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.nopalsoft.zombiedash.objects.Hero
import com.nopalsoft.zombiedash.screens.SettingsScreen

object Settings {
    const val NUM_GEMS_SHARE_FACEBOOK: Int = 250
    const val NUM_GEMS_INVITE_FACEBOOK: Int = 50

    const val isTest: Boolean = false
    private val pref: Preferences = Gdx.app.getPreferences("com.nopalsoft.zombiedash")
    var isMusicOn: Boolean = false

    @JvmStatic
    var isSoundOn: Boolean = false
    var showHelp: Boolean = false
    var didBuyNoAds: Boolean = false
    var didLikeFacebook: Boolean = false
    var didRate: Boolean = false
    var buttonSize: Float = 0f
    var buttonFirePositionX: Float = 0f
    var buttonFirePositionY: Float = 0f
    var buttonJumpPositionX: Float = 0f
    var buttonJumpPositionY: Float = 0f
    var numeroVecesJugadas: Int = 0
    var skinSeleccionada: Int = 0
    var gemsTotal: Int = 0
    var numBullets: Int = 0
    var bestScore: Int = 0
    var LEVEL_LIFE: Int = 0
    var LEVEL_SHIELD: Int = 0
    var LEVEL_SECOND_JUMP: Int = 0
    var LEVEL_WEAPON: Int = 0

    fun save() {
        pref.putBoolean("isMusicOn", isMusicOn)
        pref.putBoolean("isSoundOn", isSoundOn)

        pref.putBoolean("didBuyNoAds", didBuyNoAds)
        pref.putBoolean("didLikeFacebook", didLikeFacebook)
        pref.putBoolean("didRate", didRate)
        pref.putBoolean("showHelp", showHelp)

        pref.putFloat("buttonSize", buttonSize)
        pref.putFloat("buttonFirePositionX", buttonFirePositionX)
        pref.putFloat("buttonFirePositionY", buttonFirePositionY)
        pref.putFloat("buttonJumpPositionX", buttonJumpPositionX)
        pref.putFloat("buttonJumpPositionY", buttonJumpPositionY)

        pref.putInteger("numeroVecesJugadas", numeroVecesJugadas)
        pref.putInteger("skinSeleccionada", skinSeleccionada)
        pref.putInteger("gemsTotal", gemsTotal)
        pref.putInteger("numBullets", numBullets)
        pref.putInteger("bestScore", bestScore)

        pref.putInteger("LEVEL_WEAPON", LEVEL_WEAPON)
        pref.putInteger("LEVEL_SECOND_JUMP", LEVEL_SECOND_JUMP)
        pref.putInteger("LEVEL_LIFE", LEVEL_LIFE)
        pref.putInteger("LEVEL_SHIELD", LEVEL_SHIELD)

        pref.flush()
    }

    @JvmStatic
    fun load() {
        if (isTest) {
            pref.clear()
            pref.flush()
        }

        isMusicOn = pref.getBoolean("isMusicOn", true)
        isSoundOn = pref.getBoolean("isSoundOn", true)

        didBuyNoAds = pref.getBoolean("didBuyNoAds", false)
        didLikeFacebook = pref.getBoolean("didLikeFacebook", false)
        didRate = pref.getBoolean("didRate", false)
        showHelp = pref.getBoolean("showHelp", true)

        buttonSize = pref.getFloat("buttonSize", SettingsScreen.DEFAULT_SIZE_BUTTONS.toFloat())
        buttonFirePositionX = pref.getFloat("buttonFirePositionX", SettingsScreen.DEFAULT_POSITION_BUTTON_FIRE.x)
        buttonFirePositionY = pref.getFloat("buttonFirePositionY", SettingsScreen.DEFAULT_POSITION_BUTTON_FIRE.y)
        buttonJumpPositionX = pref.getFloat("buttonJumpPositionX", SettingsScreen.DEFAULT_POSITION_BUTTON_JUMP.x)
        buttonJumpPositionY = pref.getFloat("buttonJumpPositionY", SettingsScreen.DEFAULT_POSITION_BUTTON_JUMP.y)

        numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0)
        skinSeleccionada = pref.getInteger("skinSeleccionada", Hero.TIPO_SWAT)
        gemsTotal = pref.getInteger("gemsTotal", 0)
        numBullets = pref.getInteger("numBullets", 120)
        bestScore = pref.getInteger("bestScore", 0)

        LEVEL_WEAPON = pref.getInteger("LEVEL_WEAPON", 0)
        LEVEL_SECOND_JUMP = pref.getInteger("LEVEL_SECOND_JUMP", 0)
        LEVEL_LIFE = pref.getInteger("LEVEL_LIFE", 0)
        LEVEL_SHIELD = pref.getInteger("LEVEL_SHIELD", 0)

        if (isTest) {
            gemsTotal += 500000
            numBullets += 50
        }
    }

    fun saveNewButtonFireSettings(x: Float, y: Float, size: Float) {
        buttonSize = size
        buttonFirePositionX = x
        buttonFirePositionY = y
        save()
    }

    fun saveNewButtonJumpSettings(x: Float, y: Float, size: Float) {
        buttonSize = size
        buttonJumpPositionX = x
        buttonJumpPositionY = y
        save()
    }

    fun changeHighestScore(distance: Int) {
        if (bestScore < distance) {
            bestScore = distance
            save()
        }
    }
}
