package com.nopalsoft.ponyrace.handlers;

public interface GameServicesHandler {

	/**
	 * Este metodo abstrae a GPGS o a AGC
	 * 
	 * @param tiempoLap
	 */
	public void submitScore(float tiempoLap, String leaderBoard);

	/**
	 * Este metodo abstrae a GPGS o a AGC
	 * 
	 * @param achievementId
	 */
	public void unlockAchievement(String achievementId);


	public void getLeaderboard();


	public void getAchievements();

	public boolean isSignedIn();

	public void signIn();

	public void signOut();

	public void unlockIncrementalAchievement(String achievementId,
                                             int pasosDados);

}
