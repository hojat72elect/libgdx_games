package com.nopalsoft.zombiewars.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class Bullet(
    val body: Body, // the one who fired the bullet
    @JvmField val oPerWhoFired: Personajes
) {
    @JvmField
    val DAMAGE = oPerWhoFired.DAMAGE

    @JvmField
    var state = STATE_MUZZLE

    @JvmField
    var position: Vector2 = Vector2(body.getPosition().x, body.getPosition().y)

    @JvmField
    var stateTime = 0f

    @JvmField
    var isFacingLeft = oPerWhoFired.isFacingLeft

    @JvmField
    var isVisible: Boolean

    init {

        if (isFacingLeft) body.setLinearVelocity(-VELOCIDAD, 0f)
        else body.setLinearVelocity(VELOCIDAD, 0f)

        isVisible = oPerWhoFired.tipo == Personajes.TIPO_RANGO
    }

    fun update(delta: Float) {
        if (state == STATE_MUZZLE || state == STATE_NORMAL) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y

            // Si es invisible no pueede avanzar mucho la bala si no hay nada enfente
            if (!isVisible) {
                if (oPerWhoFired.position.dst(position) > oPerWhoFired.DISTANCE_ATTACK + .025f) hit()
            }
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
        const val STATE_MUZZLE = 0
        const val STATE_NORMAL = 1
        const val STATE_HIT = 2
        const val STATE_DESTROY = 3
        val DURATION_MUZZLE = Assets.muzzle.animationDuration
        var VELOCIDAD = 5F
    }
}
