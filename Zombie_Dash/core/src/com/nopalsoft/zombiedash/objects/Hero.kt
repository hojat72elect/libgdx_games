package com.nopalsoft.zombiedash.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.Settings

class Hero(x: Float, y: Float, tipo: Int) {
    @JvmField
    val tipo: Int
    val VELOCIDAD_SECOND_JUMP: Float
    val MAX_VIDAS: Int = Settings.LEVEL_LIFE + 3

    @JvmField
    val MAX_SHIELD: Int = Settings.LEVEL_SHIELD + 1
    val initialPosition: Vector2 = Vector2(x, y)

    @JvmField
    var state: Int

    @JvmField
    var position: Vector2 = Vector2(x, y)

    @JvmField
    var stateTime: Float

    @JvmField
    var isJumping: Boolean = false // To know if i can draw the jumping animation
    var numPisosEnContacto: Int = 0 // Pisos que esta tocando actualmente si es ==0 no puede saltar

    @JvmField
    var didGetHurtAtLeastOnce: Boolean

    /**
     * Verdadero si toca las escaleras
     */
    @JvmField
    var vidas: Int

    @JvmField
    var shield: Int
    private var canJump: Boolean
    private var canDoubleJump: Boolean

    init {
        state = STATE_NORMAL
        stateTime = 0f
        this.tipo = tipo
        canJump = true
        canDoubleJump = true
        didGetHurtAtLeastOnce = false

        shield = MAX_SHIELD
        vidas = MAX_VIDAS

        VELOCIDAD_SECOND_JUMP = when (Settings.LEVEL_SECOND_JUMP) {
            0 -> 3.5f
            1 -> 4f
            2, 3 -> 4.35f
            4, 5 -> 4.7f
            6 -> 5f
            else -> 5f
        }
    }

    fun update(delta: Float, body: Body, didJump: Boolean, isJumpPressed: Boolean) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        when (state) {
            STATE_REVIVE -> {
                state = STATE_NORMAL
                canJump = true
                isJumping = false
                canDoubleJump = true
                stateTime = 0f
                vidas = MAX_VIDAS
                initialPosition.y = 3f
                position.x = initialPosition.x
                position.y = initialPosition.y
                body.setTransform(initialPosition, 0f)
                body.setLinearVelocity(0f, 0f)
            }

            STATE_HURT -> {
                stateTime += delta
                if (stateTime >= DURATION_HURT) {
                    state = STATE_NORMAL
                    stateTime = 0f
                }
                return
            }

            STATE_DEAD -> {
                stateTime += delta
                return
            }
        }

        val velocity = body.getLinearVelocity()

        if (didJump && (canJump || canDoubleJump)) {
            velocity.y = VELOCIDAD_JUMP

            if (!canJump) {
                canDoubleJump = false
                velocity.y = VELOCIDAD_SECOND_JUMP
            }

            canJump = false
            isJumping = true
            stateTime = 0f
            body.gravityScale = .9f
            Assets.playSound(Assets.jump!!, 1f)
        }
        if (!isJumpPressed) body.gravityScale = 1f

        stateTime += delta

        body.linearVelocity = velocity
    }

    val hurt: Unit
        get() {
            if (state != STATE_NORMAL) return

            if (shield > 0) {
                shield--
                state = STATE_HURT
                stateTime = 0f
                return
            }

            vidas--
            state = if (vidas > 0) {
                STATE_HURT
            } else {
                STATE_DEAD
            }
            stateTime = 0f
            didGetHurtAtLeastOnce = true
        }

    fun getShield() {
        shield += 2
        if (shield > MAX_SHIELD) shield = MAX_SHIELD
    }

    val hearth: Unit
        get() {
            vidas += 1
            if (vidas > MAX_VIDAS) vidas = MAX_VIDAS
        }

    fun die() {
        if (state != STATE_DEAD) {
            vidas = 0
            shield = 0
            state = STATE_DEAD
            stateTime = 0f
        }
    }

    fun touchFloor() {
        numPisosEnContacto++

        canJump = true
        isJumping = false
        canDoubleJump = true
        if (state == STATE_NORMAL) stateTime = 0f
    }

    fun endTouchFloor() {
        numPisosEnContacto--
        if (numPisosEnContacto == 0) {
            canJump = false

            // Si dejo de tocar el piso porque salto todavia puede saltar otra vez
            if (!isJumping) canDoubleJump = false
        }
    }

    fun revive() {
        state = STATE_REVIVE
        stateTime = 0f
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_HURT: Int = 1
        const val STATE_DEAD: Int = 2
        const val STATE_REVIVE: Int = 3
        const val TIPO_FORCE: Int = 0
        const val TIPO_RAMBO: Int = 1
        const val TIPO_SOLDIER: Int = 2
        const val TIPO_SWAT: Int = 3
        const val TIPO_VADER: Int = 4

        @JvmField
        val DURATION_DEAD: Float = Assets.heroForceDie!!.animationDuration + .2f
        const val DURATION_HURT: Float = .5f
        var VELOCIDAD_JUMP: Float = 5f
    }
}
