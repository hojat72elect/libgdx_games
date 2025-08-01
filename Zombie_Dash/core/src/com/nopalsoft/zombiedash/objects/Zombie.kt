package com.nopalsoft.zombiedash.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.game.WorldGame

class Zombie(x: Float, y: Float, tipo: Int) {
    @JvmField
    val tipo: Int

    @JvmField
    val MAX_LIFE: Int

    @JvmField
    var state: Int

    @JvmField
    var position: Vector2

    @JvmField
    var stateTime: Float

    @JvmField
    var canUpdate: Boolean

    @JvmField
    var isFacingLeft: Boolean

    @JvmField
    var vidas: Int = 0
    var VELOCIDAD_WALK: Float = .5f

    init {
        position = Vector2(x, y)
        state = STATE_RISE
        stateTime = 0f
        canUpdate = false
        this.tipo = tipo

        when (tipo) {
            TIPO_KID -> vidas = 2
            TIPO_CUASY -> vidas = 3
            TIPO_MUMMY -> vidas = 4
            TIPO_PAN -> vidas = 5
            TIPO_FRANK -> vidas = 6
        }
        MAX_LIFE = vidas

        isFacingLeft = MathUtils.randomBoolean()

        if (isFacingLeft) VELOCIDAD_WALK = -VELOCIDAD_WALK
    }

    fun update(delta: Float, body: Body) {
        body.isAwake = true
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        if (!canUpdate) return
        val velocity = body.getLinearVelocity()

        velocity.x = WorldGame.velocidadX + VELOCIDAD_WALK

        body.linearVelocity = velocity

        if (state == STATE_RISE) {
            stateTime += delta
            if (stateTime >= DURATION_RISE) {
                state = STATE_NORMAL
                stateTime = 0f
            }
            return
        } else if (state == STATE_DEAD) {
            stateTime += delta
            return
        } else if (state == STATE_HURT) {
            stateTime += delta
            if (stateTime >= DURATION_HURT) {
                state = STATE_NORMAL
                stateTime = 0f
            }
            // body.setLinearVelocity(0, velocity.y);
            return
        }

        body.linearVelocity = velocity

        stateTime += delta
    }

    fun getHurt(damage: Int) {
        if (state == STATE_NORMAL || state == STATE_HURT) {
            vidas -= damage
            if (vidas <= 0) {
                state = STATE_DEAD
                stateTime = 0f
            } else {
                if (state == STATE_NORMAL) {
                    state = STATE_HURT
                    stateTime = 0f
                }
            }
        }
    }

    companion object {
        const val STATE_RISE: Int = 0
        const val STATE_NORMAL: Int = 1
        const val STATE_HURT: Int = 2
        const val STATE_DEAD: Int = 3
        const val TIPO_KID: Int = 0
        const val TIPO_FRANK: Int = 1
        const val TIPO_CUASY: Int = 2
        const val TIPO_PAN: Int = 3
        const val TIPO_MUMMY: Int = 4
        val DURATION_RISE: Float = Assets.zombieKidRise!!.animationDuration + .2f

        @JvmField
        val DURATION_DEAD: Float = Assets.zombieKidDie!!.animationDuration + .2f
        const val DURATION_HURT: Float = .3f
    }
}
