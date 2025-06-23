package com.nopalsoft.donttap.handlers;

public interface GameServicesHandler {

	/**
	 * Este metodo abstrae a GPGS o a AGC
	 * 
	 * @param tiempoLap
	 */
	public void submitScoreTimeMode(long score);

	public void submitScoreClassicMode(float time);

	public void submitScoreEndlessMode(long score);

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
