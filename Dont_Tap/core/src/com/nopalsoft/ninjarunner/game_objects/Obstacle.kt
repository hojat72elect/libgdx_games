package com.nopalsoft.ninjarunner.game_objects

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.ninjarunner.Assets

open class Obstacle : Poolable {

    @JvmField
    var state = 0

    @JvmField
    val position = Vector2()

    var stateTime = 0f

    @JvmField
    var effect: PooledEffect? = null

    fun initialize(x: Float, y: Float) {
        position.set(x, y)
        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float) {
        stateTime += delta
    }

    fun setDestroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DESTROY
            stateTime = 0f
            effect = Assets.boxesEffectPool?.obtain()
            effect!!.setPosition(position.x, position.y)
        }
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_DESTROY = 1
    }
}
