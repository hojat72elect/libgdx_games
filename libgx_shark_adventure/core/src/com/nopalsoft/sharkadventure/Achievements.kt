package com.nopalsoft.sharkadventure

import com.badlogic.gdx.Gdx

object Achievements {
    private var didInit: Boolean = false


    private var doneFirstStep = false
    private var doneSwimmer = false
    private var doneYouCanNotHitMe = false

    fun init() {
        didInit = true
    }

    /**
     * Called when u start a new game so u can try to do achievements once more
     */
    fun tryAgainAchievements() {
        doneFirstStep = false
        doneYouCanNotHitMe = false
        doneSwimmer = false
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
        if (distance > 1 && !doneFirstStep) {
            doneFirstStep = true
        }
        if (distance > 2500 && !doneSwimmer) {
            doneSwimmer = true
        }
        if (distance > 850 && !doneYouCanNotHitMe && !didGetHurt) {
            doneYouCanNotHitMe = true
        }
    }
}