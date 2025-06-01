package com.nopalsoft.superjumper.handlers;

public interface GameServicesHandler {

    void submitScore(long score);

    void unlockAchievement(String achievementId);

    void getLeaderboard();

    void getAchievements();

    boolean isSignedIn();

    void signIn();

    void signOut();
}
