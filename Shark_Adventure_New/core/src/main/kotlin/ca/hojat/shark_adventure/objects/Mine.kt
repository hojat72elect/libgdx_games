package ca.hojat.shark_adventure.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import ca.hojat.shark_adventure.Assets
import ca.hojat.shark_adventure.Settings
import ca.hojat.shark_adventure.screens.Screens

class Mine : Poolable {
    @JvmField
    var state = 0

    @JvmField
    var type = 0

    @JvmField
    val position = Vector2()

    @JvmField
    var stateTime = 0f

    fun init(x: Float, y: Float) {
        position.set(x, y)
        stateTime = 0f
        state = STATE_NORMAL
        type = MathUtils.random(3)
    }

    fun update(body: Body, delta: Float) {
        if (state == STATE_NORMAL) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y

            if (position.x < -3 || position.y > Screens.WORLD_HEIGHT + 3) hit()
        } else if (state == STATE_EXPLODE && stateTime >= EXPLOSION_DURATION) {
            state = STATE_REMOVE
            stateTime = 0f
        }

        stateTime += delta
    }

    fun hit() {
        if (state == STATE_NORMAL) {
            state = STATE_EXPLODE
            stateTime = 0f
            if (Settings.isSoundOn) {
                Assets.playExplosionSound()
            }
        }
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_EXPLODE = 1
        const val STATE_REMOVE = 2

        const val SPEED_X = -1

        const val EXPLOSION_DURATION = .8f

        const val DRAW_WIDTH = .56f
        const val DRAW_HEIGHT = .64f

        const val WIDTH = .53f
        const val HEIGHT = .61f

        const val TYPE_GRAY = 2
        const val TYPE_RUSTY = 3
    }
}