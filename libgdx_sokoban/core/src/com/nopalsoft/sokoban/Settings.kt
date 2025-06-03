package com.nopalsoft.sokoban

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.nopalsoft.sokoban.objects.Level

object Settings {
    @JvmField
    var isTest: Boolean = true

    @JvmField
    var animationWalkIsON: Boolean = false

    @JvmField
    var NUM_MAPS: Int = 62

    @JvmField
    var levels: Array<Level?> = emptyArray() // Each position is a level

    private val preferences: Preferences = Gdx.app.getPreferences("com.nopalsoft.sokoban")

    fun load() {
        levels = arrayOfNulls(NUM_MAPS)

        animationWalkIsON = preferences.getBoolean("animationWalkIsON", false)

        for (i in 0..<NUM_MAPS) {
            levels[i] = Level()
            levels[i]!!.numStars = preferences.getInteger("numStars$i", 0)
            levels[i]!!.bestMoves = preferences.getInteger("bestMoves$i", 0)
            levels[i]!!.bestTime = preferences.getInteger("bestTime$i", 0)
        }
    }

    @JvmStatic
    fun save() {
        preferences.putBoolean("animationWalkIsON", animationWalkIsON)
        preferences.flush()
    }

    @JvmStatic
    fun levelCompeted(level: Int, moves: Int, time: Int) {
        levels[level]!!.numStars = 1
        levels[level]!!.bestMoves = moves
        levels[level]!!.bestTime = time

        preferences.putInteger("numStars$level", levels[level]!!.numStars)
        preferences.putInteger("bestMoves$level", levels[level]!!.bestMoves)
        preferences.putInteger("bestTime$level", levels[level]!!.bestTime)

        preferences.flush()
    }
}
