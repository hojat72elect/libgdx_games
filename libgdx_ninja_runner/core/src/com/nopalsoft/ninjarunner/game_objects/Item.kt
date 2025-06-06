package com.nopalsoft.ninjarunner.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.ninjarunner.Assets

open class Item(width: Float, height: Float) : Poolable {
    @JvmField
    var state: Int = 0

    @JvmField
    val WIDTH: Float

    @JvmField
    val HEIGHT: Float

    var velocity: Vector2

    @JvmField
    val position: Vector2 = Vector2()

    @JvmField
    var stateTime: Float = 0f

    init {
        velocity = Vector2()
        this.WIDTH = width
        this.HEIGHT = height
    }

    fun init(x: Float, y: Float) {
        position.set(x, y)
        velocity.set(0f, 0f)
        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float, body: Body, oPlayer: Player) {
        if (state == STATE_NORMAL) {
            position.x = body.getPosition().x
            position.y = body.getPosition().y

            // First I check if they are attached to the character
            if (oPlayer.isMagnetEnabled && position.dst(oPlayer.position) <= 5f) {
                moveCoinsToTarget(body, oPlayer.position)
            } else body.setLinearVelocity(0f, 0f)
        }

        stateTime += delta
    }

    private fun moveCoinsToTarget(body: Body, targetPosition: Vector2?) {
        velocity = body.getLinearVelocity()
        velocity.set(targetPosition).sub(position).scl(Player.DASH_SPEED + 3)
        body.linearVelocity = velocity
    }

    fun setPicked() {
        if (state == STATE_NORMAL) {
            state = STATE_DESTROY
            stateTime = 0f
        }
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_DESTROY: Int = 1

        @JvmField
        val DURATION_PICK = Assets.pickUpAnimation!!.animationDuration + .1f
    }
}
