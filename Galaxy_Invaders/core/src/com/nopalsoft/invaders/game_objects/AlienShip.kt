package com.nopalsoft.invaders.game_objects

import kotlin.math.abs

class AlienShip(vida: Int, speedIncrease: Float, x: Float, y: Float) : DynamicGameObject(x, y, RADIUS) {
    val POINTS_PER_HIT: Int = 10

    @JvmField
    var remainingLives: Int

    @JvmField
    var score: Int

    @JvmField
    var stateTime: Float = 0f

    @JvmField
    var state: Int
    var movedDistance: Float
    var speedIncrease: Float

    init {
        state = MOVE_SIDES
        velocity[SPEED] = SPEED_DOWN
        movedDistance = 0f
        score = POINTS_PER_HIT
        remainingLives = vida
        this.speedIncrease = 1 + speedIncrease
    }

    fun update(deltaTime: Float) {
        if (state != EXPLODING) {
            when (state) {
                MOVE_SIDES -> {
                    position.x += velocity.x * deltaTime * speedIncrease
                    movedDistance += abs((velocity.x * deltaTime)) * speedIncrease
                    if (movedDistance > HORIZONTAL_MOVEMENT_RANGE) {
                        state = MOVE_DOWN
                        velocity.x *= -1f
                        movedDistance = 0f
                    }
                }

                MOVE_DOWN -> {
                    position.y += velocity.y * deltaTime * speedIncrease
                    movedDistance += abs((velocity.x * deltaTime)) * speedIncrease
                    if (movedDistance > VERTICAL_MOVEMENT_RANGE) {
                        state = MOVE_SIDES
                        movedDistance = 0f
                    }
                }
            }
        }

        boundsCircle!!.x = position.x
        boundsCircle!!.y = position.y
        stateTime += deltaTime
    }

    /**
     * Calling this method is bullet power 1
     */
    fun beingHit() {
        remainingLives--
        if (remainingLives <= 0) {
            state = EXPLODING
            velocity.add(0f, 0f)
            stateTime = 0f
        }
    }

    companion object {
        const val RADIUS: Float = 1.5f

        const val DRAW_WIDTH: Float = 3.5f
        const val DRAW_HEIGHT: Float = 3.5f

        const val MOVE_SIDES: Int = 0
        const val MOVE_DOWN: Int = 2
        const val EXPLODING: Int = 3
        const val SPEED: Float = 4f
        const val SPEED_DOWN: Float = -3.5f

        const val HORIZONTAL_MOVEMENT_RANGE: Float = 6.7f
        const val VERTICAL_MOVEMENT_RANGE: Float = 1.2f
        const val EXPLOSION_DURATION: Float = 0.05f * 19
    }
}
