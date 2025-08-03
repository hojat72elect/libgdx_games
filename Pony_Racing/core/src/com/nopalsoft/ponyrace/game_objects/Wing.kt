package com.nopalsoft.ponyrace.game_objects

import java.util.Random

class Wing(x: Float, y: Float, oRan: Random) : BaseGameObject(x, y) {

    var stateTime = oRan.nextFloat() * 5F
    var lastStateTime = stateTime
    var state = State.IDLE

    fun update(delta: Float) {
        lastStateTime = stateTime
        stateTime += delta
    }

    enum class State {
        IDLE, ACTIVE
    }
}