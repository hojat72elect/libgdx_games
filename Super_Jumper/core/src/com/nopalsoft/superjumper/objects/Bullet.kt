package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable

class Bullet : Poolable {
    var state = 0
    val position = Vector2()
    var stateTime = 0f

    fun init(x: Float, y: Float) {
        position[x] = y
        stateTime = 0f
        state = STATE_NORMAL
    }

    fun update(body: Body, delta: Float) {
        position.x = body.position.x
        position.y = body.position.y
        stateTime += delta
    }

    fun destroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DESTROY
            stateTime = 0f
        }
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_DESTROY: Int = 1
        const val SIZE: Float = .15f

        const val SPEED: Float = 8f
    }
}
