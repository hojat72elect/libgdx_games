package com.nopalsoft.clumsy.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Ufo(x: Float, y: Float) {
    @JvmField
    var position: Vector2 = Vector2(x, y)

    @JvmField
    var state: Int

    @JvmField
    var stateTime: Float = 0f

    @JvmField
    var angleRad: Float = 0f

    init {
        state = STATE_NORMAL
    }

    fun update(delta: Float, body: Body?) {
        if (body != null) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y

            val velocity = body.getLinearVelocity()

            angleRad = MathUtils.atan2(-.1f, velocity.y)
            val angleLimitRad: Float

            val maxAngleDegrees = 15
            angleLimitRad = Math.toRadians(maxAngleDegrees.toDouble()).toFloat()

            if (angleRad > angleLimitRad) angleRad = angleLimitRad
            else if (angleRad < -angleLimitRad) angleRad = -angleLimitRad
        }

        stateTime += delta
    }

    val hurt: Unit
        get() {
            state = STATE_HURT
            stateTime = 0f
        }

    fun die() {
        state = STATE_DEAD
        stateTime = 0f
    }

    companion object {
        const val HURT_DURATION: Float = .5f
        const val DEATH_DURATION: Float = .75f

        @JvmField
        var JUMP_SPEED: Float = 5f

        @JvmField
        var STATE_NORMAL: Int = 0

        @JvmField
        var STATE_HURT: Int = 1

        @JvmField
        var STATE_DEAD: Int = 2
    }
}
