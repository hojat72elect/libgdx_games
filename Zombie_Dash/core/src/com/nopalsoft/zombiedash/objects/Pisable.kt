package com.nopalsoft.zombiedash.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

open class Pisable(x: Float, y: Float, drawWidth: Float, drawHeight: Float) {
    @JvmField
    var state: Int = STATE_NORMAL

    @JvmField
    var position: Vector2 = Vector2(x, y)
    var stateTime: Float = 0f

    @JvmField
    var DRAW_WIDTH: Float = drawWidth

    @JvmField
    var DRAW_HEIGHT: Float = drawHeight

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
