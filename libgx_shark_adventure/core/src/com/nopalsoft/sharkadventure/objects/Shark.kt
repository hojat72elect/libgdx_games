package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.Settings

class Shark(x: Float, y: Float) : Poolable {

    val position = Vector2(x, y)
    var state = STATE_NORMAL
    var life = MAX_LIFE
    var energy = MAX_ENERGY.toFloat()
    private var speed = Vector2()
    var angleDegree = 0F
    var stateTime = 0F
    var isTurbo = false
    var isFacingLeft = false
    var isFiring = false
    var didGetHurtOnce = false
    var didFlipX =
        false // Indicates if an X flip has just been performed to recreate the body
    private var durationTurbo = 0F
    private var timeToRechargeEnergy = 0F
    private var setSpeedDie = true

    fun updateStateTime(delta: Float) {
        stateTime += delta
    }

    fun update(body: Body, delta: Float, accelX: Float, didSwimUp: Boolean) {
        position.x = body.position.x
        position.y = body.position.y

        speed = body.linearVelocity

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

                if (Settings.isSoundOn) Assets.soundSwim?.play()
            }

            timeToRechargeEnergy += delta
            if (timeToRechargeEnergy >= TIME_TO_RECHARGE_ENERGY) {
                timeToRechargeEnergy -= TIME_TO_RECHARGE_ENERGY
                energy += 1.5F
                if (energy > MAX_ENERGY) energy = MAX_ENERGY.toFloat()
            }
        } else {
            if (setSpeedDie) {
                speed.set(0F, 0F)
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

        const val STATE_NORMAL = 0
        const val STATE_DEAD = 1
        const val MAX_LIFE = 5
        const val MAX_ENERGY = 50
        private const val DURATION_TURBO = 3F
        private const val DURATION_FIRING = .075F * 5
        private const val TIME_TO_RECHARGE_ENERGY = 1.25F
        private const val SPEED_X = 3.5F
        private const val SPEED_Y = 1.85F
    }
}