package com.nopalsoft.dragracer.handlers;

public interface RequestHandler {
	public void showRater();

	public void showInterstitial();

	public void showFacebook();

	public void showMoreGames();

	public void shareOnFacebook(final String mensaje);

	public void shareOnTwitter(final String mensaje);

	public void removeAds();

	public void showAdBanner();

	public void hideAdBanner();

	public void buy50milCoins();

}
