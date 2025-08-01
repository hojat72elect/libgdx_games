package com.nopalsoft.zombiedash.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiedash.game.WorldGame

class Spike(x: Float, y: Float) {
    @JvmField
    val DRAW_WIDTH: Float = 1f

    @JvmField
    val DRAW_HEIGHT: Float = .2f

    @JvmField
    var state: Int

    @JvmField
    var position: Vector2 = Vector2(x, y)

    var stateTime: Float = 0f

    @JvmField
    var didTouchLeft: Boolean = false

    @JvmField
    var didTouchRight: Boolean = false
    var canDraw: Boolean = false

    init {
        state = STATE_NORMAL
    }

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        if (didTouchLeft || didTouchRight) {
            if (didTouchLeft != didTouchRight) state = STATE_DESTROY
            else {
                canDraw = true
                body.setLinearVelocity(WorldGame.velocidadX, 0f)
            }
        }

        stateTime += delta
    }

    companion object {
        const val STATE_NORMAL: Int = 0

        @JvmField
        var STATE_DESTROY: Int = 1
    }
}
