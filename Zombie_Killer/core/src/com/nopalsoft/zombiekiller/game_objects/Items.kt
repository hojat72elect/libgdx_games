package com.nopalsoft.zombiekiller.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType

open class Items(x: Float, y: Float, drawWidth: Float, drawHeight: Float) {

    val DRAW_WIDTH: Float
    val DRAW_HEIGHT: Float
    var state: Int
    var position: Vector2
    var angleDeg: Float = 0f
    var stateTime: Float = 0f

    init {
        position = Vector2(x, y)
        state = STATE_NORMAL
        DRAW_HEIGHT = drawHeight
        DRAW_WIDTH = drawWidth
    }

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
        const val STATE_NORMAL: Int = 0
        const val STATE_TAKEN: Int = 1
    }
}
