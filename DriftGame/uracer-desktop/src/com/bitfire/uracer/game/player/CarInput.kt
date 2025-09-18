package com.bitfire.uracer.game.player

/**
 * Represents the input forces for the simulator.
 */
class CarInput {

    @JvmField
    var throttle = 0F

    @JvmField
    var steerAngle = 0F

    @JvmField
    var brake = 0F

    @JvmField
    var updated = false

    fun reset() {
        updated = false
        throttle = 0F
        steerAngle = 0F
        brake = 0F
    }

    fun set(other: CarInput) {
        this.throttle = other.throttle
        this.steerAngle = other.steerAngle
        this.updated = other.updated
        this.brake = other.brake
    }
}
