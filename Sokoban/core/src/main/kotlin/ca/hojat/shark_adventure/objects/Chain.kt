package ca.hojat.shark_adventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import ca.hojat.shark_adventure.screens.Screens

class Chain : Poolable {
    @JvmField
    var state = 0

    @JvmField
    val position = Vector2()

    @JvmField
    var angleDegree = 0f

    fun init(x: Float, y: Float) {
        position.set(x, y)
        state = STATE_NORMAL
    }

    fun update(body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y
        angleDegree = MathUtils.radDeg * body.angle

        if (position.x < -3 || position.y > Screens.WORLD_HEIGHT + 3) {
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
        const val STATE_REMOVE = 2
        const val DRAW_WIDTH = .16f
        const val DRAW_HEIGHT = .24f
        const val WIDTH = .13f
        const val HEIGHT = .21f
        const val SPEED_X = -1f
    }
}
