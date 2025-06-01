package com.nopalsoft.superjumper.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.nopalsoft.superjumper.MainSuperJumper;
import com.nopalsoft.superjumper.handlers.GameServicesHandler;
import com.nopalsoft.superjumper.handlers.RequestHandler;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(480, 800);
        new Lwjgl3Application(new MainSuperJumper(handler, gameHandler), config);
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
        public void shareOnTwitter(String mensaje) {

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
    };


    static GameServicesHandler gameHandler = new GameServicesHandler() {

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
        public void getAchievements() {

        }
    };
}
