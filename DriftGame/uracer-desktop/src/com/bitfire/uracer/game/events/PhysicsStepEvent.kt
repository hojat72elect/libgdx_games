package com.bitfire.uracer.game.events

class PhysicsStepEvent : Event<PhysicsStepEvent.Type, PhysicsStepEvent.Order>(Type::class.java, Order::class.java) {

    enum class Type {
        OnBeforeTimestep, OnAfterTimestep, OnSubstepCompleted
    }

    enum class Order {
        Default
    }

    fun interface Listener : Event.Listener<Type, Order> {
        override fun handle(source: Any, type: Type, order: Order)
    }
}
