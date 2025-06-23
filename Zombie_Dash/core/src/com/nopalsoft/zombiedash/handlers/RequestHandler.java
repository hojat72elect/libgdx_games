package com.nopalsoft.zombiedash.handlers;

public interface RequestHandler {
	public void showRater();

	public void showInterstitial();

	public void showMoreGames();

	
	public void shareOnTwitter(final String mensaje);

	public void removeAds();

	public void showAdBanner();

	public void hideAdBanner();

	public void buy5milCoins();

	public void buy15milCoins();

	public void buy30milCoins();

	public void buy50milCoins();

}
