package com.nopalsoft.ponyrace.game_objects

import java.util.Random

class BloodStone(x: Float, y: Float, val type: Type, random: Random) : BaseGameObject(x, y) {

    var stateTime = random.nextFloat() * 5F
    var lastStateTime = stateTime

    fun update(delta: Float) {
        lastStateTime = stateTime
        stateTime += delta
    }

    enum class Type {
        SMALL, MEDIUM, LARGE
    }
}