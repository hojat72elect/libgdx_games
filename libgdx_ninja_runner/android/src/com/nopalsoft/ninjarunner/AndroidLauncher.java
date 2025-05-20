package com.nopalsoft.ninjarunner;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.ninjarunner.handlers.FacebookHandler;
import com.nopalsoft.ninjarunner.handlers.GoogleGameServicesHandler;
import com.nopalsoft.ninjarunner.handlers.RequestHandler;
import com.nopalsoft.ninjarunner.leaderboard.Person;

public class AndroidLauncher extends AndroidApplication implements RequestHandler, FacebookHandler, GoogleGameServicesHandler {

    private static boolean isSigned = true;
    protected MainGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        game = new MainGame(this, this, this);
        initialize(game, config);
    }

    @Override
    public void facebookSignIn() {
        isSigned = true;
        facebookGetScores();
    }

    @Override
    public void facebookSignOut() {
        isSigned = false;
    }

    @Override
    public boolean facebookIsSignedIn() {
        return isSigned;
    }

    @Override
    public void facebookGetScores() {
        String imageURL = "https://randomuser.me/api/portraits/men/";
        Array<Person> arrPerson = new Array<>();
        arrPerson.add(new Person(Person.TipoCuenta.FACEBOOK, "1", "Jessie", 15000, imageURL + "18.jpg"));
        arrPerson.add(new Person(Person.TipoCuenta.FACEBOOK, "2", "Rogelio", 10000, imageURL + "17.jpg"));
        arrPerson.add(new Person(Person.TipoCuenta.FACEBOOK, "3", "Susana", 8000, imageURL + "16.jpg"));
        arrPerson.add(new Person(Person.TipoCuenta.FACEBOOK, "4", "Flavia", 5000, imageURL + "15.jpg"));
        arrPerson.add(new Person(Person.TipoCuenta.FACEBOOK, "5", "Micky", 2500, imageURL + "14.jpg"));
        arrPerson.add(new Person(Person.TipoCuenta.FACEBOOK, "6", "Carlos", 1000, imageURL + "13.jpg"));
        Person yayo = new Person(Person.TipoCuenta.FACEBOOK, "7", "Yayo", 0, imageURL + "12.jpg");
        yayo.isMe = true;
        arrPerson.add(yayo);
        game.setArrayPerson(arrPerson);
    }

    @Override
    public void facebookSubmitScore(long score) {

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
    public void removeAds() {

    }

    @Override
    public void showAdBanner() {

    }

    @Override
    public void hideAdBanner() {

    }

    @Override
    public void shareApp() {

    }

    @Override
    public void buy5milCoins() {

    }

    @Override
    public void buy15milCoins() {

    }

    @Override
    public void buy30milCoins() {

    }

    @Override
    public void buy50milCoins() {

    }
}
