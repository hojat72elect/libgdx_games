package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.screens.BaseScreen

/**
 * Represents the chain which holds an under-water mine in our game. The physical behavior of this
 * chain is implemented via Box2D. Our chain has properties such as position, angle, and state. Everything is cleaned up when the mine attached to chain is hit or chain goes off-screen.
 */
class Chain : Poolable {

    var state = STATE_NORMAL
    val position = Vector2()
    var angleDegree = 0F

    fun initialize(x: Float, y: Float) {
        position.set(x, y)
        state = STATE_NORMAL
    }

    fun update(body: Body) {
        position.x = body.position.x
        position.y = body.position.y
        angleDegree = MathUtils.radDeg * body.angle

        if (position.x < -3 || position.y > BaseScreen.WORLD_HEIGHT + 3) state = STATE_REMOVE
    }

    fun hit() {
        if (state == STATE_NORMAL) state = STATE_REMOVE
    }

    override fun reset() {}

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_REMOVE = 2
        const val DRAW_WIDTH = .16F
        const val DRAW_HEIGHT = .24F
        const val WIDTH = .13F
        const val HEIGHT = .21F
        const val SPEED_X = -1F
    }
}