package com.bitfire.uracer.game.events

class GhostLapCompletionMonitorEvent : Event<GhostLapCompletionMonitorEvent.Type, GhostLapCompletionMonitorEvent.Order>(Type::class.java, Order::class.java) {

    enum class Type {
        OnLapCompleted
    }

    enum class Order {
        MINUS_4, MINUS_3, MINUS_2, MINUS_1, DEFAULT, PLUS_1, PLUS_2, PLUS_3, PLUS_4
    }

    interface Listener : Event.Listener<Type, Order> {
        override fun handle(source: Any, type: Type, order: Order)
    }
}
