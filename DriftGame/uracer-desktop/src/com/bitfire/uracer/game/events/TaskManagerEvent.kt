package com.bitfire.uracer.game.events

class TaskManagerEvent : Event<TaskManagerEvent.Type, TaskManagerEvent.Order>(Type::class.java, Order::class.java) {

    enum class Type {
        OnTick, OnTickCompleted, OnPause, OnResume
    }

    enum class Order {
        MINUS_4, MINUS_3, MINUS_2, MINUS_1, DEFAULT, PLUS_1, PLUS_2, PLUS_3, PLUS_4
    }

    fun interface Listener : Event.Listener<Type, Order> {
        override fun handle(source: Any, type: Type, order: Order)
    }
}
