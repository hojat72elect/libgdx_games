package com.nopalsoft.invaders.game_objects

import com.badlogic.gdx.math.Circle

class Missile(x: Float, y: Float) : DynamicGameObject(x, y, WIDTH, HEIGHT) {
    val SPEED: Float = 30f

    @JvmField
    var stateTime: Float

    @JvmField
    var state: Int

    /**
     * X and Y are the position of the tip of the ship
     *
     * @param x The same as Bob.x
     * @param y The same as Bob.y
     */
    init {
        // I also initialize the radius because the explosion is going to be cool.
        boundsCircle = Circle(position.x, position.y, RADIO_EXPLOSION)
        state = STATE_LAUNCHED
        stateTime = 0f
        velocity[0f] = SPEED
    }

    fun update(deltaTime: Float) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)
        boundsRectangle!!.x = position.x - WIDTH / 2
        boundsRectangle.y = position.y - HEIGHT / 2
        boundsCircle!!.x = position.x
        boundsCircle!!.y = position.y
        stateTime += deltaTime
    }

    fun hitTarget() {
        velocity[0f] = 0f
        stateTime = 0f
        state = STATE_EXPLODING
    }

    companion object {
        const val WIDTH: Float = 0.4f
        const val HEIGHT: Float = 1.4f

        const val RADIO_EXPLOSION: Float = 7.5f
        const val EXPLOSION_DURATION: Float = 0.05f * 19
        const val STATE_LAUNCHED: Int = 0
        const val STATE_EXPLODING: Int = 1
    }
}
