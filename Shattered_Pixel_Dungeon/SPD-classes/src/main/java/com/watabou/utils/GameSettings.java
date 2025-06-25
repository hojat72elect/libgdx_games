

package com.watabou.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.watabou.noosa.Game;

public class GameSettings {

    public static final String DEFAULT_PREFS_FILE = "settings.xml";

    private static Preferences prefs;

    private static Preferences get() {
        if (prefs == null) {
            prefs = Gdx.app.getPreferences(DEFAULT_PREFS_FILE);
        }
        return prefs;
    }

    //allows setting up of preferences directly during game initialization
    public static void set(Preferences prefs) {
        GameSettings.prefs = prefs;
    }

    public static boolean contains(String key) {
        return get().contains(key);
    }

    public static int getInt(String key, int defValue) {
        return getInt(key, defValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int getInt(String key, int defValue, int min, int max) {
        try {
            int i = get().getInteger(key, defValue);
            if (i < min || i > max) {
                int val = (int) GameMath.gate(min, i, max);
                put(key, val);
                return val;
            } else {
                return i;
            }
        } catch (Exception e) {
            Game.reportException(e);
            put(key, defValue);
            return defValue;
        }
    }

    public static long getLong(String key, long defValue) {
        return getLong(key, defValue, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static long getLong(String key, long defValue, long min, long max) {
        try {
            long i = get().getLong(key, defValue);
            if (i < min || i > max) {
                long val = (long) GameMath.gate(min, i, max);
                put(key, val);
                return val;
            } else {
                return i;
            }
        } catch (Exception e) {
            Game.reportException(e);
            put(key, defValue);
            return defValue;
        }
    }

    public static boolean getBoolean(String key, boolean defValue) {
        try {
            return get().getBoolean(key, defValue);
        } catch (Exception e) {
            Game.reportException(e);
            return defValue;
        }
    }

    public static String getString(String key, String defValue) {
        return getString(key, defValue, Integer.MAX_VALUE);
    }

    public static String getString(String key, String defValue, int maxLength) {
        try {
            String s = get().getString(key, defValue);
            if (s != null && s.length() > maxLength) {
                put(key, defValue);
                return defValue;
            } else {
                return s;
            }
        } catch (Exception e) {
            Game.reportException(e);
            put(key, defValue);
            return defValue;
        }
    }

    public static void put(String key, int value) {
        get().putInteger(key, value);
        get().flush();
    }

    public static void put(String key, long value) {
        get().putLong(key, value);
        get().flush();
    }

    public static void put(String key, boolean value) {
        get().putBoolean(key, value);
        get().flush();
    }

    public static void put(String key, String value) {
        get().putString(key, value);
        get().flush();
    }
}
