package com.nopalsoft.zombiekiller

import com.badlogic.gdx.Gdx

object Achievements {
    var didInit: Boolean = false

    var collector: String? = null
    var collectorMaster: String? = null
    var aLittlePatience: String? = null
    var anHonorableKill: String? = null
    var stayDead: String? = null
    var cleaningUp: String? = null

    fun init() {
        collector = "CgkI8sWCvZ0NEAIQAw"
        collectorMaster = "CgkI8sWCvZ0NEAIQBA"
        aLittlePatience = "CgkI8sWCvZ0NEAIQCA"
        anHonorableKill = "CgkI8sWCvZ0NEAIQCQ"
        stayDead = "CgkI8sWCvZ0NEAIQBQ"
        cleaningUp = "CgkI8sWCvZ0NEAIQBg"
        didInit = true
    }

    private fun didInit() {
        if (didInit) return
        Gdx.app.log("Achievements", "You must call first Achievements.init()")
    }

    fun unlockKilledZombies() {
        didInit()
    }

    fun unlockCollectedSkulls() {
        didInit()
    }
}
