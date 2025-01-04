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

    static GoogleGameServicesHandler gamehandler = new GoogleGameServicesHandler() {

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

    static FacebookHandler facebookHandler = new FacebookHandler() {

        @Override
        public void showFacebook() {

        }

        @Override
        public void facebookSignOut() {

        }

        @Override
        public void facebookSignIn() {

        }

        @Override
        public void facebookShareFeed(String message) {

        }

        @Override
        public boolean facebookIsSignedIn() {
            return false;
        }

        @Override
        public void facebookInviteFriends(String message) {

        }
    };
}
