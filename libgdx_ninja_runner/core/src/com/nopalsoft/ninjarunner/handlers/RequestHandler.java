package com.nopalsoft.ninjarunner.handlers;

public interface RequestHandler {
	public void showRater();

	/**
	 * Normalmente se llama cuando se inicia el juego
	 */
	public void loadInterstitial();

	public void showInterstitial();

	public void showMoreGames();

	public void removeAds();

	public void showAdBanner();

	public void hideAdBanner();

	public void shareApp();

	public void buy5milCoins();

	public void buy15milCoins();

	public void buy30milCoins();

	public void buy50milCoins();

}
