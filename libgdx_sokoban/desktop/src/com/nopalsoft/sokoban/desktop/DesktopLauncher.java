package com.nopalsoft.sokoban.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.nopalsoft.sokoban.MainSokoban;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Sokoban");
        config.setWindowedMode(800, 480);
        new Lwjgl3Application(new MainSokoban(), config);
    }
}
