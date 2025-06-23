package com.nopalsoft.lander.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.lander.MainLander;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Lander";
        config.width = 480;
        config.height = 800;
        new LwjglApplication(new MainLander(), config);
    }
}
