package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Gas(x: Float, y: Float) {

    var position = Vector2(x, y)
    var stateTime = 0f
    var state = STATE_NORMAL

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y
        stateTime += delta
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_TOMADA = 1
    }
}
