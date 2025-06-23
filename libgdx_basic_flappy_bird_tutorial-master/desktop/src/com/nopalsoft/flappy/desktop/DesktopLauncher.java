package com.nopalsoft.flappy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.flappy.MainFlappyBird;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Flappy Bird";
        config.width = 480;
        config.height = 800;
        new LwjglApplication(new MainFlappyBird(), config);
    }
}
