package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.lander.Settings

class Nave(x: Float, y: Float) {
    var velocidadFly = VELOCIDAD_FLY
    var velocidadMove = VELOCIDAD_MOVE

    @JvmField
    var gas = GAS_INICIAL
    var vida = VIDA_INICIAL
    var position = Vector2(x, y)
    private lateinit var velocity: Vector2

    @JvmField
    var angleRad = 0f

    @JvmField
    var state = STATE_NORMAL

    @JvmField
    var stateTime = 0f

    @JvmField
    var isFlying = false
    var isHurtByBomb = false

    /**
     * Cuando aterrizo en el area de ganar el juego
     */
    @JvmField
    var isLanded = false

    init {
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
                if (velocity.y > MAX_SPEED_Y) {
                    velocity.y = MAX_SPEED_Y
                    body.linearVelocity = velocity
                } else if (velocity.y < MIN_SPEED_Y) {
                    velocity.y = MIN_SPEED_Y
                    body.linearVelocity = velocity
                }
                if (velocity.x > MAX_SPEED_X) {
                    velocity.x = MAX_SPEED_X
                    body.linearVelocity = velocity
                } else if (velocity.x < -MAX_SPEED_X) {
                    velocity.x = -MAX_SPEED_X
                    body.linearVelocity = velocity
                }
            }

            angleRad = MathUtils.atan2(-accelX, accelY)

            position.x = body.position.x
            position.y = body.position.y


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
        const val DRAW_WIDTH = .7f
        const val DRAW_HEIGHT = 1.59f
        const val WIDTH = .5f
        const val HEIGHT = 1.0f
        const val DENSIDAD_INICIAL = .7f
        const val VELOCIDAD_FLY = 2f
        const val MAX_SPEED_Y = 2f
        const val MIN_SPEED_Y = -4f
        const val VELOCIDAD_MOVE = 1.3f
        const val MAX_SPEED_X = 1f
        const val GAS_INICIAL = 100f
        const val VIDA_INICIAL = 20f
        const val STATE_NORMAL = 0
        const val STATE_EXPLODE = 1
        private const val MAX_ANGLE_DEGREES = 35F
        const val EXPLODE_TIME = .05f * 20
        var TIME_HURT_BY_BOMB = .05f // Debe ser un numero pequeno
    }
}
