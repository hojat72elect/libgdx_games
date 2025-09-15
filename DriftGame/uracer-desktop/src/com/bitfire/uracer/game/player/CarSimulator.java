package com.bitfire.uracer.game.player;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitfire.uracer.game.actors.CarDescriptor;
import com.bitfire.uracer.utils.AlgebraMath;
import com.bitfire.uracer.utils.VectorMathUtils;

public final class CarSimulator {
    private static final float DampingThrottle = AlgebraMath.damping(0.98f);
    public CarDescriptor carDesc;
    // exports
    public Vector2 lateralForceFront, lateralForceRear;
    private final Vector2 velocity = new Vector2();
    private final Vector2 acceleration_wc = new Vector2();
    private final Vector2 flatf = new Vector2();
    private final Vector2 flatr = new Vector2();
    private final Vector2 ftraction = new Vector2();
    private final Vector2 resistance = new Vector2();
    private final Vector2 force = new Vector2();
    private final Vector2 acceleration = new Vector2();
    private float thisSign;

    public CarSimulator(CarDescriptor carDesc) {
        this.carDesc = carDesc;
        thisSign = 1f;

        // exports
        lateralForceFront = new Vector2();
        lateralForceRear = new Vector2();
    }

    public void applyInput(CarInput input) {
        float maxForce = carDesc.carModel.max_force;
        boolean hasDir = false, hasSteer = false;

        carDesc.brake = input.brake;
        if (input.updated) {
            // throttle
            if (AlgebraMath.fixup(input.throttle) > 0) {
                // acceleration
                carDesc.throttle = Math.min(input.throttle, maxForce);

                hasDir = true;
            } else if (AlgebraMath.fixup(input.throttle) < 0) {
                // deceleration
                carDesc.throttle = Math.max(input.throttle, -maxForce);

                hasDir = true;
            }

            // steering
            if (AlgebraMath.fixup(input.steerAngle) < 0) {
                // left
                carDesc.steeringAngle = input.steerAngle;
                if (carDesc.steeringAngle < -AlgebraMath.PI_4) {
                    carDesc.steeringAngle = -AlgebraMath.PI_4;
                }

                hasSteer = true;
            } else if (AlgebraMath.fixup(input.steerAngle) > 0) {
                // right
                carDesc.steeringAngle = input.steerAngle;
                if (carDesc.steeringAngle > AlgebraMath.PI_4) {
                    carDesc.steeringAngle = AlgebraMath.PI_4;
                }

                hasSteer = true;
            }
        }

        if (!hasDir) {
            if (Math.abs(carDesc.velocityWorldCoordinates.x) > 0.5f || Math.abs(carDesc.velocityWorldCoordinates.y) > 0.5f) {
                if (!AlgebraMath.isZero(carDesc.throttle)) {
                    carDesc.throttle *= DampingThrottle;
                }

                if (!AlgebraMath.isZero(carDesc.brake)) {
                    carDesc.brake *= DampingThrottle;
                }

                // carDesc.brake = 200f;
            } else {
                carDesc.velocityWorldCoordinates.set(0, 0);
                carDesc.angularVelocity = 0;
                carDesc.brake = 0;
                carDesc.throttle = 0;
            }
        }

        if (!hasSteer) {
            carDesc.steeringAngle = 0;
        }

        carDesc.throttle = AlgebraMath.clamp(carDesc.throttle, -maxForce, maxForce);
        carDesc.brake = AlgebraMath.clamp(carDesc.brake, -maxForce * 2, maxForce * 2);
    }

    public void step(float dt, float bodyAngle) {
        float sn = MathUtils.sin(AlgebraMath.normalRelativeAngle(-bodyAngle));
        float cs = MathUtils.cos(AlgebraMath.normalRelativeAngle(-bodyAngle));

        //
        // SAE convention: x is to the front of the car, y is to the right, z is
        // down
        //

        // car's velocity: Vlat and Vlong
        // transform velocity in world reference frame to velocity in car
        // reference frame
        velocity.x = cs * carDesc.velocityWorldCoordinates.y + sn * carDesc.velocityWorldCoordinates.x;
        velocity.y = -sn * carDesc.velocityWorldCoordinates.y + cs * carDesc.velocityWorldCoordinates.x;
        VectorMathUtils.fixup(velocity);

        float yawspeed = carDesc.carModel.wheelbase * 0.5f * carDesc.angularVelocity;
        float sideslip, rot_angle;
        float slipanglefront, slipanglerear;

        if (AlgebraMath.isZero(velocity.x)) {
            rot_angle = 0;
            sideslip = 0;

            slipanglefront = sideslip + rot_angle;
        } else {
            // compute rotational angle
            rot_angle = MathUtils.atan2(yawspeed, velocity.x);

            // compute the side slip angle of the car (a.k.a. beta)
            sideslip = MathUtils.atan2(velocity.y, velocity.x);

            slipanglefront = sideslip + rot_angle - carDesc.steeringAngle;
        }
        slipanglerear = sideslip - rot_angle;

        flatf.x = 0;
        flatf.y = carDesc.carModel.stiffness_front * slipanglefront;
        flatf.y = Math.min(carDesc.carModel.max_grip, flatf.y);
        flatf.y = Math.max(-carDesc.carModel.max_grip, flatf.y);
        lateralForceFront.set(flatf);
        flatf.y *= carDesc.carModel.weight;

        // lateral force on rear wheels
        flatr.x = 0;
        flatr.y = carDesc.carModel.stiffness_rear * slipanglerear;
        flatr.y = Math.min(carDesc.carModel.max_grip, flatr.y);
        flatr.y = Math.max(-carDesc.carModel.max_grip, flatr.y);
        lateralForceRear.set(flatr);
        flatr.y *= carDesc.carModel.weight;
        thisSign = AlgebraMath.sign(velocity.x);

        ftraction.set(100f * (carDesc.throttle - carDesc.brake * thisSign), 0);

        // torque on body from lateral forces
        float torque = carDesc.carModel.b * flatf.y - carDesc.carModel.c * flatr.y;
        torque = AlgebraMath.fixup(torque);

        // drag and rolling resistance
        resistance.x = -(carDesc.carModel.resistance * velocity.x + carDesc.carModel.drag * velocity.x * Math.abs(velocity.x));
        resistance.y = -(carDesc.carModel.resistance * velocity.y + carDesc.carModel.drag * velocity.y * Math.abs(velocity.y));

        // sum forces
        force.x = ftraction.x + MathUtils.sin(carDesc.steeringAngle) * flatf.x + flatr.x + resistance.x;
        force.y = ftraction.y + MathUtils.cos(carDesc.steeringAngle) * flatf.y + flatr.y + resistance.y;

        // Convert the force into acceleration
        // Newton F = m.a, therefore a = F/m
        acceleration.set(force.x * carDesc.carModel.invmass, force.y * carDesc.carModel.invmass);
        VectorMathUtils.fixup(acceleration);

        // transform acceleration from car reference frame to world reference
        // frame
        acceleration_wc.x = cs * acceleration.y + sn * acceleration.x;
        acceleration_wc.y = -sn * acceleration.y + cs * acceleration.x;
        VectorMathUtils.fixup(acceleration_wc);

        // velocity is integrated acceleration
        carDesc.velocityWorldCoordinates.x += dt * acceleration_wc.x;
        carDesc.velocityWorldCoordinates.y += dt * acceleration_wc.y;
        VectorMathUtils.fixup(carDesc.velocityWorldCoordinates);

        // make sure vehicle doesn't exceed maximum velocity
        VectorMathUtils.truncate(carDesc.velocityWorldCoordinates, carDesc.carModel.max_speed);

        // Angular acceleration, angular velocity and heading

        float angular_acceleration = torque * carDesc.carModel.invinertia;

        // integrate angular acceleration to get angular velocity
        carDesc.angularVelocity += dt * angular_acceleration;
        carDesc.angularVelocity = AlgebraMath.fixup(carDesc.angularVelocity);
    }

    public void resetPhysics() {
        carDesc.velocityWorldCoordinates.set(0, 0);
        carDesc.angularVelocity = 0;
        carDesc.brake = 0;
        carDesc.throttle = 0;
        carDesc.steeringAngle = 0;
        acceleration_wc.set(0, 0);
        velocity.set(0, 0);
        thisSign = 1f;
        lateralForceFront.set(0, 0);
        lateralForceRear.set(0, 0);
    }
}
