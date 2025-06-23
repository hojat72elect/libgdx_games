package com.nopalsoft.thetruecolor.handlers;

public interface GameServicesHandler {

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param tiempoLap
     */
    public void submitScore(long score);

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param score
     */
    public void unlockAchievement(String achievementId);

    public void unlockStepAchievement(float steps, String achievementID);

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param score
     */
    public void getLeaderboard();

    public void getScores();

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
