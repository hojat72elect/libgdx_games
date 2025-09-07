package com.bitfire.uracer.game.actors

import com.badlogic.gdx.math.Vector2

/**
 * Describes the global state of the car entity providing access to both the base physical model information and the processed
 * per-timestep data resulting after each integration.
 */
class CarDescriptor(@JvmField val carModel: CarModel) {

    @JvmField
    val velocityWorldCoordinates = Vector2(0F, 0F) // velocity vector of car in world coordinates

    @JvmField
    var angularVelocity = 0F

    @JvmField
    var steeringAngle = 0F

    @JvmField
    var throttle = 0F // amount of throttle

    @JvmField
    var brake = 0F // amount of braking
}
