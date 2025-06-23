package com.nopalsoft.dosmil.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.dosmil.MainGame;

public class DesktopLauncher {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "2048 8Bit";
        cfg.width = 480;
        cfg.height = 800;

        new LwjglApplication(new MainGame(), cfg);
    }

}
