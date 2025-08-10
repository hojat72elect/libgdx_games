package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Plataforma(x: Float, y: Float, width: Float, height: Float) {

    @JvmField
    var isFinal = false
    var position = Vector2(x, y)
    var size = Vector2(width, height)
    var stateTime = 0f
    var state = STATE_NORMAL

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y
        stateTime += delta
    }

    companion object {
        const val STATE_NORMAL = 0
    }
}
