package com.nopalsoft.zombiekiller.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Crate(x: Float, y: Float, size: Float) {
    @JvmField
    val SIZE: Float
    var state: Int

    @JvmField
    var position: Vector2

    @JvmField
    var angleDeg: Float = 0f

    var stateTime: Float = 0f

    init {
        position = Vector2(x, y)
        state = STATE_NORMAL
        SIZE = size
    }

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        angleDeg = Math.toDegrees(body.angle.toDouble()).toFloat()
        stateTime += delta
    }

    companion object {
        const val STATE_NORMAL: Int = 0
    }
}
