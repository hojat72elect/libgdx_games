package com.nopalsoft.ponyrace.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.ponyrace.MainPonyRace;
import com.nopalsoft.ponyrace.handlers.FloatFormatter;


public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Pony Racing";
        cfg.width = 800;
        cfg.height = 480;
        new LwjglApplication(new MainPonyRace(formatter), cfg);
    }

    static FloatFormatter formatter = String::format;

}
