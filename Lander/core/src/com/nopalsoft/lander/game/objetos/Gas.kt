package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Gas(x: Float, y: Float, width: Float, height: Float) {

    var position: Vector2 = Vector2(x, y)
    var size: Vector2? = Vector2(width, height)
    var stateTime: Float = 0f
    var state: Int = STATE_NORMAL

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
