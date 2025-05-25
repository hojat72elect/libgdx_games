package com.nopalsoft.ninjarunner.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.nopalsoft.ninjarunner.MainGame;
import com.nopalsoft.ninjarunner.handlers.GoogleGameServicesHandler;
import com.nopalsoft.ninjarunner.handlers.RequestHandler;

public class DesktopLauncher {

    public static MainGame game;

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 480);
        config.setTitle("Ninja Runner");
        game = new MainGame(handler, gameHandler);
        new Lwjgl3Application(game, config);
    }

    static GoogleGameServicesHandler gameHandler = new GoogleGameServicesHandler() {

        @Override
        public void unlockAchievement(String achievementId) {

        }

        @Override
        public void submitScore(long score) {

        }

        @Override
        public void signOut() {

        }

        @Override
        public void signIn() {

        }

        @Override
        public boolean isSignedIn() {
            return false;
        }

        @Override
        public void getLeaderboard() {

        }

        @Override
        public void getScores() {

        }

        @Override
        public void getAchievements() {

        }
    };

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
