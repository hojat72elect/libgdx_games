package ca.hojat.shark_adventure.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import ca.hojat.shark_adventure.Assets
import ca.hojat.shark_adventure.Settings
import ca.hojat.shark_adventure.screens.Screens

class Torpedo : Poolable {
    @JvmField
    var state = 0

    @JvmField
    val position = Vector2()

    @JvmField
    var stateTime = 0f

    @JvmField
    var isGoingLeft = false

    fun init(x: Float, y: Float, isGoingLeft: Boolean) {
        position.set(x, y)
        stateTime = 0f
        state = STATE_NORMAL
        this.isGoingLeft = isGoingLeft
    }

    fun update(body: Body, delta: Float) {
        if (state == STATE_NORMAL) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y

            if (position.y < -3 || position.x > Screens.WORLD_WIDTH + 3) hit()
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
        const val EXPLOSION_DURATION = .1f * 8f

        const val SPEED_X = 1.7f
        const val DRAW_WIDTH = .8f
        const val DRAW_HEIGHT = .3f
    }
}
