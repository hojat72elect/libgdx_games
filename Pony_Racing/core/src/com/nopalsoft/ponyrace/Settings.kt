package com.nopalsoft.ponyrace

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    const val MONEDAS_REGALO_FACEBOOK: Int = 3500
    const val MONEDAS_REGALO_SHARE_FACEBOOK: Int = 1000

    /**
     * I didn't use enums because with the way implemented to change the difficulty it was easier to use integers.
     */
    const val DIFFICULTY_EASY: Int = 0
    const val DIFFICULTY_NORMAL: Int = 1
    const val DIFFICULTY_HARD: Int = 2
    const val DIFFICULTY_VERY_HARD: Int = 3

    private val pref: Preferences = Gdx.app.getPreferences("com.nopalsoft.ponyRace.settings")

    @JvmField
    var isEnabledSecretWorld: Boolean = false // This variable is always false at startup and is not saved.

    @JvmField
    var isSoundOn: Boolean = false

    @JvmField
    var isMusicOn: Boolean = false

    @JvmField
    var numberOfGameLevelsUnlocked: Int = 0
    var wasAppLiked: Boolean = false

    @JvmField
    var wasAppRated: Boolean = false

    var selectedSkin: String? = null

    @JvmField
    var numeroBombas: Int = 0 // Bombs to use in game

    @JvmField
    var numeroWoods: Int = 0 // Logs available for use in game
    var numeroMonedasActual: Int = 0 // Coins available to spend
    var totalCoinsCollected: Int = 0 // Total coin statistics.

    @JvmField
    var statTimesPlayed: Int = 0

    @JvmField
    var difficultyLevel: Int = DIFFICULTY_NORMAL

    @JvmField
    var chocolateLevel: Int = 0
    var balloonLevel: Int = 0
    var chiliLevel: Int = 0
    var bombLevel: Int = 0
    var woodLevel: Int = 0

    @JvmField
    var coinLevel: Int = 0

    @JvmField
    var timeLevel: Int = 0

    @JvmField
    var isBackGroundEnabled: Boolean = true

    fun cargar() {
        isSoundOn = pref.getBoolean("isSonidoON", true)
        isMusicOn = pref.getBoolean("isMusicaON", true)

        wasAppLiked = pref.getBoolean("seDioLike", false)
        wasAppRated = pref.getBoolean("seCalificoApp", false)

        selectedSkin = pref.getString("skinSeleccionada", "Cloud")
        numberOfGameLevelsUnlocked = pref.getInteger("mundosDesbloqueados", 1)

        // Usable items in the game Bomb coins
        numeroBombas = pref.getInteger("numeroBombas", 25)
        numeroWoods = pref.getInteger("numeroWoods", 25)
        numeroMonedasActual = pref.getInteger("numeroMonedasActual", 500)

        // Coin Statistics
        totalCoinsCollected = pref.getInteger("statMonedasTotal", 0)
        statTimesPlayed = pref.getInteger("statTimesPlayed", 0)

        // Levels of things
        chocolateLevel = pref.getInteger("nivelChocolate", 0)
        balloonLevel = pref.getInteger("nivelBallon", 0)
        chiliLevel = pref.getInteger("nivelChili", 0)
        bombLevel = pref.getInteger("nivelBomb", 0)
        woodLevel = pref.getInteger("nivelWood", 0)
        coinLevel = pref.getInteger("nivelCoin", 0)
        timeLevel = pref.getInteger("nivelTime", 0)

        difficultyLevel = pref.getInteger(
            "dificultadActual",
            DIFFICULTY_NORMAL
        )
    }

    fun guardar() {
        pref.putBoolean("isSonidoON", isSoundOn)
        pref.putBoolean("isMusicaON", isMusicOn)

        pref.putBoolean("seDioLike", wasAppLiked)
        pref.putBoolean("seCalificoApp", wasAppRated)

        pref.putString("skinSeleccionada", selectedSkin)
        pref.putInteger("mundosDesbloqueados", numberOfGameLevelsUnlocked)

        pref.putInteger("numeroBombas", numeroBombas)
        pref.putInteger("numeroWoods", numeroWoods)
        pref.putInteger("numeroMonedasActual", numeroMonedasActual)

        pref.putInteger("statMonedasTotal", totalCoinsCollected)
        pref.putInteger("statTimesPlayed", statTimesPlayed)

        pref.putInteger("nivelChocolate", chocolateLevel)
        pref.putInteger("nivelBallon", balloonLevel)
        pref.putInteger("nivelChili", chiliLevel)
        pref.putInteger("nivelBomb", bombLevel)
        pref.putInteger("nivelWood", woodLevel)
        pref.putInteger("nivelCoin", coinLevel)
        pref.putInteger("nivelTime", timeLevel)

        pref.putInteger("dificultadActual", difficultyLevel)
        pref.flush()
    }

    /**
     * Adds numberOfCoins to the total coins to spend and to the statistics for coins collected for achievements.
     *
     * @param numberOfCoins Number of coins to increase.
     */
    @JvmStatic
    fun sumarMonedas(numberOfCoins: Int) {
        numeroMonedasActual += numberOfCoins
        // These increase by 1 regardless of whether a coin worth x2 or x3 has been taken. To make it easier to implement Google Game Services.
        totalCoinsCollected += numberOfCoins
    }
}
