package com.nopalsoft.fifteen.handlers;

public interface RequestHandler {
    void showRater();

    void showInterstitial();

    void showFacebook();

    void showMoreGames();

    void shareOnFacebook(final String mensaje);

    void shareOnTwitter(final String mensaje);

    void buyNoAds();

    void restorePurchases();

    void removeAds();

    void showAdBanner();

    void hideAdBanner();
}
