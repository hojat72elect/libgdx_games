package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable

class PlatformPiece : Poolable {
    @JvmField
    var state = 0

    @JvmField
    var color = 0

    @JvmField
    var type = 0

    @JvmField
    val position = Vector2()

    var stateTime = 0f

    @JvmField
    var angleDegree = 0f

    fun initialize(x: Float, y: Float, type: Int, color: Int) {
        position[x] = y
        this.type = type
        this.color = color
        angleDegree = 0f
        stateTime = 0f
        state = STATE_NORMAL
    }

    fun update(delta: Float, body: Body) {
        position.x = body.position.x
        position.y = body.position.y
        angleDegree = MathUtils.radiansToDegrees * body.angle

        if (angleDegree > 90) {
            body.setTransform(position, MathUtils.degreesToRadians * 90)
            angleDegree = 90f
        }

        stateTime += delta
    }

    override fun reset() {}

    fun setDestroy() {
        state = STATE_DESTROY
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_DESTROY = 1
        const val DRAW_WIDTH_NORMAL = Platform.DRAW_WIDTH_NORMAL / 2f
        const val DRAW_HEIGHT_NORMAL = Platform.DRAW_HEIGHT_NORMAL
        const val WIDTH_NORMAL = Platform.WIDTH_NORMAL / 2f
        const val HEIGHT_NORMAL = Platform.HEIGHT_NORMAL

        const val TYPE_LEFT = 0
        const val TYPE_RIGHT = 1
    }
}
