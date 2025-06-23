package com.nopalsoft.flappy.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.nopalsoft.flappy.MainFlappyBird;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        // 480 x 800 = 3:5 aspect ratio.
        // width = 3 / 5 * height; <<-- To calculate the width and keep aspect ratio given height
        int height = com.google.gwt.user.client.Window.getClientHeight();
        int width = (int) (0.6 * height);

        return new GwtApplicationConfiguration(width, height);
    }


    @Override
    public ApplicationListener createApplicationListener() {
        return new MainFlappyBird();
    }
}