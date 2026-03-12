package com.nopalsoft.ninjarunner

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.nopalsoft.ninjarunner.game_objects.Mascot.MascotType
import com.nopalsoft.ninjarunner.game_objects.Player

object Settings {
    @JvmField
    var isSoundEnabled: Boolean = false

    @JvmField
    var isMusicEnabled: Boolean = false

    @JvmField
    var selectedSkin: Int = Player.TYPE_NINJA

    @JvmField
    var totalCoins: Int = 1500000

    @JvmField
    var selectedMascot: MascotType = MascotType.PINK_BIRD

    @JvmField
    var MASCOT_LEVEL_BIRD: Int = 0

    @JvmField
    var MASCOT_LEVEL_BOMB: Int = 0

    @JvmField
    var LEVEL_MAGNET: Int = 0

    @JvmField
    var LEVEL_LIFE: Int = 0

    @JvmField
    var LEVEL_ENERGY: Int = 0

    @JvmField
    var LEVEL_COINS: Int = 0

    @JvmField
    var LEVEL_TREASURE_CHEST: Int = 0

    private val preferences: Preferences = Gdx.app.getPreferences("com.nopalsoft.ninjarunner.settings")

    @JvmField
    var bestScore: Long = 0

    fun load() {
        LEVEL_MAGNET = preferences.getInteger("LEVEL_MAGNET", 0)
        LEVEL_LIFE = preferences.getInteger("LEVEL_LIFE", 0)
        LEVEL_ENERGY = preferences.getInteger("LEVEL_ENERGY", 0)
        LEVEL_COINS = preferences.getInteger("LEVEL_COINS", 0)
        LEVEL_TREASURE_CHEST = preferences.getInteger("LEVEL_TREASURE_CHEST", 0)
    }

    @JvmStatic
    fun setNewScore(score: Long) {
        if (score > bestScore) {
            bestScore = score
        }
    }
}
