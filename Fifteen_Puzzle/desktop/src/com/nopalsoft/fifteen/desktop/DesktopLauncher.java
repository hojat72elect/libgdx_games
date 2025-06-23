package com.nopalsoft.fifteen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.fifteen.MainFifteen;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "FifteenPuzzle";
        cfg.width = 480;
        cfg.height = 800;

        new LwjglApplication(
                new MainFifteen(), cfg);
    }

}
