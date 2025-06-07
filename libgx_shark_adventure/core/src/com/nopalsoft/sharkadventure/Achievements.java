package com.nopalsoft.sharkadventure;

import com.badlogic.gdx.Gdx;

public class Achievements {

    static boolean didInit = false;

    static String firstStep, swimmer, submarineKiller, submarineHunter, submarineSlayer, youCanNotHitMe;
    private static boolean achievedFirstStep, achievedSwimmer, achievedYouCanNotHitMe;

    public static void initialize() {
        firstStep = "FirstStepID";
        swimmer = "SwimmerID";
        submarineKiller = "SubmarinekillerID";
        submarineHunter = "SubmarinehunterID";
        submarineSlayer = "SubmarineslayerID";
        youCanNotHitMe = "YouCantHitMe";
        didInit = true;
    }

    /**
     * Called when u start a new game so u can try to do achievements once more
     */
    public static void tryAgainAchievements() {
        achievedFirstStep = false;
        achievedYouCanNotHitMe = false;
        achievedSwimmer = false;
    }

    private static void didInit() {
        if (didInit)
            return;
        Gdx.app.log("Achievements", "You must call first Achievements.init()");
    }

    public static void unlockKilledSubmarines() {
        didInit();
    }

    public static void distance(long distance, boolean didGetHurt) {
        didInit();
        if (distance > 1 && !achievedFirstStep) {
            achievedFirstStep = true;
        }
        if (distance > 2500 && !achievedSwimmer) {
            achievedSwimmer = true;
        }
        if (distance > 850 && !achievedYouCanNotHitMe && !didGetHurt) {
            achievedYouCanNotHitMe = true;
        }
    }
}