package com.bitfire.uracer.game.actors;

import com.badlogic.gdx.Gdx;

/**
 * Encapsulates a set of one CarModel and one CarAspect, indicized by a single mnemonic, describing both physical and graphical
 * settings for the specified Type.
 */
public final class CarPreset {

    public Type type;
    public CarModel model = new CarModel();

    public CarPreset(Type type) {
        setTo(type);
    }

    public void setTo(Type type) {
        switch (type) {
            case Default:
            case Car_Yellow:
                model.toModel2();
                model.width = 2.4f;
                model.length = model.width * 1.72f;
                model.max_force = 300f;
                model.max_grip = 4.5f;
                model.friction = 8f;
                model.restitution = 0.35f;
                model.stiffness_rear = -3.8f; // rear cornering stiffness
                model.stiffness_front = -3.5f; // front cornering stiffness
                break;

            default:
                Gdx.app.log("CarPreset", "No type definition available for \"" + type + "\"");
                break;
        }

        this.type = type;
        this.model.presetType = type;
    }

    public enum Type {

        Default("car"),
        Car("car"),
        Car_Yellow("car_yellow"),

        ;


        public String regionName;

        Type(String name) {
            regionName = name;
        }
    }
}
