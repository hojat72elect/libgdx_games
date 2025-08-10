package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Estrella(x: Float, y: Float) {

    var position = Vector2(x, y)
    var stateTime = 0F
    var state = STATE_NORMAL

    fun update(delta: Float, body: Body) {
        position.x = body.position.x
        position.y = body.position.y
        stateTime += delta
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_TOMADA = 1
    }
}
