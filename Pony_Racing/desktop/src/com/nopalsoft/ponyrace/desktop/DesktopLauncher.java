package com.nopalsoft.ponyrace.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tiledmappacker.TiledMapPacker;
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
        public void showRater() {}

        @Override
        public void showMoreGames() {


        }

        @Override
        public void showInterstitial() {

        }

        @Override
        public void showFacebook() {

        }

        @Override
        public void showAdBanner() {

        }

        @Override
        public void shareOnFacebook(String mensaje) {

        }

        @Override
        public void hideAdBanner() {

        }

        @Override
        public void shareOnTwitter(String mensaje) {

        }
    };

    static GameServicesHandler gameServicesHandler = new GameServicesHandler() {

        @Override
        public void unlockAchievement(String achievementId) {

        }

        @Override
        public void submitScore(float tiempoLap, String leaderBoard) {

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

        @Override
        public void unlockIncrementalAchievement(String achievementId, int pasosDados) {

        }
    };
}
