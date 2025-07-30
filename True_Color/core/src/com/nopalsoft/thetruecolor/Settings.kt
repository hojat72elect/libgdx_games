package com.nopalsoft.thetruecolor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.nopalsoft.thetruecolor.scene2d.DialogHelpSettings.Languages

object Settings {
    @JvmField
    var selectedLanguage: Languages = Languages.DEFAULT

    var bestScore: Int = 0

    @JvmField
    var numberOfTimesPlayed: Int = 0

    private val preferences: Preferences = Gdx.app.getPreferences("com.nopalsoft.thetruecolor")

    @JvmStatic
    fun save() {
        preferences.putInteger("bestScore", bestScore)
        preferences.putInteger("numVecesJugadas", numberOfTimesPlayed)
        preferences.putString("selectedLanguage", selectedLanguage.toString())
        preferences.flush()
    }

    fun load() {
        bestScore = preferences.getInteger("bestScore", 0)
        numberOfTimesPlayed = preferences.getInteger("numVecesJugadas", 0)
        selectedLanguage = Languages.valueOf(preferences.getString("selectedLanguage", Languages.DEFAULT.toString()))
    }

    @JvmStatic
    fun setNewScore(score: Int) {
        if (score > bestScore) {
            bestScore = score
            save()
        }
    }
}
