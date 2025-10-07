package ca.hojat.shark_adventure

import com.badlogic.gdx.Gdx

object Achievements {
    var didInit: Boolean = false

    var firstStep: String? = null
    var swimmer: String? = null
    var submarineKiller: String? = null
    var submarineHunter: String? = null
    var submarineSlayer: String? = null
    var youCanNotHitMe: String? = null
    private var achievedFirstStep = false
    private var achievedSwimmer = false
    private var achievedYouCanNotHitMe = false

    fun initialize() {
        firstStep = "FirstStepID"
        swimmer = "SwimmerID"
        submarineKiller = "SubmarinekillerID"
        submarineHunter = "SubmarinehunterID"
        submarineSlayer = "SubmarineslayerID"
        youCanNotHitMe = "YouCantHitMe"
        didInit = true
    }

    /**
     * Called when u start a new game so u can try to do achievements once more
     */
    fun tryAgainAchievements() {
        achievedFirstStep = false
        achievedYouCanNotHitMe = false
        achievedSwimmer = false
    }

    private fun didInit() {
        if (didInit) return
        Gdx.app.log("Achievements", "You must call first Achievements.init()")
    }

    fun unlockKilledSubmarines() {
        didInit()
    }

    fun distance(distance: Long, didGetHurt: Boolean) {
        didInit()
        if (distance > 1 && !achievedFirstStep) {
            achievedFirstStep = true
        }
        if (distance > 2500 && !achievedSwimmer) {
            achievedSwimmer = true
        }
        if (distance > 850 && !achievedYouCanNotHitMe && !didGetHurt) {
            achievedYouCanNotHitMe = true
        }
    }
}