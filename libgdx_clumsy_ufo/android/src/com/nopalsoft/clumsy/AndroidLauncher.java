package com.nopalsoft.clumsy;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nopalsoft.clumsy.handlers.GameServicesHandler;
import com.nopalsoft.clumsy.handlers.RequestHandler;

public class AndroidLauncher extends AndroidApplication implements RequestHandler, GameServicesHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // When I change flavors load different assets
        if (BuildConfig.APPLICATION_ID.equals("com.nopalsoft.penis")) {
            Assets.LOAD_FLAPPY_PENIS = true;
        }
        initialize(new MainClumsy(this, this), config);
    }

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
    public void shareOnTwitter(String mensaje) {

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
    public void restorePurchases() {

    }
}
