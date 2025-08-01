package com.nopalsoft.impossibledial

object Achievements {
    var begginerEasy: String? = null
    var intermediateEasy: String? = null
    var advancedEasy: String? = null
    var expertEasy: String? = null
    var masterHard: String? = null
    var godHard: String? = null
    var iLikeThisGame: String? = null
    var iLoveThisGame: String? = null

    @JvmStatic
    fun init() {
        begginerEasy = "beginnerID"
        intermediateEasy = "intermediateID"
        advancedEasy = "advancedID"
        expertEasy = "expertID"
        iLikeThisGame = "iLikeThisGame"
        iLoveThisGame = "iLoveThisGameID"
        masterHard = "masterID"
        godHard = "godID"
    }

    @JvmStatic
    fun unlockScoreAchievementsEasy() {
    }

    @JvmStatic
    fun unlockScoreAchievementsHard() {
    }

    @JvmStatic
    fun unlockTimesPlayedAchievements() {
    }
}