package com.nopalsoft.ponyrace.game_objects

import java.util.Random

class Bonfire(x: Float, y: Float, oRan: Random) : BaseGameObject(x, y) {
    @JvmField
    var lastStateTime = 0F

    @JvmField
    var stateTime = oRan.nextFloat() * 5f

    init {
        lastStateTime = stateTime
    }

    fun update(delta: Float) {
        lastStateTime = stateTime
        stateTime += delta
    }
}
