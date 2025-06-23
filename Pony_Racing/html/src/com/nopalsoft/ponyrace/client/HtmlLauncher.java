package com.nopalsoft.ponyrace.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.i18n.client.NumberFormat;
import com.nopalsoft.ponyrace.MainPonyRace;
import com.nopalsoft.ponyrace.handlers.FloatFormatter;
import com.nopalsoft.ponyrace.handlers.GameServicesHandler;
import com.nopalsoft.ponyrace.handlers.RequestHandler;

public class HtmlLauncher extends GwtApplication implements GameServicesHandler, RequestHandler, FloatFormatter {
    // https://stackoverflow.com/a/13124309/3479489
    NumberFormat numFormat = NumberFormat.getFormat(".#");

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(800, 480);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new MainPonyRace(this, this, this);
    }

    @Override
    public String format(String _unused, float number) {
        // Given that GWT does not support String format
        return numFormat.format(number);
    }

    @Override
    public void submitScore(float tiempoLap, String leaderBoard) {

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
    public void unlockIncrementalAchievement(String achievementId, int pasosDados) {

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