package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.Settings
import com.nopalsoft.sharkadventure.screens.BaseScreen

/**
 * Each one of the missile that are shot by the submarine.
 */
class Missile : Poolable {

    var state = STATE_NORMAL
    val position = Vector2()
    var stateTime = 0F
    var isGoingLeft = false

    fun initialize(x: Float, y: Float, isGoingLeft: Boolean) {
        position.set(x, y)
        stateTime = 0F
        state = STATE_NORMAL
        this.isGoingLeft = isGoingLeft
    }

    fun update(body: Body, delta: Float) {
        if (state == STATE_NORMAL) {
            position.x = body.position.x
            position.y = body.position.y

            if (position.y < -3 || position.x > BaseScreen.WORLD_WIDTH + 3) hit()
        } else if (state == STATE_EXPLODE && stateTime >= EXPLOSION_DURATION) {
            state = STATE_REMOVE
            stateTime = 0F
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

        const val EXPLOSION_DURATION = 0.8F
        const val SPEED_X = 1.7F
        const val DRAW_WIDTH = .8F
        const val DRAW_HEIGHT = .3F
    }
}