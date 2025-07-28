package com.gamestudio24.martianrun.utils;

/**
 * Game events that are platform specific (i.e. submitting a score or displaying an ad inn an
 * Android app is different than doing the same in a desktop app).
 */
public interface GameEventListener {

    /**
     * Displays an ad
     */
    void displayAd();

    /**
     * Hides an ad
     */
    void hideAd();

    /**
     * Submits a given score. Used every time the game is over
     */
    void submitScore(int score);

    /**
     * Displays the scores leaderboard
     */
    void displayLeaderboard();

    /**
     * Displays the game's achievements
     */
    void displayAchievements();

    /**
     * Shares the game's website
     */
    void share();

    /**
     * Unlocks an achievement with the given ID
     *
     * @param id achievement ID
     * @see <a href="https://developers.google.com/games/services/">Google Play Game Services</a>
     */
    void unlockAchievement(String id);

    /**
     * Increments an achievement with the given ID
     *
     * @param id    achievement ID
     * @param steps incremental steps
     * @see <a href="https://developers.google.com/games/services/">Google Play Game Services</a>
     */
    void incrementAchievement(String id, int steps);

    /*
     * The following are getters for specific achievement IDs used in this game
     */

    /**
     * @return "Getting Started" achievement ID
     */
    String getGettingStartedAchievementId();

    /**
     * @return "Like a Rover" achievement ID
     */
    String getLikeARoverAchievementId();

    /**
     * @return "Spirit" achievement ID
     */
    String getSpiritAchievementId();

    /**
     * @return "Curiosity" achievement ID
     */
    String getCuriosityAchievementId();

    /**
     * @return "5k Club" achievement ID
     */
    String get5kClubAchievementId();

    /**
     * @return "10k Club" achievement ID
     */
    String get10kClubAchievementId();

    /**
     * @return "25k Club" achievement ID
     */
    String get25kClubAchievementId();

    /**
     * @return "50k Club" achievement ID
     */
    String get50kClubAchievementId();

    /**
     * @return "10 Jump Street" achievement ID
     */
    String get10JumpStreetAchievementId();

    /**
     * @return "100 Jump Street" achievement ID
     */
    String get100JumpStreetAchievementId();

    /**
     * @return "500 Jump Street" achievement ID
     */
    String get500JumpStreetAchievementId();
}
