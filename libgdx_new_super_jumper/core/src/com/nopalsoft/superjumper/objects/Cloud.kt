package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.superjumper.screens.Screens

/**
 * Clouds are indestructible. They all start out happy until you shoot them.
 */
class Cloud : Poolable {
    @JvmField
    var state: Int = 0

    private var timeToBlow: Float = 0f

    private var durationBlow: Float = 0f

    @JvmField
    var type: Int = 0
    private var timeUntilLightning: Float = 0f

    @JvmField
    val position: Vector2 = Vector2()
    private var speed: Vector2

    @JvmField
    var isBlowing: Boolean = false

    @JvmField
    var isLightning: Boolean = false

    var stateTime: Float = 0f

    init {
        speed = Vector2()
    }

    fun init(x: Float, y: Float) {
        position[x] = y
        speed[0f] = 0f // I set the speed from the method where I create it.
        stateTime = 0f
        state = STATE_NORMAL
        type = TYPE_HAPPY

        isLightning = false
        isBlowing = false

        durationBlow = 0f
        timeToBlow = durationBlow
        timeUntilLightning = MathUtils.random(TIME_UNTIL_LIGHTNING)
    }

    fun update(body: Body, delta: Float) {
        position.x = body.position.x
        position.y = body.position.y

        speed = body.linearVelocity

        if (position.x >= Screens.WORLD_WIDTH || position.x <= 0) {
            speed.x *= -1
        }

        body.linearVelocity = speed
        speed = body.linearVelocity

        if (type == TYPE_ANGRY) {
            timeToBlow += delta
            if (!isBlowing && timeToBlow >= TIME_TO_BLOW) {
                if (MathUtils.randomBoolean()) isBlowing = true
                timeToBlow = 0f
            }

            if (isBlowing) {
                durationBlow += delta
                if (durationBlow >= DURATION_BLOW) {
                    durationBlow = 0f
                    isBlowing = false
                }
            }
        } else { // TYPE HAPPY

            if (!isLightning) {
                timeUntilLightning += delta
                if (timeUntilLightning >= TIME_UNTIL_LIGHTNING) {
                    isLightning = true
                }
            }
        }

        stateTime += delta
    }

    fun fireLighting() {
        isLightning = false
        timeUntilLightning = MathUtils.random(TIME_UNTIL_LIGHTNING)
    }

    fun hit() {
        if (type == TYPE_HAPPY) {
            type = TYPE_ANGRY
            durationBlow = 0f
            timeToBlow = durationBlow
            stateTime = timeToBlow
        }
    }

    fun destroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DEAD
            stateTime = 0f
        }
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_DEAD: Int = 1
        const val DRAW_WIDTH: Float = .95f
        const val DRAW_HEIGHT: Float = .6f

        const val WIDTH: Float = .65f
        const val HEIGHT: Float = .4f

        const val SPEED_X: Float = .5f

        const val TYPE_HAPPY: Int = 0
        const val TYPE_ANGRY: Int = 1
        const val TIME_UNTIL_LIGHTNING: Float = 5f

        const val TIME_TO_BLOW: Float = 2f
        const val DURATION_BLOW: Float = 3f
    }
}
