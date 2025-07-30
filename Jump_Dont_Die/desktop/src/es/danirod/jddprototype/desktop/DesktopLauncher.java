package es.danirod.jddprototype.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import es.danirod.jddprototype.game.MainGame;

/**
 * This is the launcher that is used to show the game in desktop computers. While the point of this
 * tutorial is not to run the game in desktop computers, it is always useful to debug things using
 * a desktop computer.
 */
public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 640;
        config.height = 360;
        new LwjglApplication(new MainGame(), config);
    }
}
