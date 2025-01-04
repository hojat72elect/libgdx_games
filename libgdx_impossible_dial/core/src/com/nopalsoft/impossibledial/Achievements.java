package com.nopalsoft.impossibledial;


import com.nopalsoft.impossibledial.handlers.GameServicesHandler;
import com.nopalsoft.impossibledial.handlers.GoogleGameServicesHandler;

public class Achievements {

    static GameServicesHandler gameHandler;

    static String begginerEasy, intermediateEasy, advancedEasy, expertEasy, masterHard, godHard, iLikeThisGame, iLoveThisGame;

    public static void init(com.nopalsoft.impossibledial.MainGame game) {
        gameHandler = game.gameServiceHandler;

        if (gameHandler instanceof GoogleGameServicesHandler) {
            begginerEasy = "CgkIlp_HkPwMEAIQAw";
            intermediateEasy = "CgkIlp_HkPwMEAIQBA";
            advancedEasy = "CgkIlp_HkPwMEAIQBQ";
            expertEasy = "CgkIlp_HkPwMEAIQBg";
            iLikeThisGame = "CgkIlp_HkPwMEAIQBw";
            iLoveThisGame = "CgkIlp_HkPwMEAIQCA";
            masterHard = "CgkIlp_HkPwMEAIQCg";
            godHard = "CgkIlp_HkPwMEAIQCw";
        } else {
            begginerEasy = "beginnerID";
            intermediateEasy = "intermediateID";
            advancedEasy = "advancedID";
            expertEasy = "expertID";
            iLikeThisGame = "iLikeThisGame";
            iLoveThisGame = "iLoveThisGameID";
            masterHard = "masterID";
            godHard = "godID";
        }

    }

    public static void unlockScoreAchievementsEasy(long num) {
        if (num == 125) {
            gameHandler.unlockAchievement(expertEasy);
        } else if (num == 75) {
            gameHandler.unlockAchievement(advancedEasy);
        } else if (num == 50) {
            gameHandler.unlockAchievement(intermediateEasy);
        } else if (num == 25) {
            gameHandler.unlockAchievement(begginerEasy);
        }
    }

    public static void unlockScoreAchievementsHard(long num) {
        if (num == 40) {
            gameHandler.unlockAchievement(expertEasy);
        } else if (num == 70) {
            gameHandler.unlockAchievement(advancedEasy);
        }
    }

    public static void unlockTimesPlayedAchievements() {
        if (gameHandler instanceof GoogleGameServicesHandler) {
            gameHandler.unlockStepAchievement(1, iLikeThisGame);
            gameHandler.unlockStepAchievement(1, iLoveThisGame);
        } else {//Amazon
            gameHandler.unlockStepAchievement(Settings.numVecesJugadas, iLikeThisGame);
            gameHandler.unlockStepAchievement(Settings.numVecesJugadas * 100f / 250f, iLoveThisGame);//Para llegar al 100% se deben hacer 250 juegos
        }
    }

}