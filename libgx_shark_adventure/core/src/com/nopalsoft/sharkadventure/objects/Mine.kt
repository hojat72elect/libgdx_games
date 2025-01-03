package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.Settings
import com.nopalsoft.sharkadventure.screens.BaseScreen

class Mine : Poolable {
    var state = STATE_NORMAL
    var type = MathUtils.random(3)
    val position = Vector2()
    var stateTime = 0F

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

            if (position.x < -3 || position.y > BaseScreen.WORLD_HEIGHT + 3) hit()
        } else if (state == STATE_EXPLODE && stateTime >= EXPLOSION_DURATION) {
            state = STATE_REMOVE
            stateTime = 0f
        }

        stateTime += delta
    }

    fun hit() {
        if (state == STATE_NORMAL) {
            state = STATE_EXPLODE
            stateTime = 0F
            if (Settings.isSoundOn) {
                Assets.playExplosionSound()
            }
        }
    }

    override fun reset() {}

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_EXPLODE = 1
        const val STATE_REMOVE = 2

        const val SPEED_X = -1

        const val EXPLOSION_DURATION = .8F

        const val DRAW_WIDTH = .56F
        const val DRAW_HEIGHT = .64F

        const val WIDTH = .53F
        const val HEIGHT = .61F

        const val TYPE_GRAY = 2
        const val TYPE_OXIDE = 3
    }
}