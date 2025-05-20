package com.nopalsoft.ninjarunner.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.ninjarunner.MainGame;
import com.nopalsoft.ninjarunner.handlers.FacebookHandler;
import com.nopalsoft.ninjarunner.handlers.GoogleGameServicesHandler;
import com.nopalsoft.ninjarunner.handlers.RequestHandler;
import com.nopalsoft.ninjarunner.leaderboard.Person;

public class DesktopLauncher {

    public static MainGame game;
    private static boolean isSigned = true;

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 480);
        config.setTitle("Ninja Runner");
        game = new MainGame(handler, gameHandler, faceHandler);
        new Lwjgl3Application(game, config);
    }

    static FacebookHandler faceHandler = new FacebookHandler() {

        @Override
        public void facebookSignOut() {
            isSigned = false;
            Gdx.app.log("Facebook", "Sign out");
        }

        @Override
        public void facebookSignIn() {
            isSigned = true;
            Gdx.app.log("Facebook", "Sign in");
            facebookGetScores();
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
            // TODO Auto-generated method stub

        }
    };

    static GoogleGameServicesHandler gameHandler = new GoogleGameServicesHandler() {

        @Override
        public void unlockAchievement(String achievementId) {
            // TODO Auto-generated method stub

        }

        @Override
        public void submitScore(long score) {
            // TODO Auto-generated method stub

        }

        @Override
        public void signOut() {
            // TODO Auto-generated method stub

        }

        @Override
        public void signIn() {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isSignedIn() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void getLeaderboard() {
            // TODO Auto-generated method stub

        }

        @Override
        public void getScores() {

        }

        @Override
        public void getAchievements() {
            // TODO Auto-generated method stub

        }
    };

    static RequestHandler handler = new RequestHandler() {

        @Override
        public void showRater() {
            // TODO Auto-generated method stub

        }

        @Override
        public void showMoreGames() {
            // TODO Auto-generated method stub

        }

        @Override
        public void showInterstitial() {
            // TODO Auto-generated method stub

        }

        @Override
        public void showAdBanner() {
            // TODO Auto-generated method stub

        }

        @Override
        public void shareApp() {
            // TODO Auto-generated method stub

        }

        @Override
        public void removeAds() {
            // TODO Auto-generated method stub

        }

        @Override
        public void hideAdBanner() {
            // TODO Auto-generated method stub

        }

        @Override
        public void buy5milCoins() {
            // TODO Auto-generated method stub

        }

        @Override
        public void buy50milCoins() {
            // TODO Auto-generated method stub

        }

        @Override
        public void buy30milCoins() {
            // TODO Auto-generated method stub

        }

        @Override
        public void buy15milCoins() {
            // TODO Auto-generated method stub

        }

        @Override
        public void loadInterstitial() {
            // TODO Auto-generated method stub

        }
    };
}
