package com.salvai.centrum.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.salvai.centrum.CentrumGameClass;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
//        config.width = (int) Constants.SCREEN_WIDTH;
//        config.height = (int) Constants.SCREEN_HEIGHT;
//        config.title = Text.GAME_NAME;
        new Lwjgl3Application(new CentrumGameClass(), config);
    }
}
