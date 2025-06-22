package com.nopalsoft.slamthebird.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Settings

class Player(x: Float, y: Float) {

    var durationSuperJump: Float = 0f

    var invincibilityDuration: Float = 0f

    var position = Vector2(x, y)

    var state: Int

    var stateTime: Float = 0f

    private var jump: Boolean

    var slam: Boolean = false

    var isSuperJump: Boolean = false

    var isInvincible: Boolean = false

    var angleDegrees: Float = 0f

    var velocity: Vector2

    init {
        state = STATE_JUMPING
        velocity = Vector2()
        jump = true // to make the first jump

        DURATION_SUPER_JUMP += Settings.BOOST_SUPER_JUMP.toFloat()
        INVINCIBLE_DURATION += Settings.BOOST_INVINCIBLE.toFloat()
    }

    fun update(delta: Float, body: Body, acelX: Float, slam: Boolean) {
        this.slam = slam // To draw the rapid fall =)
        position.x = body.position.x
        position.y = body.position.y
        angleDegrees = 0f
        if (state == STATE_FALLING || state == STATE_JUMPING) {
            if (slam) body.gravityScale = 2.5f
            else body.gravityScale = 1f

            if (jump) {
                jump = false
                state = STATE_JUMPING
                stateTime = 0f
                if (isSuperJump) {
                    body.setLinearVelocity(
                        body.linearVelocity.x,
                        JUMP_SPEED + 3
                    )
                } else {
                    body.setLinearVelocity(
                        body.linearVelocity.x,
                        JUMP_SPEED
                    )
                }
            }

            val velocity = body.linearVelocity

            if (velocity.y < 0 && state != STATE_FALLING) {
                state = STATE_FALLING
                stateTime = 0f
            }
            body.setLinearVelocity(acelX * MOVE_SPEED, velocity.y)

            if (isSuperJump) {
                durationSuperJump += delta
                if (durationSuperJump >= DURATION_SUPER_JUMP) {
                    isSuperJump = false
                    durationSuperJump = 0f
                }
            }

            if (isInvincible) {
                invincibilityDuration += delta
                if (invincibilityDuration >= INVINCIBLE_DURATION) {
                    isInvincible = false
                    invincibilityDuration = 0f
                }
            }
        } else if (state == STATE_DEAD) {
            body.setLinearVelocity(0f, -3f)
            body.isFixedRotation = false
            angleDegrees = Math.toDegrees(body.angle.toDouble()).toFloat()
            body.angularVelocity = Math.toRadians(20.0).toFloat()
        }
        velocity = body.linearVelocity
        stateTime += delta
    }

    fun updateReady(body: Body, accelerationX: Float) {
        position.x = body.position.x
        position.y = body.position.y

        body.setLinearVelocity(accelerationX * MOVE_SPEED, 0f)
        velocity = body.linearVelocity
    }

    fun jump() {
        if (state == STATE_FALLING) {
            jump = true
            stateTime = 0f
            Assets.playSound(Assets.soundJump!!)
        }
    }

    /**
     * The robot is hit and dies.
     */
    fun hit() {
        state = STATE_DEAD
        stateTime = 0f
    }

    companion object {

        private var JUMP_SPEED = 6.25f
        private var MOVE_SPEED = 5f
        var DURATION_SUPER_JUMP = 5f
        var INVINCIBLE_DURATION = 5f
        var RADIUS = .28f
        var STATE_FALLING = 0
        var STATE_JUMPING = 1
        var STATE_DEAD = 2
        const val DEAD_ANIMATION_DURATION = 2f
    }
}
