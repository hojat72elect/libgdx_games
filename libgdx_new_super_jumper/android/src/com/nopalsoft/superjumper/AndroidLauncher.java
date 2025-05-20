package com.nopalsoft.superjumper;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nopalsoft.superjumper.MainSuperJumper;
import com.nopalsoft.superjumper.handlers.FacebookHandler;
import com.nopalsoft.superjumper.handlers.GameServicesHandler;
import com.nopalsoft.superjumper.handlers.RequestHandler;

public class AndroidLauncher extends AndroidApplication implements FacebookHandler, RequestHandler, GameServicesHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MainSuperJumper(this, this, this), config);
    }

    @Override
    public void facebookSignIn() {

    }

    @Override
    public void facebookSignOut() {

    }

    @Override
    public boolean facebookIsSignedIn() {
        return false;
    }

    @Override
    public void showFacebook() {

    }

    @Override
    public void facebookInviteFriends(String message) {

    }

    @Override
    public void facebookShareFeed(String message) {

    }

    @Override
    public void submitScore(long score) {

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
    public void unlockAchievement(String achievementId) {

    }

    @Override
    public void showRater() {

    }

    @Override
    public void showInterstitial() {

    }

    @Override
    public void showMoreGames() {

    }

    @Override
    public void removeAds() {

    }

    @Override
    public void showAdBanner() {

    }

    @Override
    public void hideAdBanner() {

    }

    @Override
    public void buy5milCoins() {

    }

    @Override
    public void buy15milCoins() {

    }

    @Override
    public void buy30milCoins() {

    }

    @Override
    public void buy50milCoins() {

    }

    @Override
    public void shareOnTwitter(String mensaje) {

    }
}
