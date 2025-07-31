package com.nopalsoft.zombiekiller.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType

open class Items(x: Float, y: Float, val drawWidth: Float, val drawHeight: Float) {


    var state: Int = STATE_NORMAL
    var position: Vector2 = Vector2(x, y)
    var angleDeg: Float = 0f
    var stateTime: Float = 0f

    fun update(delta: Float, body: Body) {
        if (body.getType() != BodyType.StaticBody) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y
            angleDeg = Math.toDegrees(body.angle.toDouble()).toFloat()
        }
        stateTime += delta
    }

    fun taken() {
        state = STATE_TAKEN
        stateTime = 0f
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_TAKEN = 1
    }
}
