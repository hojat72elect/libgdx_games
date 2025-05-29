package com.nopalsoft.sharkadventure;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nopalsoft.sharkadventure.handlers.RequestHandler;

public class AndroidLauncher extends AndroidApplication implements RequestHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MainShark(this), config);
    }

    @Override
    public void showInterstitial() {

    }

    @Override
    public void shareOnTwitter(String mensaje) {
        String tweetUrl = "https://twitter.com/intent/tweet?text=" + mensaje + " Download it from &url=https://play.google.com/&hashtags=SharkAdventure";
        Gdx.net.openURI(tweetUrl);
    }

    @Override
    public void showAdBanner() {

    }

    @Override
    public void hideAdBanner() {

    }
}
