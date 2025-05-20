package com.nopalsoft.slamthebird;

import android.os.Bundle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nopalsoft.slamthebird.MainSlamBird;
import com.nopalsoft.slamthebird.handlers.GameServicesHandler;
import com.nopalsoft.slamthebird.handlers.RequestHandler;

public class AndroidLauncher extends AndroidApplication implements RequestHandler, GameServicesHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MainSlamBird(this, this), config);
    }

    @Override
    public void submitScore(long score) {

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
}
