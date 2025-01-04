package com.nopalsoft.clumsy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.clumsy.MainClumsy;
import com.nopalsoft.clumsy.handlers.GoogleGameServicesHandler;
import com.nopalsoft.clumsy.handlers.RequestHandler;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Clumsy Ufo";
        cfg.width = 480;
        cfg.height = 800;

        new LwjglApplication(new MainClumsy(reqHandler, gameHandler), cfg);
    }

    static RequestHandler reqHandler = new RequestHandler() {

        @Override
        public void showRater() {

        }

        @Override
        public void showInterstitial() {

        }

        @Override
        public void showFacebook() {

        }

        @Override
        public void showMoreGames() {

        }

        @Override
        public void shareOnFacebook(String mensaje) {

        }

        @Override
        public void showAdBanner() {

        }

        @Override
        public void hideAdBanner() {

        }

        @Override
        public void comprarRemoveAds() {

        }

        @Override
        public void shareOnTwitter(String mensaje) {

        }

        @Override
        public void restorePurchases() {

        }
    };

    static GoogleGameServicesHandler gameHandler = new GoogleGameServicesHandler() {

        @Override
        public void submitScore(String leaderboard, long score) {

        }

        @Override
        public void unlockAchievement(String achievementId) {

        }

        @Override
        public void getLeaderboard() {

        }

        @Override
        public void getAchievements() {

        }

        @Override
        public boolean isSignedIn() {
            return false;
        }

        @Override
        public void signIn() {

        }

        @Override
        public void signOut() {

        }
    };
}
