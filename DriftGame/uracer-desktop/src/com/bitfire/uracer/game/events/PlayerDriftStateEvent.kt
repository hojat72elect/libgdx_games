package com.bitfire.uracer.game.events

class PlayerDriftStateEvent : Event<PlayerDriftStateEvent.Type, PlayerDriftStateEvent.Order>(Type::class.java, Order::class.java) {

    enum class Type {
        OnBeginDrift, OnEndDrift
    }

    enum class Order {
        Default
    }

    fun interface Listener : Event.Listener<Type, Order> {
        override fun handle(source: Any, type: Type, order: Order)
    }
}
