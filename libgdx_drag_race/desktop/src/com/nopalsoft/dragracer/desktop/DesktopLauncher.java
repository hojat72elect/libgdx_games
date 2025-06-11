package com.nopalsoft.dragracer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.dragracer.MainStreet;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "StreetSwipinRace";
        cfg.width = 480;
        cfg.height = 800;

        new LwjglApplication(new MainStreet(), cfg);
    }

}
