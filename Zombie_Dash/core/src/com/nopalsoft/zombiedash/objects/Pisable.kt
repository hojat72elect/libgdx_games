package com.nopalsoft.zombiedash.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

open class Pisable(x: Float, y: Float, drawWidth: Float, drawHeight: Float) {
    @JvmField
    var state: Int

    @JvmField
    var position: Vector2
    var stateTime: Float

    @JvmField
    var DRAW_WIDTH: Float
    @JvmField
    var DRAW_HEIGHT: Float

    init {
        position = Vector2(x, y)
        stateTime = 0f
        state = STATE_NORMAL
        DRAW_WIDTH = drawWidth
        DRAW_HEIGHT = drawHeight
    }

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y
        stateTime += delta
    }

    companion object {
        var STATE_NORMAL: Int = 0
        @JvmField
        var STATE_DESTROY: Int = 1
    }
}
