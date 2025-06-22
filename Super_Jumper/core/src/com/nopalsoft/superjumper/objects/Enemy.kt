package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.superjumper.screens.Screens

class Enemy : Poolable {

    var state = 0
    val position = Vector2()
    private var speed: Vector2 = Vector2()
    private var angleDegree = 0f
    var stateTime = 0f


    fun initialize(x: Float, y: Float) {
        position[x] = y
        speed[0f] = 0f // I set the speed from the method where I create it.
        stateTime = 0f
        state = STATE_NORMAL
        angleDegree = 0f
    }

    fun update(body: Body, delta: Float) {
        position.x = body.position.x
        position.y = body.position.y

        speed = body.linearVelocity

        if (state == STATE_NORMAL) {
            if (position.x >= Screens.WORLD_WIDTH || position.x <= 0) {
                speed.x *= -1
            }
        } else {
            body.angularVelocity = MathUtils.degRad * 360
            if (speed.y < -5) speed.y = -5f
        }

        body.linearVelocity = speed

        angleDegree = body.angle * MathUtils.radDeg

        speed = body.linearVelocity
        stateTime += delta
    }

    fun hit() {
        if (state == STATE_NORMAL) {
            state = STATE_DEAD
            stateTime = 0f
        }
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_DEAD = 1
        const val DRAW_WIDTH = .95f
        const val DRAW_HEIGHT = .6f
        const val WIDTH = .65f
        const val HEIGHT = .4f
        const val SPEED_X = 2f
    }
}
