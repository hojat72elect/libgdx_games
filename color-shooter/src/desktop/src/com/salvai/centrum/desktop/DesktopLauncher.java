package com.salvai.centrum.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.salvai.centrum.CentrumGameClass;
import com.salvai.centrum.utils.Constants;
import com.salvai.centrum.utils.Text;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//        config.width = (int) Constants.SCREEN_WIDTH;
//        config.height = (int) Constants.SCREEN_HEIGHT;
        config.title = Text.GAME_NAME;
        new LwjglApplication(new CentrumGameClass(), config);
    }
}
