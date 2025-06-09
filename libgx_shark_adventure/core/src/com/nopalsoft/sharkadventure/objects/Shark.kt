package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.Settings

class Shark(x: Float, y: Float) : Poolable {
    @JvmField
    var state: Int

    @JvmField
    var life: Int

    @JvmField
    var energy: Float

    var durationTurbo: Float = 0f


    var timeToRechargeEnergy: Float = 0f

    @JvmField
    val position: Vector2 = Vector2(x, y)
    var speed: Vector2

    @JvmField
    var angleDegree: Float = 0f

    @JvmField
    var stateTime: Float

    @JvmField
    var isTurbo: Boolean = false

    @JvmField
    var isFacingLeft: Boolean = false

    @JvmField
    var isFiring: Boolean = false

    var setSpeedDie: Boolean

    @JvmField
    var didGetHurtOnce: Boolean

    /**
     * Indicates whether an X flip was just performed to recreate the body.
     */
    @JvmField
    var didFlipX: Boolean = false

    init {
        speed = Vector2()

        stateTime = 0f
        state = STATE_NORMAL

        life = MAX_LIFE
        energy = MAX_ENERGY.toFloat()

        setSpeedDie = true
        didGetHurtOnce = false
    }

    fun updateStateTime(delta: Float) {
        stateTime += delta
    }

    fun update(body: Body, delta: Float, accelX: Float, didSwimUp: Boolean) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        speed = body.getLinearVelocity()

        if (state == STATE_NORMAL) {
            if (isTurbo) {
                durationTurbo += delta
                if (durationTurbo >= DURATION_TURBO) {
                    durationTurbo = 0f
                    isTurbo = false
                }
            }

            if (isFiring) {
                if (stateTime >= DURATION_FIRING) {
                    stateTime = 0f
                    isFiring = false
                }
            }

            speed.x = accelX * SPEED_X

            if (speed.x > 0 && isFacingLeft) {
                didFlipX = true
                isFacingLeft = false
            } else if (speed.x < 0 && !isFacingLeft) {
                didFlipX = true
                isFacingLeft = true
            }

            if (didSwimUp) {
                speed.y = SPEED_Y

                if (Settings.isSoundOn) Assets.swimSound.play()
            }

            timeToRechargeEnergy += delta
            if (timeToRechargeEnergy >= TIME_TO_RECHARGE_ENERGY) {
                timeToRechargeEnergy -= TIME_TO_RECHARGE_ENERGY
                energy += 1.5f
                if (energy > MAX_ENERGY) energy = MAX_ENERGY.toFloat()
            }
        } else {
            if (setSpeedDie) {
                speed.set(0f, 0f)
                setSpeedDie = false
            }

            body.gravityScale = -.15f
            body.angularVelocity = MathUtils.degreesToRadians * 90
            angleDegree = MathUtils.radDeg * body.angle

            if (angleDegree >= 180) {
                body.angularVelocity = 0f
                angleDegree = 180f
            }
        }

        body.linearVelocity = speed
        stateTime += delta
    }

    fun fire() {
        if (state == STATE_NORMAL) {
            isFiring = true
            stateTime = 0f
            energy -= 1.25f
            if (energy < 0) energy = 0f
        }
    }

    fun hit() {
        if (state == STATE_NORMAL) {
            life--
            if (life == 0) {
                state = STATE_DEAD
                stateTime = 0f
            }
            didGetHurtOnce = true
        }
    }

    override fun reset() {
    }

    companion object {
        private const val SPEED_X = 3.5f
        private const val SPEED_Y = 1.85f
        private const val TIME_TO_RECHARGE_ENERGY = 1.25f
        const val STATE_NORMAL = 0
        const val STATE_DEAD = 1
        const val MAX_LIFE = 5
        const val MAX_ENERGY = 50
        const val DURATION_TURBO = 3f
        const val DURATION_FIRING = .075f * 5
    }
}
