package com.nopalsoft.clumsy.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Pipes(x: Float, y: Float, type: Int) {
    @JvmField
    var position: Vector2
    var stateTime: Float

    @JvmField
    var state: Int

    @JvmField
    var type: Int

    init {
        position = Vector2(x, y)
        stateTime = 0f
        state = STATE_NORMAL
        this.type = type
    }

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y
        stateTime += delta
    }

    companion object {
        @JvmField
        var UPPER_PIPE: Int = 0

        @JvmField
        var LOWER_PIPE: Int = 1

        @JvmField
        var WIDTH: Float = .7f

        @JvmField
        var HEIGHT: Float = 4f

        var STATE_NORMAL: Int = 0

        @JvmField
        var STATE_DESTROY: Int = 1

        @JvmField
        var SPEED_X: Float = -2f
    }
}
