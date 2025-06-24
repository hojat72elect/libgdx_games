package com.nopalsoft.thetruecolor.handlers;

public interface GameServicesHandler {

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param tiempoLap
     */
    void submitScore(long score);

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param score
     */
    void unlockAchievement(String achievementId);

    void unlockStepAchievement(float steps, String achievementID);

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param score
     */
    void getLeaderboard();

    void getScores();

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param score
     */
    void getAchievements();

    boolean isSignedIn();

    void signIn();

    void signOut();
}
