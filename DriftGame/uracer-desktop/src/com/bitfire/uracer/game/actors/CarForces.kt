package com.bitfire.uracer.game.actors

/**
 * Represents the forces computed by the car simulator.
 */
class CarForces {
    var velocityX = 0F
    var velocityY = 0F
    var angularVelocity = 0F

    init {
        reset()
    }

    fun reset() {
        velocityX = 0F
        velocityY = 0F
        angularVelocity = 0F
    }

    fun set(other: CarForces) {
        this.velocityX = other.velocityX
        this.velocityY = other.velocityY
        this.angularVelocity = other.angularVelocity
    }
}
