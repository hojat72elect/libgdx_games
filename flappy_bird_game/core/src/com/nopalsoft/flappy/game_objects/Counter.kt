package com.nopalsoft.flappy.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Counter {

    var state = STATE_NORMAL
    var position = Vector2()

    // Update object position to match with the Box2D body.
    fun update(body: Body) {
        position.x = body.position.x
        position.y = body.position.y
    }

    companion object {
        const val WIDTH = 0.1F
        const val HEIGHT = 1.85F
        const val STATE_NORMAL = 0
        const val STATE_REMOVE = 1

        val SPEED_X = Pipe.SPEED_X // It moves at same speed than pipes.
    }
}