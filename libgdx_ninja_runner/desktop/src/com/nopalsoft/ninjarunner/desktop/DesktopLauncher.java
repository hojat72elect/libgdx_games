package com.nopalsoft.ninjarunner.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.nopalsoft.ninjarunner.MainGame;
import com.nopalsoft.ninjarunner.handlers.RequestHandler;

public class DesktopLauncher {

    public static MainGame game;

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 480);
        config.setTitle("Ninja Runner");
        game = new MainGame(handler);
        new Lwjgl3Application(game, config);
    }

    static RequestHandler handler = new RequestHandler() {

        @Override
        public void showRater() {

        }

        @Override
        public void showMoreGames() {

        }

        @Override
        public void showInterstitial() {

        }

        @Override
        public void showAdBanner() {

        }

        @Override
        public void shareApp() {

        }

        @Override
        public void removeAds() {

        }

        @Override
        public void hideAdBanner() {

        }

        @Override
        public void buy5milCoins() {

        }

        @Override
        public void buy50milCoins() {

        }

        @Override
        public void buy30milCoins() {

        }

        @Override
        public void buy15milCoins() {

        }

        @Override
        public void loadInterstitial() {

        }
    };
}
