package com.nopalsoft.donttap;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.nopalsoft.donttap.handlers.FloatFormatter;
import com.nopalsoft.donttap.handlers.GameServicesHandler;
import com.nopalsoft.donttap.handlers.RequestHandler;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate implements RequestHandler, GameServicesHandler, FloatFormatter {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new MainDoNot(this, this, this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
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