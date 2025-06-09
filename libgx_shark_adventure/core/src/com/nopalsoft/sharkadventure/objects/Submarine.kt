package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.Settings
import com.nopalsoft.sharkadventure.screens.Screens

class Submarine : Poolable {
    @JvmField
    var state: Int = 0

    var timeToFire: Float = 0f

    @JvmField
    var type: Int = 0

    val targetPosition: Vector2 = Vector2()

    @JvmField
    val position: Vector2 = Vector2()

    @JvmField
    var speed: Vector2

    var angleDeg: Float = 0f

    var stateTime: Float = 0f

    @JvmField
    var explosionStateTimes: FloatArray

    @JvmField
    var didFire: Boolean = false

    var lives: Int = 0

    init {
        speed = Vector2()
        explosionStateTimes = FloatArray(5)
    }

    fun init(x: Float, y: Float, targetX: Float, targetY: Float) {
        position.set(x, y)
        targetPosition.set(targetX, targetY)
        stateTime = 0f
        state = STATE_NORMAL
        type = MathUtils.random(1)
        timeToFire = 0f
        TIME_TO_FIRE = MathUtils.random(1.25f, 2.75f)
        lives = 10

        explosionStateTimes[0] = -1f
        explosionStateTimes[1] = -.5f
        explosionStateTimes[2] = -.7f
        explosionStateTimes[3] = 0f
        explosionStateTimes[4] = -.3f
    }

    fun update(body: Body, delta: Float) {
        speed = body.getLinearVelocity()

        if (state == STATE_NORMAL) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y
            angleDeg = MathUtils.radDeg * body.angle

            if (position.y < -4 || position.y > Screens.WORLD_HEIGHT + 4 || position.x < -4 || position.x > Screens.WORLD_WIDTH + 3) remove()

            speed.set(targetPosition).sub(position).nor().scl(SPEED)

            timeToFire += delta
            if (timeToFire > TIME_TO_FIRE) {
                timeToFire -= TIME_TO_FIRE
                didFire = true
            }
        } else if (state == STATE_EXPLODE) {
            var remove = true
            for (i in explosionStateTimes.indices) {
                explosionStateTimes[i] += delta
                if (explosionStateTimes[i] < EXPLOSION_DURATION) {
                    remove = false
                }
            }

            if (remove) {
                state = STATE_REMOVE
                stateTime = 0f
            }
        }

        body.linearVelocity = speed

        stateTime += delta
    }

    fun hit() {
        if (state == STATE_NORMAL) {
            lives--
            if (lives <= 0) {
                state = STATE_EXPLODE
                stateTime = 0f
                if (Settings.isSoundOn) {
                    Assets.playExplosionSound()
                }
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
        const val SPEED = 1.2f

        var TIME_TO_FIRE = 0f
        const val DRAW_WIDTH = 1.28f
        const val DRAW_HEIGHT = 1.12f

        const val WIDTH = 1.25f
        const val HEIGHT = 1.09f

        const val TYPE_YELLOW = 0
        const val TYPE_RED = 1
    }
}
