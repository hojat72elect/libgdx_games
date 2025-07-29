package com.nopalsoft.thetruecolor.handlers;

public interface GameServicesHandler {

    /**
     * Este metodo abstrae a GPGS o a AGC
     */
    void submitScore(long score);

    /**
     * Este metodo abstrae a GPGS o a AGC
     */
    void unlockAchievement(String achievementId);

    void unlockStepAchievement(float steps, String achievementID);

    /**
     * Este metodo abstrae a GPGS o a AGC
     */
    void getLeaderboard();

    void getScores();

    /**
     * Este metodo abstrae a GPGS o a AGC
     */
    void getAchievements();

    boolean isSignedIn();

    void signIn();

    void signOut();
}
