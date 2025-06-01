package com.nopalsoft.slamthebird.handlers;

public interface RequestHandler {
    void showRater();

    void showInterstitial();

    void showFacebook();

    void showMoreGames();

    void shareOnFacebook(final String mensaje);

    void shareOnTwitter(final String mensaje);

    void removeAds();

    void showAdBanner();

    void hideAdBanner();

    void buy5milCoins();

    void buy15milCoins();

    void buy30milCoins();

    void buy50milCoins();
}
