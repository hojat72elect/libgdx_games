package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.screens.BaseScreen

/**
 * This class represents any of the bullets that Shark can fire.
 */
class Blast : Poolable {
    var state = STATE_NORMAL
    val position = Vector2()
    var velocity = Vector2()
    var stateTime = 0F

    fun initialize(x: Float, y: Float) {
        position.set(x, y)
        velocity.set(0F, 0F)
        stateTime = 0F
        state = STATE_NORMAL
    }

    fun update(body: Body, delta: Float) {
        if (state == STATE_NORMAL) {
            position.x = body.position.x
            position.y = body.position.y

            velocity = body.linearVelocity

            if (position.y < -3 || position.x > BaseScreen.WORLD_WIDTH + 3) hit()
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

    override fun reset() {}

    companion object {

        const val STATE_NORMAL = 0
        const val STATE_HIT = 1
        const val STATE_REMOVE = 2

        const val DURATION_HIT = 0.3F
        const val SPEED_X = 5.5F
        const val DRAW_WIDTH = .32F
        const val DRAW_HEIGHT = .32F
        const val WIDTH = .31F
        const val HEIGHT = .31F
    }
}