package com.bitfire.uracer.configuration

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.bitfire.postprocessing.filters.RadialBlur
import com.bitfire.uracer.game.GameplaySettings.TimeDilateInputMode
import com.bitfire.uracer.game.logic.post.effects.Ssao

/**
 * Represents user-configurable properties.
 * The functionalities can be safely accessed statically just after user preferences have been loaded.
 * Keep in mind that querying configuration values is time consuming so use it with care and use for initialization or one-off
 * stuff. If you really need to do that per-frame then think about caching the values and just refresh them once every n-frames.
 * On the desktop, user settings are stored in ~/.prefs/ while on mobile we are wrapping SharedPreferences.
 */
object UserPreferences {

    private lateinit var prefs: Preferences

    @JvmStatic
    fun load() {
        prefs = Gdx.app.getPreferences(Storage.PREFERENCES)

        var isDifferent = false
        for (p in Preference.entries) {
            if (!prefs.contains(p.name)) {
                isDifferent = true
                break
            }
        }
        if (isDifferent || (prefs.get().isEmpty())) toDefaults()
    }

    @JvmStatic
    fun save() {
        prefs.flush()
        Gdx.app.debug("UserPreferences", "User preferences updated.")
    }

    private fun toDefaults() {
        prefs.clear()

        bool(Preference.PostProcessing, true)
        bool(Preference.Vignetting, true)
        bool(Preference.Bloom, true)
        bool(Preference.ZoomRadialBlur, true)
        string(Preference.ZoomRadialBlurQuality, RadialBlur.Quality.VeryHigh.toString())
        bool(Preference.CrtScreen, true)
        bool(Preference.EarthCurvature, true)
        bool(Preference.Ssao, true)
        string(Preference.SsaoQuality, Ssao.Quality.Ultra.toString())

        string(Preference.TimeDilateInputMode, TimeDilateInputMode.TouchAndRelease.toString())
        bool(Preference.NightMode, true)
        string(Preference.LastPlayedTrack, "")
        real(Preference.SfxVolume, 0.6f)
        real(Preference.MusicVolume, 0.85f)

        prefs.flush()
    }

    @JvmStatic
    fun bool(pref: Preference) = prefs.getBoolean(pref.name)

    @JvmStatic
    fun bool(pref: Preference, value: Boolean) {
        prefs.putBoolean(pref.name, value)
    }

    @JvmStatic
    fun real(pref: Preference) = prefs.getFloat(pref.name)

    fun real(pref: Preference, value: Float) {
        prefs.putFloat(pref.name, value)
    }

    @JvmStatic
    fun string(pref: Preference): String = prefs.getString(pref.name)

    @JvmStatic
    fun string(pref: Preference, value: String) {
        prefs.putString(pref.name, value)
    }

    enum class Preference {
        PostProcessing,
        Vignetting,
        Bloom,
        ZoomRadialBlur,
        ZoomRadialBlurQuality,
        CrtScreen,
        EarthCurvature,
        Ssao,
        SsaoQuality,
        TimeDilateInputMode,
        NightMode,
        LastPlayedTrack,
        SfxVolume,
        MusicVolume,
        ;
    }
}
