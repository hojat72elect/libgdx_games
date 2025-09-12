package com.salvador.towers.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.salvador.towers.Main;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowSizeLimits(800, 450, 800, 450);
        config.setTitle("Towers of Hanoi");
        new Lwjgl3Application(new Main(), config);
    }
}
