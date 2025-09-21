package com.bitfire.uracer.configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.bitfire.postprocessing.filters.RadialBlur;
import com.bitfire.uracer.game.GameplaySettings.TimeDilateInputMode;
import com.bitfire.uracer.game.logic.post.effects.Ssao;

/**
 * Represents user-configurable properties.
 * <p>
 * The functionalities can be safely accessed statically just after user preferences have been loaded.
 * <p>
 * Keep in mind that querying configuration values is time consuming so use it with care and use for initialization or one-off
 * stuff. If you really need to do that per-frame then think about caching the values and just refresh them once every n-frames.
 * <p>
 * On the desktop, user settings are stored in ~/.prefs/ while on mobile we are wrapping SharedPreferences.
 */
public final class UserPreferences {

    private static Preferences prefs = null;

    private UserPreferences() {
    }

    public static void load() {
        prefs = Gdx.app.getPreferences(Storage.PREFERENCES);

        boolean isDifferent = false;
        for (Preference p : Preference.values()) {
            if (!prefs.contains(p.name())) {
                isDifferent = true;
                break;
            }
        }

        if (isDifferent || (prefs.get().isEmpty())) {
            toDefaults();
        }
    }

    public static void save() {
        prefs.flush();
        Gdx.app.debug("UserPreferences", "User preferences updated.");
    }

    private static void toDefaults() {
        prefs.clear();

        bool(Preference.PostProcessing, true);
        bool(Preference.Vignetting, true);
        bool(Preference.Bloom, true);
        bool(Preference.ZoomRadialBlur, true);
        string(Preference.ZoomRadialBlurQuality, RadialBlur.Quality.VeryHigh.toString());
        bool(Preference.CrtScreen, true);
        bool(Preference.EarthCurvature, true);
        bool(Preference.Ssao, true);
        string(Preference.SsaoQuality, Ssao.Quality.Ultra.toString());

        string(Preference.TimeDilateInputMode, TimeDilateInputMode.TouchAndRelease.toString());
        bool(Preference.NightMode, true);
        string(Preference.LastPlayedTrack, "");
        real(Preference.SfxVolume, 0.6f);
        real(Preference.MusicVolume, 0.85f);

        // ensure the new configuration gets saved
        prefs.flush();
    }

    /**
     * boolean
     */
    public static boolean bool(Preference pref) {
        return prefs.getBoolean(pref.name);
    }

    public static void bool(Preference pref, boolean value) {
        prefs.putBoolean(pref.name, value);
    }

    /**
     * get float
     */
    public static float real(Preference pref) {
        return prefs.getFloat(pref.name);
    }

    public static void real(Preference pref, float value) {
        prefs.putFloat(pref.name, value);
    }

    /**
     * get string
     */
    public static String string(Preference pref) {
        return prefs.getString(pref.name);
    }

    public static void string(Preference pref, String value) {
        prefs.putString(pref.name, value);
    }

    public enum Preference {
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

        public final String name;

        Preference() {
            this.name = this.name();
        }
    }
}
