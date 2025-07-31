package com.nopalsoft.zombiekiller.game_objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.Settings

class Bullet(x: Float, y: Float, isFacingLeft: Boolean) {

    val type: Int
    val forceImpact: Float
    val damage: Int
    var state: Int
    var position: Vector2 = Vector2(x, y)
    var stateTime: Float = 0f
    var isFacingLeft: Boolean

    init {
        state = STATE_MUZZLE
        this.isFacingLeft = isFacingLeft

        when (Settings.LEVEL_WEAPON) {
            LEVEL_0 -> {
                forceImpact = 0f
                damage = 1
                type = LEVEL_0
            }

            LEVEL_1 -> {
                forceImpact = .075f
                damage = 2
                type = LEVEL_1
            }

            LEVEL_2 -> {
                forceImpact = .1f
                damage = 3
                type = LEVEL_2
            }

            LEVEL_3 -> {
                forceImpact = .25f
                damage = 4
                type = LEVEL_3
            }

            else -> {
                when (Settings.LEVEL_WEAPON) {
                    4 -> {
                        forceImpact = .5f
                        damage = 5
                    }

                    5 -> {
                        forceImpact = .75f
                        damage = 6
                    }

                    else -> {
                        forceImpact = 1f
                        damage = 7
                    }
                }
                type = LEVEL_4_AND_UP
            }
        }

        Gdx.app.log("Weapon level", Settings.LEVEL_WEAPON.toString() + "")
        Gdx.app.log("Damage", damage.toString() + "")
        Gdx.app.log("Force impact ", forceImpact.toString() + "")
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
                state = if (state == STATE_MUZZLE) STATE_NORMAL
                else STATE_DESTROY
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


        var VELOCIDAD: Float = 5f
    }
}
