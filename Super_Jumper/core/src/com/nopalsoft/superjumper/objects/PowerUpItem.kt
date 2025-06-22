package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable

class PowerUpItem : Poolable {

    var state = 0
    var type = 0
    val position = Vector2()
    var stateTime = 0f

    fun init(x: Float, y: Float) {
        position[x] = y
        state = STATE_NORMAL
        stateTime = 0f

        type = MathUtils.random(2)
    }

    fun update(delta: Float) {
        stateTime += delta
    }

    fun take() {
        state = STATE_TAKEN
        stateTime = 0f
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_TAKEN = 1
        const val DRAW_WIDTH = .27f
        const val DRAW_HEIGHT = .34f
        const val WIDTH = .25f
        const val HEIGHT = .32f
        const val TYPE_JETPACK = 0
        const val TYPE_BUBBLE = 1
        const val TYPE_GUN = 2
    }
}
