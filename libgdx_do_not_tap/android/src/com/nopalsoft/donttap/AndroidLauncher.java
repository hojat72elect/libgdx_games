package com.nopalsoft.donttap;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nopalsoft.donttap.handlers.FloatFormatter;
import com.nopalsoft.donttap.handlers.GameServicesHandler;
import com.nopalsoft.donttap.handlers.RequestHandler;

public class AndroidLauncher extends AndroidApplication implements RequestHandler, GameServicesHandler, FloatFormatter {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MainDoNot(this, this, this), config);
    }

    @Override
    public String format(String format, float number) {
        return String.format(format, number);
    }

    @Override
    public void submitScoreTimeMode(long score) {

    }

    @Override
    public void submitScoreClassicMode(float time) {

    }

    @Override
    public void submitScoreEndlessMode(long score) {

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
}
