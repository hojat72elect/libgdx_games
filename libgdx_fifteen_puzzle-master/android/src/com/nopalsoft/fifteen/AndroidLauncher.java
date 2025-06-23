package com.nopalsoft.fifteen;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nopalsoft.fifteen.MainFifteen;
import com.nopalsoft.fifteen.handlers.GameServicesHandler;
import com.nopalsoft.fifteen.handlers.RequestHandler;

public class AndroidLauncher extends AndroidApplication implements RequestHandler, GameServicesHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MainFifteen(this, this), config);
    }

    @Override
    public void submitScore(long score, String leaderboard) {

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
    public void buyNoAds() {

    }

    @Override
    public void restorePurchases() {

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
}
