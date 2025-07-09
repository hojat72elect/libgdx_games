package com.nopalsoft.flappy.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Pipe(x: Float, y: Float, @JvmField var type: Int) {

    @JvmField
    var state = STATE_NORMAL

    @JvmField
    var position = Vector2(x, y)

    fun update(body: Body) {
        position.x = body.position.x
        position.y = body.position.y
    }

    companion object {
        @JvmField
        var TYPE_UP = 0

        @JvmField
        var TYPE_DOWN = 1

        @JvmField
        var WIDTH = .85f

        @JvmField
        var HEIGHT = 4f

        var STATE_NORMAL = 0

        @JvmField
        var STATE_REMOVE = 1

        /**
         * Speed of pipes. Pipes will move from right to left
         */
        @JvmField
        var SPEED_X = -2f
    }
}
