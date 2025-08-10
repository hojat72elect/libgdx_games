package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Laser(x: Float, y: Float, var width: Float, var height: Float, tiempoApagado: Float, tiempoApagadoInicio: Float, directionString: String) {

    var timeOFF = tiempoApagadoInicio
    var timeON = 0f

    @JvmField
    var position = Vector2(x, y)

    @JvmField
    var stateTime = timeON

    @JvmField
    var state = STATE_NORMAL

    @JvmField
    var directionInteger = 0

    @JvmField
    var isTouchingShip = false

    var timeOffStep = tiempoApagado

    init {

        if (height > width) {
            width = DRAW_ANCHO
        } else {
            height = DRAW_ANCHO
        }


        if (directionString == "horizontal") {
            directionInteger = DIRECCION_HORIZONTAL
        } else if (directionString == "vertical") {
            directionInteger = DIRECCION_VERTICAL
        }
    }

    fun update(delta: Float, body: Body) {
        position.x = body.position.x
        position.y = body.position.y

        if (state == STATE_NORMAL) {
            timeOFF += delta
            if (timeOFF >= timeOffStep) {
                state = STATE_FIRE
                timeOFF -= timeOffStep
                stateTime = 0f
            }
        }

        if (state == STATE_FIRE) {
            timeON += delta
            if (timeON >= TIME_ON_STEP) {
                state = STATE_NORMAL
                timeON -= TIME_ON_STEP
                stateTime = 0f
            }
        }

        stateTime += delta
    }

    companion object {

        const val DIRECCION_HORIZONTAL = 0
        private const val DIRECCION_VERTICAL = 1
        private const val DRAW_ANCHO = .35f
        private const val STATE_NORMAL = 0
        const val STATE_FIRE = 1
        const val TIME_ON_STEP = 3f
    }
}
