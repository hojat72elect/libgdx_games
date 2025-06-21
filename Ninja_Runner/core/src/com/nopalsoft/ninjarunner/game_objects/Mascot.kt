package com.nopalsoft.ninjarunner.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

/**
 * The creature that follows our player throughout the game.
 */
class Mascot(x: Float, y: Float, @JvmField var mascotType: MascotType) {
    var state: Int

    enum class MascotType {
        PINK_BIRD, BOMB
    }

    @JvmField
    var drawWidth = 0f

    @JvmField
    var drawHeight = 0f

    @JvmField
    var dashDrawWidth = 0f

    @JvmField
    var dashDrawHeight = 0f

    @JvmField
    val position = Vector2(x, y)
    val targetPosition = Vector2(x, y)
    var velocity: Vector2

    @JvmField
    var stateTime: Float

    init {
        velocity = Vector2()
        state = STATE_NORMAL
        stateTime = 0f

        when (mascotType) {
            MascotType.PINK_BIRD -> {
                drawWidth = .73f
                drawHeight = .66f
                dashDrawWidth = 2.36f
                dashDrawHeight = 1.25f
            }

            MascotType.BOMB -> {
                run {
                    dashDrawWidth = .52f
                    drawWidth = dashDrawWidth
                }
                run {
                    dashDrawHeight = .64f
                    drawHeight = dashDrawHeight
                }
            }
        }
    }

    fun update(body: Body, delta: Float, targetX: Float, targetY: Float) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y

        targetPosition.set(targetX, targetY)

        velocity = body.getLinearVelocity()
        velocity.set(targetPosition).sub(position).scl(SPEED)
        body.linearVelocity = velocity
        stateTime += delta
    }

    fun updateStateTime(delta: Float) {
        stateTime += delta
    }

    companion object {
        const val STATE_NORMAL = 0
        const val SPEED = 5f

        const val RADIUS = .25f
    }
}
