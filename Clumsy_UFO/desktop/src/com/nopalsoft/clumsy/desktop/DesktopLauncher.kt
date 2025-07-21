package com.nopalsoft.clumsy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.clumsy.ClumsyUfoGame;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Clumsy Ufo";
        config.width = 480;
        config.height = 800;

        new LwjglApplication(new ClumsyUfoGame(), config);
    }
}
