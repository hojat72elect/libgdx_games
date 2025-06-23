package com.nopalsoft.fifteen.handlers;

public interface RequestHandler {
	public void showRater();

	public void showInterstitial();

	public void showFacebook();

	public void showMoreGames();

	public void shareOnFacebook(final String mensaje);

	public void shareOnTwitter(final String mensaje);

	public void buyNoAds();

	public void restorePurchases();

	public void removeAds();

	public void showAdBanner();

	public void hideAdBanner();

}
