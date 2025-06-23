package com.nopalsoft.zombiekiller.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tiledmappacker.TiledMapPacker;
import com.nopalsoft.zombiekiller.MainZombie;
import com.nopalsoft.zombiekiller.Settings;
import com.nopalsoft.zombiekiller.handlers.FacebookHandler;
import com.nopalsoft.zombiekiller.handlers.GoogleGameServicesHandler;
import com.nopalsoft.zombiekiller.handlers.RequestHandler;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 480;
        new LwjglApplication(new MainZombie(handler, gameHandler, faceHandler), config);

        // // Pack the levels
//        String[] arg = {"/Users/yayo/Downloads/ZombiePlataformer/ZombiePlataformer/Maps", "/Users/yayo/Downloads/ZombiePlataformer/ZombiePlataformer/Maps/packed", "--strip-unused"};
//
//        TiledMapPacker.main(arg);
    }

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
        public void showAdBanner() {
            // TODO Auto-generated method stub

        }

        @Override
        public void shareOnTwitter(String mensaje) {
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
        public void buy5milCoins() {
            Settings.gemsTotal += 5000;
            Settings.save();
        }

        @Override
        public void buy50milCoins() {
            Settings.gemsTotal += 50000;
            Settings.save();
        }

        @Override
        public void buy30milCoins() {
            Settings.gemsTotal += 30000;
            Settings.save();
        }

        @Override
        public void buy15milCoins() {
            Settings.gemsTotal += 15000;
            Settings.save();
        }
    };

    static GoogleGameServicesHandler gameHandler = new GoogleGameServicesHandler() {

        @Override
        public void unlockAchievement(String achievementId) {
            // TODO Auto-generated method stub

        }

        @Override
        public void submitScore(long score) {
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
    };
    static FacebookHandler faceHandler = new FacebookHandler() {

        @Override
        public void showFacebook() {
            // TODO Auto-generated method stub

        }

        @Override
        public void facebookSignOut() {
            // TODO Auto-generated method stub

        }

        @Override
        public void facebookSignIn() {
            // TODO Auto-generated method stub

        }

        @Override
        public void facebookShareFeed(String message) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean facebookIsSignedIn() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void facebookInviteFriends(String message) {
            // TODO Auto-generated method stub

        }
    };
}
