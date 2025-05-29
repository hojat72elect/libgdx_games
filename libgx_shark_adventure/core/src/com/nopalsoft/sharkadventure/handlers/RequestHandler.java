package com.nopalsoft.sharkadventure.handlers;

public interface RequestHandler {

    void showInterstitial();

    void shareOnTwitter(final String mensaje);

    void showAdBanner();

    void hideAdBanner();
}
