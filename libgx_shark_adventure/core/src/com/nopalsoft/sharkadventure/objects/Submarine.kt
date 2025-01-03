package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.Settings
import com.nopalsoft.sharkadventure.screens.BaseScreen

class Submarine : Poolable {
    var state = 0
    var type = 0
    private var timeToFire = 0F
    private val targetPosition = Vector2()
    val position = Vector2()
    var velocity = Vector2()
    private var angleDegree = 0F
    private var stateTime = 0F
    val explosionStateTimes = FloatArray(5)
    var didFire = false

    private var life = 0

    fun initialize(x: Float, y: Float, targetX: Float, targetY: Float) {
        position.set(x, y)
        targetPosition.set(targetX, targetY)
        stateTime = 0f
        state = STATE_NORMAL
        type = MathUtils.random(1)
        timeToFire = 0f
        life = 10

        explosionStateTimes[0] = -1f
        explosionStateTimes[1] = -.5f
        explosionStateTimes[2] = -.7f
        explosionStateTimes[3] = 0f
        explosionStateTimes[4] = -.3f
    }

    fun update(body: Body, delta: Float) {
        velocity = body.linearVelocity

        if (state == STATE_NORMAL) {
            position.x = body.position.x
            position.y = body.position.y
            angleDegree = MathUtils.radDeg * body.angle

            if (position.y < -4 || position.y > BaseScreen.WORLD_HEIGHT + 4 || position.x < -4 || position.x > BaseScreen.WORLD_WIDTH + 3) remove()

            velocity.set(targetPosition).sub(position).nor().scl(SPEED)

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

        body.linearVelocity = velocity

        stateTime += delta
    }

    fun hit() {
        if (state == STATE_NORMAL) {
            life--
            if (life <= 0) {
                state = STATE_EXPLODE
                stateTime = 0f
                if (Settings.isSoundOn) {
                    Assets.playExplosionSound()
                }
            }
        }
    }

    private fun remove() {
        state = STATE_REMOVE
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_EXPLODE = 1
        const val STATE_REMOVE = 2
        const val EXPLOSION_DURATION = 0.8F
        private const val SPEED = 1.2F
        private val TIME_TO_FIRE = MathUtils.random(1.25f, 2.75f)

        const val DRAW_WIDTH = 1.28F
        const val DRAW_HEIGHT = 1.12F
        const val WIDTH = 1.25F
        const val HEIGHT = 1.09F
        const val TYPE_YELLOW = 0
        const val TYPE_RED = 1

    }
}