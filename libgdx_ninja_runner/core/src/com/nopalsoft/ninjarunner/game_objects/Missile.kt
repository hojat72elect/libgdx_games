package com.nopalsoft.ninjarunner.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.ninjarunner.Assets


class Missile() : Poolable, Comparable<Missile?> {
    @JvmField
    var state: Int = 0

    @JvmField
    val position: Vector2 = Vector2()

    @JvmField
    var stateTime = 0f

    @JvmField
    var distanceFromPlayer = 0f

    fun initialize(x: Float, y: Float) {
        position.set(x, y)
        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float, body: Body, oPlayer: Player) {
        if (state == STATE_NORMAL) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y
        }
        if (state == STATE_EXPLODE) {
            if (stateTime >= DURATION_EXPLOSION) {
                state = STATE_DESTROY
                stateTime = 0f
            }
        }

        distanceFromPlayer = oPlayer.position.dst(position)
        stateTime += delta
    }

    fun setHitTarget() {
        if (state == STATE_NORMAL) {
            state = STATE_EXPLODE
            stateTime = 0f
        }
    }

    fun setDestroy() {
        if (state != STATE_DESTROY) {
            state = STATE_DESTROY
            stateTime = 0f
        }
    }

    override fun reset() {
    }

    override fun compareTo(o2: Missile?): Int {
        return distanceFromPlayer.compareTo(o2!!.distanceFromPlayer)
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_EXPLODE: Int = 1
        const val STATE_DESTROY: Int = 2
        private val DURATION_EXPLOSION = Assets.explosionAnimation.animationDuration + .1f

        const val WIDTH = 1.27f
        const val HEIGHT = .44f

        @JvmField
        val SPEED_X = -2.5f
    }
}
