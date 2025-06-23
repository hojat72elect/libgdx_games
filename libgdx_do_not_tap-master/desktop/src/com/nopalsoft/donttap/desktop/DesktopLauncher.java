package com.nopalsoft.donttap.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.donttap.MainDoNot;
import com.nopalsoft.donttap.handlers.FloatFormatter;
import com.nopalsoft.donttap.handlers.GoogleGameServicesHandler;
import com.nopalsoft.donttap.handlers.RequestHandler;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Do not tap the wrong tile";
        cfg.width = 480;
        cfg.height = 800;
        new LwjglApplication(new MainDoNot(handler, gameHandler, formatter), cfg);
    }

    static FloatFormatter formatter = new FloatFormatter() {
        @Override
        public String format(String format, float number) {
            return String.format(format, number);
        }
    };

    static RequestHandler handler = new RequestHandler() {

        @Override
        public void showRater() {
            // TODO Auto-generated method stub
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
        public void hideAdBanner() {
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
        public void submitScoreTimeMode(long score) {
            // TODO Auto-generated method stub

        }

        @Override
        public void submitScoreClassicMode(float score) {
            // TODO Auto-generated method stub

        }

        @Override
        public void submitScoreEndlessMode(long score) {
            // TODO Auto-generated method stub

        }
    };
}
