package com.nopalsoft.thetruecolor;

import com.nopalsoft.thetruecolor.handlers.GameServicesHandler;
import com.nopalsoft.thetruecolor.handlers.GoogleGameServicesHandler;

public class Achievements {

    static GameServicesHandler gameHandler;

    static String begginer, intermediate, advanced, expert, god, iLikeThisGame, iLoveThisGame;

    public static void init(MainTheTrueColor game) {
        gameHandler = game.gameServiceHandler;

        if (gameHandler instanceof GoogleGameServicesHandler) {
            begginer = "CgkIvIu0qPsVEAIQAg";
            intermediate = "CgkIvIu0qPsVEAIQBA";
            advanced = "CgkIvIu0qPsVEAIQBQ";
            expert = "CgkIvIu0qPsVEAIQBg";
            god = "CgkIvIu0qPsVEAIQBw";
            iLikeThisGame = "CgkIvIu0qPsVEAIQCw";
            iLoveThisGame = "CgkIvIu0qPsVEAIQDA";
        } else {
            begginer = "BeginnerID";
            intermediate = "IntermediateID";
            advanced = "AdvancedID";
            expert = "expertID";
            god = "godId";
            iLikeThisGame = "ILikeThisGameID";
            iLoveThisGame = "ILoveThisGameId";
        }
    }

    public static void unlockScoreAchievements(long num) {

        if (num == 250) {
            gameHandler.unlockAchievement(god);
        } else if (num == 175) {
            gameHandler.unlockAchievement(expert);
        } else if (num == 100) {
            gameHandler.unlockAchievement(advanced);
        } else if (num == 60) {
            gameHandler.unlockAchievement(intermediate);
        } else if (num == 30) {
            gameHandler.unlockAchievement(begginer);
        }
    }

    public static void unlockTimesPlayedAchievements(int num) {
        if (gameHandler instanceof GoogleGameServicesHandler) {
            gameHandler.unlockStepAchievement(1, iLikeThisGame);
            gameHandler.unlockStepAchievement(1, iLoveThisGame);
        } else {//Amazon
            gameHandler.unlockStepAchievement(Settings.numVecesJugadas, iLikeThisGame);
            //250 = 100%
            //numVecesJugadas = X%
            gameHandler.unlockStepAchievement(Settings.numVecesJugadas * 100f / 250f, iLoveThisGame);//Para llegar al 100% se deben hacer 250 juegos
        }
    }
}