package com.nopalsoft.zombiekiller.game_objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.Settings

class Bullet(x: Float, y: Float, isFacingLeft: Boolean) {
    @JvmField
    val tipo: Int

    @JvmField
    val FORCE_IMPACT: Float

    @JvmField
    val DAMAGE: Int

    @JvmField
    var state: Int

    @JvmField
    var position: Vector2

    @JvmField
    var stateTime: Float = 0f

    @JvmField
    var isFacingLeft: Boolean

    init {
        position = Vector2(x, y)
        state = STATE_MUZZLE
        this.isFacingLeft = isFacingLeft

        when (Settings.LEVEL_WEAPON) {
            LEVEL_0 -> {
                FORCE_IMPACT = 0f
                DAMAGE = 1
                tipo = LEVEL_0
            }

            LEVEL_1 -> {
                FORCE_IMPACT = .075f
                DAMAGE = 2
                tipo = LEVEL_1
            }

            LEVEL_2 -> {
                FORCE_IMPACT = .1f
                DAMAGE = 3
                tipo = LEVEL_2
            }

            LEVEL_3 -> {
                FORCE_IMPACT = .25f
                DAMAGE = 4
                tipo = LEVEL_3
            }

            else -> {
                if (Settings.LEVEL_WEAPON == 4) {
                    FORCE_IMPACT = .5f
                    DAMAGE = 5
                } else if (Settings.LEVEL_WEAPON == 5) {
                    FORCE_IMPACT = .75f
                    DAMAGE = 6
                } else {
                    FORCE_IMPACT = 1f
                    DAMAGE = 7
                }
                tipo = LEVEL_4_AND_UP
            }
        }

        Gdx.app.log("Weapon level", Settings.LEVEL_WEAPON.toString() + "")
        Gdx.app.log("Damage", DAMAGE.toString() + "")
        Gdx.app.log("Force impact ", FORCE_IMPACT.toString() + "")
    }

    fun update(delta: Float, body: Body) {
        if (state == STATE_MUZZLE || state == STATE_NORMAL) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y
        }

        if (state == STATE_HIT) body.setLinearVelocity(0f, 0f)

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
        val DURATION_MUZZLE: Float = Assets.muzzle!!.animationDuration

        @JvmField
        var VELOCIDAD: Float = 5f
    }
}
