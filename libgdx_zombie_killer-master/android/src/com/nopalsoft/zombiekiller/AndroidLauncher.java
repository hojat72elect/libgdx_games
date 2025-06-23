package com.nopalsoft.zombiekiller;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nopalsoft.zombiekiller.MainZombie;
import com.nopalsoft.zombiekiller.handlers.FacebookHandler;
import com.nopalsoft.zombiekiller.handlers.GameServicesHandler;
import com.nopalsoft.zombiekiller.handlers.RequestHandler;

public class AndroidLauncher extends AndroidApplication implements RequestHandler, GameServicesHandler, FacebookHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MainZombie(this, this, this), config);
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
    public void facebookShareFeed(String message) {

    }

    @Override
    public void showFacebook() {

    }

    @Override
    public void facebookInviteFriends(String message) {

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
    public void showMoreGames() {

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
}
