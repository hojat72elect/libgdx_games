

package com.watabou.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.watabou.noosa.Game;

//TODO migrate to platformSupport class
public class DeviceCompat {

    public static boolean supportsFullScreen() {
        switch (Gdx.app.getType()) {
            case Android:
                //Android 4.4+ supports hiding UI via immersive mode
                return Gdx.app.getVersion() >= 19;
            case iOS:
                //iOS supports hiding UI via drawing into the gesture safe area
                return Gdx.graphics.getSafeInsetBottom() != 0;
            default:
                //TODO implement functionality for other platforms here
                return true;
        }
    }

    //return APi level on Android, major OS version on iOS, 0 on desktop
    public static int getPlatformVersion() {
        return Gdx.app.getVersion();
    }

    public static boolean isAndroid() {
        return SharedLibraryLoader.isAndroid;
    }

    public static boolean isiOS() {
        return SharedLibraryLoader.isIos;
    }

    public static boolean isDesktop() {
        return SharedLibraryLoader.isWindows || SharedLibraryLoader.isMac || SharedLibraryLoader.isLinux;
    }

    public static boolean hasHardKeyboard() {
        return Gdx.input.isPeripheralAvailable(Input.Peripheral.HardwareKeyboard);
    }

    public static boolean isDebug() {
        return Game.version.contains("INDEV");
    }

    public static void log(String tag, String message) {
        Gdx.app.log(tag, message);
    }

    public static RectF getSafeInsets() {
        RectF result = new RectF();
        result.left = Gdx.graphics.getSafeInsetLeft();
        result.top = Gdx.graphics.getSafeInsetTop();
        result.right = Gdx.graphics.getSafeInsetRight();
        result.bottom = Gdx.graphics.getSafeInsetBottom();
        return result;
    }
}
