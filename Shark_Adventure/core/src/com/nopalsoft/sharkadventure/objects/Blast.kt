package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.screens.Screens

class Blast : Poolable {
    @JvmField
    var state = 0

    @JvmField
    val position = Vector2()

    @JvmField
    var velocity = Vector2()

    @JvmField
    var stateTime = 0f

    fun init(x: Float, y: Float) {
        position.set(x, y)
        velocity.set(0f, 0f)
        stateTime = 0f
        state = STATE_NORMAL
    }

    fun update(body: Body, delta: Float) {
        if (state == STATE_NORMAL) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y

            velocity = body.getLinearVelocity()

            if (position.y < -3 || position.x > Screens.WORLD_WIDTH + 3) hit()
        } else if (state == STATE_HIT && stateTime >= DURATION_HIT) {
            state = STATE_REMOVE
            stateTime = 0f
        }

        stateTime += delta
    }

    fun hit() {
        if (state == STATE_NORMAL) {
            state = STATE_HIT
            stateTime = 0f
        }
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_HIT = 1
        const val STATE_REMOVE = 2
        const val DURATION_HIT = .05f * 6f
        const val SPEED_X = 5.5f
        const val DRAW_WIDTH = .32f
        const val DRAW_HEIGHT = .32f
        const val WIDTH = .31f
        const val HEIGHT = .31f
    }
}
