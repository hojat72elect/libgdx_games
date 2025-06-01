package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Gdx;

public class Achievements {

    static boolean didInit = false;

    static String SuperJump, SlamCombo, SuperSlam, InvencibleSlam, YouGotAnySpareChange, coinMaster;

    public static void init() {

        SuperJump = "20274";
        SlamCombo = "20276";
        SuperSlam = "20278";
        InvencibleSlam = "20280";
        YouGotAnySpareChange = "20282";
        coinMaster = "20284";
        didInit = true;
    }

    private static void didInit() {
        if (didInit)
            return;
        Gdx.app.log("Achievements", "You must call first Achievements.init()");
    }

    public static void unlockSuperJump() {
        didInit();
    }

    public static void unlockCombos() {
        didInit();
    }

    public static void unlockCoins() {
        didInit();
    }

}
