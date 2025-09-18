package com.bitfire.uracer.game.screens

import com.bitfire.uracer.configuration.UserPreferences
import com.bitfire.uracer.configuration.UserPreferences.Preference

object ScreensShared {
    @JvmField
    var selectedLevelId = ""

    @JvmStatic
    fun loadFromUserPrefs() {
        val lastTrack = UserPreferences.string(Preference.LastPlayedTrack)
        if (lastTrack.isNotEmpty()) selectedLevelId = lastTrack
    }
}
