package com.nopalsoft.impossibledial;

import com.nopalsoft.impossibledial.handlers.FacebookHandler;
import com.nopalsoft.impossibledial.handlers.GameServicesHandler;
import com.nopalsoft.impossibledial.handlers.RequestHandler;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.nopalsoft.impossibledial.MainGame;

public class IOSLauncher extends IOSApplication.Delegate implements FacebookHandler, GameServicesHandler, RequestHandler {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new MainGame(this, this, this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
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