package com.nopalsoft.ponyrace.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.ponyrace.MainPonyRace;
import com.nopalsoft.ponyrace.handlers.FloatFormatter;
import com.nopalsoft.ponyrace.handlers.GameServicesHandler;
import com.nopalsoft.ponyrace.handlers.RequestHandler;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Pony Racing";
        cfg.width = 800;
        cfg.height = 480;
        new LwjglApplication(new MainPonyRace(handler, gameServicesHandler, formatter), cfg);
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
        public void shareOnFacebook(String mensaje) {
            // TODO Auto-generated method stub

        }

        @Override
        public void hideAdBanner() {
            // TODO Auto-generated method stub

        }

        @Override
        public void shareOnTwitter(String mensaje) {
            // TODO Auto-generated method stub

        }
    };

    static GameServicesHandler gameServicesHandler = new GameServicesHandler() {

        @Override
        public void unlockAchievement(String achievementId) {
            // TODO Auto-generated method stub

        }

        @Override
        public void submitScore(float tiempoLap, String leaderBoard) {
            // TODO Auto-generated method stub

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
        public boolean isSignedIn() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void signIn() {
            // TODO Auto-generated method stub

        }

        @Override
        public void signOut() {
            // TODO Auto-generated method stub

        }

        @Override
        public void unlockIncrementalAchievement(String achievementId, int pasosDados) {
            // TODO Auto-generated method stub

        }
    };
}
