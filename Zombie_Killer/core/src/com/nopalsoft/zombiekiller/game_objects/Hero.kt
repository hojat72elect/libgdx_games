package com.nopalsoft.zombiekiller.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.Settings

class Hero(x: Float, y: Float, tipo: Int) {
    @JvmField
    val type: Int

    val MAX_LIVES: Int = Settings.LEVEL_LIFE + 3

    @JvmField
    val MAX_SHIELDS: Int = Settings.LEVEL_SHIELD + 1

    @JvmField
    var state: Int

    @JvmField
    var position: Vector2

    @JvmField
    var stateTime: Float

    @JvmField
    var isFacingLeft: Boolean = false

    @JvmField
    var isWalking: Boolean = false

    @JvmField
    var isFiring: Boolean = false

    @JvmField
    var isClimbing: Boolean = false

    @JvmField
    var canJump: Boolean

    @JvmField
    var bodyCrate: Body? = null // Crate body standing

    @JvmField
    var isOnStairs: Boolean = false // True if you touch the stairs

    @JvmField
    var lives: Int

    @JvmField
    var shield: Int

    init {
        position = Vector2(x, y)
        state = STATE_NORMAL
        stateTime = 0f
        this.type = tipo
        canJump = true

        shield = MAX_SHIELDS
        lives = MAX_LIVES
    }

    fun update(delta: Float, body: Body, didJump: Boolean, accelX: Float, accelY: Float) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        if (state == STATE_HURT) {
            stateTime += delta
            if (stateTime >= DURATION_HURT) {
                state = STATE_NORMAL
                stateTime = 0f
            }
            return
        } else if (state == STATE_DEAD) {
            stateTime += delta
            return
        }

        if (isFiring && stateTime >= DURATION_IS_FIRING) {
            isFiring = false
            stateTime = 0f
        }

        val velocity = body.getLinearVelocity()

        if (didJump && canJump) {
            velocity.y = JUMP_SPEED
            canJump = false

            Assets.playSound(Assets.jump!!, 1f)
        }

        if (isOnStairs) {
            if (accelY != 0f) {
                isClimbing = true
                body.gravityScale = 0f
            }

            if (accelY == 1f) {
                velocity.y = 1f
            } else if (accelY == -1f) {
                velocity.y = -1f
            } else if (isClimbing) {
                velocity.y = 0f
            }
        } else {
            body.gravityScale = 1f
            isClimbing = false
        }

        if (accelX == -1f) {
            velocity.x = -WALK_SPEED
            isFacingLeft = true
            isWalking = true
        } else if (accelX == 1f) {
            velocity.x = WALK_SPEED
            isFacingLeft = false
            isWalking = true
        } else {
            if (bodyCrate != null) velocity.x = bodyCrate!!.getLinearVelocity().x
            else velocity.x = 0f
            isWalking = false
        }

        body.linearVelocity = velocity

        if (isClimbing && accelY != 0f) stateTime += delta
        else if (!isClimbing) stateTime += delta
    }

    fun hurt() {
        if (state != STATE_NORMAL) return

        if (shield > 0) {
            shield--
            state = STATE_HURT
            stateTime = 0f
            return
        }

        lives--
        if (lives > 0) {
            state = STATE_HURT
        } else {
            state = STATE_DEAD
        }
        stateTime = 0f
    }

    fun getShield() {
        shield += 2
        if (shield > MAX_SHIELDS) shield = MAX_SHIELDS
    }

    val heart: Unit
        get() {
            lives += 1
            if (lives > MAX_LIVES) lives = MAX_LIVES
        }

    fun die() {
        if (state != STATE_DEAD) {
            lives = 0
            shield = 0
            state = STATE_DEAD
            stateTime = 0f
        }
    }

    fun fire() {
        isFiring = true
        stateTime = 0f
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_HURT: Int = 1
        const val STATE_DEAD: Int = 2

        const val TYPE_FORCE: Int = 0
        const val TYPE_RAMBO: Int = 1
        const val TYPE_SOLDIER: Int = 2
        const val TYPE_SWAT: Int = 3
        const val TYPE_VADER: Int = 4

        @JvmField
        val DURATION_DEAD: Float = Assets.heroForceDie!!.animationDuration + .2f
        const val DURATION_HURT: Float = .5f
        val DURATION_IS_FIRING: Float = Assets.heroForceShoot!!.animationDuration + .1f

        var JUMP_SPEED: Float = 5f
        var WALK_SPEED: Float = 1.5f
    }
}
