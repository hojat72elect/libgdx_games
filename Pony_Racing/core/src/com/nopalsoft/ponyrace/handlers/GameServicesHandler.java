package com.nopalsoft.ponyrace.handlers;

public interface GameServicesHandler {

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param tiempoLap
     */
    void submitScore(float tiempoLap, String leaderBoard);

    /**
     * Este metodo abstrae a GPGS o a AGC
     *
     * @param achievementId
     */
    void unlockAchievement(String achievementId);


    void getLeaderboard();


    void getAchievements();

    boolean isSignedIn();

    void signIn();

    void signOut();

    void unlockIncrementalAchievement(String achievementId,
                                      int pasosDados);
}
