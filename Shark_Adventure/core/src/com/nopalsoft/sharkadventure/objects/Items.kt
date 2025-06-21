package com.nopalsoft.sharkadventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable

class Items : Poolable {
    @JvmField
    var state = 0

    @JvmField
    var type = 0

    @JvmField
    val position = Vector2()

    fun init(x: Float, y: Float) {
        position.set(x, y)
        state = STATE_NORMAL
        type = MathUtils.random(1)
    }

    fun update(body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        if (position.x < -3) {
            state = STATE_REMOVE
        }
    }

    fun hit() {
        if (state == STATE_NORMAL) {
            state = STATE_REMOVE
        }
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_REMOVE = 1
        const val TYPE_MEAT = 1
        const val DRAW_WIDTH = .45f
        const val DRAW_HEIGHT = .45f

        const val WIDTH = .35f
        const val HEIGHT = .35f

        const val SPEED_X = -1f
    }
}
