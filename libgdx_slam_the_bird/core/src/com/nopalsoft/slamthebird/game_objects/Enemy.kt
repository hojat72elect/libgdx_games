package com.nopalsoft.slamthebird.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.slamthebird.Settings
import java.util.Random

class Enemy(x: Float, y: Float) {

    private var timeToChangeVelocity = 0f
    private var timeToEvolve = 0f
    private var durationFrozen = 0f
    private var baseFrozenDuration = 5f

    @JvmField
    var position: Vector2 = Vector2(x, y)

    @JvmField
    var velocity: Vector2

    private var isFrozen: Boolean

    @JvmField
    var state: Int

    @JvmField
    var stateTime: Float

    @JvmField
    var lives: Int

    @JvmField
    var visualScale: Float = 0f

    init {
        state = STATE_JUST_APPEARED
        lives = 2
        stateTime = 0f
        velocity = Vector2()
        isFrozen = false
        durationFrozen = 0f
        baseFrozenDuration += Settings.BOOST_FREEZE.toFloat()
    }

    fun update(delta: Float, body: Body, oRan: Random) {
        position.x = body.position.x
        position.y = body.position.y

        if (isFrozen) {
            body.setLinearVelocity(0f, 0f)
            if (durationFrozen >= baseFrozenDuration) {
                isFrozen = false
                durationFrozen = 0f
                setNewVelocity(body, oRan, false)
            }
            durationFrozen += delta
            return  // Nothing else can be done if it's frozen. It doesn't move, it doesn't change speed, it doesn't evolve, it doesn't swim.
        }

        // Whatever happens, I don't want it to be higher than 10f.
        if (position.y > 10f) {
            velocity = body.linearVelocity
            body.setLinearVelocity(velocity.x, velocity.y * -1)
        }
        if (state == STATE_JUST_APPEARED) {
            visualScale = stateTime * 1.5f / TIME_JUST_APPEARED // 1.5f maximum scale

            if (stateTime >= TIME_JUST_APPEARED) {
                state = STATE_FLYING
                stateTime = 0f
                setNewVelocity(body, oRan, false)
            }
        }

        if (state != STATE_JUST_APPEARED) {
            timeToChangeVelocity += delta
            if (timeToChangeVelocity >= TIME_TO_CHANGE_VELOCITY) {
                timeToChangeVelocity -= TIME_TO_CHANGE_VELOCITY

                val vel = body.linearVelocity

                // Change in X
                if (oRan.nextBoolean()) vel.x *= -1f

                if (state == STATE_FLYING) {
                    if (oRan.nextBoolean()) vel.y *= -1f
                }
                body.linearVelocity = vel
            }
        }

        if (state == STATE_HIT) {
            body.gravityScale = 1f
            timeToEvolve += delta
            if (timeToEvolve >= TIME_TO_EVOLVE) {
                state = STATE_EVOLVING
                stateTime = 0f
                timeToEvolve = 0f
            }
        }

        if (state == STATE_EVOLVING && stateTime >= EVOLVING_DURATION) {
            state = STATE_FLYING
            body.gravityScale = 0f
            setNewVelocity(body, oRan, true)
            lives = 3
            stateTime = 0f
        }

        velocity = body.linearVelocity

        limitSpeed(body)
        velocity = body.linearVelocity

        stateTime += delta
    }

    /**
     * Limits speed because sometimes the resulting force of the collision drove the enemy crazy.
     */
    private fun limitSpeed(body: Body) {
        var currentSpeed = MAX_SPEED_BLUE
        if (lives == 3) currentSpeed = MAX_SPEED_RED

        if (velocity.x > currentSpeed) {
            velocity.x = currentSpeed
        } else if (velocity.x < -currentSpeed) {
            velocity.x = -currentSpeed
        }

        if (lives > 1) { // So the bird falls quickly if I take off its wings
            if (velocity.y > currentSpeed) {
                velocity.y = currentSpeed
            } else if (velocity.y < -currentSpeed) {
                velocity.y = -currentSpeed
            }
        }
        body.linearVelocity = velocity
    }

    /**
     * If it is touching the floor I make the velocity in Y always generate positive.
     */
    private fun setNewVelocity(body: Body, random: Random, isTouchingFloor: Boolean) {
        var currentSpeed = MAX_SPEED_BLUE
        if (lives == 3) currentSpeed = MAX_SPEED_RED

        val velocityX = random.nextFloat() * currentSpeed * 2 - currentSpeed
        val velocityY = if (isTouchingFloor) random.nextFloat() * currentSpeed
        else random.nextFloat() * currentSpeed * 2 - currentSpeed

        body.setLinearVelocity(velocityX, velocityY)
    }

    fun hit() {
        lives--
        if (lives == 1) state = STATE_HIT
        else if (lives == 0) state = STATE_DEAD

        stateTime = 0f
    }

    fun die() {
        lives = 0
        state = STATE_DEAD
        stateTime = 0f
    }

    fun setFrozen() {
        durationFrozen = 0f
        isFrozen = true
    }

    companion object {

        private const val EVOLVING_DURATION = 1.5f

        private const val TIME_TO_EVOLVE = 3f
        private const val TIME_JUST_APPEARED = 1.7f

        private const val TIME_TO_CHANGE_VELOCITY = 3f

        @JvmField
        var WIDTH = .4f

        @JvmField
        var HEIGHT = .4f

        @JvmField
        var STATE_JUST_APPEARED = 0

        @JvmField
        var STATE_FLYING = 1
        var STATE_HIT = 2

        @JvmField
        var STATE_EVOLVING = 3 // So that it can fly again

        @JvmField
        var STATE_DEAD = 4

        var MAX_SPEED_BLUE = 1.75f
        var MAX_SPEED_RED = 3.25f
    }
}
