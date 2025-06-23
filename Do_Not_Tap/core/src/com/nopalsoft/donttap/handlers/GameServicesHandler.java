package com.nopalsoft.donttap.handlers;

public interface GameServicesHandler {

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param tiempoLap
     */
    void submitScoreTimeMode(long score);

    void submitScoreClassicMode(float time);

    void submitScoreEndlessMode(long score);

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param score
     */
    void unlockAchievement(String achievementId);

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param score
     */
    void getLeaderboard();

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
