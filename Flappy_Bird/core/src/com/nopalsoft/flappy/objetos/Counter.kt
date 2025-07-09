package com.nopalsoft.flappy.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Counter {
    @JvmField
    var state = STATE_NORMAL // Save the current state.

    @JvmField
    var position = Vector2()

    // Update object position to match with the Box2D body.
    fun update(body: Body) {
        position.x = body.position.x
        position.y = body.position.y
    }

    companion object {
        @JvmField
        var WIDTH = .1f

        @JvmField
        var HEIGHT = 1.85f

        @JvmField
        var STATE_NORMAL = 0

        @JvmField
        var STATE_REMOVE = 1

        @JvmField
        var SPEED_X = Pipe.SPEED_X // It moves at same speed than pipes.
    }
}
