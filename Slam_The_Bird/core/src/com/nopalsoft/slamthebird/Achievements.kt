package com.nopalsoft.slamthebird

import com.badlogic.gdx.Gdx

object Achievements {
    private var didInit: Boolean = false

    private var SuperJump: String? = null
    private var SlamCombo: String? = null
    private var SuperSlam: String? = null
    private var invincibleSlam: String? = null
    private var YouGotAnySpareChange: String? = null
    private var coinMaster: String? = null

    fun init() {
        SuperJump = "20274"
        SlamCombo = "20276"
        SuperSlam = "20278"
        invincibleSlam = "20280"
        YouGotAnySpareChange = "20282"
        coinMaster = "20284"
        didInit = true
    }

    private fun didInit() {
        if (didInit) return
        Gdx.app.log("Achievements", "You must call first Achievements.init()")
    }


    fun unlockSuperJump() {
        didInit()
    }


    fun unlockCombos() {
        didInit()
    }

    fun unlockCoins() {
        didInit()
    }
}
