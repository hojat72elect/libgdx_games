package com.nopalsoft.ninjarunner;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nopalsoft.ninjarunner.handlers.RequestHandler;

public class AndroidLauncher extends AndroidApplication implements RequestHandler {

    protected NinjaRunnerGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        game = new NinjaRunnerGame(this);
        initialize(game, config);
    }

    @Override
    public void showRater() {
    }

    @Override
    public void loadInterstitial() {
    }

    @Override
    public void showMoreGames() {
    }

    @Override
    public void shareApp() {
    }
}
