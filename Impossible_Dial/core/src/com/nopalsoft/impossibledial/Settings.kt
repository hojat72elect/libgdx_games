package com.nopalsoft.impossibledial

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    private val preferences: Preferences = Gdx.app.getPreferences("com.nopalsoft.impossibledial")

    @JvmField
    var bestScoreEasy: Int = 0

    @JvmField
    var bestScoreHard: Int = 0

    @JvmField
    var numVecesJugadas: Int = 0

    @JvmStatic
    fun save() {
        preferences.putInteger("bestScore", bestScoreEasy)
        preferences.putInteger("bestScoreHard", bestScoreHard)
        preferences.putInteger("numVecesJugadas", numVecesJugadas)
        preferences.flush()
    }

    @JvmStatic
    fun load() {
        bestScoreEasy = preferences.getInteger("bestScore", 0)
        bestScoreHard = preferences.getInteger("bestScoreHard", 0)
        numVecesJugadas = preferences.getInteger("numVecesJugadas", 0)
    }

    @JvmStatic
    fun setNewScoreEasy(score: Int) {
        if (score > bestScoreEasy) {
            bestScoreEasy = score
            save()
        }
    }

    @JvmStatic
    fun setNewScoreHard(score: Int) {
        if (score > bestScoreHard) {
            bestScoreHard = score
            save()
        }
    }
}
