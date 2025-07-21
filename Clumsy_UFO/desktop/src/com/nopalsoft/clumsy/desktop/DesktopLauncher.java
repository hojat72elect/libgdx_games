package com.nopalsoft.clumsy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.clumsy.MainClumsy;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Clumsy Ufo";
        cfg.width = 480;
        cfg.height = 800;

        new LwjglApplication(new MainClumsy(), cfg);
    }
}
