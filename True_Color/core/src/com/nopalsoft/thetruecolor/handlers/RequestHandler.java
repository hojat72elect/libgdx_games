package com.nopalsoft.thetruecolor.handlers;

public interface RequestHandler {
    void showRater();

    void loadInterstitial();

    void showInterstitial();

    void showMoreGames();

    void shareOnTwitter(final String mensaje);

    void removeAds();

    void shareAPK();

    void showAdBanner();

    void hideAdBanner();
}
