package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Laser(x: Float, y: Float, width: Float, height: Float, tiempoApagado: Float, tiempoApagadoInicio: Float, direccion: String) {
    @JvmField
    val width: Float

    @JvmField
    val height: Float
    var timeOFF: Float
    var timeON: Float

    @JvmField
    var position: Vector2

    @JvmField
    var stateTime: Float

    @JvmField
    var state: Int

    @JvmField
    var direccion: Int = 0

    @JvmField
    var isTouchingShip: Boolean = false
    var TIME_OFF: Float
    var TIME_ON: Float = 3f

    init {
        var width = width
        var height = height
        position = Vector2(x, y)
        timeOFF = tiempoApagadoInicio
        TIME_OFF = tiempoApagado
        timeON = 0f
        stateTime = timeON
        state = STATE_NORMAL

        if (height > width) width = DRAW_ANCHO
        else height = DRAW_ANCHO

        this.width = width
        this.height = height

        if (direccion == "horizontal") this.direccion = DIRECCION_HORIZONTAL
        else if (direccion == "vertical") this.direccion = DIRECCION_VERTICAL
    }

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        if (state == STATE_NORMAL) {
            timeOFF += delta
            if (timeOFF >= TIME_OFF) {
                state = STATE_FIRE
                timeOFF -= TIME_OFF
                stateTime = 0f
            }
        }

        if (state == STATE_FIRE) {
            timeON += delta
            if (timeON >= TIME_ON) {
                state = STATE_NORMAL
                timeON -= TIME_ON
                stateTime = 0f
            }
        }

        stateTime += delta
    }

    companion object {
        @JvmField
        var DIRECCION_HORIZONTAL: Int = 0
        var DIRECCION_VERTICAL: Int = 1

        var DRAW_ANCHO: Float = .35f
        var STATE_NORMAL: Int = 0

        @JvmField
        var STATE_FIRE: Int = 1
    }
}
