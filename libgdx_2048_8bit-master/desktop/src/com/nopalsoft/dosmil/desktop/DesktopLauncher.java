package com.nopalsoft.dosmil.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.dosmil.MainGame;
import com.nopalsoft.dosmil.handlers.GoogleGameServicesHandler;
import com.nopalsoft.dosmil.handlers.RequestHandler;

public class DesktopLauncher {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "2048 8Bit";
        cfg.width = 480;
        cfg.height = 800;

        new LwjglApplication(new MainGame(reqHandler, gameHandler), cfg);
    }

    static RequestHandler reqHandler = new RequestHandler() {

        @Override
        public void showRater() {
            // TODO Auto-generated method stub

        }

        @Override
        public void loadInterstitial() {

        }

        @Override
        public void showMoreGames() {
            // TODO Auto-generated method stub

        }

        @Override
        public void showInterstitial() {
            // TODO Auto-generated method stub

        }

        @Override
        public void showFacebook() {
            // TODO Auto-generated method stub

        }

        @Override
        public void showAdBanner() {
            // TODO Auto-generated method stub

        }

        @Override
        public void shareOnTwitter(String mensaje) {
            // TODO Auto-generated method stub

        }

        @Override
        public void shareOnFacebook(String mensaje) {
            // TODO Auto-generated method stub

        }

        @Override
        public void restorePurchases() {
            // TODO Auto-generated method stub

        }

        @Override
        public void removeAds() {
            // TODO Auto-generated method stub

        }

        @Override
        public void hideAdBanner() {
            // TODO Auto-generated method stub

        }

        @Override
        public void buyNoAds() {
            // TODO Auto-generated method stub

        }
    };

    static GoogleGameServicesHandler gameHandler = new GoogleGameServicesHandler() {

        @Override
        public void unlockAchievement(String achievementId) {
            // TODO Auto-generated method stub

        }

        @Override
        public void signOut() {
            // TODO Auto-generated method stub

        }

        @Override
        public void signIn() {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isSignedIn() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void getLeaderboard() {
            // TODO Auto-generated method stub

        }

        @Override
        public void getAchievements() {
            // TODO Auto-generated method stub

        }

        @Override
        public void submitScore(long score) {
            // TODO Auto-generated method stub

        }
    };
}
