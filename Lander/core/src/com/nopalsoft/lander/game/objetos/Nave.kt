package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.lander.Settings

class Nave(x: Float, y: Float) {
    var velocidadFly: Float = VELOCIDAD_FLY
    var velocidadMove: Float = VELOCIDAD_MOVE

    @JvmField
    var gas: Float = GAS_INICIAL

    @JvmField
    var vida: Float = VIDA_INICIAL

    @JvmField
    var position: Vector2 = Vector2(x, y)
    var velocity: Vector2? = null

    @JvmField
    var angleRad: Float = 0f

    @JvmField
    var state: Int = STATE_NORMAL

    @JvmField
    var stateTime: Float = 0f

    @JvmField
    var isFlying: Boolean
    var isHurtByBomb: Boolean = false

    /**
     * Cuando aterrizo en el area de ganar el juego
     */
    @JvmField
    var isLanded: Boolean = false

    init {
        isFlying = false

        // Upgrades
        velocidadFly += (.09f * Settings.nivelVelocidadY)
        velocidadMove += (.02f * Settings.nivelRotacion)
        vida += (5.3f * Settings.nivelVida)
        gas += (33.3f * Settings.nivelGas)
    }

    fun update(delta: Float, body: Body, accelX: Float, accelY: Float) {
        var accelX = accelX
        var accelY = accelY
        if (state == STATE_NORMAL) {
            if (gas < 0 || accelY == 0f) {
                accelY = 0f
                accelX = accelY
                isFlying = false
            } else isFlying = true

            // I put the speed on
            body.applyForceToCenter(velocidadMove * accelX, velocidadFly * accelY, true)

            // I put the speed in x to the opponent so that it reduces its speed
            body.applyForceToCenter(body.getLinearVelocity().x * -.015f, 0f, true)

            velocity = body.getLinearVelocity()

            if (isHurtByBomb && stateTime > TIME_HURT_BY_BOMB) isHurtByBomb = false

            if (!isHurtByBomb) {
                if (velocity!!.y > MAX_SPEED_Y) {
                    velocity!!.y = MAX_SPEED_Y
                    body.linearVelocity = velocity
                } else if (velocity!!.y < MIN_SPEED_Y) {
                    velocity!!.y = MIN_SPEED_Y
                    body.linearVelocity = velocity
                }
                if (velocity!!.x > MAX_SPEED_X) {
                    velocity!!.x = MAX_SPEED_X
                    body.linearVelocity = velocity
                } else if (velocity!!.x < -MAX_SPEED_X) {
                    velocity!!.x = -MAX_SPEED_X
                    body.linearVelocity = velocity
                }
            }

            angleRad = MathUtils.atan2(-accelX, accelY)

            position.x = body.getPosition().x
            position.y = body.getPosition().y

            val MAX_ANGLE_DEGREES = 35F
            val angleLimitRad = Math.toRadians(MAX_ANGLE_DEGREES.toDouble()).toFloat()

            if (angleRad > angleLimitRad) angleRad = angleLimitRad
            else if (angleRad < -angleLimitRad) angleRad = -angleLimitRad

            body.setTransform(position.x, position.y, angleRad)

            if (accelX != 0f || accelY != 0f) gas -= (5 * delta)
        } else {
            body.setLinearVelocity(0f, 0f)
        }

        stateTime += delta
    }

    fun colision(fuerzaImpacto: Float) {
        if (state == STATE_NORMAL) {
            vida -= fuerzaImpacto
            if (vida <= 0) {
                state = STATE_EXPLODE
                stateTime = 0f
            }
        }
    }

    fun getHurtByLaser(dano: Float) {
        isHurtByBomb = true
        stateTime = 0f
        colision(dano)
    }

    fun getHurtByBomb(dano: Float) {
        isHurtByBomb = true
        stateTime = 0f
        colision(dano)
    }

    companion object {
        const val DRAW_WIDTH: Float = .7f
        const val DRAW_HEIGHT: Float = 1.59f
        const val WIDTH: Float = .5f
        const val HEIGHT: Float = 1.0f

        const val DENSIDAD_INICIAL: Float = .7f

        const val VELOCIDAD_FLY: Float = 2f
        const val MAX_SPEED_Y: Float = 2f
        const val MIN_SPEED_Y: Float = -4f
        const val VELOCIDAD_MOVE: Float = 1.3f
        const val MAX_SPEED_X: Float = 1f
        const val GAS_INICIAL: Float = 100f
        const val VIDA_INICIAL: Float = 20f

        @JvmField
        var STATE_NORMAL: Int = 0

        @JvmField
        var STATE_EXPLODE: Int = 1

        @JvmField
        var EXPLODE_TIME: Float = .05f * 20
        var TIME_HURT_BY_BOMB: Float = .05f // Debe ser un numero pequeno
    }
}
