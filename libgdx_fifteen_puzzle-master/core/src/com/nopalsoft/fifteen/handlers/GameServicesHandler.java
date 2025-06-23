package com.nopalsoft.fifteen.handlers;

public interface GameServicesHandler {

	/**
	 * Este metodo abstrae a GPGS o a AGC
	 * 
	 * @param tiempoLap
	 */
	public void submitScore(long score, String leaderboard);

	/**
	 * Este metodo abstrae a GPGS o a AGC
	 * 
	 * @param score
	 */
	public void unlockAchievement(String achievementId);

	/**
	 * Este metodo abstrae a GPGS o a AGC
	 * 
	 * @param score
	 */
	public void getLeaderboard();

	/**
	 * Este metodo abstrae a GPGS o a AGC
	 * 
	 * @param score
	 */
	public void getAchievements();

	public boolean isSignedIn();

	public void signIn();

	public void signOut();

}
