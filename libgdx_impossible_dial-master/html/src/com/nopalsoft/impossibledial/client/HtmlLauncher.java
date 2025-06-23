package com.nopalsoft.impossibledial.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.nopalsoft.impossibledial.MainGame;
import com.nopalsoft.impossibledial.handlers.FacebookHandler;
import com.nopalsoft.impossibledial.handlers.GameServicesHandler;
import com.nopalsoft.impossibledial.handlers.RequestHandler;

public class HtmlLauncher extends GwtApplication implements GameServicesHandler, RequestHandler, FacebookHandler {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(480, 800);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new MainGame(this, this, this);
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