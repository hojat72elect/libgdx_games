package com.nopalsoft.clumsy.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class ScoreKeeper {
    @JvmField
    var position: Vector2
    var stateTime: Float

    @JvmField
    var state: Int

    init {
        position = Vector2()
        stateTime = 0f
        state = STATE_NORMAL
    }

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y
        stateTime += delta
    }

    companion object {
        @JvmField
        var WIDTH: Float = .1f

        @JvmField
        var HEIGHT: Float = 1.65f

        @JvmField
        var STATE_NORMAL: Int = 0

        @JvmField
        var STATE_DESTROY: Int = 1

        @JvmField
        var SPEED_X: Float = Pipes.SPEED_X
    }
}
