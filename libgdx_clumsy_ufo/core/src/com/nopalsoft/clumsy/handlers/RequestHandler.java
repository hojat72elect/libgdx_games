package com.nopalsoft.clumsy.handlers;

public interface RequestHandler {
    void showRater();

    void showInterstitial();

    void showFacebook();

    void showMoreGames();

    void shareOnFacebook(final String mensaje);

    void shareOnTwitter(final String mensaje);

    void showAdBanner();

    void hideAdBanner();

    void comprarRemoveAds();

    void restorePurchases();
}
