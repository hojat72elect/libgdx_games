package com.bitfire.uracer.game.actors

import com.badlogic.gdx.Gdx

/**
 * Encapsulates a set of one CarModel and one CarAspect, indexed by a single mnemonic, describing both physical and graphical
 * settings for the specified Type.
 */
class CarPreset(@JvmField val type: Type) {

    @JvmField
    val model = CarModel()

    init {
        when (type) {
            Type.Default, Type.CarYellow -> {
                model.toModel2()
                model.width = 2.4F
                model.length = model.width * 1.72F
                model.max_force = 300F
                model.max_grip = 4.5F
                model.friction = 8F
                model.restitution = 0.35F
                model.stiffness_rear = -3.8F // rear cornering stiffness
                model.stiffness_front = -3.5F // front cornering stiffness
            }

            else -> Gdx.app.log("CarPreset", "No type definition available for \"$type\"")
        }

        this.model.presetType = type
    }

    enum class Type(@JvmField val regionName: String) {
        Default("car"),
        Car("car"),
        CarYellow("car_yellow");
    }
}
