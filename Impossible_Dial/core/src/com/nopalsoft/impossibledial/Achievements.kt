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

    fun initialize() {
        begginerEasy = "beginnerID"
        intermediateEasy = "intermediateID"
        advancedEasy = "advancedID"
        expertEasy = "expertID"
        iLikeThisGame = "iLikeThisGame"
        iLoveThisGame = "iLoveThisGameID"
        masterHard = "masterID"
        godHard = "godID"
    }

    fun unlockScoreAchievementsEasy() {
    }

    fun unlockScoreAchievementsHard() {
    }

    fun unlockTimesPlayedAchievements() {
    }
}