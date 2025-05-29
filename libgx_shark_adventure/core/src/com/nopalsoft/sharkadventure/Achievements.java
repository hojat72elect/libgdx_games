package com.nopalsoft.sharkadventure;

import com.badlogic.gdx.Gdx;

public class Achievements {

    static boolean didInit = false;

    static String firstStep, swimer, submarineKiller, submarineHunter, submarineSlayer, youCanNotHitMe;
    private static boolean doneFristStep, doneSwimer, doneYouCanNotHitMe;

    public static void init() {
        firstStep = "FirstStepID";
        swimer = "SwimmerID";
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
        doneFristStep = false;
        doneYouCanNotHitMe = false;
        doneSwimer = false;
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
        if (distance > 1 && !doneFristStep) {
            doneFristStep = true;
        }
        if (distance > 2500 && !doneSwimer) {
            doneSwimer = true;
        }
        if (distance > 850 && !doneYouCanNotHitMe && !didGetHurt) {
            doneYouCanNotHitMe = true;
        }
    }
}