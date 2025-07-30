package com.nopalsoft.zombiekiller;

import com.badlogic.gdx.Gdx;

public class Achievements {

    static boolean didInit = false;

    static String collector, collectorMaster, aLittlePatience, anHonorableKill, stayDead, cleaningUp;

    public static void init() {
        collector = "CgkI8sWCvZ0NEAIQAw";
        collectorMaster = "CgkI8sWCvZ0NEAIQBA";
        aLittlePatience = "CgkI8sWCvZ0NEAIQCA";
        anHonorableKill = "CgkI8sWCvZ0NEAIQCQ";
        stayDead = "CgkI8sWCvZ0NEAIQBQ";
        cleaningUp = "CgkI8sWCvZ0NEAIQBg";
        didInit = true;
    }

    private static void didInit() {
        if (didInit)
            return;
        Gdx.app.log("Achievements", "You must call first Achievements.init()");
    }

    public static void unlockKilledZombies() {
        didInit();
    }

    public static void unlockCollectedSkulls() {
        didInit();
    }
}
