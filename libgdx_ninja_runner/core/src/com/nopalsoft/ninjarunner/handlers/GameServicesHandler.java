package com.nopalsoft.ninjarunner.handlers;

public interface GameServicesHandler {

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param score
     */
    public void submitScore(long score);

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param achievementId
     */
    public void unlockAchievement(String achievementId);

    public void getLeaderboard();

    public void getScores();

    public void getAchievements();

    public boolean isSignedIn();

    public void signIn();

    public void signOut();

}
