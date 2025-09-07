package com.bitfire.uracer.game.actors;

import com.badlogic.gdx.math.Vector2;

/**
 * Describes the global state of the car entity providing access to both the base physical model information and the processed
 * per-timestep data resulting after each integration.
 */
public final class CarDescriptor {

    public CarModel carModel;

    public Vector2 position_wc = new Vector2(); // position of car center in world coordinates
    public Vector2 velocity_wc = new Vector2(); // velocity vector of car in world coordinates

    public float angularVelocity;
    public float steeringAngle;

    public float throttle; // amount of throttle
    public float brake; // amount of braking

    public CarDescriptor(CarModel model) {
        angularVelocity = 0;
        steeringAngle = 0;
        throttle = 0;
        brake = 0;
        position_wc.set(0, 0);
        velocity_wc.set(0, 0);
        this.carModel = model;
    }
}
