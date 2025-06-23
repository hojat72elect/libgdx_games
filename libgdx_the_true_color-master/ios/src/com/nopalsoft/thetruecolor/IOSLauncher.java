package com.nopalsoft.thetruecolor;

import com.nopalsoft.thetruecolor.handlers.FacebookHandler;
import com.nopalsoft.thetruecolor.handlers.GameServicesHandler;
import com.nopalsoft.thetruecolor.handlers.HandlerGWT;
import com.nopalsoft.thetruecolor.handlers.RequestHandler;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.nopalsoft.thetruecolor.MainTheTrueColor;

public class IOSLauncher extends IOSApplication.Delegate implements RequestHandler, FacebookHandler, GameServicesHandler, HandlerGWT {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new MainTheTrueColor(this, this, this, this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
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
    public void facebookGetScores() {

    }

    @Override
    public void facebookSubmitScore(int score) {

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
    public void unlockStepAchievement(float steps, String achievementID) {

    }

    @Override
    public void getLeaderboard() {

    }

    @Override
    public void getScores() {

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
    public void getTextureFromFacebook(String url, OnTextureLoaded onTextureLoaded) {

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
    public void showMoreGames() {

    }

    @Override
    public void shareOnTwitter(String mensaje) {

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