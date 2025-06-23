package com.nopalsoft.impossibledial.handlers;

public interface GameServicesHandler {


    public void submitScore(long score, String leaderboardID);


    public void unlockAchievement(String achievementId);


    public void getLeaderboard();

    public void unlockStepAchievement(float steps, String achievementID);

    public void getAchievements();

    public boolean isSignedIn();

    public void signIn();

    public void signOut();

}
