package com.nopalsoft.zombiedash.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.Settings

class Bullet(x: Float, y: Float) {
    @JvmField
    val tipo: Int
    @JvmField
    var state: Int
    @JvmField
    var position: Vector2
    @JvmField
    var stateTime: Float = 0f

    @JvmField
    var isFacingLeft: Boolean = false
    @JvmField
    var DAMAGE: Int = Settings.LEVEL_WEAPON + 1

    init {
        position = Vector2(x, y)
        state = STATE_MUZZLE

        tipo = LEVEL_0
    }

    fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        if (state == STATE_MUZZLE || state == STATE_HIT) {
            stateTime += delta
            if (stateTime >= DURATION_MUZZLE) {
                if (state == STATE_MUZZLE) state = STATE_NORMAL
                else state = STATE_DESTROY
                stateTime = 0f
            }
            return
        }

        stateTime += delta
    }

    fun hit() {
        state = STATE_HIT
        stateTime = 0f
    }

    companion object {
        const val STATE_MUZZLE: Int = 0
        const val STATE_NORMAL: Int = 1
        const val STATE_HIT: Int = 2
        const val STATE_DESTROY: Int = 3
        const val LEVEL_0: Int = 0
        const val LEVEL_1: Int = 1
        const val LEVEL_2: Int = 2
        const val LEVEL_3: Int = 3
        const val LEVEL_4_AND_UP: Int = 4
        val DURATION_MUZZLE: Float = Assets.muzzle.animationDuration
        @JvmField
        var VELOCIDAD: Float = 5f
    }
}
