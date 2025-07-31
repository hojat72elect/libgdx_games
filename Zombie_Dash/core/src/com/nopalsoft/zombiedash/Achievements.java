package com.nopalsoft.zombiedash;

import com.badlogic.gdx.Gdx;

public class Achievements {

    static boolean didInit = false;


    static String firstStep, zombieKiller, zombieHunter, zombieSlayer, zombieDestroyer, youCanNotHitMe;
    private static boolean doneFristStep, doneZombieYouCanNotHitMe;

    public static void init(MainZombieDash game) {

        firstStep = "20640";
        zombieKiller = "20642";
        zombieHunter = "20644";
        zombieSlayer = "20646";
        zombieDestroyer = "20648";
        youCanNotHitMe = "20650";
        didInit = true;
    }

    /**
     * Called when u start a new game so u can try to do achievements once more
     */
    public static void tryAgainAchievements() {
        doneFristStep = false;
        doneZombieYouCanNotHitMe = false;
    }

    private static void didInit() {
        if (didInit)
            return;
        Gdx.app.log("Achievements", "You must call first Achievements.init()");
    }

    public static void unlockKilledZombies() {
        didInit();
    }

    public static void distance(int distance, boolean didGetHurt) {
        didInit();
        if (distance > 1 && !doneFristStep) {
            doneFristStep = true;
        } else if (distance > 500 && !doneZombieYouCanNotHitMe && !didGetHurt) {
            doneZombieYouCanNotHitMe = true;
        }
    }
}