package com.nopalsoft.zombiekiller.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiekiller.Assets

class Zombie(x: Float, y: Float, type: Int) {
    @JvmField
    val type: Int

    @JvmField
    val MAX_LIFE: Int
    val TIME_TO_HURT_PLAYER: Float = 1f

    @JvmField
    var state: Int

    var WALK_SPEED: Float = 0f

    @JvmField
    var FORCE_IMPACT: Float = 0f

    @JvmField
    var position: Vector2

    @JvmField
    var stateTime: Float

    @JvmField
    var isFacingLeft: Boolean = false

    @JvmField
    var isWalking: Boolean = false

    @JvmField
    var canUpdate: Boolean
    var isFollowing: Boolean

    @JvmField
    var lives: Int = 0

    @JvmField
    var isTouchingPlayer: Boolean = false
    var timeToHurtPlayer: Float = 0f

    init {
        position = Vector2(x, y)
        state = STATE_RISE
        stateTime = 0f
        this.type = type
        canUpdate = false

        isFollowing = true

        when (type) {
            TYPE_KID -> {
                lives = 5
                FORCE_IMPACT = 2.5f
                WALK_SPEED = 1.1f
            }

            TYPE_CUASY -> {
                lives = 15
                FORCE_IMPACT = 3f
                WALK_SPEED = .5f
            }

            TYPE_MUMMY -> {
                lives = 100
                FORCE_IMPACT = 8f
                WALK_SPEED = .5f
            }

            TYPE_PAN -> {
                lives = 50
                FORCE_IMPACT = 4f
                WALK_SPEED = .7f
            }

            TYPE_FRANK -> {
                lives = 120
                FORCE_IMPACT = 5f
                WALK_SPEED = 1.3f
            }
        }
        MAX_LIFE = lives
    }

    fun update(delta: Float, body: Body, accelX: Float, oHero: Hero) {
        var accelX = accelX
        body.isAwake = true
        position.x = body.getPosition().x
        position.y = body.getPosition().y
        val velocity = body.getLinearVelocity()

        // So that if they cannot be updated, at least the objects fall and remain at ground level. Then the earth looks stuck to the ground when state==Rise
        if (!canUpdate) {
            body.setLinearVelocity(0f, velocity.y)
            return
        }

        isFacingLeft = !(oHero.position.x > position.x)

        if (state == STATE_RISE) {
            stateTime += delta
            if (stateTime >= RISE_DURATION) {
                state = STATE_NORMAL
                stateTime = 0f
            }
            return
        } else if (state == STATE_DEAD) {
            stateTime += delta
            return
        } else if (state == STATE_HURT) {
            stateTime += delta
            if (stateTime >= HURT_DURATION) {
                state = STATE_NORMAL
                stateTime = 0f
            }
            return
        }

        if (isTouchingPlayer) {
            timeToHurtPlayer += delta
            if (timeToHurtPlayer >= TIME_TO_HURT_PLAYER) {
                timeToHurtPlayer -= TIME_TO_HURT_PLAYER
                oHero.hurt()
            }
        } else {
            timeToHurtPlayer = 0f
        }

        if (isFollowing) {
            if (oHero.position.x + .1f < position.x) accelX = -1f
            else if (oHero.position.x - .1f > position.x) accelX = 1f
            else accelX = 0f
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
            velocity.x = 0f
            isWalking = false
        }

        body.linearVelocity = velocity

        stateTime += delta
    }

    fun getHurt(damage: Int) {
        if (state == STATE_NORMAL || state == STATE_HURT) {
            lives -= damage
            if (lives <= 0) {
                state = STATE_DEAD
                stateTime = 0f
            } else {
                if (state == STATE_NORMAL) {
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

    companion object {
        const val STATE_RISE: Int = 0
        const val STATE_NORMAL: Int = 1
        const val STATE_HURT: Int = 2
        const val STATE_DEAD: Int = 3

        const val TYPE_KID: Int = 0
        const val TYPE_FRANK: Int = 1
        const val TYPE_CUASY: Int = 2
        const val TYPE_PAN: Int = 3
        const val TYPE_MUMMY: Int = 4

        val RISE_DURATION: Float = Assets.zombieKidRise.animationDuration + .2f

        @JvmField
        val DEAD_DURATION: Float = Assets.zombieKidDie.animationDuration + .2f
        const val HURT_DURATION: Float = .3f
    }
}
