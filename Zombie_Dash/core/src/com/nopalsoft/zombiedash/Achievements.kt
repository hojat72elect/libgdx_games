package com.nopalsoft.zombiedash

import com.badlogic.gdx.Gdx

object Achievements {
    var didInit: Boolean = false


    var firstStep: String? = null
    var zombieKiller: String? = null
    var zombieHunter: String? = null
    var zombieSlayer: String? = null
    var zombieDestroyer: String? = null
    var youCanNotHitMe: String? = null
    private var doneFristStep = false
    private var doneZombieYouCanNotHitMe = false

    fun init() {
        firstStep = "20640"
        zombieKiller = "20642"
        zombieHunter = "20644"
        zombieSlayer = "20646"
        zombieDestroyer = "20648"
        youCanNotHitMe = "20650"
        didInit = true
    }

    /**
     * Called when u start a new game so u can try to do achievements once more
     */
    fun tryAgainAchievements() {
        doneFristStep = false
        doneZombieYouCanNotHitMe = false
    }

    private fun didInit() {
        if (didInit) return
        Gdx.app.log("Achievements", "You must call first Achievements.init()")
    }

    fun unlockKilledZombies() {
        didInit()
    }

    fun distance(distance: Int, didGetHurt: Boolean) {
        didInit()
        if (distance > 1 && !doneFristStep) {
            doneFristStep = true
        } else if (distance > 500 && !doneZombieYouCanNotHitMe && !didGetHurt) {
            doneZombieYouCanNotHitMe = true
        }
    }
}