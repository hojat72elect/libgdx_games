package com.nopalsoft.zombiewars.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.zombiewars.Assets

class Bullet(
    val body: Body, // the one who fired the bullet
    @JvmField val oPerWhoFired: GameCharacter
) {
    @JvmField
    val DAMAGE: Int

    @JvmField
    var state = 0

    @JvmField
    var position: Vector2 = Vector2(body.position.x, body.position.y)

    @JvmField
    var stateTime: Float = 0f

    @JvmField
    var isFacingLeft: Boolean

    @JvmField
    var isVisible: Boolean

    init {
        state = STATE_MUZZLE

        this.isFacingLeft = oPerWhoFired.isFacingLeft
        this.DAMAGE = oPerWhoFired.damage

        if (isFacingLeft) body.setLinearVelocity(-VELOCIDAD, 0f)
        else body.setLinearVelocity(VELOCIDAD, 0f)

        isVisible = oPerWhoFired.type == GameCharacter.TYPE_RANGE
    }

    fun update(delta: Float) {
        if (state == STATE_MUZZLE || state == STATE_NORMAL) {
            position.x = body.position.x
            position.y = body.position.y

            // Si es invisible no pueede avanzar mucho la bala si no hay nada enfente
            if (!isVisible) {
                if (oPerWhoFired.position.dst(position) > oPerWhoFired.attackDistance + .025f) hit()
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
        val DURATION_MUZZLE: Float = Assets.muzzle!!.animationDuration
        var VELOCIDAD: Float = 5f
    }
}
