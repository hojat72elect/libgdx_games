package com.nopalsoft.zombiedash.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.zombiedash.MainZombieDash;
import com.nopalsoft.zombiedash.handlers.FacebookHandler;
import com.nopalsoft.zombiedash.handlers.GoogleGameServicesHandler;
import com.nopalsoft.zombiedash.handlers.RequestHandler;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 480;
        new LwjglApplication(new MainZombieDash(handler, gamehandler, facebookHandler), config);
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
            // TODO Auto-generated method stub

        }

        @Override
        public void buy50milCoins() {
            // TODO Auto-generated method stub

        }

        @Override
        public void buy30milCoins() {
            // TODO Auto-generated method stub

        }

        @Override
        public void buy15milCoins() {
            // TODO Auto-generated method stub

        }
    };

    static GoogleGameServicesHandler gamehandler = new GoogleGameServicesHandler() {

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

    static FacebookHandler facebookHandler = new FacebookHandler() {

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
