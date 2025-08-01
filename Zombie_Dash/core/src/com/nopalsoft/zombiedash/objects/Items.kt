package com.nopalsoft.zombiedash.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

open class Items(x: Float, y: Float, drawWidth: Float, drawHeight: Float) {
    @JvmField
    val DRAW_WIDTH: Float
    @JvmField
    val DRAW_HEIGHT: Float
    @JvmField
    var state: Int
    @JvmField
    var position: Vector2 = Vector2(x, y)
    var angleDeg: Float = 0f

    var stateTime: Float = 0f

    init {
        state = STATE_NORMAL
        DRAW_HEIGHT = drawHeight
        DRAW_WIDTH = drawWidth
    }

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y
        angleDeg = Math.toDegrees(body.angle.toDouble()).toFloat()
        stateTime += delta
    }

    fun taken() {
        state = STATE_TAKEN
        stateTime = 0f
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_TAKEN: Int = 1
    }
}
