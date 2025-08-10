package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Bomba(x: Float, y: Float, directionString: String) {

    @JvmField
    var position = Vector2(x, y)

    lateinit var velocidad: Vector2

    @JvmField
    var stateTime = 0F

    @JvmField
    var state = STATE_NORMAL
    var directionInteger = 0

    init {
        when (directionString) {
            "arriba" -> {
                this.directionInteger = DIRECCION_UP
                velocidad = Vector2(0f, VELOCIDAD)
            }

            "abajo" -> {
                this.directionInteger = DIRECCION_UP
                velocidad = Vector2(0f, -VELOCIDAD)
            }

            "derecha" -> {
                this.directionInteger = DIRECCION_RIGHT
                velocidad = Vector2(VELOCIDAD, 0f)
            }

            "izquierda" -> {
                this.directionInteger = DIRECCION_RIGHT
                velocidad = Vector2(-VELOCIDAD, 0f)
            }
        }
    }

    fun update(delta: Float, body: Body) {
        position.x = body.position.x
        position.y = body.position.y

        if (state == STATE_TOMADA && stateTime > TIME_TOMADA) {
            state = STATE_EXPLOSION
            stateTime = 0f
        }

        stateTime += delta
    }

    fun cambioDireccion() {
        velocidad.x *= -1f
        velocidad.y *= -1f
    }

    companion object {
        private const val TIME_TOMADA = .5F
        private const val VELOCIDAD = 1F
        const val STATE_NORMAL = 0
        const val STATE_TOMADA = 1
        const val STATE_EXPLOSION = 2
        const val TIME_EXPLOSION = .05F * 20
        private const val DIRECCION_UP = 0
        private const val DIRECCION_RIGHT = 1
    }
}
