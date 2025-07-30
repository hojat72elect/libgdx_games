package com.nopalsoft.zombiekiller.handlers;

public interface GameServicesHandler {

    /**
     * Este metodo abstrae a GPGS o a AGC
     */
    void submitScore(long score);

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     */
    void unlockAchievement(String achievementId);

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     */
    void getLeaderboard();

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     */
    void getAchievements();

    boolean isSignedIn();

    void signIn();

    void signOut();
}
