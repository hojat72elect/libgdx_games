package com.nopalsoft.flappy.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Pipe(x: Float, y: Float, val type: Int) {


    var state = STATE_NORMAL
    val position = Vector2(x, y)

    fun update(body: Body) {
        position.x = body.position.x
        position.y = body.position.y
    }

    companion object {
        const val TYPE_UP = 0
        const val TYPE_DOWN = 1
        const val WIDTH = 0.85F
        const val HEIGHT = 4F
        const val STATE_NORMAL = 0
        const val STATE_REMOVE = 1

        // Speed of pipes(from right to left).
        const val SPEED_X = -2F
    }
}