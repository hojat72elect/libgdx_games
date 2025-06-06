package com.nopalsoft.ninjarunner.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable

class Wall : Poolable {
    @JvmField
    var state = 0

    @JvmField
    val position = Vector2()
    var stateTime = 0f


    fun initialize(x: Float, y: Float) {
        position.set(x, y)
        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float) {
        stateTime += delta
    }

    fun setDestroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DESTROY
            stateTime = 0f
        }
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_DESTROY = 1
        const val WIDTH = .98f
        const val HEIGHT = 4.30f
    }
}
