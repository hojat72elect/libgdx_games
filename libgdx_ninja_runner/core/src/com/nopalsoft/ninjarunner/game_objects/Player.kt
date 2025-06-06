package com.nopalsoft.ninjarunner.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.Settings

class Player(x: Float, y: Float, type: Int) {
    @JvmField
    var state: Int

    @JvmField
    val type: Int

    val SECOND_JUMP_SPEED: Float = 4f

    val DURATION_MAGNET: Float
    var durationMagnet = 0f

    val DURATION_DASH = 5f
    var durationDash: Float = 0f

    val initialPosition: Vector2 = Vector2(x, y)

    @JvmField
    var position: Vector2 = Vector2(x, y)

    @JvmField
    var stateTime: Float

    @JvmField
    var isJumping: Boolean = false // To know if I can draw the jumping animation

    var numberOfFloorsInContact: Int = 0 // Floors you are currently touching if ==0 you cannot jump

    private var canJump: Boolean
    private var canDoubleJump: Boolean

    var didGetHurtAtLeastOnce: Boolean

    @JvmField
    var lives: Int

    val MAX_LIVES = Settings.LEVEL_LIFE + 5

    @JvmField
    var isDash: Boolean = false

    @JvmField
    var isSlide: Boolean = false

    @JvmField
    var isIdle: Boolean

    var isMagnetEnabled: Boolean = false

    init {
        state = STATE_NORMAL
        stateTime = 0f
        this.type = type
        canJump = true
        canDoubleJump = true
        didGetHurtAtLeastOnce = false
        isIdle = true

        lives = MAX_LIVES
        DURATION_MAGNET = 10f
    }

    fun update(delta: Float, body: Body, didJump: Boolean, isJumpPressed: Boolean, dash: Boolean, didSlide: Boolean) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        isIdle = false

        // It doesn't matter if he's alive/dizzy/ or whatever, time is running out.
        if (isMagnetEnabled) {
            durationMagnet += delta
            if (durationMagnet >= DURATION_MAGNET) {
                durationMagnet = 0f
                isMagnetEnabled = false
            }
        }

        if (state == STATE_REVIVE) {
            state = STATE_NORMAL
            canJump = true
            isJumping = false
            canDoubleJump = true
            stateTime = 0f
            lives = MAX_LIVES
            initialPosition.y = 3f
            position.x = initialPosition.x
            position.y = initialPosition.y
            body.setTransform(initialPosition, 0f)
            body.setLinearVelocity(0f, 0f)
        } else if (state == STATE_HURT) {
            stateTime += delta
            if (stateTime >= DURATION_HURT) {
                state = STATE_NORMAL
                stateTime = 0f
            }
        } else if (state == STATE_DIZZY) {
            stateTime += delta
            body.setLinearVelocity(0f, body.getLinearVelocity().y)
            if (stateTime >= DURATION_DIZZY) {
                state = STATE_NORMAL
                stateTime = 0f
            }
            return
        } else if (state == STATE_DEAD) {
            stateTime += delta
            body.setLinearVelocity(0f, body.getLinearVelocity().y)
            return
        }

        val velocity = body.getLinearVelocity()

        if (didJump && (canJump || canDoubleJump)) {
            velocity.y = JUMP_SPEED

            if (!canJump) {
                canDoubleJump = false
                velocity.y = SECOND_JUMP_SPEED
            }

            canJump = false
            isJumping = true
            stateTime = 0f

            isSlide = false

            body.gravityScale = .9f
            Assets.playSound(Assets.jumpSound!!, 1)
        }
        if (!isJumpPressed) body.gravityScale = 1f

        if (!isJumping) {
            isSlide = didSlide
        }

        if (dash) {
            isDash = true
            durationDash = 0f
        }

        if (isDash) {
            durationDash += delta
            velocity.x = DASH_SPEED
            if (durationDash >= DURATION_DASH) {
                isDash = false
                stateTime = 0f
                velocity.x = RUN_SPEED
            }
        } else {
            velocity.x = RUN_SPEED
        }
        stateTime += delta

        body.linearVelocity = velocity
    }

    val hurt: Unit
        get() {
            if (state != STATE_NORMAL) return

            lives--
            state = if (lives > 0) {
                STATE_HURT
            } else {
                STATE_DEAD
            }
            stateTime = 0f
            didGetHurtAtLeastOnce = true
        }

    val dizzy: Unit
        get() {
            if (state != STATE_NORMAL) return

            lives--
            state = if (lives > 0) {
                STATE_DIZZY
            } else {
                STATE_DEAD
            }
            stateTime = 0f
            didGetHurtAtLeastOnce = true
        }

    fun die() {
        if (state != STATE_DEAD) {
            lives = 0

            state = STATE_DEAD
            stateTime = 0f
        }
    }

    fun touchFloor() {
        numberOfFloorsInContact++

        canJump = true
        isJumping = false
        canDoubleJump = true
        if (state == STATE_NORMAL) stateTime = 0f
    }

    fun endTouchFloor() {
        numberOfFloorsInContact--
        if (numberOfFloorsInContact == 0) {
            canJump = false

            // If I stop touching the floor because I jump, I can still jump again.
            if (!isJumping) canDoubleJump = false
        }
    }

    fun updateStateTime(delta: Float) {
        stateTime += delta
    }

    fun setPickUpMagnet() {
        durationMagnet = 0f
        isMagnetEnabled = true
    }

    companion object {
        const val STATE_NORMAL: Int = 0 // NORMAL APPLIES TO RUN, DASH, SLIDE, JUMP
        const val STATE_HURT: Int = 1
        const val STATE_DIZZY: Int = 2
        const val STATE_DEAD: Int = 3
        const val STATE_REVIVE: Int = 4
        const val TYPE_GIRL: Int = 0
        const val TYPE_BOY: Int = 1
        const val TYPE_NINJA: Int = 2
        const val DRAW_WIDTH: Float = 1.27f
        const val DRAW_HEIGHT: Float = 1.05f

        const val WIDTH: Float = .55f
        const val HEIGHT: Float = 1f

        const val HEIGHT_SLIDE: Float = .45f

        const val RUN_SPEED: Float = 3f
        const val DASH_SPEED: Float = 7f

        var JUMP_SPEED: Float = 5f

        @JvmField
        val DURATION_DEAD: Float = Assets.girlDeathAnimation!!.animationDuration + .5f
        val DURATION_HURT: Float = Assets.girlHurtAnimation!!.animationDuration + .1f
        const val DURATION_DIZZY: Float = 1.25f
    }
}
