package com.nopalsoft.clumsy.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable

class Tail : Poolable {
    @JvmField
    val position: Vector2 = Vector2()

    fun update(delta: Float) {
        position.x += SPEED_X * delta
    }

    fun init(x: Float, y: Float) {
        position.set(x, y)
    }

    override fun reset() {
    }

    companion object {
        var SPEED_X: Float = -5f
    }
}
