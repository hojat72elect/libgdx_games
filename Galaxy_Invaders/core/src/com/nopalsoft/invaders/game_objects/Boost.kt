package com.nopalsoft.invaders.game_objects

class Boost(@JvmField val type: Int, x: Float, y: Float) : DynamicGameObject(x, y, RADIUS) {
    var stateTime: Int

    init {
        velocity.add(0f, SPEED)
        stateTime = 0
    }

    fun update(deltaTime: Float) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)
        boundsCircle!!.x = position.x
        boundsCircle!!.y = position.y
        stateTime = (stateTime + deltaTime).toInt()
    }

    companion object {
        const val DRAW_SIZE: Float = 5f
        const val RADIUS: Float = 1f
        const val SPEED: Float = -10f

        const val EXTRA_LIFE_BOOST: Int = 0
        const val EXTRA_MISSILE_BOOST: Int = 1
        const val EXTRA_SHIELD_BOOST: Int = 2
        const val SHIELD: Int = 3
    }
}
