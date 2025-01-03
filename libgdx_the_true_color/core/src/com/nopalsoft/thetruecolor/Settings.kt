package com.nopalsoft.thetruecolor

import com.badlogic.gdx.Gdx
import com.nopalsoft.thetruecolor.scene2d.DialogHelpSettings.Languages

object Settings {

    @JvmField
    var selectedLanguage = Languages.DEFAULT
    var bestScore = 0

    @JvmField
    var numTimesPlayed = 0

    private val pref = Gdx.app.getPreferences("com.nopalsoft.thetruecolor")

    @JvmStatic
    fun save() {
        pref.putInteger("bestScore", bestScore)
        pref.putInteger("numVecesJugadas", numTimesPlayed)
        pref.putString("selectedLanguage", selectedLanguage.toString())
        pref.flush()
    }

    @JvmStatic
    fun load() {
        bestScore = pref.getInteger("bestScore", 0)
        numTimesPlayed = pref.getInteger("numVecesJugadas", 0)
        selectedLanguage =
            Languages.valueOf(pref.getString("selectedLanguage", Languages.DEFAULT.toString()))
    }

    @JvmStatic
    fun setNewScore(score: Int) {
        if (score > bestScore) {
            bestScore = score
            save()
        }
    }
}