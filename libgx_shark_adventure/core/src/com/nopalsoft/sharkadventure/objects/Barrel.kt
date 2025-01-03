package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.Settings

/**
 * Represents a barrel object in our game. It can have 3 states : 1- Normal, 2- Exploding,
 * or 3- Removed. It also has properties like position, angle, and type (color of the barrel).
 * We define its behavior, including how it's initialized, updated over time, and how it reacts to
 * being hit.
 */
class Barrel : Poolable {
    var state = STATE_NORMAL
    val position = Vector2()
    var angleDegree = 0F
    var stateTime = 0F
    var type = 0

    fun initialize(x: Float, y: Float) {
        position.set(x, y)
        stateTime = 0F
        state = STATE_NORMAL
        type = MathUtils.random(3)
    }

    fun update(body: Body, delta: Float) {
        if (state == STATE_NORMAL) {
            position.x = body.position.x
            position.y = body.position.y
            angleDegree = MathUtils.radDeg * body.angle

            if (position.y < -3) remove()
        } else if (state == STATE_EXPLODE && stateTime >= EXPLOSION_DURATION) {
            state = STATE_REMOVE
            stateTime = 0f
        }

        stateTime += delta
    }

    fun hit() {
        if (state == STATE_NORMAL) {
            state = STATE_EXPLODE
            stateTime = 0f
            if (Settings.isSoundOn) Assets.playExplosionSound()
        }
    }

    private fun remove() {
        state = STATE_REMOVE
    }

    override fun reset() {}

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_EXPLODE = 1
        const val STATE_REMOVE = 2
        private const val EXPLOSION_DURATION = .1F * 8F
        const val DRAW_WIDTH = .43F
        const val DRAW_HEIGHT = .68F

        const val WIDTH = .40F
        const val HEIGHT = .65F
        const val TYPE_YELLOW = 0
        const val TYPE_BLACK = 1
        const val TYPE_RED = 2
        const val TYPE_GREEN = 3
    }
}