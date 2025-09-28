package com.bitfire.uracer.game.player

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.bitfire.uracer.game.actors.CarDescriptor
import com.bitfire.uracer.utils.AlgebraMath
import com.bitfire.uracer.utils.AlgebraMath.clamp
import com.bitfire.uracer.utils.AlgebraMath.damping
import com.bitfire.uracer.utils.AlgebraMath.fixup
import com.bitfire.uracer.utils.AlgebraMath.isZero
import com.bitfire.uracer.utils.AlgebraMath.normalRelativeAngle
import com.bitfire.uracer.utils.AlgebraMath.sign
import com.bitfire.uracer.utils.VectorMathUtils.fixup
import com.bitfire.uracer.utils.VectorMathUtils.truncate
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CarSimulator(var carDesc: CarDescriptor) {

    @JvmField
    var lateralForceFront = Vector2()

    @JvmField
    var lateralForceRear = Vector2()
    private val velocity = Vector2()
    private val accelerationInWorldCoordinates = Vector2()
    private val flatf = Vector2()
    private val flatr = Vector2()
    private val ftraction = Vector2()
    private val resistance = Vector2()
    private val force = Vector2()
    private val acceleration = Vector2()
    private var thisSign = 1F

    fun applyInput(input: CarInput) {
        val maxForce = carDesc.carModel.max_force
        var hasDir = false
        var hasSteer = false
        carDesc.brake = input.brake

        if (input.updated) {

            if (fixup(input.throttle) > 0) {
                carDesc.throttle = min(input.throttle, maxForce)
                hasDir = true
            } else if (fixup(input.throttle) < 0) {
                carDesc.throttle = max(input.throttle, -maxForce)
                hasDir = true
            }


            if (fixup(input.steerAngle) < 0) {

                carDesc.steeringAngle = input.steerAngle
                if (carDesc.steeringAngle < -AlgebraMath.PI_4) {
                    carDesc.steeringAngle = -AlgebraMath.PI_4
                }

                hasSteer = true
            } else if (fixup(input.steerAngle) > 0) {
                carDesc.steeringAngle = input.steerAngle
                if (carDesc.steeringAngle > AlgebraMath.PI_4) {
                    carDesc.steeringAngle = AlgebraMath.PI_4
                }

                hasSteer = true
            }
        }

        if (!hasDir) {
            if (abs(carDesc.velocityWorldCoordinates.x) > 0.5F || abs(carDesc.velocityWorldCoordinates.y) > 0.5F) {
                if (!isZero(carDesc.throttle)) carDesc.throttle *= DampingThrottle
                if (!isZero(carDesc.brake)) carDesc.brake *= DampingThrottle
            } else {
                carDesc.velocityWorldCoordinates.set(0F, 0F)
                carDesc.angularVelocity = 0F
                carDesc.brake = 0F
                carDesc.throttle = 0F
            }
        }

        if (!hasSteer) carDesc.steeringAngle = 0f
        carDesc.throttle = clamp(carDesc.throttle, -maxForce, maxForce)
        carDesc.brake = clamp(carDesc.brake, -maxForce * 2, maxForce * 2)
    }

    fun step(dt: Float, bodyAngle: Float) {
        val sn = MathUtils.sin(normalRelativeAngle(-bodyAngle))
        val cs = MathUtils.cos(normalRelativeAngle(-bodyAngle))
        velocity.x = cs * carDesc.velocityWorldCoordinates.y + sn * carDesc.velocityWorldCoordinates.x
        velocity.y = -sn * carDesc.velocityWorldCoordinates.y + cs * carDesc.velocityWorldCoordinates.x
        fixup(velocity)

        val yawspeed = carDesc.carModel.wheelbase * 0.5f * carDesc.angularVelocity
        val sideslip: Float
        val rot_angle: Float
        val slipanglefront: Float

        if (isZero(velocity.x)) {
            rot_angle = 0f
            sideslip = 0f

            slipanglefront = sideslip + rot_angle
        } else {
            // compute rotational angle
            rot_angle = MathUtils.atan2(yawspeed, velocity.x)

            // compute the side slip angle of the car (a.k.a. beta)
            sideslip = MathUtils.atan2(velocity.y, velocity.x)
            slipanglefront = sideslip + rot_angle - carDesc.steeringAngle
        }

        val slipanglerear = sideslip - rot_angle

        flatf.x = 0F
        flatf.y = carDesc.carModel.stiffness_front * slipanglefront
        flatf.y = min(carDesc.carModel.max_grip, flatf.y)
        flatf.y = max(-carDesc.carModel.max_grip, flatf.y)
        lateralForceFront.set(flatf)
        flatf.y *= carDesc.carModel.weight

        // lateral force on rear wheels
        flatr.x = 0f
        flatr.y = carDesc.carModel.stiffness_rear * slipanglerear
        flatr.y = min(carDesc.carModel.max_grip, flatr.y)
        flatr.y = max(-carDesc.carModel.max_grip, flatr.y)
        lateralForceRear.set(flatr)
        flatr.y *= carDesc.carModel.weight
        thisSign = sign(velocity.x)

        ftraction.set(100F * (carDesc.throttle - carDesc.brake * thisSign), 0F)

        // torque on body from lateral forces
        var torque = carDesc.carModel.b * flatf.y - carDesc.carModel.c * flatr.y
        torque = fixup(torque)

        // drag and rolling resistance
        resistance.x = -(carDesc.carModel.resistance * velocity.x + carDesc.carModel.drag * velocity.x * abs(velocity.x))
        resistance.y = -(carDesc.carModel.resistance * velocity.y + carDesc.carModel.drag * velocity.y * abs(velocity.y))

        // sum forces
        force.x = ftraction.x + MathUtils.sin(carDesc.steeringAngle) * flatf.x + flatr.x + resistance.x
        force.y = ftraction.y + MathUtils.cos(carDesc.steeringAngle) * flatf.y + flatr.y + resistance.y

        // Convert the force into acceleration
        // Newton F = m.a, therefore a = F/m
        acceleration.set(force.x * carDesc.carModel.invmass, force.y * carDesc.carModel.invmass)
        fixup(acceleration)

        // transform acceleration from car reference frame to world reference
        // frame
        accelerationInWorldCoordinates.x = cs * acceleration.y + sn * acceleration.x
        accelerationInWorldCoordinates.y = -sn * acceleration.y + cs * acceleration.x
        fixup(accelerationInWorldCoordinates)

        // velocity is integrated acceleration
        carDesc.velocityWorldCoordinates.x += dt * accelerationInWorldCoordinates.x
        carDesc.velocityWorldCoordinates.y += dt * accelerationInWorldCoordinates.y
        fixup(carDesc.velocityWorldCoordinates)

        // make sure vehicle doesn't exceed maximum velocity
        truncate(carDesc.velocityWorldCoordinates, carDesc.carModel.max_speed)

        // Angular acceleration, angular velocity and heading
        val angular_acceleration = torque * carDesc.carModel.invinertia

        // integrate angular acceleration to get angular velocity
        carDesc.angularVelocity += dt * angular_acceleration
        carDesc.angularVelocity = fixup(carDesc.angularVelocity)
    }

    fun resetPhysics() {
        carDesc.velocityWorldCoordinates.set(0F, 0F)
        carDesc.angularVelocity = 0F
        carDesc.brake = 0F
        carDesc.throttle = 0F
        carDesc.steeringAngle = 0F
        accelerationInWorldCoordinates.set(0F, 0F)
        velocity.set(0F, 0F)
        thisSign = 1F
        lateralForceFront.set(0F, 0F)
        lateralForceRear.set(0F, 0F)
    }

    companion object {
        private val DampingThrottle = damping(0.98F)
    }
}
