package com.nopalsoft.zombiewars.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

open class Personajes(var body: Body) {
    val DURATION_HURT: Float = .3f

    @JvmField
    var state: Int

    @JvmField
    var tipo: Int = 0
    var DURATION_ATTACK: Float = 0f

    @JvmField
    var DURATION_DEAD: Float = 0f
    var DAMAGE: Int = 0

    @JvmField
    var DISTANCE_ATTACK: Float = 0f

    @JvmField
    var position: Vector2 = Vector2(body.getPosition().x, body.getPosition().y)

    @JvmField
    var stateTime: Float
    var vidas: Int = 0
    var attack: Boolean = false // solo puede herir una vez cada vez que atacca

    /**
     * Los buenos caminan a la derecha
     */
    @JvmField
    var isFacingLeft: Boolean = false
    protected var TIME_TO_ATTACK_AGAIN: Float = 0f
    var VELOCIDAD_WALK: Float = 0f

    init {

        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float) {
        body.isAwake = true
        position.x = body.getPosition().x
        position.y = body.getPosition().y
        val velocity = body.getLinearVelocity()

        when {
            state == STATE_DEAD -> {
                stateTime += delta
                body.setLinearVelocity(0f, velocity.y)
                return
            }

            state == STATE_HURT -> {
                stateTime += delta
                if (stateTime >= DURATION_HURT) {
                    state = STATE_NORMAL
                    stateTime = 0f
                }
                body.setLinearVelocity(0f, velocity.y)
                return
            }

            state == STATE_ATTACK -> {
                stateTime += delta
                if (stateTime >= DURATION_ATTACK) {
                    if ((stateTime - DURATION_ATTACK) >= TIME_TO_ATTACK_AGAIN) {
                        state = STATE_NORMAL
                        stateTime = 0f
                    }
                }
                body.setLinearVelocity(0f, velocity.y)
                return
            }

            isFacingLeft -> velocity.x = -VELOCIDAD_WALK
            else -> velocity.x = VELOCIDAD_WALK
        }
        body.linearVelocity = velocity
        stateTime += delta
    }

    fun getHurt(damage: Int) {
        if (state != STATE_DEAD) {
            vidas -= damage
            if (vidas <= 0) {
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
     * Regresa si si ataco
     */
    fun attack(): Boolean {
        if (state == STATE_NORMAL) {
            state = STATE_ATTACK
            attack = true
            stateTime = 0f
            return true
        }
        return false
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_HURT: Int = 1
        const val STATE_ATTACK: Int = 2
        const val STATE_DEAD: Int = 3
        const val TIPO_RANGO: Int = 0
        const val TIPO_NO_RANGO: Int = 1
    }
}
