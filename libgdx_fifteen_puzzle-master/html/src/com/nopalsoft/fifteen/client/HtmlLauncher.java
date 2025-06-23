package com.nopalsoft.fifteen.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.nopalsoft.fifteen.MainFifteen;
import com.nopalsoft.fifteen.handlers.GameServicesHandler;
import com.nopalsoft.fifteen.handlers.RequestHandler;

public class HtmlLauncher extends GwtApplication implements RequestHandler, GameServicesHandler {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(480, 800);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new MainFifteen(this, this);
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