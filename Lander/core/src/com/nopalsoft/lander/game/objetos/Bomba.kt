package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Bomba(x: Float, y: Float, direccion: String) {

    @JvmField
    var position: Vector2 = Vector2(x, y)

    @JvmField
    var velocidad: Vector2? = null

    @JvmField
    var stateTime: Float

    @JvmField
    var state: Int
    var direccion: Int = 0

    init {
        stateTime = 0f
        state = STATE_NORMAL

        when (direccion) {
            "arriba" -> {
                this.direccion = DIRECCION_UP
                velocidad = Vector2(0f, VELOCIDAD)
            }

            "abajo" -> {
                this.direccion = DIRECCION_UP
                velocidad = Vector2(0f, -VELOCIDAD)
            }

            "derecha" -> {
                this.direccion = DIRECCION_RIGHT
                velocidad = Vector2(VELOCIDAD, 0f)
            }

            "izquierda" -> {
                this.direccion = DIRECCION_RIGHT
                velocidad = Vector2(-VELOCIDAD, 0f)
            }
        }
    }

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        if (state == STATE_TOMADA && stateTime > TIME_TOMADA) {
            state = STATE_EXPLOSION
            stateTime = 0f
        }

        stateTime += delta
    }

    fun cambioDireccion() {
        velocidad!!.x *= -1f
        velocidad!!.y *= -1f
    }

    companion object {
        private const val TIME_TOMADA: Float = .5f
        private const val VELOCIDAD: Float = 1f
        const val STATE_NORMAL: Int = 0
        const val STATE_TOMADA: Int = 1
        const val STATE_EXPLOSION: Int = 2
        const val TIME_EXPLOSION: Float = .05f * 20
        private const val DIRECCION_UP: Int = 0
        private const val DIRECCION_RIGHT: Int = 1
    }
}
