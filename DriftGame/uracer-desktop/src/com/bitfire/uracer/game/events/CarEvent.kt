package com.bitfire.uracer.game.events

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Fixture
import com.bitfire.uracer.game.actors.Car
import com.bitfire.uracer.game.actors.CarForces

class CarEvent() : Event<CarEvent.Type, CarEvent.Order>(Type::class.java, Order::class.java) {

    @JvmField
    val data = Data()

    enum class Type {
        OnPhysicsForcesReady, OnCollision, OnOutOfTrack, OnBackInTrack
    }

    /**
     * Do not delete the contents of this class!
     */
    enum class Order {
        Default
    }

    interface Listener : Event.Listener<Type, Order> {
        override fun handle(source: Any, type: Type, order: Order)
    }

    class Data {
        var car: Car? = null

        /**
         * collision data
         */
        var other: Fixture? = null

        @JvmField
        var impulses: Vector2? = null

        @JvmField
        var frontRatio = 0F

        /**
         * computed forces data
         */
        @JvmField
        var forces: CarForces? = null

        fun setCollisionData(other: Fixture, impulses: Vector2, frontRatio: Float) {
            this.other = other
            this.impulses = impulses
            this.frontRatio = frontRatio
        }

        fun setForces(forces: CarForces) {
            this.forces = forces
        }
    }
}
