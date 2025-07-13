package com.nopalsoft.dosmil.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.dosmil.MainGame;

public class DesktopLauncher {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "2048 8Bit";
        config.width = 480;
        config.height = 800;

        new LwjglApplication(new MainGame(), config);
    }
}
