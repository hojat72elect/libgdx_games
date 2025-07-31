package com.nopalsoft.zombiekiller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.nopalsoft.zombiekiller.game_objects.Hero
import com.nopalsoft.zombiekiller.screens.SettingsScreen

object Settings {
    const val NUM_GEMS_INVITE_FACEBOOK: Int = 50

    const val isTest: Boolean = false
    const val NUM_MAPS: Int = 41
    private val pref: Preferences = Gdx.app.getPreferences("com.nopalsoft.zombiekiller")
    var isMusicOn: Boolean = false
    var isSoundOn: Boolean = false
    var didBuyNoAds: Boolean = false
    var didLikeFacebook: Boolean = false
    var didRate: Boolean = false
    var isPadEnabled: Boolean = false
    var padSize: Float = 0f
    var padPositionX: Float = 0f
    var padPositionY: Float = 0f
    var buttonSize: Float = 0f
    var buttonFirePositionX: Float = 0f
    var buttonFirePositionY: Float = 0f
    var buttonJumpPositionX: Float = 0f
    var buttonJumpPositionY: Float = 0f
    var numeroVecesJugadas: Int = 0
    var skinSeleccionada: Int = 0
    var gemsTotal: Int = 0
    var zombiesKilled: Long = 0
    var LEVEL_WEAPON: Int = 0
    var LEVEL_CHANCE_DROP: Int = 0
    var LEVEL_LIFE: Int = 0
    var LEVEL_SHIELD: Int = 0
    lateinit var arrSkullsMundo: IntArray // Cada posicion es un mundo

    fun save() {
        pref.putBoolean("isMusicOn", isMusicOn)
        pref.putBoolean("isSoundOn", isSoundOn)

        pref.putBoolean("didBuyNoAds", didBuyNoAds)
        pref.putBoolean("didLikeFacebook", didLikeFacebook)
        pref.putBoolean("didRate", didRate)

        pref.putBoolean("isPadEnabled", isPadEnabled)
        pref.putFloat("padSize", padSize)
        pref.putFloat("padPositionX", padPositionX)
        pref.putFloat("padPositionY", padPositionY)
        pref.putFloat("buttonSize", buttonSize)
        pref.putFloat("buttonFirePositionX", buttonFirePositionX)
        pref.putFloat("buttonFirePositionY", buttonFirePositionY)
        pref.putFloat("buttonJumpPositionX", buttonJumpPositionX)
        pref.putFloat("buttonJumpPositionY", buttonJumpPositionY)

        pref.putInteger("numeroVecesJugadas", numeroVecesJugadas)
        pref.putInteger("skinSeleccionada", skinSeleccionada)
        pref.putInteger("gemsTotal", gemsTotal)
        pref.putLong("zombiesKilled", zombiesKilled)

        pref.putInteger("LEVEL_WEAPON", LEVEL_WEAPON)
        pref.putInteger("LEVEL_CHANCE_DROP", LEVEL_CHANCE_DROP)
        pref.putInteger("LEVEL_LIFE", LEVEL_LIFE)
        pref.putInteger("LEVEL_SHIELD", LEVEL_SHIELD)

        for (i in arrSkullsMundo.indices) {
            pref.putInteger("arrSkullsMundo" + i, arrSkullsMundo[i])
        }
        pref.flush()
    }

    fun load() {
        arrSkullsMundo = IntArray(NUM_MAPS)

        if (isTest) {
            pref.clear()
            pref.flush()
        }

        isMusicOn = pref.getBoolean("isMusicOn", true)
        isSoundOn = pref.getBoolean("isSoundOn", true)

        didBuyNoAds = pref.getBoolean("didBuyNoAds", false)
        didLikeFacebook = pref.getBoolean("didLikeFacebook", false)
        didRate = pref.getBoolean("didRate", false)

        isPadEnabled = pref.getBoolean("isPadEnabled", false)
        padSize = pref.getFloat("padSize", SettingsScreen.DEFAULT_SIZE_PAD.toFloat())
        padPositionX = pref.getFloat("padPositionX", SettingsScreen.DEFAULT_POSITION_PAD.x)
        padPositionY = pref.getFloat("padPositionY", SettingsScreen.DEFAULT_POSITION_PAD.y)
        buttonSize = pref.getFloat("buttonSize", SettingsScreen.DEFAULT_SIZE_BUTTONS.toFloat())
        buttonFirePositionX = pref.getFloat("buttonFirePositionX", SettingsScreen.DEFAULT_POSITION_BUTTON_FIRE.x)
        buttonFirePositionY = pref.getFloat("buttonFirePositionY", SettingsScreen.DEFAULT_POSITION_BUTTON_FIRE.y)
        buttonJumpPositionX = pref.getFloat("buttonJumpPositionX", SettingsScreen.DEFAULT_POSITION_BUTTON_JUMP.x)
        buttonJumpPositionY = pref.getFloat("buttonJumpPositionY", SettingsScreen.DEFAULT_POSITION_BUTTON_JUMP.y)

        numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0)
        skinSeleccionada = pref.getInteger("skinSeleccionada", Hero.TYPE_SWAT)
        gemsTotal = pref.getInteger("gemsTotal", 0)
        zombiesKilled = pref.getLong("zombiesKilled", 0)

        LEVEL_WEAPON = pref.getInteger("LEVEL_WEAPON", 0)
        LEVEL_CHANCE_DROP = pref.getInteger("LEVEL_CHANCE_DROP", 0)
        LEVEL_LIFE = pref.getInteger("LEVEL_LIFE", 0)
        LEVEL_SHIELD = pref.getInteger("LEVEL_SHIELD", 0)

        for (i in 0..<NUM_MAPS) {
            arrSkullsMundo[i] = pref.getInteger("arrSkullsMundo" + i, 0)
        }

        if (isTest) {
            gemsTotal += 999999
        }
    }

    fun saveLevel(level: Int, skulls: Int) {
        val actualSkulls = arrSkullsMundo[level]
        if (actualSkulls < skulls) {
            arrSkullsMundo[level] = skulls
        }
        save()
    }

    fun saveNewPadSettings(x: Float, y: Float, size: Float) {
        padSize = size
        padPositionX = x
        padPositionY = y
        save()
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
}
