package com.nopalsoft.sokoban.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.nopalsoft.sokoban.MainSokoban;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        // 800 x 480 = 5:3 aspect ratio.
        // height = 5 / 3 * width; <<-- To calculate the height and keep aspect ratio given width
        int height = com.google.gwt.user.client.Window.getClientHeight();
        int width = (int) (1.6 * height);
        return new GwtApplicationConfiguration(width, height);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new MainSokoban();
    }
}