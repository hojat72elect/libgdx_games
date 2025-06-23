package com.nopalsoft.thetruecolor;

import android.os.Bundle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.thetruecolor.handlers.FacebookHandler;
import com.nopalsoft.thetruecolor.handlers.GoogleGameServicesHandler;
import com.nopalsoft.thetruecolor.handlers.HandlerGWT;
import com.nopalsoft.thetruecolor.handlers.RequestHandler;
import com.nopalsoft.thetruecolor.leaderboard.Person;

public class AndroidLauncher extends AndroidApplication implements RequestHandler, HandlerGWT, GoogleGameServicesHandler, FacebookHandler {
    public MainTheTrueColor game;
    private boolean isSigned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        game = new MainTheTrueColor(this, this, this, this);
        initialize(game, config);
    }

    @Override
    public void facebookSignOut() {
        isSigned = false;
        Gdx.app.log("Facebook", "Sign out");
        game.removeFromArray(Person.TipoCuenta.FACEBOOK);
    }

    @Override
    public void facebookSignIn() {
        isSigned = true;
        Gdx.app.log("Facebook", "Sign in");
        facebookGetScores();
    }


    public boolean facebookIsSignedIn() {
        return isSigned;
    }


    @Override
    public void facebookShareFeed(String message) {

    }

    @Override
    public void showFacebook() {

    }

    @Override
    public void facebookGetScores() {
        Array<Person> arrPerson = new Array<>();
        arrPerson.add(new Person(Person.TipoCuenta.FACEBOOK, "1", "Jessie", 15000));
        arrPerson.add(new Person(Person.TipoCuenta.FACEBOOK, "2", "Rogelio", 10000));
        arrPerson.add(new Person(Person.TipoCuenta.GOOGLE_PLAY, "3", "Susana", 8000));
        arrPerson.add(new Person(Person.TipoCuenta.FACEBOOK, "4", "Flavia", 5000));
        arrPerson.add(new Person(Person.TipoCuenta.FACEBOOK, "5", "Micky", 2500));
        arrPerson.add(new Person(Person.TipoCuenta.GOOGLE_PLAY, "6", "Carlos", 1000));
        game.setArrayPerson(arrPerson);
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
    public void getTextureFromFacebook(String url, final OnTextureLoaded onTextureLoaded) {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl(url);
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                final byte[] bytes = httpResponse.getResult();
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
                        Texture texture = new Texture(new PixmapTextureData(pixmap, pixmap.getFormat(), false, false, true));
                        pixmap.dispose();
                        onTextureLoaded.onTextureLoaded(texture);
                    }
                });

            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("EmptyDownloadTest", "Failed", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("EmptyDownloadTest", "Cancelled");
            }
        });
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
