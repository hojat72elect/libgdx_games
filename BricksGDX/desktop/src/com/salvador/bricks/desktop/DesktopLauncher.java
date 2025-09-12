package com.salvador.bricks.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.salvador.bricks.BrickBreaker;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Arkanoid";
        cfg.height = 800;
        cfg.width = 450;
        new LwjglApplication(new BrickBreaker(), cfg);
    }
}
