package com.nopalsoft.impossibledial;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nopalsoft.impossibledial.MainGame;
import com.nopalsoft.impossibledial.handlers.FacebookHandler;
import com.nopalsoft.impossibledial.handlers.GameServicesHandler;
import com.nopalsoft.impossibledial.handlers.RequestHandler;

public class AndroidLauncher extends AndroidApplication implements FacebookHandler, GameServicesHandler, RequestHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MainGame(this, this, this), config);
    }

    @Override
    public void showFacebook() {

    }

    @Override
    public void submitScore(long score, String leaderboardID) {

    }

    @Override
    public void unlockAchievement(String achievementId) {

    }

    @Override
    public void getLeaderboard() {

    }

    @Override
    public void unlockStepAchievement(float steps, String achievementID) {

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
    public void loadInterstitial() {

    }

    @Override
    public void showInterstitial() {

    }

    @Override
    public void removeAds() {

    }

    @Override
    public void shareAPK() {

    }

    @Override
    public void showAdBanner() {

    }

    @Override
    public void hideAdBanner() {

    }
}
