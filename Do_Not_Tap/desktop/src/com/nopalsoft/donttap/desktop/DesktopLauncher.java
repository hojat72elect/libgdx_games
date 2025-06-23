package com.nopalsoft.donttap.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.donttap.MainDoNot;
import com.nopalsoft.donttap.handlers.FloatFormatter;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Do not tap the wrong tile";
        cfg.width = 480;
        cfg.height = 800;
        new LwjglApplication(new MainDoNot(formatter), cfg);
    }

    static FloatFormatter formatter = new FloatFormatter() {
        @Override
        public String format(String format, float number) {
            return String.format(format, number);
        }
    };
}
