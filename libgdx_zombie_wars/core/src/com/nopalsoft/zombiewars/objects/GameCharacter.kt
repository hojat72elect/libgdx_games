package com.nopalsoft.zombiewars.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

open class GameCharacter(var body: Body) {
    private val hurtAnimationDuration = .3F

    @JvmField
    var state = 0

    @JvmField
    var type = 0

    @JvmField
    var attackAnimationDuration = 0F

    @JvmField
    var deathAnimationDuration = 0F

    @JvmField
    var damage = 0

    @JvmField
    var attackDistance = 0F

    @JvmField
    var position = Vector2(body.position.x, body.position.y)

    @JvmField
    var stateTime = 0F

    @JvmField
    var lives = 0
    private var attack = false // can only hurt once each time it attacks

    /**
     * The good walk on the right
     */
    @JvmField
    var isFacingLeft = false

    @JvmField
    protected var timeToAttackAgain = 0F

    @JvmField
    var walkSpeed = 0F

    init {
        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float) {
        body.isAwake = true
        position.x = body.position.x
        position.y = body.position.y
        val velocity = body.linearVelocity

        when {
            state == STATE_DEAD -> {
                stateTime += delta
                body.setLinearVelocity(0f, velocity.y)
                return
            }

            state == STATE_HURT -> {
                stateTime += delta
                if (stateTime >= hurtAnimationDuration) {
                    state = STATE_NORMAL
                    stateTime = 0f
                }
                body.setLinearVelocity(0f, velocity.y)
                return
            }

            state == STATE_ATTACK -> {
                stateTime += delta
                if (stateTime >= attackAnimationDuration) {
                    if ((stateTime - attackAnimationDuration) >= timeToAttackAgain) {
                        state = STATE_NORMAL
                        stateTime = 0f
                    }
                }
                body.setLinearVelocity(0f, velocity.y)
                return
            }

            isFacingLeft -> velocity.x = -walkSpeed
            else -> velocity.x = walkSpeed
        }
        body.linearVelocity = velocity
        stateTime += delta
    }

    fun getHurt(damage: Int) {
        if (state != STATE_DEAD) {
            lives -= damage
            if (lives <= 0) {
                state = STATE_DEAD
                stateTime = 0f
            } else {
                if (state != STATE_HURT) {
                    state = STATE_HURT
                    stateTime = 0f
                }
            }
        }
    }

    fun die() {
        if (state != STATE_DEAD) {
            state = STATE_DEAD
            stateTime = 0f
        }
    }

    /**
     * Come back if I attack.
     */
    fun attack(): Boolean {
        if (state == STATE_NORMAL) {
            state = STATE_ATTACK
            attack = true
            stateTime = 0F
            return true
        }
        return false
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_HURT = 1
        const val STATE_ATTACK = 2
        const val STATE_DEAD = 3
        const val TYPE_RANGE = 0
        const val TYPE_NO_RANGE = 1
    }
}
