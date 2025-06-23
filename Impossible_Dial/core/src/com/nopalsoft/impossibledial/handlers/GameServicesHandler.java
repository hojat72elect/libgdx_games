package com.nopalsoft.impossibledial.handlers;

public interface GameServicesHandler {


    void submitScore(long score, String leaderboardID);


    void unlockAchievement(String achievementId);


    void getLeaderboard();

    void unlockStepAchievement(float steps, String achievementID);

    void getAchievements();

    boolean isSignedIn();

    void signIn();

    void signOut();
}
