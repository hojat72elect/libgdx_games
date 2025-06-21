package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.superjumper.screens.Screens

class Player(x: Float, y: Float) {
    @JvmField
    var state: Int


    private var durationBubble: Float = 0f


    @JvmField
    var durationJetPack: Float = 0f

    @JvmField
    val position: Vector2 = Vector2(x, y)

    @JvmField
    var speed: Vector2

    @JvmField
    var angleDegree: Float = 0f

    var stateTime: Float

    private var didJump: Boolean = false

    @JvmField
    var isBubble: Boolean = false

    @JvmField
    var isJetPack: Boolean = false

    init {
        speed = Vector2()

        stateTime = 0f
        state = STATE_NORMAL
    }

    fun update(body: Body, delta: Float, accelerationX: Float) {
        position.x = body.position.x
        position.y = body.position.y

        speed = body.linearVelocity

        if (state == STATE_NORMAL) {
            if (didJump) {
                didJump = false
                stateTime = 0f
                speed.y = SPEED_JUMP
            }

            speed.x = accelerationX * SPEED_X

            if (isBubble) {
                durationBubble += delta
                if (durationBubble >= DURATION_BUBBLE) {
                    durationBubble = 0f
                    isBubble = false
                }
            }

            if (isJetPack) {
                durationJetPack += delta
                if (durationJetPack >= DURATION_JETPACK) {
                    durationJetPack = 0f
                    isJetPack = false
                }
                speed.y = SPEED_JUMP
            }
        } else {
            body.angularVelocity = MathUtils.degRad * 360
            speed.x = 0f
        }

        body.linearVelocity = speed

        if (position.x >= Screens.WORLD_WIDTH) {
            position.x = 0f
            body.setTransform(position, 0f)
        } else if (position.x <= 0) {
            position.x = Screens.WORLD_WIDTH
            body.setTransform(position, 0f)
        }

        angleDegree = body.angle * MathUtils.radDeg

        speed = body.linearVelocity
        stateTime += delta
    }

    fun jump() {
        didJump = true
    }

    fun hit() {
        if (state == STATE_NORMAL && !isBubble && !isJetPack) {
            state = STATE_DEAD
            stateTime = 0f
        }
    }

    fun die() {
        if (state == STATE_NORMAL) {
            state = STATE_DEAD
            stateTime = 0f
        }
    }

    fun setBubble() {
        if (state == STATE_NORMAL) {
            isBubble = true
            durationBubble = 0f
        }
    }

    fun setJetPack() {
        if (state == STATE_NORMAL) {
            isJetPack = true
            durationJetPack = 0f
        }
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_DEAD = 1
        const val DRAW_WIDTH = .75f
        const val DRAW_HEIGHT = .8f

        const val WIDTH = .4f
        const val HEIGHT = .6f

        private const val SPEED_JUMP = 7.5f
        private const val SPEED_X = 5f
        private const val DURATION_BUBBLE = 3f
        private const val DURATION_JETPACK = 3f
    }
}
