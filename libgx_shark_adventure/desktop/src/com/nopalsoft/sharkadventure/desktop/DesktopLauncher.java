package com.nopalsoft.sharkadventure.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.sharkadventure.MainShark;
import com.nopalsoft.sharkadventure.handlers.RequestHandler;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 480;
        new LwjglApplication(new MainShark(handler), config);
    }

    static RequestHandler handler = new RequestHandler() {

        @Override
        public void showInterstitial() {


        }

        @Override
        public void showAdBanner() {


        }

        @Override
        public void shareOnTwitter(String mensaje) {
            String tweetUrl = "https://twitter.com/intent/tweet?text=" + mensaje + " Download it from &url=https://play.google.com/&hashtags=SharkAdventure";
            Gdx.net.openURI(tweetUrl);
        }

        @Override
        public void hideAdBanner() {


        }
    };
}
