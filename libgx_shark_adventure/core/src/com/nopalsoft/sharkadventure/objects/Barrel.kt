package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.Settings

class Barrel : Poolable {
    @JvmField
    var state = 0

    @JvmField
    var type = 0

    @JvmField
    val position = Vector2()

    @JvmField
    var angleDegree = 0f

    @JvmField
    var stateTime = 0f

    fun init(x: Float, y: Float) {
        position.set(x, y)
        stateTime = 0f
        state = STATE_NORMAL
        type = MathUtils.random(3)
    }

    fun update(body: Body, delta: Float) {
        if (state == STATE_NORMAL) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y
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
            if (Settings.isSoundOn) {
                Assets.playExplosionSound()
            }
        }
    }

    fun remove() {
        state = STATE_REMOVE
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_EXPLODE = 1
        const val STATE_REMOVE = 2
        const val EXPLOSION_DURATION = .1f * 8f

        const val DRAW_WIDTH = .43f
        const val DRAW_HEIGHT = .68f

        const val WIDTH = .40f
        const val HEIGHT = .65f

        const val TYPE_YELLOW = 0
        const val TYPE_BLACK = 1
        const val TYPE_RED = 2
        const val TYPE_GREEN = 3
    }
}
