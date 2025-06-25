

package com.watabou.utils;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.watabou.noosa.Game;

//wrapper for libGDX reflection
public class Reflection {

    public static boolean isMemberClass(Class cls) {
        return ClassReflection.isMemberClass(cls);
    }

    public static boolean isStatic(Class cls) {
        return ClassReflection.isStaticClass(cls);
    }

    public static <T> T newInstance(Class<T> cls) {
        try {
            return ClassReflection.newInstance(cls);
        } catch (Exception e) {
            Game.reportException(e);
            return null;
        }
    }

    public static <T> T newInstanceUnhandled(Class<T> cls) throws Exception {
        return ClassReflection.newInstance(cls);
    }

    public static Class forName(String name) {
        try {
            return ClassReflection.forName(name);
        } catch (Exception e) {
            Game.reportException(e);
            return null;
        }
    }

    public static Class forNameUnhandled(String name) throws Exception {
        return ClassReflection.forName(name);
    }
}
