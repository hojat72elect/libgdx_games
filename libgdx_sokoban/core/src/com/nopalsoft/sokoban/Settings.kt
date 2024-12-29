package com.nopalsoft.sokoban

import com.badlogic.gdx.Gdx
import com.nopalsoft.sokoban.game_objects.Level

object Settings {

    @JvmField
    var animationWalkIsON = false
    const val NUM_MAPS = 62

    @JvmField
    var arrLevel: Array<Level?>? = null // Each position is a level

    private val pref = Gdx.app.getPreferences("com.nopalsoft.sokoban")

    fun load() {
        arrLevel = arrayOfNulls(NUM_MAPS)

        animationWalkIsON = pref.getBoolean("animationWalkIsON", false)

        for (i in 0..<NUM_MAPS) {
            arrLevel?.set(i, Level())
            arrLevel?.get(i)?.numStars = pref.getInteger("numStars$i", 0)
            arrLevel?.get(i)?.bestMoves = pref.getInteger("bestMoves$i", 0)
            arrLevel?.get(i)?.bestTime = pref.getInteger("bestTime$i", 0)
        }
    }

    @JvmStatic
    fun save() {
        pref.putBoolean("animationWalkIsON", animationWalkIsON)
        pref.flush()
    }

    @JvmStatic
    fun levelCompleted(level: Int, moves: Int, time: Int) {
        arrLevel?.get(level)?.numStars = 1
        arrLevel?.get(level)?.bestMoves = moves
        arrLevel?.get(level)?.bestTime = time

        pref.putInteger("numStars$level", arrLevel?.get(level)!!.numStars)
        pref.putInteger("bestMoves$level", arrLevel?.get(level)!!.bestMoves)
        pref.putInteger("bestTime$level", arrLevel?.get(level)!!.bestTime)

        pref.flush()
    }
}