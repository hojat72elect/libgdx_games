package com.nopalsoft.impossibledial

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    private val preferences: Preferences = Gdx.app.getPreferences("com.nopalsoft.impossibledial")

    var bestScoreEasy: Int = 0

    var bestScoreHard: Int = 0

    var numVecesJugadas: Int = 0

    fun save() {
        preferences.putInteger("bestScore", bestScoreEasy)
        preferences.putInteger("bestScoreHard", bestScoreHard)
        preferences.putInteger("numVecesJugadas", numVecesJugadas)
        preferences.flush()
    }

    fun load() {
        bestScoreEasy = preferences.getInteger("bestScore", 0)
        bestScoreHard = preferences.getInteger("bestScoreHard", 0)
        numVecesJugadas = preferences.getInteger("numVecesJugadas", 0)
    }

    fun setNewScoreEasy(score: Int) {
        if (score > bestScoreEasy) {
            bestScoreEasy = score
            save()
        }
    }

    fun setNewScoreHard(score: Int) {
        if (score > bestScoreHard) {
            bestScoreHard = score
            save()
        }
    }
}
