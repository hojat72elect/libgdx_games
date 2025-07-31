package com.nopalsoft.zombiekiller.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Saw(x: Float, y: Float, val size: Float) {

    var state: Int = STATE_NORMAL
    var position: Vector2 = Vector2(x, y)
    var angleDeg: Float = 0f
    var stateTime: Float = 0f

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
