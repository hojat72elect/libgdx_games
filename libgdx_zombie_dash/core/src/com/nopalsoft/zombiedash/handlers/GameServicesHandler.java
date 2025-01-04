package com.nopalsoft.zombiedash.handlers;

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
