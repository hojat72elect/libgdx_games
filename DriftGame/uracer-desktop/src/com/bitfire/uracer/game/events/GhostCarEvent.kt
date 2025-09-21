package com.bitfire.uracer.game.events

class GhostCarEvent : Event<GhostCarEvent.Type, GhostCarEvent.Order>(Type::class.java, Order::class.java) {

    enum class Type {
        OnGhostFadingOut, ReplayStarted, ReplayEnded
    }

    enum class Order {
        Default
    }

    interface Listener : Event.Listener<Type, Order> {
        override fun handle(source: Any, type: Type, order: Order)
    }
}
