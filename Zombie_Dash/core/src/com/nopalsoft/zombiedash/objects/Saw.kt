package com.nopalsoft.zombiedash.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiedash.game.WorldGame

class Saw(x: Float, y: Float) {
    val DURATION_STATE_DIALOG: Float = 2f
    val velocityX: Float = -2f

    @JvmField
    var state: Int

    @JvmField
    var position: Vector2 = Vector2(x, y)

    @JvmField
    var angleDeg: Float = 0f

    var stateTime: Float = 0f

    init {
        state = STATE_DIALOG
    }

    fun update(delta: Float, body: Body, oHero: Hero) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        if (state == STATE_DIALOG) {
            if (stateTime >= DURATION_STATE_DIALOG) {
                state = STATE_NORMAL
                stateTime = 0f
            }
            val y = oHero.position.y
            body.setTransform(position.x, y, 0f)
        } else {
            body.setLinearVelocity(WorldGame.velocidadX + velocityX, 0f)
        }

        angleDeg = Math.toDegrees(body.angle.toDouble()).toFloat()
        stateTime += delta
    }

    companion object {
        const val STATE_DIALOG: Int = 0
        const val STATE_NORMAL: Int = 1
        const val SIZE: Float = .5f

        @JvmField
        var STATE_DESTROY: Int = 2
    }
}
